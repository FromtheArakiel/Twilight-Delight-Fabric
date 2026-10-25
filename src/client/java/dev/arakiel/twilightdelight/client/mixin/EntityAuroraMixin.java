package dev.arakiel.twilightdelight.client.mixin;

import dev.arakiel.twilightdelight.client.AuroraGlowClient;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
abstract class EntityAuroraMixin {
    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void twilightdelight$auroraColor(CallbackInfoReturnable<Integer> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity.level() != null && entity.level().isClientSide()
                && AuroraGlowClient.shouldRender(entity)) {
            cir.setReturnValue(AuroraGlowClient.color(entity));
        }
    }

    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void twilightdelight$auroraGlowing(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = (Entity) (Object) this;
        if (entity.level() != null && entity.level().isClientSide()
                && AuroraGlowClient.shouldRender(entity)) {
            cir.setReturnValue(true);
        }
    }
}
