package dev.arakiel.twilightdelight.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class RoseTeaItem extends TDDrinkableItem {
    public static final ResourceKey<DamageType> ROSE_TEA = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath("twilightdelight", "thorn_rose_tea")
    );

    public RoseTeaItem(Properties properties, boolean fast) {
        super(properties, fast);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity consumer) {
        DamageSource source = new DamageSource(
                level.registryAccess()
                        .registryOrThrow(Registries.DAMAGE_TYPE)
                        .getHolderOrThrow(ROSE_TEA)
        );
        consumer.hurt(source, 4.0F);
        return super.finishUsingItem(stack, level, consumer);
    }
}
