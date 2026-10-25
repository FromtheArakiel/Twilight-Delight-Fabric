package dev.arakiel.twilightdelight.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;

public final class WroughtIronSwordItem extends SwordItem {
    private static final ResourceLocation REACH_ID =
            ResourceLocation.fromNamespaceAndPath("twilightdelight", "wrought_iron_sword");

    public WroughtIronSwordItem() {
        super(
                TDTiers.WROUGHT_IRON,
                new Properties().attributes(
                        SwordItem.createAttributes(TDTiers.WROUGHT_IRON, 3, -2.4F)
                                .withModifierAdded(
                                        Attributes.ENTITY_INTERACTION_RANGE,
                                        new AttributeModifier(
                                                REACH_ID,
                                                1.0,
                                                AttributeModifier.Operation.ADD_VALUE
                                        ),
                                        EquipmentSlotGroup.MAINHAND
                                )
                )
        );
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable(getDescriptionId() + ".tooltip")
                .withStyle(ChatFormatting.GRAY));
    }
}
