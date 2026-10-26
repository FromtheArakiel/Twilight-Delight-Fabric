package dev.arakiel.twilightdelight.effect;

import dev.arakiel.twilightdelight.TwilightDelightFabric;
import dev.arakiel.twilightdelight.config.TDConfig;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.AABB;

public final class TwilightAuraEffect extends MobEffect {
    public TwilightAuraEffect() {
        super(MobEffectCategory.BENEFICIAL, 10040012);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide()) {
            return true;
        }
        if (entity.tickCount % 20 == 0) {
            double range = TDConfig.effectRange();
            AABB area = entity.getBoundingBox().inflate(range);
            List<Mob> candidates = entity.level().getEntitiesOfClass(
                    Mob.class,
                    area,
                    mob -> mob != entity && canTransform(mob)
            );
            if (!candidates.isEmpty()) {
                transform(candidates.get(entity.level().getRandom().nextInt(candidates.size())));
            }
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    private static boolean canTransform(Mob mob) {
        var id = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType());
        if (id == null || "twilightforest".equals(id.getNamespace())) {
            return false;
        }
        EntityType<?> target = getTransformTarget(mob);
        if (target == null) {
            return false;
        }
        var targetId = BuiltInRegistries.ENTITY_TYPE.getKey(target);
        return targetId != null && "twilightforest".equals(targetId.getNamespace());
    }

    private static EntityType<?> getTransformTarget(Mob mob) {
        return TwilightTransformationMaps.target(mob.getType());
    }

    private static void transform(Mob old) {
        if (!(old.level() instanceof ServerLevel server)) {
            return;
        }
        EntityType<?> type = getTransformTarget(old);
        if (type == null || !(type.create(server) instanceof Mob converted)) {
            return;
        }
        converted.moveTo(
                old.getX(), old.getY(), old.getZ(),
                old.getYRot(), old.getXRot()
        );
        converted.finalizeSpawn(
                server,
                server.getCurrentDifficultyAt(converted.blockPosition()),
                MobSpawnType.CONVERSION,
                null
        );
        UUID uuid = converted.getUUID();
        try {
            CompoundTag tag = new CompoundTag();
            old.saveWithoutId(tag);
            converted.load(tag);
        } catch (Exception exception) {
            TwilightDelightFabric.LOGGER.warn("Could not transform entity NBT data", exception);
        }
        converted.setUUID(uuid);
        server.addFreshEntity(converted);
        old.discard();
        server.sendParticles(
                net.minecraft.core.particles.ParticleTypes.PORTAL,
                converted.getX(), converted.getY(0.5), converted.getZ(),
                24, 0.5, 0.5, 0.5, 0.1
        );
    }
}
