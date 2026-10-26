package dev.arakiel.twilightdelight.client;

import dev.arakiel.twilightdelight.TwilightDelightFabric;
import dev.arakiel.twilightdelight.item.ReusableDrinkItem;
import dev.arakiel.twilightdelight.item.IronwoodKnifeItem;
import dev.arakiel.twilightdelight.item.SteeleafKnifeItem;
import dev.arakiel.twilightdelight.registry.TDBlocks;
import dev.arakiel.twilightdelight.registry.TDItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.client.Minecraft;

public final class TwilightDelightFabricClient implements ClientModInitializer {
    @Override
    @SuppressWarnings("deprecation")
    public void onInitializeClient() {
        TDClientModels.init();
        RangeEffectVisuals.init();
        TwilightDelightFabric.setCreativeStackDecorator(stack -> {
            if (Minecraft.getInstance().level == null) {
                return stack;
            }
            if (stack.getItem() instanceof IronwoodKnifeItem knife) {
                return knife.creativeStack(Minecraft.getInstance().level.registryAccess());
            }
            if (stack.getItem() instanceof SteeleafKnifeItem knife) {
                return knife.creativeStack(Minecraft.getInstance().level.registryAccess());
            }
            return stack;
        });
        FabricModelPredicateProviderRegistry.register(
                TDItems.CHAOS_INK_DRINK,
                TwilightDelightFabric.id("sips"),
                (stack, level, entity, seed) -> ReusableDrinkItem.getSipsProperty(stack)
        );
        FabricModelPredicateProviderRegistry.register(
                TDItems.ANTLER_BOW,
                ResourceLocation.withDefaultNamespace("pulling"),
                (stack, level, entity, seed) ->
                        entity != null && entity.isUsingItem() && entity.getUseItem() == stack
                                ? 1.0F
                                : 0.0F
        );
        FabricModelPredicateProviderRegistry.register(
                TDItems.ANTLER_BOW,
                ResourceLocation.withDefaultNamespace("pull"),
                (stack, level, entity, seed) ->
                        entity != null && entity.getUseItem() == stack
                                ? (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks())
                                * 2.0F / 20.0F
                                : 0.0F
        );

        ColorProviderRegistry.BLOCK.register(
                (state, level, pos, tint) ->
                        level != null && pos != null
                                ? BiomeColors.getAverageFoliageColor(level, pos)
                                : FoliageColor.getDefaultColor(),
                TDBlocks.IRONWOOD_LEAVES
        );
        ColorProviderRegistry.BLOCK.register(
                (state, level, pos, tint) ->
                        tint == 1
                                ? FoliageColor.get(0.5, 1.0)
                                : -1,
                TDBlocks.FIDDLEHEAD_CLUMP,
                TDBlocks.FIDDLEHEAD_CLUMP_UPPER
        );
        ColorProviderRegistry.ITEM.register(
                (stack, tint) -> stack.getItem() instanceof BlockItem blockItem
                        && blockItem.getBlock() == TDBlocks.IRONWOOD_LEAVES
                        ? FoliageColor.getDefaultColor()
                        : -1,
                TDBlocks.IRONWOOD_LEAVES.asItem()
        );
        ColorProviderRegistry.ITEM.register(
                (stack, tint) -> tint == 1
                        ? FoliageColor.get(0.5, 1.0)
                        : -1,
                TDBlocks.FIDDLEHEAD_CLUMP.asItem()
        );
    }
}
