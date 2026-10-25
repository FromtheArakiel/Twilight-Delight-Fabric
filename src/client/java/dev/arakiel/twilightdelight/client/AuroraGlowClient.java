package dev.arakiel.twilightdelight.client;

import dev.arakiel.twilightdelight.config.TDConfig;
import dev.arakiel.twilightdelight.registry.TDEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class AuroraGlowClient {
    private AuroraGlowClient() {
    }

    public static boolean shouldRender(Entity entity) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return false;
        }
        return player.hasEffect(
                net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(
                        TDEffects.AURORA_GLOWING
                )
        ) || entity instanceof LivingEntity living && living.hasEffect(
                net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(
                        TDEffects.AURORA_GLOWING
                )
        );
    }

    public static int color(Entity entity) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return -1;
        }
        float partialTick = Minecraft.getInstance()
                .getTimer()
                .getGameTimeDeltaPartialTick(true);
        Vec3 offset = entity.getPosition(partialTick)
                .subtract(player.getPosition(partialTick));
        double direction = Math.atan2(offset.x, offset.z) / Math.PI * 180.0 / 6.0;
        float tick = (float) entity.level().getGameTime()
                + partialTick
                + (float) direction;
        return hsb(
                tick / Math.max(1, TDConfig.auroraPeriod()) % 1.0F,
                1.0F,
                1.0F
        );
    }

    private static int hsb(float hue, float saturation, float brightness) {
        float h = (hue - (float) Math.floor(hue)) * 6.0F;
        float fraction = h - (float) Math.floor(h);
        float p = brightness * (1.0F - saturation);
        float q = brightness * (1.0F - saturation * fraction);
        float t = brightness * (1.0F - saturation * (1.0F - fraction));
        float r;
        float g;
        float b;
        switch ((int) h) {
            case 0 -> {
                r = brightness;
                g = t;
                b = p;
            }
            case 1 -> {
                r = q;
                g = brightness;
                b = p;
            }
            case 2 -> {
                r = p;
                g = brightness;
                b = t;
            }
            case 3 -> {
                r = p;
                g = q;
                b = brightness;
            }
            case 4 -> {
                r = t;
                g = p;
                b = brightness;
            }
            default -> {
                r = brightness;
                g = p;
                b = q;
            }
        }
        int red = (int) (r * 255.0F + 0.5F);
        int green = (int) (g * 255.0F + 0.5F);
        int blue = (int) (b * 255.0F + 0.5F);
        return 0xFF000000 | red << 16 | green << 8 | blue;
    }
}
