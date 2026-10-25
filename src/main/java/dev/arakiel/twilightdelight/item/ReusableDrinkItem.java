package dev.arakiel.twilightdelight.item;

import dev.arakiel.twilightdelight.component.TDComponents;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import twilightforest.init.TFItems;

public final class ReusableDrinkItem extends Item {
    private static final int MAX_SIPS = 4;

    public ReusableDrinkItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationModifier(0.3F)
                        .effect(new MobEffectInstance(
                                net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(
                                        dev.arakiel.twilightdelight.registry.TDEffects.LICH_CHARGE
                                ),
                                3600
                        ), 1.0F)
                        .build()));
    }

    public static int getSips(ItemStack stack) {
        TDComponents.Sips sips = dev.arakiel.gemini.component.GeminiComponents.get(
                TDComponents.sips(),
                stack
        );
        return Math.max(0, Math.min(MAX_SIPS, sips.value()));
    }

    public static float getSipsProperty(ItemStack stack) {
        return (MAX_SIPS - getSips(stack)) / (float) MAX_SIPS;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand
    ) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide && user instanceof Player player) {
            FoodProperties food = stack.get(DataComponents.FOOD);
            if (food != null) {
                player.getFoodData().eat(food.nutrition(), food.saturation());
                for (FoodProperties.PossibleEffect possible : food.effects()) {
                    if (level.random.nextFloat() < possible.probability()) {
                        player.addEffect(possible.effect());
                    }
                }
            }
        }

        int remaining = getSips(stack) - 1;
        if (remaining <= 0) {
            return new ItemStack(TFItems.GREATER_FLASK.get());
        }
        dev.arakiel.gemini.component.GeminiComponents.get(TDComponents.sips(), stack)
                .value(remaining);
        return stack;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        if (vectorwing.farmersdelight.common.Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            TDFoodItem.getFoodEffects(stack, tooltip);
        }
    }
}
