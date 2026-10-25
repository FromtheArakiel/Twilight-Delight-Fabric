package dev.arakiel.twilightdelight.block;

import dev.arakiel.twilightdelight.registry.TDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;

public final class FiddleheadClumpBlock extends MushroomColonyBlock {
    public FiddleheadClumpBlock(Holder<Item> item, Properties properties) {
        super(item, properties);
    }

    public static void updateUpperBlock(Level level, BlockPos pos) {
        BlockState lower = level.getBlockState(pos);
        if (!lower.is(TDBlocks.FIDDLEHEAD_CLUMP)) {
            return;
        }
        int age = lower.getValue(COLONY_AGE);
        BlockPos upperPos = pos.above();
        BlockState upperState = level.getBlockState(upperPos);
        if (age > 0) {
            int top = Math.min(age, 3);
            if ((upperState.isAir() || upperState.is(TDBlocks.FIDDLEHEAD_CLUMP_UPPER))
                    && (!upperState.is(TDBlocks.FIDDLEHEAD_CLUMP_UPPER)
                    || upperState.getValue(FiddleheadClumpUpperBlock.AGE) != top)) {
                level.setBlock(
                        upperPos,
                        TDBlocks.FIDDLEHEAD_CLUMP_UPPER.defaultBlockState()
                                .setValue(FiddleheadClumpUpperBlock.AGE, top),
                        3
                );
            }
        } else if (upperState.is(TDBlocks.FIDDLEHEAD_CLUMP_UPPER)) {
            level.setBlock(upperPos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        super.onPlace(state, level, pos, oldState, moved);
        updateUpperBlock(level, pos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        updateUpperBlock(level, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(COLONY_AGE);
        BlockState ground = level.getBlockState(pos.below());
        if (age < getMaxAge()
                && ground.is(vectorwing.farmersdelight.common.tag.ModTags.Blocks.MUSHROOM_COLONY_GROWABLE_ON)
                && random.nextInt(4) == 0) {
            level.setBlock(pos, state.setValue(COLONY_AGE, age + 1), 2);
        }
        updateUpperBlock(level, pos);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        super.performBonemeal(level, random, pos, state);
        updateUpperBlock(level, pos);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        InteractionResult result = super.useWithoutItem(state, level, pos, player, hit);
        if (result.consumesAction()) {
            updateUpperBlock(level, pos);
        }
        return result;
    }

    @Override
    public ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        ItemInteractionResult result = super.useItemOn(stack, state, level, pos, player, hand, hit);
        if (result.consumesAction()) {
            updateUpperBlock(level, pos);
        }
        return result;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moved) {
        super.onRemove(state, level, pos, newState, moved);
        if (!state.is(newState.getBlock())
                && level.getBlockState(pos.above()).is(TDBlocks.FIDDLEHEAD_CLUMP_UPPER)) {
            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 3);
        }
    }
}
