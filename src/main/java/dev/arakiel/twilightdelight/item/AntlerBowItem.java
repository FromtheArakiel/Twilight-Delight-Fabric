package dev.arakiel.twilightdelight.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public final class AntlerBowItem extends BowItem {
    public AntlerBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(
            ItemStack stack,
            net.minecraft.world.level.Level level,
            LivingEntity user,
            int remainingUseDuration
    ) {
        int duration = getUseDuration(stack, user);
        int elapsed = duration - remainingUseDuration;
        int doubledRemaining = duration - Math.min(duration, elapsed * 2);
        super.releaseUsing(stack, level, user, doubledRemaining);
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
