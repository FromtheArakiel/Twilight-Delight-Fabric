package dev.arakiel.twilightdelight.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.arakiel.twilightdelight.registry.TDItems;
import dev.arakiel.twilightdelight.util.FieryCuttingRecipe;
import java.util.Optional;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

@Mixin(CuttingBoardBlockEntity.class)
abstract class CuttingBoardFieryMixin {
    @ModifyReturnValue(method = "getMatchingRecipe", at = @At("RETURN"), remap = false)
    private Optional<RecipeHolder<CuttingBoardRecipe>> twilightdelight$cookWithFieryKnife(
            Optional<RecipeHolder<CuttingBoardRecipe>> original,
            @Local(argsOnly = true) ItemStack tool
    ) {
        if (!tool.is(TDItems.FIERY_KNIFE)) {
            return original;
        }
        BlockEntity blockEntity = (BlockEntity) (Object) this;
        return FieryCuttingRecipe.transform(
                blockEntity.getLevel(),
                Vec3.atBottomCenterOf(blockEntity.getBlockPos()).add(0.0, 0.1, 0.0),
                original
        );
    }
}
