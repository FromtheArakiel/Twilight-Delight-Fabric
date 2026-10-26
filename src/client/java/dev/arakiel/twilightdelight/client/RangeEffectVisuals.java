package dev.arakiel.twilightdelight.client;

import dev.arakiel.twilightdelight.config.TDConfig;
import dev.arakiel.twilightdelight.registry.TDEffects;
import java.util.List;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class RangeEffectVisuals {
    private RangeEffectVisuals() {
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level == null || client.player == null
                    || client.level.getGameTime() % 2 != 0) {
                return;
            }
            LivingEntity player = client.player;
            List<LivingEntity> affected = client.level.getEntitiesOfClass(
                    LivingEntity.class,
                    player.getBoundingBox().inflate(64.0),
                    entity -> hasRangeEffect(entity)
            );
            for (LivingEntity entity : affected) {
                spawnRing(client, entity);
            }
        });
    }

    private static boolean hasRangeEffect(LivingEntity entity) {
        return entity.hasEffect(holder(TDEffects.FIRE_RANGE))
                || entity.hasEffect(holder(TDEffects.POISON_RANGE))
                || entity.hasEffect(holder(TDEffects.FROZEN_RANGE))
                || entity.hasEffect(holder(TDEffects.TWILIGHT_AURA));
    }

    private static net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> holder(
            net.minecraft.world.effect.MobEffect effect
    ) {
        return BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
    }

    private static void spawnRing(Minecraft client, LivingEntity entity) {
        ParticleOptions particle;
        if (entity.hasEffect(holder(TDEffects.FIRE_RANGE))) {
            particle = ParticleTypes.FLAME;
        } else if (entity.hasEffect(holder(TDEffects.POISON_RANGE))) {
            particle = ParticleTypes.COMPOSTER;
        } else if (entity.hasEffect(holder(TDEffects.FROZEN_RANGE))) {
            particle = ParticleTypes.SNOWFLAKE;
        } else {
            particle = ParticleTypes.PORTAL;
        }

        double radius = TDConfig.effectRange();
        Vec3 center = entity.position().add(0.0, entity.getEyeHeight() / 2.0, 0.0);
        for (int i = 0; i < 5; i++) {
            float angle = entity.level().random.nextFloat()
                    * (float) (Math.PI * 2.0);
            Vec3 offset = new Vec3(0.0, radius, 0.0)
                    .xRot((float) (Math.PI / 4.0))
                    .yRot(angle);
            client.level.addAlwaysVisibleParticle(
                    particle,
                    center.x + offset.x,
                    center.y + offset.y,
                    center.z + offset.z,
                    0.0,
                    0.0,
                    0.0
            );
        }
    }
}
