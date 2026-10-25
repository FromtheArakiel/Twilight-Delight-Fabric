package dev.arakiel.twilightdelight.block;

import com.mojang.serialization.MapCodec;
import dev.arakiel.twilightdelight.registry.TDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import twilightforest.init.TFBlocks;

public final class FiddleheadClumpUpperBlock extends BushBlock
        implements BonemealableBlock {
    public static final MapCodec<FiddleheadClumpUpperBlock> CODEC =
            simpleCodec(FiddleheadClumpUpperBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public FiddleheadClumpUpperBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(AGE, 1));
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(TDBlocks.FIDDLEHEAD_CLUMP)
                && below.getValue(MushroomColonyBlock.COLONY_AGE) > 0;
    }

    @Override
    public boolean isValidBonemealTarget(
            LevelReader level,
            BlockPos pos,
            BlockState state
    ) {
        BlockState below = level.getBlockState(pos.below());
        return below.getBlock() instanceof BonemealableBlock growable
                && growable.isValidBonemealTarget(level, pos.below(), below);
    }

    @Override
    public boolean isBonemealSuccess(
            net.minecraft.world.level.Level level,
            RandomSource random,
            BlockPos pos,
            BlockState state
    ) {
        return true;
    }

    @Override
    public void performBonemeal(
            ServerLevel level,
            RandomSource random,
            BlockPos pos,
            BlockState state
    ) {
        BlockState below = level.getBlockState(pos.below());
        if (below.getBlock() instanceof BonemealableBlock growable) {
            growable.performBonemeal(level, random, pos.below(), below);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        return below.getBlock() instanceof FiddleheadClumpBlock clump
                ? clump.useWithoutItem(below, level, belowPos, player, hit)
                : InteractionResult.PASS;
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
        BlockPos belowPos = pos.below();
        BlockState below = level.getBlockState(belowPos);
        return below.getBlock() instanceof FiddleheadClumpBlock clump
                ? clump.useItemOn(stack, below, level, belowPos, player, hand, hit)
                : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        super.onRemove(state, level, pos, newState, moved);
        if (!state.is(newState.getBlock())) {
            BlockPos belowPos = pos.below();
            BlockState below = level.getBlockState(belowPos);
            if (below.is(TDBlocks.FIDDLEHEAD_CLUMP)
                    && below.getValue(MushroomColonyBlock.COLONY_AGE) > 0) {
                level.destroyBlock(belowPos, true);
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(TFBlocks.FIDDLEHEAD.get());
    }
}
