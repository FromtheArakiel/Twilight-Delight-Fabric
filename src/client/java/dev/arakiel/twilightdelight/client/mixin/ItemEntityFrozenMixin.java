package dev.arakiel.twilightdelight.client.mixin;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
abstract class ItemEntityFrozenMixin extends Entity {
    private ItemEntityFrozenMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void twilightdelight$frozenParticles(CallbackInfo ci) {
        if (getType() == EntityType.ITEM
                && level().isClientSide()
                && getTicksFrozen() > 0
                && level().random.nextBoolean()) {
            double x = Mth.randomBetween(level().getRandom(), -1.0F, 1.0F) / 12.0F;
            double z = Mth.randomBetween(level().getRandom(), -1.0F, 1.0F) / 12.0F;
            level().addParticle(
                    ParticleTypes.SNOWFLAKE,
                    getX(),
                    getY() + getBbHeight() / 2.0F,
                    getZ(),
                    x,
                    0.05F,
                    z
            );
        }
    }
}
