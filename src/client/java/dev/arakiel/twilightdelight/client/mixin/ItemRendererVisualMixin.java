package dev.arakiel.twilightdelight.client.mixin;

import dev.arakiel.twilightdelight.registry.TDItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
abstract class ItemRendererVisualMixin {
    private static final ModelResourceLocation WROUGHT_HAND_MODEL =
            new ModelResourceLocation(
                    ResourceLocation.fromNamespaceAndPath(
                            "twilightdelight",
                            "item/wrought_iron_sword_hand"
                    ),
                    "standalone"
            );

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true)
    private BakedModel twilightdelight$perspectiveModel(
            BakedModel model,
            ItemStack stack,
            ItemDisplayContext context
    ) {
        if (stack.is(TDItems.WROUGHT_IRON_SWORD)
                && context != ItemDisplayContext.GUI) {
            return Minecraft.getInstance()
                    .getModelManager()
                    .getModel(WROUGHT_HAND_MODEL);
        }
        return model;
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int twilightdelight$emissiveLight(int light, ItemStack stack) {
        return stack.is(TDItems.FIERY_KNIFE) || stack.is(TDItems.TEARDROP_SWORD)
                ? 0xF000F0
                : light;
    }
}
