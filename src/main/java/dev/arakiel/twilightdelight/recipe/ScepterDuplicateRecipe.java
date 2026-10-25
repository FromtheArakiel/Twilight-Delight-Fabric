package dev.arakiel.twilightdelight.recipe;

import com.mojang.serialization.MapCodec;
import dev.arakiel.twilightdelight.registry.TwilightRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public final class ScepterDuplicateRecipe implements CraftingRecipe {
    private final ShapedRecipe shapedRecipe;
    private final Item scepter;

    private ScepterDuplicateRecipe(ShapedRecipe shapedRecipe) {
        this.shapedRecipe = shapedRecipe;
        this.scepter = shapedRecipe.getResultItem(null).getItem();
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (!shapedRecipe.matches(input, level)) {
            return false;
        }
        ItemStack center = input.getItem(4);
        return !center.isEmpty() && center.is(scepter) && !center.isDamaged();
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return shapedRecipe.assemble(input, registries);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return shapedRecipe.canCraftInDimensions(width, height);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return shapedRecipe.getResultItem(registries);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return shapedRecipe.getIngredients();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = shapedRecipe.getRemainingItems(input);
        ItemStack center = input.getItem(4);
        if (!center.isEmpty() && center.is(scepter)) {
            remaining.set(4, center.copy());
        }
        return remaining;
    }

    @Override
    public CraftingBookCategory category() {
        return shapedRecipe.category();
    }

    @Override
    public String getGroup() {
        return shapedRecipe.getGroup();
    }

    @Override
    public boolean showNotification() {
        return shapedRecipe.showNotification();
    }

    @Override
    public ItemStack getToastSymbol() {
        return shapedRecipe.getToastSymbol();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return TwilightRecipes.SCEPTER_DUPLICATE_SERIALIZER;
    }

    public static final class Serializer implements RecipeSerializer<ScepterDuplicateRecipe> {
        private static final MapCodec<ScepterDuplicateRecipe> CODEC =
                ShapedRecipe.Serializer.CODEC.xmap(
                        ScepterDuplicateRecipe::new,
                        recipe -> recipe.shapedRecipe
                );
        private static final StreamCodec<RegistryFriendlyByteBuf, ScepterDuplicateRecipe> STREAM_CODEC =
                ShapedRecipe.Serializer.STREAM_CODEC.map(
                        ScepterDuplicateRecipe::new,
                        recipe -> recipe.shapedRecipe
                );

        @Override
        public MapCodec<ScepterDuplicateRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ScepterDuplicateRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
