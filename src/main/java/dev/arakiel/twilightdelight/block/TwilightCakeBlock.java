package dev.arakiel.twilightdelight.block;

import com.mojang.serialization.MapCodec;
import dev.arakiel.twilightdelight.registry.TDItems;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.utility.ItemUtils;

public final class TwilightCakeBlock extends CakeBlock {
    public static final MapCodec<TwilightCakeBlock> CODEC =
            simpleCodec(TwilightCakeBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final Supplier<Item> slice;

    public TwilightCakeBlock(Properties properties) {
        super(properties);
        this.slice = () -> TDItems.TWILIGHT_CAKE_SLICE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MapCodec<CakeBlock> codec() {
        return (MapCodec<CakeBlock>) (MapCodec<?>) CODEC;
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(BITES, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(
                FACING,
                context.getHorizontalDirection().getOpposite()
        );
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        Direction facing = state.getValue(FACING);
        double z0 = 1.0 + state.getValue(BITES) * 2.0;
        return Shapes.or(
                boxFor(facing, 1.0, 0.0, z0, 15.0, 8.0, 15.0),
                boxFor(facing, 1.0, 8.0, 13.0, 6.0, 14.0, 15.0)
        );
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        if (level.isClientSide) {
            InteractionResult result = eatSlice(level, pos, state, player);
            if (result.consumesAction()) {
                return InteractionResult.SUCCESS;
            }
            return player.getMainHandItem().isEmpty()
                    ? InteractionResult.CONSUME
                    : result;
        }
        return eatSlice(level, pos, state, player);
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (!stack.is(vectorwing.farmersdelight.common.tag.ModTags.Items.KNIVES)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hit);
        }
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        int bites = state.getValue(BITES);
        level.setBlock(
                pos,
                bites < 6 ? state.setValue(BITES, bites + 1) : Blocks.AIR.defaultBlockState(),
                3
        );
        Direction advance = state.getValue(FACING).getOpposite();
        ItemUtils.spawnItemEntity(
                level,
                new ItemStack(this.slice.get()),
                pos.getX() + 0.5 + advance.getStepX() * (bites * 0.1 - 0.5),
                pos.getY() + 0.2,
                pos.getZ() + 0.5 + advance.getStepZ() * (bites * 0.1 - 0.5),
                advance.getStepX() * -0.05,
                0.0,
                advance.getStepZ() * -0.05
        );
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);
        level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
        return ItemInteractionResult.SUCCESS;
    }

    private static InteractionResult eatSlice(
            LevelAccessor level,
            BlockPos pos,
            BlockState state,
            Player player
    ) {
        FoodProperties food = new ItemStack(TDItems.TWILIGHT_CAKE_SLICE)
                .get(DataComponents.FOOD);
        if (food == null || !player.canEat(food.canAlwaysEat())) {
            return InteractionResult.PASS;
        }
        player.awardStat(Stats.EAT_CAKE_SLICE);
        player.getFoodData().eat(food.nutrition(), food.saturation());
        if (!level.isClientSide()) {
            for (FoodProperties.PossibleEffect possible : food.effects()) {
                if (level.getRandom().nextFloat() < possible.probability()) {
                    player.addEffect(possible.effect());
                }
            }
        }
        int bites = state.getValue(BITES);
        if (bites < 6) {
            level.setBlock(pos, state.setValue(BITES, bites + 1), 3);
        } else {
            level.removeBlock(pos, false);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }
        level.gameEvent(player, GameEvent.EAT, pos);
        return InteractionResult.SUCCESS;
    }

    private static VoxelShape boxFor(
            Direction facing,
            double x0,
            double y0,
            double z0,
            double x1,
            double y1,
            double z1
    ) {
        return switch (facing) {
            case SOUTH -> Block.box(16.0 - x1, y0, 16.0 - z1, 16.0 - x0, y1, 16.0 - z0);
            case EAST -> Block.box(16.0 - z1, y0, x0, 16.0 - z0, y1, x1);
            case WEST -> Block.box(z0, y0, 16.0 - x1, z1, y1, 16.0 - x0);
            default -> Block.box(x0, y0, z0, x1, y1, z1);
        };
    }
}
