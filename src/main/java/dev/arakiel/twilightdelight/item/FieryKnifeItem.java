package dev.arakiel.twilightdelight.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.util.TFToolMaterials;

public final class FieryKnifeItem extends TDKnifeItem {
    public FieryKnifeItem() {
        super(
                TFToolMaterials.FIERY,
                new Item.Properties().rarity(Rarity.UNCOMMON).fireResistant()
        );
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (result && !target.level().isClientSide() && !target.fireImmune()) {
            target.setRemainingFireTicks(300);
        } else {
            for (int i = 0; i < 20; i++) {
                double x = target.getX() + target.level().getRandom().nextFloat()
                        * target.getBbWidth() * 2.0F - target.getBbWidth();
                double y = target.getY() + target.level().getRandom().nextFloat()
                        * target.getBbHeight();
                double z = target.getZ() + target.level().getRandom().nextFloat()
                        * target.getBbWidth() * 2.0F - target.getBbWidth();
                target.level().addParticle(
                        ParticleTypes.FLAME,
                        x, y, z,
                        0.02, 0.02, 0.02
                );
            }
        }
        return result;
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
