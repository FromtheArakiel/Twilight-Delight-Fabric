package dev.arakiel.twilightdelight.registry;

import dev.arakiel.twilightdelight.TwilightDelightFabric;
import dev.arakiel.twilightdelight.recipe.FrozenRecipe;
import dev.arakiel.twilightdelight.recipe.ScepterDuplicateRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public final class TwilightRecipes {
    public static final RecipeType<FrozenRecipe> FROZEN = Registry.register(
            BuiltInRegistries.RECIPE_TYPE,
            TwilightDelightFabric.id("frozen"),
            new RecipeType<>() {
                @Override
                public String toString() {
                    return TwilightDelightFabric.MOD_ID + ":frozen";
                }
            }
    );
    public static final RecipeSerializer<FrozenRecipe> FROZEN_SERIALIZER = Registry.register(
            BuiltInRegistries.RECIPE_SERIALIZER,
            TwilightDelightFabric.id("frozen"),
            new FrozenRecipe.Serializer()
    );
    public static final RecipeSerializer<ScepterDuplicateRecipe> SCEPTER_DUPLICATE_SERIALIZER =
            Registry.register(
                    BuiltInRegistries.RECIPE_SERIALIZER,
                    TwilightDelightFabric.id("scepter_duplicate"),
                    new ScepterDuplicateRecipe.Serializer()
            );

    private TwilightRecipes() {
    }

    public static void init() {
    }
}
