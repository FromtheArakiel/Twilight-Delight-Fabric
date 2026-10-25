package dev.arakiel.twilightdelight.util;

import dev.arakiel.twilightdelight.registry.TDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.HugeLilyPadBlock;
import twilightforest.enums.HugeLilypadPiece;
import twilightforest.init.TFBlocks;
import vectorwing.farmersdelight.common.Configuration;

public final class TwilightRichSoilUtil {
    private TwilightRichSoilUtil() {
    }

    public static void convert(BlockPos pos, Level level) {
        convertLiveRoot(pos, level);
        convertLilyPad(pos, level);
        convertIronwood(pos, level);
    }

    private static void convertLiveRoot(BlockPos pos, Level level) {
        BlockPos first = pos.relative(Direction.getRandom(level.random));
        if (!level.getBlockState(first).is(TFBlocks.ROOT_BLOCK.get())) {
            return;
        }
        BlockPos second = first.relative(Direction.getRandom(level.random));
        if (level.getBlockState(second).is(TFBlocks.LIVEROOT_BLOCK.get())) {
            level.setBlockAndUpdate(
                    first,
                    TFBlocks.LIVEROOT_BLOCK.get().defaultBlockState()
            );
        }
    }

    private static void convertLilyPad(BlockPos pos, Level level) {
        BlockPos target = pos.above(2);
        BlockState state = level.getBlockState(target);
        if (state.is(Blocks.LILY_PAD)) {
            if (level.random.nextBoolean()) {
                Direction direction = Direction.from2DDataValue(level.random.nextInt(4));
                if (level.getBlockState(target.relative(direction))
                        .is(TFBlocks.HUGE_LILY_PAD.get())) {
                    level.setBlockAndUpdate(
                            target,
                            TFBlocks.HUGE_WATER_LILY.get().defaultBlockState()
                    );
                    return;
                }
            }
            convertPadToHuge(target, level);
        } else if (state.is(TFBlocks.HUGE_WATER_LILY.get())) {
            spreadLilyPad(target, level);
        }
    }

    private static void spreadLilyPad(BlockPos target, Level level) {
        Direction direction = Direction.from2DDataValue(level.random.nextInt(4));
        BlockPos candidate = target.relative(direction);
        BlockState current = level.getBlockState(candidate);
        if ((current.isAir() || current.is(Blocks.LILY_PAD))
                && level.getBlockState(candidate.below()).is(Blocks.WATER)
                && level.getFluidState(candidate.below()).isSource()) {
            level.setBlockAndUpdate(candidate, Blocks.LILY_PAD.defaultBlockState());
        }
    }

    private static void convertPadToHuge(BlockPos target, Level level) {
        HugeLilyPadBlock block = TFBlocks.HUGE_LILY_PAD.get();
        Direction direction = Direction.from2DDataValue(level.random.nextInt(4));
        HugeLilypadPiece piece = HugeLilypadPiece.values()[level.random.nextInt(4)];
        BlockState state = block.defaultBlockState()
                .setValue(HugeLilyPadBlock.FACING, direction)
                .setValue(HugeLilyPadBlock.PIECE, piece);
        for (BlockPos pos : block.getAllMyBlocks(target, state)) {
            BlockState current = level.getBlockState(pos);
            BlockState below = level.getBlockState(pos.below());
            if ((!current.isAir() && !current.is(Blocks.LILY_PAD))
                    || !below.is(Blocks.WATER)
                    || !below.getFluidState().isSource()) {
                return;
            }
        }
        BlockPos origin = switch (piece) {
            case NE -> target.west();
            case SE -> target.north().west();
            case SW -> target.north();
            default -> target;
        };
        level.setBlockAndUpdate(origin, state.setValue(HugeLilyPadBlock.PIECE, HugeLilypadPiece.NW));
        level.setBlockAndUpdate(origin.east(), state.setValue(HugeLilyPadBlock.PIECE, HugeLilypadPiece.NE));
        level.setBlockAndUpdate(origin.south(), state.setValue(HugeLilyPadBlock.PIECE, HugeLilypadPiece.SW));
        level.setBlockAndUpdate(origin.south().east(), state.setValue(HugeLilyPadBlock.PIECE, HugeLilypadPiece.SE));
    }

    private static void convertIronwood(BlockPos pos, Level level) {
        BlockPos above = pos.above();
        if (!level.getBlockState(above).is(TFBlocks.DARKWOOD_SAPLING.get())
                || level.random.nextFloat() > Configuration.RICH_SOIL_BOOST_CHANCE.get()) {
            return;
        }
        int rootCount = 0;
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (level.getBlockState(pos.offset(x, -1, z))
                        .is(TFBlocks.LIVEROOT_BLOCK.get())) {
                    rootCount++;
                }
            }
        }
        if (level.random.nextFloat() * 10.0F < rootCount) {
            level.setBlockAndUpdate(
                    above,
                    ((SaplingBlock) TDBlocks.IRONWOOD_SAPLING).defaultBlockState()
            );
        }
    }
}
