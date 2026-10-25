package dev.arakiel.twilightdelight.mixin;

import dev.arakiel.twilightdelight.item.TomahawkFoodItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractCookingRecipe.class)
abstract class CookingRecipeComponentMixin {
    @Inject(method = "assemble", at = @At("RETURN"), require = 0)
    private void twilightdelight$copyTomahawkComponents(
            SingleRecipeInput input,
            HolderLookup.Provider registries,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        ItemStack meat = input.item();
        ItemStack result = cir.getReturnValue();
        if (meat.getItem() instanceof TomahawkFoodItem
                && result.getItem() instanceof TomahawkFoodItem) {
            result.applyComponents(meat.getComponents());
        }
    }
}
