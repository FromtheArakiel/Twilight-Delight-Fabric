package dev.arakiel.twilightdelight.item;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.util.TFToolMaterials;

public final class IronwoodKnifeItem extends TDKnifeItem {
    public IronwoodKnifeItem() {
        super(TFToolMaterials.IRONWOOD);
    }

    public ItemStack creativeStack(HolderLookup.Provider registries) {
        ItemStack stack = getDefaultInstance();
        var lookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
        stack.enchant(lookup.getOrThrow(Enchantments.KNOCKBACK), 1);
        stack.enchant(lookup.getOrThrow(Enchantments.UNBREAKING), 1);
        return stack;
    }
}
