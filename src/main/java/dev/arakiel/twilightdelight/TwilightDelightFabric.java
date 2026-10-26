package dev.arakiel.twilightdelight;

import dev.arakiel.gemini.creative.GeminiCreativeTabs;
import dev.arakiel.twilightdelight.config.TDConfig;
import dev.arakiel.twilightdelight.effect.TwilightTransformationMaps;
import dev.arakiel.twilightdelight.event.TDEventHandlers;
import dev.arakiel.twilightdelight.registry.TDBlocks;
import dev.arakiel.twilightdelight.registry.TDEffects;
import dev.arakiel.twilightdelight.registry.TDItems;
import dev.arakiel.twilightdelight.registry.TwilightRecipes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.function.UnaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TwilightDelightFabric implements ModInitializer {
    public static final String MOD_ID = "twilightdelight";
    public static final Logger LOGGER = LoggerFactory.getLogger("Twilight Delight");
    private static UnaryOperator<ItemStack> creativeStackDecorator = UnaryOperator.identity();

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        TwilightTransformationMaps.init();
        TwilightRecipes.init();
        TDEffects.init();
        TDItems.init();
        TDBlocks.init();
        TDBlocks.finishRegistration();
        TDConfig.init();
        TDEventHandlers.init();

        GeminiCreativeTabs.register(
                id("twilight_delight"),
                Component.translatable("itemGroup.twilightdelight.twilight_delight"),
                () -> new ItemStack(TDItems.BOWL_OF_CHAOS_STEW),
                output -> {
                    for (var item : BuiltInRegistries.ITEM) {
                        ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
                        if (MOD_ID.equals(key.getNamespace())) {
                            output.accept(creativeStackDecorator.apply(new ItemStack(item)));
                        }
                    }
                }
        );

        LOGGER.info("Twilight Delight initialized with {} items", TDItems.all().size());
    }

    public static void setCreativeStackDecorator(UnaryOperator<ItemStack> decorator) {
        creativeStackDecorator = decorator;
    }
}
