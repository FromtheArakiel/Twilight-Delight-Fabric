package dev.arakiel.twilightdelight.event;

import dev.arakiel.gemini.combat.GeminiCombat;
import dev.arakiel.gemini.event.EventResult;
import dev.arakiel.gemini.event.GeminiEvents;
import dev.arakiel.gemini.event.effect.MobEffectApplicableEvent;
import dev.arakiel.gemini.event.entity.living.LivingHurtEvent;
import dev.arakiel.gemini.event.entity.player.PlayerInteractBlockEvent;
import dev.arakiel.twilightdelight.registry.TDBlocks;
import dev.arakiel.twilightdelight.registry.TDEffects;
import dev.arakiel.twilightdelight.registry.TDItems;
import dev.arakiel.twilightdelight.effect.PhantomBlessingEffect;
import java.util.List;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFMobEffects;
import twilightforest.block.Experiment115Block;

public final class TDEventHandlers {
    private static final ResourceKey<LootTable> SCRAP_115 = ResourceKey.create(
            Registries.LOOT_TABLE,
            ResourceLocation.fromNamespaceAndPath("twilightdelight", "scrap_115")
    );
    private static final ResourceKey<LootTable> KNIGHT_PHANTOM_LOOT =
            ResourceKey.create(
                    Registries.LOOT_TABLE,
                    ResourceLocation.fromNamespaceAndPath(
                            "twilightforest",
                            "entities/knight_phantom"
                    )
            );

    private TDEventHandlers() {
    }

