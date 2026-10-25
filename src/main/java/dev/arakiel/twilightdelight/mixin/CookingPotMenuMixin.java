package dev.arakiel.twilightdelight.mixin;

import dev.arakiel.twilightdelight.registry.TDBlocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;

@Mixin(CookingPotMenu.class)
abstract class CookingPotMenuMixin {
    @Shadow(remap = false)
    @Final
    private ContainerLevelAccess canInteractWithCallable;

    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
    private void twilightdelight$fieryPotStillValid(
            Player player,
            CallbackInfoReturnable<Boolean> cir
    ) {
        boolean valid = canInteractWithCallable.evaluate((level, pos) ->
                player.distanceToSqr(Vec3.atCenterOf(pos)) <= 64.0
                        && level.getBlockState(pos).is(TDBlocks.FIERY_POT)
        ).orElse(false);
        if (valid) {
            cir.setReturnValue(true);
        }
    }
}
