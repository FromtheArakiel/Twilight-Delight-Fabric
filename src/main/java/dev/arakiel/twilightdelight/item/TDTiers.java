package dev.arakiel.twilightdelight.item;

import dev.arakiel.twilightdelight.registry.TDItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum TDTiers implements Tier {
    ICE(1561, 8.0F, 3.0F, 10, Ingredient.of(Items.ICE)),
    TEARDROP(1536, 9.0F, 5.0F, 30, Ingredient.EMPTY),
    WROUGHT_IRON(250, 6.0F, 4.0F, 14, Ingredient.of(Items.IRON_INGOT)),
    TOMAHAWK(250, 1.6F, 0.0F, 10, Ingredient.EMPTY);

    private final int uses;
    private final float speed;
    private final float attackDamage;
    private final int enchantmentValue;
    private final Ingredient repairIngredient;

    TDTiers(
            int uses,
            float speed,
            float attackDamage,
            int enchantmentValue,
            Ingredient repairIngredient
    ) {
        this.uses = uses;
        this.speed = speed;
        this.attackDamage = attackDamage;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses() {
        return this.uses;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block>
    getIncorrectBlocksForDrops() {
        return this == WROUGHT_IRON
                ? BlockTags.INCORRECT_FOR_IRON_TOOL
                : BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this == TEARDROP
                ? Ingredient.of(TDItems.EXPERIMENT_113)
                : this.repairIngredient;
    }
}