    public static void init() {
        GeminiEvents.listen(LivingHurtEvent.class, TDEventHandlers::onLivingHurt);
        GeminiEvents.listen(MobEffectApplicableEvent.class, TDEventHandlers::onEffectApplicable);
        GeminiEvents.listen(PlayerInteractBlockEvent.class, TDEventHandlers::onUseBlock);
        ServerLivingEntityEvents.AFTER_DEATH.register(TDEventHandlers::onDeath);
        LootTableEvents.MODIFY.register((key, table, source, params) -> {
            if (KNIGHT_PHANTOM_LOOT.equals(key)) {
                table.pool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(TDItems.PHANTOM_BONE)
                                .apply(SetItemCountFunction.setCount(
                                        UniformGenerator.between(2.0F, 9.0F)
                                )))
                        .build());
            }
        });

    }

    private static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getSource().getDirectEntity() instanceof LivingEntity attacker)) {
            return;
        }
        LivingEntity target = (LivingEntity) event.getEntity();
        event.setAmount(PhantomBlessingEffect.shareDamage(
                target,
                event.getSource(),
                event.getAmount()
        ));
        ItemStack weapon = attacker.getMainHandItem();
        if (weapon.is(TDItems.FIERY_KNIFE) && !target.fireImmune()) {
            target.setRemainingFireTicks(20);
        }
        if (weapon.is(TDItems.TEARDROP_SWORD) && !target.fireImmune()) {
            target.setRemainingFireTicks(20);
        }
        if (weapon.is(TDItems.ICE_KNIFE)) {
            target.clearFire();
        }
        if (weapon.is(TDItems.KNIGHTMETAL_KNIFE)
                && !target.level().isClientSide
                && target.getArmorValue() > 0) {
            float bonus = target.getArmorCoverPercentage() > 0.0F
                    ? (int) (2.0F * target.getArmorCoverPercentage())
                    : 2.0F;
            event.setAmount(event.getAmount() + bonus);
            ((ServerLevel) target.level()).getChunkSource().broadcastAndSend(
                    target,
                    new ClientboundAnimatePacket(target, 5)
            );
        }
    }

    private static void onEffectApplicable(MobEffectApplicableEvent event) {
        LivingEntity entity = (LivingEntity) event.getEntity();
        var effect = event.getEffect().getEffect();
        if (effect.value() == MobEffects.POISON.value()
                && entity.hasEffect(
                BuiltInRegistries.MOB_EFFECT.wrapAsHolder(TDEffects.POISON_RANGE)
        )) {
            event.setResult(EventResult.DENY);
        }
        if (effect.value() == TFMobEffects.FROSTY.value()
                && entity.hasEffect(
                BuiltInRegistries.MOB_EFFECT.wrapAsHolder(TDEffects.FROZEN_RANGE)
        )) {
            event.setResult(EventResult.DENY);
        }
    }

    private static void onUseBlock(PlayerInteractBlockEvent event) {
        var level = event.getPlayer().level();
        BlockState state = level.getBlockState(event.getPos());
        if (event.getStack().is(TDItems.WITCHCRAFT_BONE_MEAL)
                && state.is(TFBlocks.UBEROUS_SOIL.get())) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.setBlockAndUpdate(
                        event.getPos(),
                        TDBlocks.TWILIGHT_RICH_SOIL.defaultBlockState()
                );
                serverLevel.playSound(
                        null,
                        event.getPos(),
                        SoundEvents.BONE_MEAL_USE,
                        SoundSource.BLOCKS,
                        1.0F,
                        1.0F
                );
                if (!event.getPlayer().getAbilities().instabuild) {
                    event.getStack().shrink(1);
                }
            }
            event.setCanceled(true);
            return;
        }

        if (!state.is(TFBlocks.EXPERIMENT_115.get())) {
            return;
        }
        int bites = state.getValue(Experiment115Block.BITES_TAKEN);
        if (event.getStack().is(Items.REDSTONE)
                && !state.getValue(Experiment115Block.REGENERATE)
                && bites > 0) {
            event.setCanceled(true);
            return;
        }
        if (!event.getStack().is(vectorwing.farmersdelight.common.tag.ModTags.Items.KNIVES)) {
            return;
        }
        if (level instanceof ServerLevel serverLevel) {
            if (bites < 7) {
                level.setBlock(
                        event.getPos(),
                        state.setValue(Experiment115Block.BITES_TAKEN, bites + 1),
                        3
                );
            } else {
                level.removeBlock(event.getPos(), false);
            }
            for (ItemStack drop : getLoot(
                    serverLevel,
                    SCRAP_115,
                    state,
                    event.getPos(),
                    event.getStack(),
                    event.getPlayer()
            )) {
                event.getPlayer().getInventory().placeItemBackInInventory(drop);
            }
            event.getStack().hurtAndBreak(
                    1,
                    event.getPlayer(),
                    LivingEntity.getSlotForHand(event.getPlayer().getUsedItemHand())
            );
        }
        level.playSound(
                event.getPlayer(),
                event.getPos(),
                SoundEvents.WOOL_BREAK,
                SoundSource.BLOCKS,
                1.0F,
                1.0F
        );
        event.setCanceled(true);
    }

    private static List<ItemStack> getLoot(
            ServerLevel level,
            ResourceKey<LootTable> id,
            BlockState state,
            BlockPos pos,
            ItemStack tool,
            LivingEntity entity
    ) {
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, tool)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .create(LootContextParamSets.BLOCK);
        return level.getServer()
                .reloadableRegistries()
                .getLootTable(id)
                .getRandomItems(params);
    }

    private static void onDeath(LivingEntity entity, net.minecraft.world.damagesource.DamageSource source) {
        if (!(source.getEntity() instanceof LivingEntity attacker)) {
            return;
        }
        boolean knife = attacker.getMainHandItem()
                .is(vectorwing.farmersdelight.common.tag.ModTags.Items.KNIVES);
        int looting = 0;
        if (attacker.level().registryAccess().lookup(Registries.ENCHANTMENT).isPresent()) {
            looting = EnchantmentHelper.getEnchantmentLevel(
                    attacker.level()
                            .registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(Enchantments.LOOTING),
                    attacker
            );
        }

        if (entity.getType() == twilightforest.init.TFEntities.NAGA.get()) {
            int amount = bestRoll(entity, 5, 10, looting);
            if (knife) {
                amount = Math.round(amount * 1.5F);
            }
            entity.spawnAtLocation(new ItemStack(
                    entity.isOnFire() ? TDItems.COOKED_NAGA_MEAT : TDItems.RAW_NAGA_MEAT,
                    amount
            ));
        }
        if (entity.getType() == twilightforest.init.TFEntities.LICH.get()) {
            int amount = bestRoll(entity, 2, 6, looting);
            if (knife) {
                amount = Math.round(amount * 1.5F);
            }
            entity.spawnAtLocation(new ItemStack(TDItems.WITCHCRAFT_BONE, amount));
        }
        if (entity.getType() == twilightforest.init.TFEntities.DEER.get()
                && entity.getRandom().nextFloat() < 0.3F + looting * 0.1F) {
            entity.spawnAtLocation(new ItemStack(TDItems.ANTLER));
        }

        boolean diamondAxe = attacker.getMainHandItem()
                .is(twilightforest.init.TFItems.DIAMOND_MINOTAUR_AXE.get());
        if (diamondAxe && entity.getType() == twilightforest.init.TFEntities.MINOTAUR.get()
                && entity.getRandom().nextFloat() < 0.3F) {
            entity.spawnAtLocation(new ItemStack(
                    entity.isOnFire() ? TDItems.COOKED_TOMAHAWK_SMEAK : TDItems.RAW_TOMAHAWK_SMEAK
            ));
        }
        if (diamondAxe && entity.getType() == twilightforest.init.TFEntities.MINOSHROOM.get()) {
            entity.spawnAtLocation(new ItemStack(
                    entity.isOnFire()
                            ? TDItems.COOKED_TOMAHAWK_SMEAXE
                            : TDItems.RAW_TOMAHAWK_SMEAXE
            ));
        }
        if (entity.getType() == twilightforest.init.TFEntities.HELMET_CRAB.get()
                && (knife || entity.getRandom().nextFloat() < 0.1F)) {
            entity.spawnAtLocation(new ItemStack(TDItems.HELMET_CRAB_CLAW));
        }
        if (entity.getType() == twilightforest.init.TFEntities.REDCAP.get()) {
            dropChance(entity, TDItems.OMAZEO, 0.025F);
            dropChance(entity, TDItems.REDCANP, 0.1F);
        }
        if (entity.getType() == twilightforest.init.TFEntities.REDCAP_SAPPER.get()) {
            dropChance(entity, TDItems.OMAZEO, 0.05F);
            dropChance(entity, TDItems.REDCANP, 0.15F);
        }
        if (knife) {
            if (entity.getType() == twilightforest.init.TFEntities.CARMINITE_GHASTGUARD.get()) {
                chanceWithLooting(entity, twilightforest.init.TFItems.EXPERIMENT_115.get(), 0.5F, 0.1F, looting);
            }
            if (entity.getType() == twilightforest.init.TFEntities.CARMINITE_GHASTLING.get()) {
                chanceWithLooting(entity, twilightforest.init.TFItems.EXPERIMENT_115.get(), 0.01F, 0.1F, looting);
            }
            if (entity.getType() == net.minecraft.world.entity.EntityType.GHAST) {
                chanceWithLooting(entity, twilightforest.init.TFItems.EXPERIMENT_115.get(), 0.3F, 0.1F, looting);
            }
            if (entity.getType() == twilightforest.init.TFEntities.BOAR.get() && !entity.isOnFire()) {
                chanceWithLooting(
                        entity,
                        vectorwing.farmersdelight.common.registry.ModItems.HAM.get(),
                        0.67F,
                        0.1F,
                        looting
                );
            }
        }
    }

    private static int bestRoll(LivingEntity entity, int min, int max, int looting) {
        int result = min;
        for (int i = 0; i <= looting; i++) {
            result = Math.max(result, min + entity.getRandom().nextInt(max - min + 1));
        }
        return result;
    }

    private static void dropChance(LivingEntity entity, net.minecraft.world.level.ItemLike item, float chance) {
        if (entity.getRandom().nextFloat() < chance) {
            entity.spawnAtLocation(new ItemStack(item));
        }
    }

    private static void chanceWithLooting(
            LivingEntity entity,
            net.minecraft.world.level.ItemLike item,
            float base,
            float perLevel,
            int looting
    ) {
        if (entity.getRandom().nextFloat() < base + perLevel * looting) {
            entity.spawnAtLocation(new ItemStack(item));
        }
    }
}
