package dev.arakiel.twilightdelight.compat.jei;

import dev.arakiel.twilightdelight.recipe.FrozenRecipe;
import dev.arakiel.twilightdelight.registry.TDItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;

public final class FrozenRecipeCategory implements IRecipeCategory<FrozenRecipe> {
    private static final int WIDTH = 82;
    private static final int HEIGHT = 28;

    private final RecipeType<FrozenRecipe> type = RecipeType.create(
            "twilightdelight",
            "frozen",
            FrozenRecipe.class
    );
    private final IDrawable icon;

    public FrozenRecipeCategory(IGuiHelper helper) {
        icon = helper.createDrawableItemLike(TDItems.GLACIER_ICE_TEA);
    }

    @Override
    public RecipeType<FrozenRecipe> getRecipeType() {
        return type;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.twilightdelight.frozen");
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FrozenRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(4, 5)
                .setStandardSlotBackground()
                .addIngredients(recipe.ingredient());
        builder.addOutputSlot(58, 5)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }
}
