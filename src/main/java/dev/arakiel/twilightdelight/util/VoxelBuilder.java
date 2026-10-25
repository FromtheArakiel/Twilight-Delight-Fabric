package dev.arakiel.twilightdelight.util;

import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class VoxelBuilder {
    private final Vec3 low;
    private final Vec3 high;

    public VoxelBuilder(int x0, int y0, int z0, int x1, int y1, int z1) {
        low = new Vec3(x0, y0, z0).subtract(8.0, 8.0, 8.0);
        high = new Vec3(x1, y1, z1).subtract(8.0, 8.0, 8.0);
    }

    private VoxelShape rotateFromNorth(Direction direction) {
        float angle = (float) ((180.0F - direction.toYRot()) / 180.0F * Math.PI);
        Vec3 rotatedLow = low.yRot(angle).add(8.0, 8.0, 8.0);
        Vec3 rotatedHigh = high.yRot(angle).add(8.0, 8.0, 8.0);
        AABB bounds = new AABB(rotatedLow, rotatedHigh);
        return Block.box(
                bounds.minX, bounds.minY, bounds.minZ,
                bounds.maxX, bounds.maxY, bounds.maxZ
        );
    }

    public static VoxelShape[] buildRotatedShapes(List<VoxelBuilder> boxes) {
        VoxelShape[] rotated = new VoxelShape[4];
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            VoxelShape shape = null;
            for (VoxelBuilder box : boxes) {
                VoxelShape current = box.rotateFromNorth(direction);
                shape = shape == null ? current : Shapes.or(shape, current);
            }
            rotated[direction.get2DDataValue()] = shape == null
                    ? Shapes.empty()
                    : shape;
        }
        return rotated;
    }
}
