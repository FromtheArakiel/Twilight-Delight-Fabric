package dev.arakiel.twilightdelight.block;

import dev.arakiel.twilightdelight.registry.TDBlocks;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.registry.ModSounds;

public final class MazeStoveBlock extends StoveBlock {
    public MazeStoveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MazeStoveBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        return level.isClientSide && state.getValue(LIT)
                ? createTickerHelper(
                        type,
                        TDBlocks.MAZE_STOVE_ENTITY,
                        MazeStoveBlockEntity::particleTick
                )
                : createStoveTicker(level, type, TDBlocks.MAZE_STOVE_ENTITY);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;
        if (random.nextInt(10) == 0) {
            level.playLocalSound(
                    x, y, z,
                    ModSounds.BLOCK_STOVE_CRACKLE.get(),
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    1.0F, 1.0F, false
            );
        }
        Direction direction = state.getValue(FACING);
        var axis = direction.getAxis();
        double horizontal = random.nextDouble() * 0.6 - 0.3;
        double xOffset = axis == Direction.Axis.X ? direction.getStepX() * 0.52 : horizontal;
        double yOffset = random.nextDouble() * 6.0 / 16.0;
        double zOffset = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : horizontal;
        level.addParticle(
                ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset,
                0.0, 0.0, 0.0
        );
        level.addParticle(
                ParticleTypes.FLAME, x + xOffset, y + yOffset, z + zOffset,
                0.0, 0.0, 0.0
        );
    }
}
