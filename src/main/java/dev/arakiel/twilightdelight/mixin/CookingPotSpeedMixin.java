package dev.arakiel.twilightdelight.mixin;

import dev.arakiel.twilightdelight.registry.TDBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

@Mixin(CookingPotBlockEntity.class)
abstract class CookingPotSpeedMixin {
    @Shadow(remap = false)
    private int cookTime;

    @Inject(method = "processCooking", at = @At("HEAD"), remap = false)
    private void twilightdelight$accelerateCooking(
            RecipeHolder<CookingPotRecipe> recipe,
            CookingPotBlockEntity pot,
            CallbackInfoReturnable<Boolean> cir
    ) {
        BlockEntity self = (BlockEntity) (Object) this;
        if (self.getLevel() == null) {
            return;
        }
        boolean fiery = self.getBlockState().is(TDBlocks.FIERY_POT);
        boolean maze = self.getLevel()
                .getBlockState(self.getBlockPos().below())
                .is(TDBlocks.MAZE_STOVE);
        if (!fiery && !maze) {
            return;
        }

        ItemStack result = recipe.value()
                .getResultItem(self.getLevel().registryAccess());
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.getItem());
        if (!"twilightforest".equals(id.getNamespace())
                && !"twilightdelight".equals(id.getNamespace())) {
            return;
        }

        int original = recipe.value().getCookTime();
        int reduced = original;
        if (fiery) {
            reduced = Math.min(200, reduced);
        }
        if (maze) {
            reduced /= 2;
        }
        int total = Math.max(1, reduced);
        int factor = original / total;
        cookTime += factor - 1;
    }
}
