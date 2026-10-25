package dev.arakiel.twilightdelight.compat.jei;

import dev.arakiel.gemini.integration.GeminiIntegration;
import dev.arakiel.gemini.integration.GeminiIntegrationType;
import dev.arakiel.twilightdelight.registry.TDBlocks;
import dev.arakiel.twilightdelight.registry.TDItems;
import dev.arakiel.twilightdelight.registry.TwilightRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import twilightforest.init.TFItems;

@JeiPlugin
@GeminiIntegration(value = "jei", type = GeminiIntegrationType.CLIENT)
public final class TwilightJeiPlugin implements IModPlugin {
    private static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("twilightdelight", "jei");
    private FrozenRecipeCategory frozen;

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    public void applyClient() {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        frozen = new FrozenRecipeCategory(registration.getJeiHelpers().getGuiHelper());
        registration.addRecipeCategories(frozen);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null || frozen == null) {
            return;
        }
        registration.addRecipes(
                frozen.getRecipeType(),
                level.getRecipeManager()
                        .getAllRecipesFor(TwilightRecipes.FROZEN)
                        .stream()
                        .map(holder -> holder.value())
                        .toList()
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (frozen == null) {
            return;
        }
        registration.addRecipeCatalysts(
                frozen.getRecipeType(),
                TFItems.ICE_BOMB.get(),
                TDItems.GLACIER_ICE_TEA,
                TDBlocks.FIERY_POT,
                TDBlocks.MAZE_STOVE
        );
    }
}
