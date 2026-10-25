package dev.arakiel.twilightdelight.effect;

import dev.arakiel.twilightdelight.TwilightDelightFabric;
import dev.arakiel.twilightdelight.config.TDConfig;
import dev.arakiel.twilightdelight.registry.TwilightRecipes;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.phys.AABB;

public final class RangeEffect extends MobEffect {
    public enum Kind {
        FIRE,
        POISON,
        FROZEN
    }

    private final Kind kind;

    public RangeEffect(Kind kind, int color) {
        super(MobEffectCategory.BENEFICIAL, color);
        this.kind = kind;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity user, int amplifier) {
        if (user.level().isClientSide) {
            return true;
        }
        switch (kind) {
            case FIRE -> user.clearFire();
            case POISON -> {
                if (user.hasEffect(MobEffects.POISON)) {
                    user.removeEffect(MobEffects.POISON);
                }
            }
            case FROZEN -> {
                if (user.hasEffect(twilightforest.init.TFMobEffects.FROSTY)) {
                    user.removeEffect(twilightforest.init.TFMobEffects.FROSTY);
                }
            }
        }
        if (user.tickCount % 10 != 0) {
            return true;
        }
        double range = TDConfig.effectRange();
        AABB area = user.getBoundingBox().inflate(range);
        List<Mob> targets = user.level().getEntitiesOfClass(
                Mob.class,
                area,
                mob -> mob != user && mob instanceof Enemy
        );
        for (Mob mob : targets) {
            switch (this.kind) {
                case FIRE -> mob.setRemainingFireTicks((amplifier + 2) * 20);
                case POISON -> mob.addEffect(new MobEffectInstance(MobEffects.POISON, 26));
                case FROZEN -> mob.addEffect(new MobEffectInstance(
                        twilightforest.init.TFMobEffects.FROSTY,
                        21,
                        4
                ));
            }
        }
        if (kind == Kind.FROZEN) {
            for (ItemEntity item : user.level().getEntitiesOfClass(ItemEntity.class, area)) {
                SingleRecipeInput input = new SingleRecipeInput(item.getItem());
                user.level().getRecipeManager()
                        .getRecipeFor(TwilightRecipes.FROZEN, input, user.level())
                        .ifPresent(recipe -> {
                            item.setTicksFrozen(item.getTicksFrozen() + 10);
                            if (item.isFullyFrozen()) {
                                item.setItem(recipe.value().assemble(
                                        input,
                                        user.level().registryAccess()
                                ));
                                item.setTicksFrozen(0);
                            }
                        });
            }
        }
        return true;
    }
}
