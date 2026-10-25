package dev.arakiel.twilightdelight.effect;

import dev.arakiel.twilightdelight.TwilightDelightFabric;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class TemporalSadnessEffect extends MobEffect {
    public TemporalSadnessEffect() {
        super(MobEffectCategory.HARMFUL, -1);
        addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                TwilightDelightFabric.id("temporal_sadness_attack"),
                -10.0D,
                AttributeModifier.Operation.ADD_VALUE
        );
        addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                TwilightDelightFabric.id("temporal_sadness_speed"),
                -0.5D,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        entity.setSprinting(false);
        if (entity.level() instanceof ServerLevel server) {
            server.sendParticles(
                    ParticleTypes.FALLING_WATER,
                    entity.getX(), entity.getY(), entity.getZ(),
                    5, 1.0, 1.0, 1.0, 1.0
            );
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
