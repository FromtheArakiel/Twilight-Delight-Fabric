package dev.arakiel.twilightdelight.block;

import dev.arakiel.twilightdelight.util.VoxelBuilder;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.FeastBlock;

public final class TwilightFeastBlock extends FeastBlock {
    public enum Kind {
        FIERY_SNAKES,
        LILY_CHICKEN,
        MEEF_WELLINGTON,
        SAUCY_GRILLED_NAGA,
        CHAOS_STEW,
        DELICIOUS_IN_MAZE,
        GHASTLING_MOUSSE_CAKE
    }

    private static final VoxelBuilder PLATE = new VoxelBuilder(1, 0, 1, 15, 2, 15);
    private static final VoxelBuilder TRAY = PLATE;
    private static final VoxelBuilder BOWL = new VoxelBuilder(4, 0, 4, 12, 8, 12);
    private static final VoxelBuilder HELMET = new VoxelBuilder(4, 0, 5, 12, 8, 13);
    private final VoxelShape[][] shapes;

    public TwilightFeastBlock(Supplier<Item> serving, boolean leftovers, Kind kind) {
        super(
                Properties.ofFullCopy(Blocks.WHITE_WOOL)
                        .pushReaction(PushReaction.DESTROY),
                serving,
                leftovers,
                true
        );
        shapes = createShapes(kind);
    }

    @Override
    public VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return shapes[state.getValue(SERVINGS)][state.getValue(FACING).get2DDataValue()];
    }

    private static VoxelShape[] served(VoxelBuilder base, VoxelBuilder serving) {
        return VoxelBuilder.buildRotatedShapes(List.of(base, serving));
    }

    private static VoxelShape[] empty(VoxelBuilder base) {
        return VoxelBuilder.buildRotatedShapes(List.of(base));
    }

    private static VoxelShape[] served(int x0, int y0, int z0, int x1, int y1, int z1) {
        return served(PLATE, new VoxelBuilder(x0, y0, z0, x1, y1, z1));
    }

    private static VoxelShape[][] createShapes(Kind kind) {
        return switch (kind) {
            case FIERY_SNAKES -> new VoxelShape[][]{
                    empty(PLATE),
                    served(11, 2, 2, 14, 9, 14),
                    served(8, 2, 2, 14, 9, 14),
                    served(5, 2, 2, 14, 9, 14),
                    served(2, 2, 2, 14, 9, 14)
            };
            case LILY_CHICKEN -> new VoxelShape[][]{
                    empty(PLATE),
                    served(4, 2, 9, 14, 9, 13),
                    served(2, 2, 8, 14, 9, 13),
                    served(2, 2, 3, 14, 9, 13),
                    served(2, 2, 3, 14, 9, 13)
            };
            case MEEF_WELLINGTON -> new VoxelShape[][]{
                    empty(TRAY),
                    served(4, 2, 4, 8, 9, 12),
                    served(3, 2, 4, 13, 9, 12),
                    served(2, 2, 4, 13, 9, 13),
                    served(2, 2, 3, 14, 9, 12)
            };
            case SAUCY_GRILLED_NAGA -> {
                VoxelBuilder plate = new VoxelBuilder(0, 0, 0, 16, 2, 16);
                yield new VoxelShape[][]{
                        empty(plate),
                        served(plate, new VoxelBuilder(3, 2, 3, 15, 5, 15)),
                        served(plate, new VoxelBuilder(3, 2, 3, 15, 8, 15)),
                        served(plate, new VoxelBuilder(3, 2, 3, 15, 11, 15)),
                        served(plate, new VoxelBuilder(3, 2, 3, 15, 14, 15))
                };
            }
            case CHAOS_STEW -> new VoxelShape[][]{
                    empty(BOWL),
                    served(BOWL, new VoxelBuilder(4, 8, 4, 12, 8, 12)),
                    served(BOWL, new VoxelBuilder(4, 8, 4, 12, 10, 12)),
                    served(BOWL, new VoxelBuilder(4, 8, 4, 12, 13, 12)),
                    served(BOWL, new VoxelBuilder(4, 8, 4, 12, 14, 12))
            };
            case DELICIOUS_IN_MAZE -> new VoxelShape[][]{
                    empty(HELMET),
                    served(HELMET, new VoxelBuilder(4, 1, 5, 12, 2, 13)),
                    served(HELMET, new VoxelBuilder(4, 1, 5, 12, 4, 13)),
                    served(HELMET, new VoxelBuilder(4, 1, 5, 12, 6, 13)),
                    served(HELMET, new VoxelBuilder(4, 1, 5, 12, 13, 13))
            };
            case GHASTLING_MOUSSE_CAKE -> new VoxelShape[][]{
                    empty(TRAY),
                    served(TRAY, new VoxelBuilder(8, 2, 8, 14, 14, 14)),
                    served(TRAY, new VoxelBuilder(8, 2, 2, 14, 14, 14)),
                    served(TRAY, new VoxelBuilder(2, 2, 2, 14, 14, 14)),
                    served(TRAY, new VoxelBuilder(2, 2, 2, 14, 14, 14))
            };
        };
    }
}
