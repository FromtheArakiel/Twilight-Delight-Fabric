package dev.arakiel.twilightdelight.mixin;

import dev.arakiel.twilightdelight.registry.TDBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.HeatableBlockEntity;

@Mixin(HeatableBlockEntity.class)
interface CookingPotHeatMixin {
    @Inject(method = "isHeated", at = @At("HEAD"), cancellable = true, remap = false)
    private void twilightdelight$fieryPotIsAlwaysHeated(
            Level level,
            BlockPos pos,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (this instanceof BlockEntity blockEntity
                && blockEntity.getBlockState().is(TDBlocks.FIERY_POT)) {
            cir.setReturnValue(true);
        }
    }
}
