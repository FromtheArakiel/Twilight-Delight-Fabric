package dev.arakiel.twilightdelight.component;

import dev.arakiel.gemini.component.Component;
import dev.arakiel.gemini.component.ComponentFactoryRegistries;
import dev.arakiel.gemini.component.ComponentInitializers;
import dev.arakiel.gemini.component.ComponentKey;
import dev.arakiel.gemini.component.GeminiComponents;
import dev.arakiel.twilightdelight.TwilightDelightFabric;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public final class TDComponents implements ComponentInitializers.ItemStack {
    private static ComponentKey<Sips> sips;
    private static ComponentKey<Uses> uses;

    public TDComponents() {
    }

    public static ComponentKey<Sips> sips() {
        if (sips == null) {
            sips = GeminiComponents.key(
                    TwilightDelightFabric.id("sips"),
                    Sips.class
            );
        }
        return sips;
    }

    public static ComponentKey<Uses> uses() {
        if (uses == null) {
            uses = GeminiComponents.key(
                    TwilightDelightFabric.id("uses"),
                    Uses.class
            );
        }
        return uses;
    }

    @Override
    public void registerItemStackComponentFactories(
            ComponentFactoryRegistries.ItemStack registry
    ) {
        registry.register(sips(), stack -> new Sips());
        registry.register(uses(), stack -> new Uses());
    }

    public static final class Sips implements Component {
        private int value = 4;

        public int value() {
            return this.value;
        }

        public void value(int value) {
            this.value = value;
        }

        @Override
        public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
            this.value = tag.getInt("value");
        }

        @Override
        public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
            if (this.value != 4) {
                tag.putInt("value", this.value);
            }
        }
    }

    public static final class Uses implements Component {
        private int value;

        public int value() {
            return this.value;
        }

        public void value(int value) {
            this.value = value;
        }

        @Override
        public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
            this.value = tag.getInt("value");
        }

        @Override
        public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
            if (this.value != 0) {
                tag.putInt("value", this.value);
            }
        }
    }
}
