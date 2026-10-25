package dev.arakiel.twilightdelight.item;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.util.TFToolMaterials;

public final class SteeleafKnifeItem extends TDKnifeItem {
    public SteeleafKnifeItem() {
        super(TFToolMaterials.STEELEAF);
    }

    public ItemStack creativeStack(HolderLookup.Provider registries) {
        ItemStack stack = getDefaultInstance();
        var lookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
        stack.enchant(lookup.getOrThrow(Enchantments.LOOTING), 2);
        stack.enchant(lookup.getOrThrow(Enchantments.FORTUNE), 2);
        return stack;
    }
}
