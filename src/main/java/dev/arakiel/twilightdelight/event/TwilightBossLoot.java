package dev.arakiel.twilightdelight.event;

import dev.arakiel.twilightdelight.loot.BossLootCountFunction;
import dev.arakiel.twilightdelight.registry.TDItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import twilightforest.init.TFItems;

public final class TwilightBossLoot {
    private static final ResourceKey<LootTable> NAGA =
            entityLoot("naga");
    private static final ResourceKey<LootTable> LICH =
            entityLoot("lich");
    private static final ResourceKey<LootTable> MINOSHROOM =
            entityLoot("minoshroom");

    private TwilightBossLoot() {
    }

    public static void init() {
        LootTableEvents.MODIFY.register((key, table, source, registries) -> {
            if (NAGA.equals(key)) {
                addNagaMeat(table);
            } else if (LICH.equals(key)) {
                addLichBones(table);
            } else if (MINOSHROOM.equals(key)) {
                addMinoshroomSteaks(table);
            }
        });
    }

    private static void addNagaMeat(LootTable.Builder table) {
        table.pool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(TDItems.RAW_NAGA_MEAT)
                        .when(onFire(false)))
                .apply(() -> new BossLootCountFunction(5, 10))
                .build());
        table.pool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(TDItems.COOKED_NAGA_MEAT)
                        .when(onFire(true)))
                .apply(() -> new BossLootCountFunction(5, 10))
                .build());
    }

    private static void addLichBones(LootTable.Builder table) {
        table.pool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(TDItems.WITCHCRAFT_BONE))
                .apply(() -> new BossLootCountFunction(2, 6))
                .build());
    }

    private static void addMinoshroomSteaks(LootTable.Builder table) {
        table.pool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(TDItems.RAW_TOMAHAWK_SMEAXE)
                        .when(killedByMinotaurAxe())
                        .when(onFire(false)))
                .build());
        table.pool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(TDItems.COOKED_TOMAHAWK_SMEAXE)
                        .when(killedByMinotaurAxe())
                        .when(onFire(true)))
                .build());
    }

    private static LootItemCondition.Builder onFire(boolean onFire) {
        return LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity()
                        .flags(EntityFlagsPredicate.Builder.flags()
                                .setOnFire(onFire))
        );
    }

    private static LootItemCondition.Builder killedByMinotaurAxe() {
        return LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.ATTACKER,
                EntityPredicate.Builder.entity()
                        .equipment(EntityEquipmentPredicate.Builder.equipment()
                                .mainhand(ItemPredicate.Builder.item()
                                        .of(TFItems.DIAMOND_MINOTAUR_AXE.get())))
        );
    }

    private static ResourceKey<LootTable> entityLoot(String path) {
        return ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(
                        "twilightforest",
                        "entities/" + path
                )
        );
    }
}
