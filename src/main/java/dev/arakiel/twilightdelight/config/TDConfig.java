package dev.arakiel.twilightdelight.config;

import dev.arakiel.gemini.config.Config;
import dev.arakiel.gemini.config.ConfigEntry;
import dev.arakiel.gemini.config.ConfigHandle;
import dev.arakiel.gemini.config.GeminiConfig;
import dev.arakiel.gemini.config.Range;

@Config(namespace = "twilightdelight", value = "twilight_delight")
public final class TDConfig {
    @ConfigEntry(
            value = "gameplay.effect_range",
            comment = "Range of hostile range effects."
    )
    @Range(min = 0, max = 128)
    public int effectRange = 6;

    @ConfigEntry(
            value = "gameplay.aurora_range",
            comment = "Range used by aurora-themed effects."
    )
    @Range(min = 0, max = 128)
    public int auroraRange = 24;

    @ConfigEntry(
            value = "gameplay.rich_soil_trials",
            comment = "Plant growth attempts per rich soil random tick."
    )
    @Range(min = 0, max = 32)
    public int richSoilTrials = 5;

    @ConfigEntry(
            value = "client.aurora_period",
            comment = "Ticks between aurora color changes."
    )
    @Range(min = 0, max = 10000)
    public int auroraPeriod = 30;

    private static ConfigHandle<TDConfig> handle;

    public TDConfig() {
    }

    public static void init() {
        if (handle == null) {
            handle = GeminiConfig.register(TDConfig.class);
        }
    }

    public static int effectRange() {
        return handle == null ? 6 : handle.value().effectRange;
    }

    public static int auroraRange() {
        return handle == null ? 24 : handle.value().auroraRange;
    }

    public static int richSoilTrials() {
        return handle == null ? 5 : handle.value().richSoilTrials;
    }

    public static int auroraPeriod() {
        return handle == null ? 30 : handle.value().auroraPeriod;
    }
}
