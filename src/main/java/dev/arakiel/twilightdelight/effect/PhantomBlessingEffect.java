package dev.arakiel.twilightdelight.effect;

import dev.arakiel.twilightdelight.registry.TDEffects;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public final class PhantomBlessingEffect extends MobEffect {
    private static final float SHARE_PER_PIECE = 0.1F;
    private static final float MAX_SHARE = 0.8F;
    private static final List<EquipmentSlot> ARMOR_SLOTS = List.of(
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    );

    public PhantomBlessingEffect() {
        super(MobEffectCategory.BENEFICIAL, 9075142);
    }

    public static float shareDamage(LivingEntity target, DamageSource source, float damage) {
        if (damage <= 0.0F || source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            return damage;
        }
        MobEffectInstance effect = target.getEffect(
                net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(TDEffects.PHANTOM_BLESSING)
        );
        if (effect == null) {
            return damage;
        }

        List<ArmorPiece> pieces = new ArrayList<>();
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack stack = target.getItemBySlot(slot);
            if (!stack.isEmpty() && hasArmorPoints(stack, slot)) {
                pieces.add(new ArmorPiece(slot, stack));
            }
        }
        if (pieces.isEmpty()) {
            return damage;
        }

        int level = effect.getAmplifier() + 1;
        float share = Math.min(SHARE_PER_PIECE * level, MAX_SHARE / pieces.size());
        float shared = damage * share;
        for (ArmorPiece piece : pieces) {
            piece.stack().hurtAndBreak(
                    Math.max(1, (int) Math.ceil(shared)),
                    target,
                    piece.slot()
            );
        }
        return damage - shared * pieces.size();
    }

    private static boolean hasArmorPoints(ItemStack stack, EquipmentSlot slot) {
        List<AttributeModifier> modifiers = new ArrayList<>();
        stack.forEachModifier(slot, (attribute, modifier) -> {
            if (attribute.value() == Attributes.ARMOR.value()) {
                modifiers.add(modifier);
            }
        });
        return modifiers.stream().anyMatch(modifier -> modifier.amount() > 0.0);
    }

    private record ArmorPiece(EquipmentSlot slot, ItemStack stack) {
    }
}
