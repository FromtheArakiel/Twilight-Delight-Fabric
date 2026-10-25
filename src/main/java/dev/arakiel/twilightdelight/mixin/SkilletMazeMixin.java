package dev.arakiel.twilightdelight.mixin;

import dev.arakiel.twilightdelight.registry.TDBlocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;

@Mixin(SkilletBlockEntity.class)
abstract class SkilletMazeMixin {
    @Shadow(remap = false)
    private int cookingTime;

    @Inject(method = "cookAndOutputItems", at = @At("HEAD"), remap = false)
    private void twilightdelight$mazeStoveAcceleration(
            ItemStack stack,
            Level level,
            CallbackInfo ci
    ) {
        BlockEntity self = (BlockEntity) (Object) this;
        if (level != null
                && level.getBlockState(self.getBlockPos().below())
                .is(TDBlocks.MAZE_STOVE)) {
            cookingTime++;
        }
    }
}
