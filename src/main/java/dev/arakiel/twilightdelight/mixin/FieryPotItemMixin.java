package dev.arakiel.twilightdelight.mixin;

import dev.arakiel.twilightdelight.registry.TDBlocks;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

@Mixin(CookingPotBlockEntity.class)
abstract class FieryPotItemMixin {
    @Inject(method = "getMealFromItem", at = @At("HEAD"), cancellable = true, remap = false)
    private static void twilightdelight$fieryMeal(
            ItemStack stack,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (!stack.is(TDBlocks.FIERY_POT.asItem())) {
            return;
        }
        cir.setReturnValue(stack.getOrDefault(
                ModDataComponents.MEAL.get(),
                ItemStackWrapper.EMPTY
        ).getStack());
    }

    @Inject(method = "takeServingFromItem", at = @At("HEAD"), cancellable = true, remap = false)
    private static void twilightdelight$fieryTakeServing(
            ItemStack stack,
            org.spongepowered.asm.mixin.injection.callback.CallbackInfo ci
    ) {
        if (!stack.is(TDBlocks.FIERY_POT.asItem())) {
            return;
        }
        ItemStack meal = stack.getOrDefault(
                ModDataComponents.MEAL.get(),
                ItemStackWrapper.EMPTY
        ).getStack().copy();
        meal.shrink(1);
        stack.set(ModDataComponents.MEAL.get(), new ItemStackWrapper(meal));
    }

    @Inject(method = "getContainerFromItem", at = @At("HEAD"), cancellable = true, remap = false)
    private static void twilightdelight$fieryContainer(
            ItemStack stack,
            CallbackInfoReturnable<ItemStack> cir
    ) {
        if (!stack.is(TDBlocks.FIERY_POT.asItem())) {
            return;
        }
        cir.setReturnValue(stack.getOrDefault(
                ModDataComponents.CONTAINER.get(),
                ItemStackWrapper.EMPTY
        ).getStack());
    }
}
