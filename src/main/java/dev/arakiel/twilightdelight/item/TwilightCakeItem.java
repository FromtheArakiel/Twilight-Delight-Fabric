package dev.arakiel.twilightdelight.item;

import dev.arakiel.twilightdelight.registry.TDItems;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

public final class TwilightCakeItem extends BlockItem {
    public TwilightCakeItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        super.appendHoverText(stack, context, tooltip, flag);
        if (vectorwing.farmersdelight.common.Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            TDFoodItem.getFoodEffects(new ItemStack(TDItems.TWILIGHT_CAKE_SLICE), tooltip);
        }
    }
}
