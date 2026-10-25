package dev.arakiel.twilightdelight.block;

import dev.arakiel.twilightdelight.registry.TDBlocks;
import java.lang.reflect.Field;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;
import vectorwing.farmersdelight.common.block.entity.AbstractStoveBlockEntity;
import vectorwing.farmersdelight.refabricated.inventory.ItemStackHandler;

public final class MazeStoveBlockEntity extends AbstractStoveBlockEntity {
    private static final Field COOKING_TIME = findCookingTimeField();

    public MazeStoveBlockEntity(BlockPos pos, BlockState state) {
        super(
                TDBlocks.MAZE_STOVE_ENTITY,
                pos,
                state,
                RecipeType.CAMPFIRE_COOKING
        );
    }

    public static void particleTick(
            Level level,
            BlockPos pos,
            BlockState state,
            MazeStoveBlockEntity stove
    ) {
        if (!stove.isEmpty()) {
            stove.addSmokeParticles();
        }
    }

    @Override
    public boolean placeFood(
            @Nullable Entity entity,
            ItemStack stack,
            RecipeHolder<? extends AbstractCookingRecipe> recipe
    ) {
        int slot = getNextEmptySlot();
        if (!super.placeFood(entity, stack, recipe)) {
            return false;
        }
        if (level != null) {
            ItemStack result = recipe.value().getResultItem(level.registryAccess());
            ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.getItem());
            if ("twilightforest".equals(id.getNamespace())
                    || "twilightdelight".equals(id.getNamespace())) {
                try {
                    int[] cookingTime = (int[]) COOKING_TIME.get(this);
                    cookingTime[slot] /= 2;
                } catch (IllegalAccessException exception) {
                    throw new IllegalStateException("Cannot adjust maze stove cooking time", exception);
                }
            }
        }
        return true;
    }

    private void addSmokeParticles() {
        ItemStackHandler items = getItems();
        for (int i = 0; i < items.getSlotCount(); i++) {
            if (items.getStackInSlot(i).isEmpty() || level.random.nextFloat() >= 0.2F) {
                continue;
            }
            Vec2 offset = getStoveItemOffset(i);
            Direction direction = getBlockState().getValue(AbstractStoveBlock.FACING);
            if (direction.get2DDataValue() % 2 != 0) {
                offset = new Vec2(offset.y, offset.x);
            }
            double x = worldPosition.getX() + 0.5
                    - direction.getStepX() * offset.x
                    + direction.getClockWise().getStepX() * offset.x;
            double y = worldPosition.getY() + 1.0;
            double z = worldPosition.getZ() + 0.5
                    - direction.getStepZ() * offset.y
                    + direction.getClockWise().getStepZ() * offset.y;
            for (int j = 0; j < 3; j++) {
                level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 5.0E-4, 0.0);
            }
        }
    }

    @Override
    protected int getInventorySlotCount() {
        return 6;
    }

    @Override
    public Vec2 getStoveItemOffset(int index) {
        return new Vec2[]{
                new Vec2(0.3F, 0.2F),
                new Vec2(0.0F, 0.2F),
                new Vec2(-0.3F, 0.2F),
                new Vec2(0.3F, -0.2F),
                new Vec2(0.0F, -0.2F),
                new Vec2(-0.3F, -0.2F)
        }[index];
    }

    private static Field findCookingTimeField() {
        try {
            Field field = AbstractStoveBlockEntity.class.getDeclaredField("cookingTime");
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }
}
