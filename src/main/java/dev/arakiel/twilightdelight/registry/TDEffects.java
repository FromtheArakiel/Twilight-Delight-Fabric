package dev.arakiel.twilightdelight.registry;

import dev.arakiel.gemini.registration.GeminiRegistration;
import dev.arakiel.gemini.registration.annotations.RegistryNamespace;
import dev.arakiel.gemini.registration.container.MobEffectRegistryContainer;
import dev.arakiel.twilightdelight.TwilightDelightFabric;
import dev.arakiel.twilightdelight.effect.GlowingEffect;
import dev.arakiel.twilightdelight.effect.LichChargeEffect;
import dev.arakiel.twilightdelight.effect.PhantomBlessingEffect;
import dev.arakiel.twilightdelight.effect.RangeEffect;
import dev.arakiel.twilightdelight.effect.TemporalSadnessEffect;
import dev.arakiel.twilightdelight.effect.TwilightAuraEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

@RegistryNamespace(TwilightDelightFabric.MOD_ID)
public final class TDEffects implements MobEffectRegistryContainer {
    public static final MobEffect FIRE_RANGE =
            new RangeEffect(RangeEffect.Kind.FIRE, -39424);
    public static final MobEffect POISON_RANGE =
            new RangeEffect(RangeEffect.Kind.POISON, -16751104);
    public static final MobEffect FROZEN_RANGE =
            new RangeEffect(RangeEffect.Kind.FROZEN, -16724788);
    public static final MobEffect TEMPORAL_SADNESS = new TemporalSadnessEffect();
    public static final MobEffect AURORA_GLOWING = new GlowingEffect();
    public static final MobEffect TWILIGHT_AURA = new TwilightAuraEffect();
    public static final MobEffect LICH_CHARGE = new LichChargeEffect();
    public static final MobEffect PHANTOM_BLESSING = new PhantomBlessingEffect();

    private TDEffects() {
    }

    public static void init() {
        GeminiRegistration.mobEffects(TDEffects.class);
    }
}
