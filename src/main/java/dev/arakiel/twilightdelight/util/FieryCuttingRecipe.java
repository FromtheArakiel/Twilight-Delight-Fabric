package dev.arakiel.twilightdelight.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.refabricated.inventory.RecipeWrapper;

public final class FieryCuttingRecipe extends CuttingBoardRecipe {
    private final Level level;
    private final Vec3 pos;

    public static Optional<RecipeHolder<CuttingBoardRecipe>> transform(
            Level level,
            Vec3 pos,
            Optional<RecipeHolder<CuttingBoardRecipe>> recipe
    ) {
        return recipe.map(holder -> new RecipeHolder<>(
                holder.id(),
                new FieryCuttingRecipe(level, pos, holder.value())
        ));
    }

    private FieryCuttingRecipe(Level level, Vec3 pos, CuttingBoardRecipe recipe) {
        super(
                recipe.getGroup(),
                extract(recipe.getIngredients()),
                recipe.getTool(),
                recipe.getRollableResults(),
                recipe.getSoundEvent()
        );
        this.level = level;
        this.pos = pos;
    }

    private static Ingredient extract(NonNullList<Ingredient> ingredients) {
        return ingredients.isEmpty() ? Ingredient.EMPTY : ingredients.getFirst();
    }

    @Override
    public List<ItemStack> rollResults(
            RandomSource random,
            int fortuneLevel,
            RecipeWrapper inventory
    ) {
        List<ItemStack> previous = super.rollResults(random, fortuneLevel, inventory);
        List<ItemStack> results = new ArrayList<>();
        int particles = 0;
        for (ItemStack stack : previous) {
            SingleRecipeInput input = new SingleRecipeInput(stack);
            Optional<RecipeHolder<SmeltingRecipe>> smelting = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, input, level);
            if (smelting.isEmpty()) {
                results.add(stack);
                continue;
            }
            ItemStack cooked = smelting.get().value().assemble(
                    input,
                    level.registryAccess()
            );
            cooked.setCount(stack.getCount());
            results.add(cooked);
            particles += stack.getCount();
        }
        if (particles > 0) {
            if (level instanceof ServerLevel server) {
                server.sendParticles(
                        ParticleTypes.FLAME,
                        pos.x, pos.y, pos.z,
                        particles, 0.1, 0.05, 0.1, 0.0
                );
            }
            level.playSound(
                    null, pos.x, pos.y, pos.z,
                    SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.BLOCKS,
                    1.0F, 1.0F
            );
        }
        return results;
    }
}
