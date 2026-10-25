package dev.arakiel.twilightdelight.item;

import dev.arakiel.gemini.tweak.FastUseItem;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class TDFoodItem extends Item implements FastUseItem {
    private final boolean fast;

    public TDFoodItem(Properties properties, boolean fast) {
        super(properties);
        this.fast = fast;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public boolean isFastUse(ItemStack stack) {
        return this.fast;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        if (vectorwing.farmersdelight.common.Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            getFoodEffects(stack, tooltip);
        }
    }

    public static void getFoodEffects(ItemStack stack, List<Component> tooltip) {
        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food == null) {
            return;
        }
        for (FoodProperties.PossibleEffect possible : food.effects()) {
            MobEffectInstance instance = possible.effect();
            MutableComponent text = Component.translatable(instance.getDescriptionId());
            MobEffect effect = instance.getEffect().value();
            if (instance.getAmplifier() > 0) {
                text = Component.translatable(
                        "potion.withAmplifier",
                        text,
                        Component.translatable("potion.potency." + instance.getAmplifier())
                );
            }
            if (instance.getDuration() > 20) {
                text = Component.translatable(
                        "potion.withDuration",
                        text,
                        MobEffectUtil.formatDuration(instance, 1.0F, 20.0F)
                );
            }
            Component line = text.withStyle(effect.getCategory().getTooltipFormatting());
            int chance = Math.round(possible.probability() * 100.0F);
            if (chance < 100) {
                line = Component.translatable(
                        "twilightdelight.tooltip.chance",
                        line,
                        chance
                );
            }
            tooltip.add(line);
        }
    }
}
