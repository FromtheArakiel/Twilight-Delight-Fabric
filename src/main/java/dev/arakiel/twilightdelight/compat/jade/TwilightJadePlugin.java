package dev.arakiel.twilightdelight.compat.jade;

import dev.arakiel.gemini.integration.GeminiIntegration;
import dev.arakiel.gemini.integration.GeminiIntegrationType;
import dev.arakiel.twilightdelight.block.FieryCookingPotBlock;
import dev.arakiel.twilightdelight.block.MazeStoveBlock;
import dev.arakiel.twilightdelight.block.MazeStoveBlockEntity;
import dev.arakiel.twilightdelight.block.TwilightFeastBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IPluginConfig;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.FeastBlock;

@WailaPlugin("twilightdelight")
@GeminiIntegration(value = "jade", type = GeminiIntegrationType.CLIENT)
public final class TwilightJadePlugin implements IWailaPlugin {
    public void applyClient() {
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(
                new CookingPotProvider(),
                FieryCookingPotBlock.class
        );
        registration.registerBlockComponent(
                new MazeStoveProvider(),
                MazeStoveBlock.class
        );
        registration.registerBlockComponent(
                new FeastServingsProvider(),
                TwilightFeastBlock.class
        );
    }

    private static final class CookingPotProvider implements IBlockComponentProvider {
        private static final ResourceLocation UID =
                ResourceLocation.fromNamespaceAndPath("twilightdelight", "fiery_pot");

        @Override
        public void appendTooltip(
                ITooltip tooltip,
                BlockAccessor accessor,
                IPluginConfig config
        ) {
            tooltip.add(Component.translatable("jade.twilightdelight.fiery_pot.heated")
                    .withStyle(ChatFormatting.GOLD));
            if (accessor.getBlockEntity() instanceof CookingPotBlockEntity pot
                    && !pot.getMeal().isEmpty()) {
                tooltip.add(Component.translatable(
                        "jade.twilightdelight.fiery_pot.meal",
                        pot.getMeal().getHoverName()
                ));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return UID;
        }
    }

    private static final class MazeStoveProvider implements IBlockComponentProvider {
        private static final ResourceLocation UID =
                ResourceLocation.fromNamespaceAndPath("twilightdelight", "maze_stove");

        @Override
        public void appendTooltip(
                ITooltip tooltip,
                BlockAccessor accessor,
                IPluginConfig config
        ) {
            boolean lit = accessor.getBlockState().getValue(MazeStoveBlock.LIT);
            tooltip.add(Component.translatable(
                    lit
                            ? "jade.twilightdelight.maze_stove.active"
                            : "jade.twilightdelight.maze_stove.inactive"
            ).withStyle(lit ? ChatFormatting.GOLD : ChatFormatting.GRAY));
            if (accessor.getBlockEntity() instanceof MazeStoveBlockEntity stove) {
                int occupied = 0;
                for (int slot = 0; slot < stove.getItems().getSlotCount(); slot++) {
                    if (!stove.getItems().getStackInSlot(slot).isEmpty()) {
                        occupied++;
                    }
                }
                tooltip.add(Component.translatable(
                        "jade.twilightdelight.maze_stove.slots",
                        occupied,
                        stove.getItems().getSlotCount()
                ));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return UID;
        }
    }

    private static final class FeastServingsProvider implements IBlockComponentProvider {
        private static final ResourceLocation UID =
                ResourceLocation.fromNamespaceAndPath(
                        "twilightdelight",
                        "feast_servings"
                );

        @Override
        public void appendTooltip(
                ITooltip tooltip,
                BlockAccessor accessor,
                IPluginConfig config
        ) {
            var state = accessor.getBlockState();
            if (state.getBlock() instanceof FeastBlock feast) {
                tooltip.add(Component.translatable(
                        "jade.twilightdelight.feast.servings",
                        state.getValue(FeastBlock.SERVINGS),
                        feast.getMaxServings()
                ));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return UID;
        }
    }
}
