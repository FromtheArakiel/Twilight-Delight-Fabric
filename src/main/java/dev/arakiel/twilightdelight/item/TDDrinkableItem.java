package dev.arakiel.twilightdelight.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class TDDrinkableItem extends TDFoodItem {
    public TDDrinkableItem(Properties properties, boolean fast) {
        super(properties, fast);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
}
