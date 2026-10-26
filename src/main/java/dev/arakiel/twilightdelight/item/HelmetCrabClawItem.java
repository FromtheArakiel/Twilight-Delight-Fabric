package dev.arakiel.twilightdelight.item;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.component.DataComponents;
import vectorwing.farmersdelight.common.tag.ModTags;

public final class HelmetCrabClawItem extends TDKnifeItem {
    private static final ShearsItem SHEARS = new ShearsItem(new Item.Properties());

    public HelmetCrabClawItem() {
        super(Tiers.IRON, new Item.Properties().component(
                DataComponents.TOOL,
                createToolProperties()
        ));
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        if (target instanceof Shearable shearable && shearable.readyForShearing()) {
            if (!target.level().isClientSide()) {
                shearable.shear(SoundSource.PLAYERS);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            }
            return InteractionResult.sidedSuccess(target.level().isClientSide());
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult result = SHEARS.useOn(context);
        return result == InteractionResult.PASS
                ? super.useOn(context)
                : result;
    }

    @Override
    public boolean mineBlock(
            ItemStack stack,
            Level level,
            BlockState state,
            BlockPos pos,
            LivingEntity entity
    ) {
        if (!level.isClientSide() && !state.is(BlockTags.FIRE)) {
            stack.hurtAndBreak(1, entity, EquipmentSlot.MAINHAND);
        }
        return state.is(BlockTags.LEAVES)
                || state.is(Blocks.COBWEB)
                || state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.FERN)
                || state.is(Blocks.DEAD_BUSH)
                || state.is(Blocks.HANGING_ROOTS)
                || state.is(Blocks.VINE)
                || state.is(Blocks.TRIPWIRE)
                || state.is(BlockTags.WOOL);
    }

    private static Tool createToolProperties() {
        return new Tool(
                List.of(
                        Tool.Rule.deniesDrops(Tiers.IRON.getIncorrectBlocksForDrops()),
                        Tool.Rule.minesAndDrops(
                                ModTags.Blocks.MINEABLE_WITH_KNIFE,
                                Tiers.IRON.getSpeed()
                        ),
                        Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F),
                        Tool.Rule.overrideSpeed(BlockTags.LEAVES, 15.0F),
                        Tool.Rule.overrideSpeed(BlockTags.WOOL, 5.0F),
                        Tool.Rule.overrideSpeed(
                                List.of(Blocks.VINE, Blocks.GLOW_LICHEN),
                                2.0F
                        )
                ),
                1.0F,
                1
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
