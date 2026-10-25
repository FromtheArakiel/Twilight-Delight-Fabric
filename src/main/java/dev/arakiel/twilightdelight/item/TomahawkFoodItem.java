package dev.arakiel.twilightdelight.item;

import dev.arakiel.gemini.component.GeminiComponents;
import dev.arakiel.gemini.tweak.FastUseItem;
import dev.arakiel.twilightdelight.component.TDComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.KnifeItem;

public final class TomahawkFoodItem extends KnifeItem implements FastUseItem {
    private final int maxUses;

    public TomahawkFoodItem(net.minecraft.world.item.Item.Properties properties, int maxUses) {
        super(
                TDTiers.TOMAHAWK,
                properties.stacksTo(1).attributes(
                        KnifeItem.createAttributes(TDTiers.TOMAHAWK, 10.0F, -3.0F)
                )
        );
        this.maxUses = maxUses;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public boolean isFastUse(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.remaining(stack) < this.maxUses;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * this.remaining(stack) / this.maxUses);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Mth.hsvToRgb(this.remaining(stack) / (3.0F * this.maxUses), 1.0F, 1.0F);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        ItemStack consumed = super.finishUsingItem(stack, level, user);
        if (stack.isEmpty() || consumed != stack && consumed.isEmpty()) {
            stack.setCount(1);
        }
        int remaining = this.remaining(stack) - 1;
        if (remaining <= 0) {
            return new ItemStack(Items.BONE);
        }
        GeminiComponents.get(TDComponents.uses(), stack).value(remaining);
        return stack;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    private int remaining(ItemStack stack) {
        int stored = GeminiComponents.get(TDComponents.uses(), stack).value();
        return stored == 0 ? this.maxUses : Mth.clamp(stored, 0, this.maxUses);
    }
}
