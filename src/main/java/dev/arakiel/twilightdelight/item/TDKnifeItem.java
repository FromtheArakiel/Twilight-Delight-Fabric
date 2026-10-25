package dev.arakiel.twilightdelight.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import vectorwing.farmersdelight.common.item.KnifeItem;

public class TDKnifeItem extends KnifeItem {
    public TDKnifeItem(Tier tier) {
        this(tier, new Item.Properties());
    }

    public TDKnifeItem(Tier tier, Item.Properties properties) {
        super(tier, properties.attributes(KnifeItem.createAttributes(tier, 0.5F, -2.0F)));
    }
}
