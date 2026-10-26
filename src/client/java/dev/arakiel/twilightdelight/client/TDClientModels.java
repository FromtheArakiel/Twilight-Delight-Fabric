package dev.arakiel.twilightdelight.client;

import dev.arakiel.twilightdelight.TwilightDelightFabric;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;

public final class TDClientModels {
    public static final ResourceLocation WROUGHT_IRON_SWORD_HAND =
            TwilightDelightFabric.id("item/wrought_iron_sword_hand");

    private TDClientModels() {
    }

    public static void init() {
        ModelLoadingPlugin.register(context ->
                context.addModels(WROUGHT_IRON_SWORD_HAND)
        );
    }
}
