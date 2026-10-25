package dev.arakiel.twilightdelight.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import twilightforest.block.TrophyBlock;
import twilightforest.init.TFBlocks;
import twilightforest.item.TrophyItem;

public final class TrophyRenderHelper {
    private TrophyRenderHelper() {
    }

    public static void translate(TrophyItem item, PoseStack poseStack) {
        boolean ghast = item == ((TrophyBlock) TFBlocks.UR_GHAST_TROPHY.get()).asItem();
        if (ghast) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.translate(0.0, 0.7, 0.0);
        } else {
            poseStack.translate(0.0, 0.12, 0.0);
        }
    }
}
