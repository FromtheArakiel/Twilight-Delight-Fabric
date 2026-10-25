package dev.arakiel.twilightdelight.effect;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class LichChargeEffect extends MobEffect {
    public LichChargeEffect() {
        super(MobEffectCategory.BENEFICIAL, 6962664);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide() && entity instanceof Player player) {
            for (int slot = 0; slot < 9; slot++) {
                repair(player.getInventory().getItem(slot), entity);
            }
            repair(player.getOffhandItem(), entity);
        }
        return true;
    }

    private static void repair(ItemStack stack, LivingEntity entity) {
        if (stack.isEmpty() || !stack.isDamageableItem() || !stack.isDamaged()) {
            return;
        }
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (!"twilightforest".equals(id.getNamespace()) || !id.getPath().contains("scepter")) {
            return;
        }
        int max = stack.getMaxDamage();
        if (max <= 0) {
            return;
        }
        int interval = Math.max(1, (int) Math.round(1000.0 / (1 + max)));
        if (entity.tickCount % interval == 0) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
