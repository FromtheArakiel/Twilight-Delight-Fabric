package dev.arakiel.twilightdelight.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.arakiel.twilightdelight.client.TrophyRenderHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.item.TrophyItem;
import vectorwing.farmersdelight.client.renderer.CuttingBoardRenderer;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

@Mixin(CuttingBoardRenderer.class)
abstract class CuttingBoardTrophyMixin {
    @Inject(
            method = "render(Lvectorwing/farmersdelight/common/block/entity/CuttingBoardBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderStatic(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;IILcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;I)V"
            )
    )
    private void twilightdelight$trophyOffset(
            CuttingBoardBlockEntity board,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay,
            CallbackInfo ci
    ) {
        if (board.getStoredItem().getItem() instanceof TrophyItem trophy) {
            TrophyRenderHelper.translate(trophy, poseStack);
        }
    }
}
