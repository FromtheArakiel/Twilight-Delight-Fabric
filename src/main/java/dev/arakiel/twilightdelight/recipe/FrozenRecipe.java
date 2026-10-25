package dev.arakiel.twilightdelight.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.arakiel.twilightdelight.registry.TwilightRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public final class FrozenRecipe implements Recipe<SingleRecipeInput> {
    private final Ingredient ingredient;
    private final ItemStack result;

    public FrozenRecipe(Ingredient ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    public Ingredient ingredient() {
        return ingredient;
    }

    public ItemStack result() {
        return result;
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return ingredient.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, HolderLookup.Provider registries) {
        return result.copyWithCount(result.getCount() * input.item().getCount());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(ingredient);
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TwilightRecipes.FROZEN_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return TwilightRecipes.FROZEN;
    }

    public static final class Serializer implements RecipeSerializer<FrozenRecipe> {
        private static final MapCodec<FrozenRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                ).apply(instance, FrozenRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, FrozenRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC,
                        recipe -> recipe.ingredient,
                        ItemStack.STREAM_CODEC,
                        recipe -> recipe.result,
                        FrozenRecipe::new
                );

        @Override
        public MapCodec<FrozenRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FrozenRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
