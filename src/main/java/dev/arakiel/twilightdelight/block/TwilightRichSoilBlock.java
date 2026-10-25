package dev.arakiel.twilightdelight.block;

import dev.arakiel.twilightdelight.config.TDConfig;
import dev.arakiel.twilightdelight.registry.TDBlocks;
import dev.arakiel.twilightdelight.util.TwilightRichSoilUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.init.TFBlocks;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.block.RichSoilBlock;

public final class TwilightRichSoilBlock extends RichSoilBlock implements BonemealableBlock {
    public TwilightRichSoilBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        TwilightRichSoilUtil.convert(pos, level);
        if (!tryConvertColony(level, pos.above())) {
            for (int i = 0; i < TDConfig.richSoilTrials(); i++) {
                RichSoilBlock.tryBoostingPlantsAboveAndBelow(level, pos, random);
            }
        }
    }

    public boolean tryConvertColony(ServerLevel level, BlockPos abovePos) {
        BlockState above = level.getBlockState(abovePos);
        if (convertMushroomToColony(above, abovePos, level)) {
            return true;
        }
        if (above.is(TFBlocks.MUSHGLOOM.get())) {
            level.setBlockAndUpdate(
                    abovePos,
                    TDBlocks.MUSHGLOOM_COLONY.defaultBlockState()
            );
            return true;
        }
        if (above.is(TFBlocks.FIDDLEHEAD.get())) {
            level.setBlockAndUpdate(
                    abovePos,
                    TDBlocks.FIDDLEHEAD_CLUMP.defaultBlockState()
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        BlockState above = level.getBlockState(pos.above());
        return above.is(Blocks.BROWN_MUSHROOM)
                || above.is(Blocks.RED_MUSHROOM)
                || above.is(TFBlocks.MUSHGLOOM.get())
                || above.is(TFBlocks.FIDDLEHEAD.get());
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        tryConvertColony(level, pos.above());
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
        if (stack.is(ItemTags.HOES) && level.getBlockState(pos.above()).isAir()) {
            if (!level.isClientSide()) {
                level.setBlockAndUpdate(
                        pos,
                        TDBlocks.TWILIGHT_RICH_SOIL_FARMLAND.defaultBlockState()
                );
                level.playSound(
                        null, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F
                );
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }
}
