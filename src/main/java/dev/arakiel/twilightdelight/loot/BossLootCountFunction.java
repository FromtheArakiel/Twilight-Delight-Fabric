package dev.arakiel.twilightdelight.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.arakiel.twilightdelight.TwilightDelightFabric;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import vectorwing.farmersdelight.common.tag.ModTags;

public record BossLootCountFunction(int min, int max)
        implements LootItemFunction {
    private static final MapCodec<BossLootCountFunction> CODEC =
            RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Codec.INT.fieldOf("min").forGetter(BossLootCountFunction::min),
                    Codec.INT.fieldOf("max").forGetter(BossLootCountFunction::max)
            ).apply(instance, BossLootCountFunction::new));
    private static final LootItemFunctionType<BossLootCountFunction> TYPE =
            Registry.register(
                    BuiltInRegistries.LOOT_FUNCTION_TYPE,
                    TwilightDelightFabric.id("best_of_looting"),
                    new LootItemFunctionType<>(CODEC)
            );

    public BossLootCountFunction {
        if (min < 0 || max < min) {
            throw new IllegalArgumentException("Invalid boss loot count range");
        }
    }

    public static void init() {
    }

    @Override
    public ItemStack apply(ItemStack stack, LootContext context) {
        Entity attackerEntity = context.getParamOrNull(
                LootContextParams.ATTACKING_ENTITY
        );
        LivingEntity attacker = attackerEntity instanceof LivingEntity living
                ? living
                : null;
        int looting = 0;
        if (attacker != null) {
            var enchantments = context.getLevel()
                    .registryAccess()
                    .lookupOrThrow(Registries.ENCHANTMENT);
            looting = EnchantmentHelper.getEnchantmentLevel(
                    enchantments.getOrThrow(Enchantments.LOOTING),
                    attacker
            );
        }

        int result = min;
        for (int roll = 0; roll <= looting; roll++) {
            result = Math.max(
                    result,
                    min + context.getRandom().nextInt(max - min + 1)
            );
        }
        if (attacker != null
                && attacker.getMainHandItem().is(ModTags.Items.KNIVES)) {
            result = Math.round(result * 1.5F);
        }
        stack.setCount(Mth.clamp(result, 0, stack.getMaxStackSize()));
        return stack;
    }

    @Override
    public LootItemFunctionType<? extends LootItemFunction> getType() {
        return TYPE;
    }
}
