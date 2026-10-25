package dev.arakiel.twilightdelight.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;

public final class TeardropSwordItem extends SwordItem {
    public TeardropSwordItem() {
        super(
                TDTiers.TEARDROP,
                new Properties()
                        .rarity(Rarity.UNCOMMON)
                        .fireResistant()
                        .attributes(SwordItem.createAttributes(
                                TDTiers.TEARDROP,
                                3,
                                -2.4F
                        ))
        );
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean success = super.hurtEnemy(stack, target, attacker);
        if (success && !target.fireImmune()) {
            target.setRemainingFireTicks(20);
            if (Mth.nextInt(attacker.getRandom(), 1, 3) == 1) {
                target.addEffect(new MobEffectInstance(
                        net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(
                                dev.arakiel.twilightdelight.registry.TDEffects.TEMPORAL_SADNESS
                        ),
                        100,
                        0
                ));
            }
        }
        return success;
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> tooltip,
            TooltipFlag flag
    ) {
        super.appendHoverText(stack, context, tooltip, flag);
        tooltip.add(Component.translatable(getDescriptionId() + ".desc")
                .withStyle(ChatFormatting.GRAY));
    }
}
