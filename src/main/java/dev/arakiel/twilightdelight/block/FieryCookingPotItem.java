package dev.arakiel.twilightdelight.block;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.common.item.CookingPotItem;

public final class FieryCookingPotItem extends CookingPotItem {
    public FieryCookingPotItem(Block block, Properties properties) {
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
        tooltip.add(Component.translatable(this.getDescriptionId() + ".tooltip")
                .withStyle(ChatFormatting.GRAY));
    }
}
