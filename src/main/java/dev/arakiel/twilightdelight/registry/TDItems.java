package dev.arakiel.twilightdelight.registry;

import dev.arakiel.gemini.registration.GeminiRegistration;
import dev.arakiel.gemini.registration.annotations.RegistryNamespace;
import dev.arakiel.gemini.registration.container.ItemRegistryContainer;
import dev.arakiel.twilightdelight.TwilightDelightFabric;
import dev.arakiel.twilightdelight.item.FieryKnifeItem;
import dev.arakiel.twilightdelight.item.AntlerBowItem;
import dev.arakiel.twilightdelight.item.HelmetCrabClawItem;
import dev.arakiel.twilightdelight.item.IceKnifeItem;
import dev.arakiel.twilightdelight.item.IronwoodKnifeItem;
import dev.arakiel.twilightdelight.item.KnightmetalKnifeItem;
import dev.arakiel.twilightdelight.item.RoseTeaItem;
import dev.arakiel.twilightdelight.item.SteeleafKnifeItem;
import dev.arakiel.twilightdelight.item.TDDrinkableItem;
import dev.arakiel.twilightdelight.item.TDFoodItem;
import dev.arakiel.twilightdelight.item.TDKnifeItem;
import dev.arakiel.twilightdelight.item.TDTiers;
import dev.arakiel.twilightdelight.item.TeardropSwordItem;
import dev.arakiel.twilightdelight.item.TomahawkFoodItem;
import dev.arakiel.twilightdelight.item.WroughtIronSwordItem;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import twilightforest.util.TFToolMaterials;
import vectorwing.farmersdelight.common.registry.ModEffects;

@RegistryNamespace(TwilightDelightFabric.MOD_ID)
public final class TDItems implements ItemRegistryContainer {
    private static final Map<String, Item> ALL = new LinkedHashMap<>();

    public static final Item HYDRA_PIECE = food("hydra_piece", FoodKind.HYDRA_PIECE, 9, 2.0F, List.of(effect(MobEffects.REGENERATION, 600, 0, 1.0F)));
    public static final Item RAW_VENISON_RIB = food("raw_venison_rib", FoodKind.FAST, 2, 0.7F, List.of());
    public static final Item COOKED_VENISON_RIB = food("cooked_venison_rib", FoodKind.FAST, 4, 0.8F, List.of());
    public static final Item RAW_MEEF_SLICE = food("raw_meef_slice", FoodKind.FAST, 1, 0.7F, List.of());
    public static final Item COOKED_MEEF_SLICE = food("cooked_meef_slice", FoodKind.FAST, 3, 0.8F, List.of());
    public static final Item RAW_TOMAHAWK_SMEAK = food("raw_tomahawk_smeak", FoodKind.NONE, 6, 0.7F, List.of());
    public static final Item COOKED_TOMAHAWK_SMEAK = food("cooked_tomahawk_smeak", FoodKind.NONE, 12, 0.8F, List.of());
    public static final Item RAW_TOMAHAWK_SMEAXE = food("raw_tomahawk_smeaxe", FoodKind.TOMAHAWK, 6, 0.7F, List.of(effect(MobEffects.DAMAGE_BOOST, 400, 1, 1.0F)));
    public static final Item COOKED_TOMAHAWK_SMEAXE = food("cooked_tomahawk_smeaxe", FoodKind.COOKED_TOMAHAWK, 12, 0.8F, List.of(effect(MobEffects.DAMAGE_BOOST, 400, 3, 1.0F), effect(MobEffects.MOVEMENT_SPEED, 400, 1, 1.0F)));
    public static final Item RAW_INSECT = food("raw_insect", FoodKind.NONE, 2, 0.2F, List.of());
    public static final Item COOKED_INSECT = food("cooked_insect", FoodKind.NONE, 6, 0.6F, List.of());
    public static final Item TORCHBERRY_COOKIE = food("torchberry_cookie", FoodKind.FAST, 2, 0.2F, List.of(effect(MobEffects.GLOWING, 200, 0, 1.0F), custom(TDEffects.FIRE_RANGE, 200, 0, 0.3F)));
    public static final Item NAGA_CHIP = food("naga_chip", FoodKind.FAST, 2, 0.8F, List.of(effect(MobEffects.DAMAGE_RESISTANCE, 200, 1, 1.0F), effect(MobEffects.MOVEMENT_SPEED, 200, 2, 1.0F)));
    public static final Item RAW_NAGA_PIECE = food("raw_naga_piece", FoodKind.FAST, 2, 0.6F, List.of());
    public static final Item COOKED_NAGA_PIECE = food("cooked_naga_piece", FoodKind.FAST, 4, 0.8F, List.of());
    public static final Item RAW_NAGA_MEAT = food("raw_naga_meat", FoodKind.NONE, 4, 0.6F, List.of());
    public static final Item COOKED_NAGA_MEAT = food("cooked_naga_meat", FoodKind.NONE, 8, 0.8F, List.of());
    public static final Item NAGA_SKEWERS = food("naga_skewers", FoodKind.STICK, 8, 0.9F, List.of(effect(MobEffects.DAMAGE_RESISTANCE, 1200, 0, 1.0F), effect(MobEffects.MOVEMENT_SPEED, 1200, 0, 1.0F)));
    public static final Item NAGA_STEW = food("naga_stew", FoodKind.BOWL, 12, 0.8F, List.of(effect(ModEffects.NOURISHMENT, 3600, 0, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 1800, 1, 1.0F)));
    public static final Item NAGA_MEATBALL_CONGEE = food("naga_meatball_congee", FoodKind.BOWL, 12, 0.8F, List.of(effect(ModEffects.NOURISHMENT, 3600, 0, 1.0F), effect(MobEffects.REGENERATION, 600, 0, 1.0F), effect(MobEffects.MOVEMENT_SPEED, 1800, 1, 1.0F)));
    public static final Item CHOCOLATE_WAFER = food("chocolate_wafer", FoodKind.NONE, 9, 0.6F, List.of());
    public static final Item SLIME_WAFER = food("slime_wafer", FoodKind.NONE, 9, 0.6F, List.of(effect(MobEffects.JUMP, 200, 1, 1.0F)));
    public static final Item MAZE_SLIME_WAFER = food("maze_slime_wafer", FoodKind.NONE, 9, 0.6F, List.of(effect(MobEffects.JUMP, 200, 1, 1.0F)));
    public static final Item EXPERIMENT_113 = food("experiment_113", FoodKind.STICK, 6, 1.2F, List.of(custom(TDEffects.TEMPORAL_SADNESS, 100, 0, 0.33F)));
    public static final Item CHOCOLATE_113 = food("chocolate_113", FoodKind.STICK, 6, 1.2F, List.of(custom(TDEffects.TEMPORAL_SADNESS, 100, 0, 0.33F), effect(MobEffects.DAMAGE_BOOST, 600, 4, 1.0F)));
    public static final Item MILKY_113 = food("milky_113", FoodKind.STICK, 6, 1.2F, List.of(effect(MobEffects.HEAL, 1, 0, 1.0F)));
    public static final Item GLOW_113 = food("glow_113", FoodKind.STICK, 6, 1.2F, List.of(custom(TDEffects.TEMPORAL_SADNESS, 100, 0, 0.33F), effect(MobEffects.NIGHT_VISION, 6000, 0, 1.0F), effect(MobEffects.GLOWING, 6000, 0, 1.0F)));
    public static final Item HONEY_113 = food("honey_113", FoodKind.STICK, 6, 1.2F, List.of(custom(TDEffects.TEMPORAL_SADNESS, 100, 0, 0.33F), effect(MobEffects.MOVEMENT_SPEED, 3600, 2, 1.0F)));
    public static final Item EXPERIMENT_110 = food("experiment_110", FoodKind.NONE, 12, 0.3F, List.of(effect(MobEffects.HEALTH_BOOST, 2400, 4, 1.0F), effect(MobEffects.NIGHT_VISION, 2400, 0, 1.0F), effect(MobEffects.CONFUSION, 2400, 0, 1.0F), effect(MobEffects.POISON, 2400, 0, 1.0F), effect(MobEffects.BLINDNESS, 2400, 0, 0.5F), custom(TDEffects.TEMPORAL_SADNESS, 200, 0, 0.5F)));
    public static final Item MEEF_WRAP = food("meef_wrap", FoodKind.NONE, 10, 0.8F, List.of(effect(MobEffects.DAMAGE_BOOST, 1200, 0, 1.0F), effect(MobEffects.REGENERATION, 1200, 0, 1.0F)));
    public static final Item GHAST_BURGER = food("ghast_burger", FoodKind.NONE, 11, 0.8F, List.of(effect(MobEffects.REGENERATION, 1200, 1, 1.0F)));
    public static final Item HYDRA_BURGER = food("hydra_burger", FoodKind.NONE, 18, 0.8F, List.of(effect(MobEffects.REGENERATION, 1800, 1, 1.0F), effect(MobEffects.FIRE_RESISTANCE, 6000, 0, 1.0F)));
    public static final Item BERRY_STICK = food("berry_stick", FoodKind.STICK, 6, 0.3F, List.of(effect(MobEffects.GLOWING, 200, 0, 1.0F), custom(TDEffects.FIRE_RANGE, 200, 0, 1.0F)));
    public static final Item GLOWSTEW = food("glowstew", FoodKind.BOWL, 7, 0.7F, List.of(effect(MobEffects.GLOWING, 3600, 0, 1.0F), effect(MobEffects.NIGHT_VISION, 3600, 0, 1.0F), effect(ModEffects.NOURISHMENT, 1200, 0, 1.0F)));
    public static final Item MUSHGLOOM_SAUCE = food("mushgloom_sauce", FoodKind.SAUCE, 5, 0.4F, List.of(effect(MobEffects.GLOWING, 200, 0, 1.0F)));
    public static final Item GLOW_VENISON_RIB_WITH_PASTA = food("glow_venison_rib_with_pasta", FoodKind.BOWL, 12, 0.8F, List.of(effect(MobEffects.GLOWING, 200, 0, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 1800, 1, 1.0F), effect(ModEffects.NOURISHMENT, 3600, 0, 1.0F)));
    public static final Item MUSHGLOOM_MEEF_PASTA = food("mushgloom_meef_pasta", FoodKind.BOWL, 12, 0.8F, List.of(effect(MobEffects.GLOWING, 100, 0, 1.0F), effect(MobEffects.DAMAGE_BOOST, 1200, 1, 1.0F), effect(MobEffects.REGENERATION, 1200, 1, 1.0F), effect(ModEffects.NOURISHMENT, 3600, 0, 1.0F)));
    public static final Item LIVEROOT_PORK_FRIED_RICE = food("liveroot_pork_fried_rice", FoodKind.BOWL, 12, 0.8F, List.of(effect(MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1.0F), effect(ModEffects.NOURISHMENT, 3600, 0, 1.0F)));
    public static final Item LIVEROOT_VENISON_NOODLE_SOUP = food("liveroot_venison_noodle_soup", FoodKind.BOWL, 10, 0.8F, List.of(effect(MobEffects.DAMAGE_RESISTANCE, 1200, 1, 1.0F), effect(ModEffects.NOURISHMENT, 3600, 0, 1.0F)));
    public static final Item TORCHBERRY_VENISON_SANDWICH = food("torchberry_venison_sandwich", FoodKind.NONE, 10, 0.8F, List.of(custom(TDEffects.FIRE_RANGE, 3600, 0, 1.0F), effect(ModEffects.NOURISHMENT, 3600, 0, 1.0F)));
    public static final Item FRIED_INSECT = food("fried_insect", FoodKind.BOWL, 10, 0.8F, List.of(effect(ModEffects.NOURISHMENT, 3600, 0, 1.0F)));
    public static final Item THOUSAND_PLANT_STEW = food("thousand_plant_stew", FoodKind.BOWL, 10, 0.6F, List.of(effect(MobEffects.HEALTH_BOOST, 600, 1, 1.0F), effect(MobEffects.CONFUSION, 300, 0, 0.1F), effect(ModEffects.NOURISHMENT, 1200, 0, 1.0F)));
    public static final Item GRILLED_GHAST = food("grilled_ghast", FoodKind.BOWL, 10, 0.8F, List.of(effect(ModEffects.NOURISHMENT, 6000, 0, 1.0F), effect(MobEffects.REGENERATION, 6000, 1, 1.0F), effect(MobEffects.FIRE_RESISTANCE, 6000, 0, 1.0F)));
    public static final Item GRILLED_TOMAHAWK_SMEAK = food("grilled_tomahawk_smeak", FoodKind.BOWL, 14, 0.8F, List.of(effect(ModEffects.NOURISHMENT, 6000, 0, 1.0F), effect(MobEffects.DAMAGE_BOOST, 6000, 1, 1.0F), effect(MobEffects.MOVEMENT_SPEED, 6000, 1, 1.0F)));
    public static final Item BORER_TEAR_SOUP = food("borer_tear_soup", FoodKind.BOWL, 6, 0.6F, List.of(effect(MobEffects.SATURATION, 10, 0, 1.0F)));
    public static final Item GHAST_BRAIN_SALAD = food("ghast_brain_salad", FoodKind.BOWL, 6, 0.6F, List.of(effect(MobEffects.HEALTH_BOOST, 2400, 4, 1.0F), effect(MobEffects.NIGHT_VISION, 2400, 0, 1.0F), effect(MobEffects.DAMAGE_BOOST, 2400, 4, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 2400, 2, 1.0F), effect(MobEffects.CONFUSION, 2400, 0, 1.0F), effect(MobEffects.POISON, 2400, 0, 1.0F), effect(MobEffects.BLINDNESS, 2400, 0, 0.5F), custom(TDEffects.TEMPORAL_SADNESS, 200, 0, 0.5F)));
    public static final Item PLATE_OF_LILY_CHICKEN = food("plate_of_lily_chicken", FoodKind.BOWL, 16, 0.9F, List.of(effect(MobEffects.FIRE_RESISTANCE, 6000, 0, 1.0F), effect(ModEffects.NOURISHMENT, 6000, 0, 1.0F)));
    public static final Item PLATE_OF_FIERY_SNAKES = food("plate_of_fiery_snakes", FoodKind.BOWL, 20, 1.9F, List.of(effect(ModEffects.NOURISHMENT, 6000, 0, 1.0F), effect(MobEffects.DAMAGE_BOOST, 6000, 2, 1.0F), effect(MobEffects.REGENERATION, 6000, 1, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 1800, 2, 1.0F)));
    public static final Item PLATE_OF_MEEF_WELLINGTON = food("plate_of_meef_wellington", FoodKind.BOWL, 14, 0.8F, List.of(effect(MobEffects.DAMAGE_BOOST, 3600, 1, 1.0F), effect(MobEffects.REGENERATION, 3600, 1, 1.0F), effect(MobEffects.GLOWING, 200, 0, 1.0F), effect(ModEffects.NOURISHMENT, 6000, 0, 1.0F)));
    public static final Item PLATE_OF_SAUCY_GRILLED_NAGA = food("plate_of_saucy_grilled_naga", FoodKind.BOWL, 16, 0.9F, List.of(effect(ModEffects.NOURISHMENT, 6000, 0, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 1800, 1, 1.0F), effect(MobEffects.MOVEMENT_SPEED, 1800, 2, 1.0F)));
    public static final Item RED_THREAD_PASTA = food("red_thread_pasta", FoodKind.BOWL, 12, 0.8F, List.of(effect(MobEffects.NIGHT_VISION, 3600, 0, 1.0F), effect(MobEffects.DAMAGE_BOOST, 600, 1, 1.0F), effect(MobEffects.DIG_SPEED, 600, 1, 1.0F)));
    public static final Item HYDRA_SKEWER = food("hydra_skewer", FoodKind.STICK, 8, 0.9F, List.of(effect(MobEffects.REGENERATION, 1800, 0, 1.0F), effect(MobEffects.FIRE_RESISTANCE, 6000, 0, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 600, 1, 1.0F)));
    public static final Item FIERY_BLOOD_CURD = food("fiery_blood_curd", FoodKind.NONE, 14, 0.8F, List.of(effect(ModEffects.NOURISHMENT, 6000, 0, 1.0F), effect(MobEffects.REGENERATION, 1800, 2, 1.0F), effect(MobEffects.FIRE_RESISTANCE, 6000, 0, 1.0F), custom(TDEffects.FIRE_RANGE, 1800, 0, 1.0F)));
    public static final Item HYDRA_STUFFED_POTATO = food("hydra_stuffed_potato", FoodKind.NONE, 8, 0.6F, List.of(effect(MobEffects.REGENERATION, 1800, 0, 1.0F), effect(MobEffects.FIRE_RESISTANCE, 6000, 0, 1.0F)));
    public static final Item HYDRA_STEW = food("hydra_stew", FoodKind.BOWL, 12, 0.8F, List.of(effect(ModEffects.NOURISHMENT, 6000, 0, 1.0F), effect(MobEffects.REGENERATION, 1200, 2, 1.0F), effect(MobEffects.FIRE_RESISTANCE, 6000, 0, 1.0F)));
    public static final Item FRIED_HYDRA_CHOP = food("fried_hydra_chop", FoodKind.NONE, 12, 0.8F, List.of(effect(MobEffects.REGENERATION, 1800, 2, 1.0F), effect(MobEffects.FIRE_RESISTANCE, 6000, 0, 1.0F)));
    public static final Item INSECT_ROLL = food("insect_roll", FoodKind.NONE, 6, 0.6F, List.of());
    public static final Item TWILIGHT_SALAD = food("twilight_salad", FoodKind.BOWL, 8, 0.6F, List.of());
    public static final Item GLOWING_CURRY_RICE = food("glowing_curry_rice", FoodKind.BOWL, 12, 0.8F, List.of(effect(MobEffects.DIG_SPEED, 200, 0, 1.0F)));
    public static final Item DEADBUG_STICK = food("deadbug_stick", FoodKind.STICK, 6, 0.3F, List.of());
    public static final Item VELVET_ANTLER_SLICE = food("velvet_antler_slice", FoodKind.NONE, 2, 0.3F, List.of(effect(MobEffects.REGENERATION, 100, 0, 1.0F)));
    public static final Item ANTLER_WINE = food("antler_wine", FoodKind.DRINK, 4, 0.25F, List.of(effect(MobEffects.DIG_SPEED, 200, 0, 1.0F), effect(MobEffects.MOVEMENT_SPEED, 400, 0, 1.0F), effect(MobEffects.JUMP, 200, 0, 1.0F), effect(MobEffects.CONFUSION, 60, 0, 1.0F)));
    public static final Item VELVET_ANTLER_STEWED_CHICKEN_SOUP = food("velvet_antler_stewed_chicken_soup", FoodKind.BOWL, 10, 0.8F, List.of(effect(MobEffects.REGENERATION, 600, 0, 1.0F)));
    public static final Item WITCHCRAFT_BONE_BROTH = food("witchcraft_bone_broth", FoodKind.BOWL, 8, 0.7F, List.of(effect(ModEffects.NOURISHMENT, 2400, 0, 1.0F), custom(TDEffects.LICH_CHARGE, 1200, 0, 1.0F)));
    public static final Item MAGIC_CAKE_SCROLL = food("magic_cake_scroll", FoodKind.NONE, 6, 0.6F, List.of(custom(TDEffects.LICH_CHARGE, 600, 0, 1.0F)));
    public static final Item BOWL_OF_CHAOS_STEW = food("bowl_of_chaos_stew", FoodKind.BOWL, 14, 1.0F, List.of(effect(ModEffects.NOURISHMENT, 4800, 0, 1.0F), custom(TDEffects.LICH_CHARGE, 1800, 0, 1.0F)));
    public static final Item THORN_ROSE_TEA = food("thorn_rose_tea", FoodKind.ROSE, 4, 0.25F, List.of(effect(MobEffects.REGENERATION, 300, 1, 1.0F)));
    public static final Item TORCHBERRY_JUICE = food("torchberry_juice", FoodKind.DRINK, 4, 0.25F, List.of(custom(TDEffects.FIRE_RANGE, 1200, 0, 1.0F)));
    public static final Item PHYTOCHEMICAL_JUICE = food("phytochemical_juice", FoodKind.DRINK, 4, 0.25F, List.of(custom(TDEffects.POISON_RANGE, 1200, 0, 1.0F)));
    public static final Item GLACIER_ICE_TEA = food("glacier_ice_tea", FoodKind.DRINK, 4, 0.25F, List.of(custom(TDEffects.FROZEN_RANGE, 1200, 0, 1.0F)));
    public static final Item TWILIGHT_SPRING = food("twilight_spring", FoodKind.DRINK, 0, 0.0F, List.of(effect(MobEffects.DAMAGE_RESISTANCE, 1200, 1, 1.0F)));
    public static final Item TEAR_DRINK = food("tear_drink", FoodKind.DRINK, 1, 0.0F, List.of(effect(MobEffects.FIRE_RESISTANCE, 12000, 0, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 12000, 1, 1.0F), custom(TDEffects.TEMPORAL_SADNESS, 1200, 0, 1.0F)));
    public static final Item COOKED_HELMET_CRAB_CLAW = food("cooked_helmet_crab_claw", FoodKind.FAST, 4, 0.8F, List.of());
    public static final Item CRAB_CLAW_PASTA = food("crab_claw_pasta", FoodKind.BOWL, 12, 0.8F, List.of(effect(MobEffects.DIG_SPEED, 600, 0, 1.0F), effect(ModEffects.NOURISHMENT, 1800, 0, 1.0F)));
    public static final Item STUFFED_CRAB_CLAW = food("stuffed_crab_claw", FoodKind.NONE, 8, 0.6F, List.of(effect(MobEffects.DAMAGE_BOOST, 1200, 0, 1.0F), effect(ModEffects.NOURISHMENT, 1800, 0, 1.0F)));
    public static final Item OMAZEO = food("omazeo", FoodKind.FAST, 2, 0.2F, List.of(effect(MobEffects.NIGHT_VISION, 1200, 0, 1.0F), effect(MobEffects.LUCK, 1200, 0, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 1200, 0, 1.0F)));
    public static final Item REDCANP = food("redcanp", FoodKind.NONE, 6, 0.6F, List.of(effect(MobEffects.HERO_OF_THE_VILLAGE, 300, 0, 1.0F), effect(MobEffects.FIRE_RESISTANCE, 3600, 0, 1.0F), effect(ModEffects.NOURISHMENT, 3000, 0, 1.0F)));
    public static final Item PHANTOM_BONE_BROTH = food("phantom_bone_broth", FoodKind.BOWL, 8, 0.8F, List.of(custom(TDEffects.PHANTOM_BLESSING, 600, 0, 1.0F), effect(ModEffects.NOURISHMENT, 1800, 0, 1.0F)));
    public static final Item TOMB_OF_THE_PHANTOM = food("tomb_of_the_phantom", FoodKind.NONE, 10, 0.8F, List.of(custom(TDEffects.PHANTOM_BLESSING, 1200, 0, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 1200, 0, 1.0F), effect(ModEffects.NOURISHMENT, 1800, 0, 1.0F)));
    public static final Item PHANTOM_ENERGY_BAR = food("phantom_energy_bar", FoodKind.NONE, 6, 0.4F, List.of(custom(TDEffects.PHANTOM_BLESSING, 300, 0, 1.0F)));
    public static final Item JACK_PHANTOM = food("jack_phantom", FoodKind.DRINK, 4, 0.25F, List.of(custom(TDEffects.PHANTOM_BLESSING, 1200, 0, 1.0F), effect(MobEffects.CONFUSION, 200, 0, 1.0F), effect(MobEffects.DAMAGE_BOOST, 600, 2, 1.0F)));
    public static final Item PLATE_OF_DELICIOUS_IN_MAZE = food("plate_of_delicious_in_maze", FoodKind.BOWL, 16, 0.9F, List.of(custom(TDEffects.PHANTOM_BLESSING, 1800, 0, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 1200, 2, 1.0F), effect(ModEffects.NOURISHMENT, 3000, 0, 1.0F)));
    public static final Item TOMATO_SAUCE_113 = food("tomato_sauce_113", FoodKind.STICK, 6, 1.2F, List.of(effect(MobEffects.FIRE_RESISTANCE, 3600, 0, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 3600, 0, 1.0F), custom(TDEffects.TEMPORAL_SADNESS, 100, 0, 0.33F)));
    public static final Item REGRET_SOUP = food("regret_soup", FoodKind.BOWL, 8, 0.7F, List.of(custom(TDEffects.TEMPORAL_SADNESS, 200, 0, 1.0F), effect(MobEffects.REGENERATION, 900, 1, 1.0F)));
    public static final Item GHAST_TENTACLE_SALAD = food("ghast_tentacle_salad", FoodKind.BOWL, 6, 0.6F, List.of(custom(TDEffects.TEMPORAL_SADNESS, 200, 0, 0.33F), effect(MobEffects.REGENERATION, 1000, 1, 1.0F), effect(MobEffects.DAMAGE_RESISTANCE, 1000, 1, 1.0F)));
    public static final Item THICKCUT_GHAST_FRIES = food("thickcut_ghast_fries", FoodKind.NONE, 10, 0.8F, List.of(custom(TDEffects.TEMPORAL_SADNESS, 200, 0, 0.33F), effect(MobEffects.REGENERATION, 900, 1, 1.0F), effect(ModEffects.NOURISHMENT, 3000, 0, 1.0F)));
    public static final Item BOWL_OF_GHASTLING_MOUSSE_CAKE = food("bowl_of_ghastling_mousse_cake", FoodKind.NONE, 10, 0.8F, List.of(custom(TDEffects.TEMPORAL_SADNESS, 200, 0, 0.33F), effect(MobEffects.SLOW_FALLING, 3600, 0, 1.0F), effect(MobEffects.FIRE_RESISTANCE, 6000, 0, 1.0F), effect(MobEffects.ABSORPTION, 1200, 3, 1.0F)));

    public static final Item AURORA_PIE_SLICE = food("aurora_pie_slice", FoodKind.FAST, 3, 0.3F, List.of(custom(TDEffects.AURORA_GLOWING, 300, 0, 1.0F), effect(MobEffects.MOVEMENT_SPEED, 300, 2, 1.0F), effect(MobEffects.JUMP, 300, 1, 1.0F)));
    public static final Item TORCHBERRY_PIE_SLICE = food("torchberry_pie_slice", FoodKind.FAST, 3, 0.3F, List.of(custom(TDEffects.FIRE_RANGE, 300, 0, 1.0F)));
    public static final Item TWILIGHT_CAKE_SLICE = food("twilight_cake_slice", FoodKind.NONE, 2, 0.3F, List.of(effect(MobEffects.NIGHT_VISION, 600, 0, 1.0F), effect(MobEffects.DIG_SPEED, 600, 0, 1.0F), custom(TDEffects.TWILIGHT_AURA, 600, 0, 1.0F)));
    public static final Item WITCHCRAFT_BONE = register("witchcraft_bone", new Item(new Item.Properties()));
    public static final Item WITCHCRAFT_BONE_MEAL = register("witchcraft_bone_meal", new Item(new Item.Properties()));
    public static final Item ANTLER = register("antler", new Item(new Item.Properties()));
    public static final Item PHANTOM_BONE = register("phantom_bone", new Item(new Item.Properties()));
    public static final Item HELMET_CRAB_CLAW = register("helmet_crab_claw", new HelmetCrabClawItem());
    public static final Item CHAOS_INK_DRINK = register("chaos_ink_drink", new dev.arakiel.twilightdelight.item.ReusableDrinkItem());
    public static final Item ANTLER_BOW = register("antler_bow", new AntlerBowItem(new Item.Properties().durability(1384)));
    public static final Item FIERY_KNIFE = register("fiery_knife", new FieryKnifeItem());
    public static final Item IRONWOOD_KNIFE = register("ironwood_knife", new IronwoodKnifeItem());
    public static final Item STEELEAF_KNIFE = register("steeleaf_knife", new SteeleafKnifeItem());
    public static final Item KNIGHTMETAL_KNIFE = register("knightmetal_knife", new KnightmetalKnifeItem());
    public static final Item ICE_KNIFE = register("ice_knife", new IceKnifeItem());
    public static final Item TEARDROP_SWORD = register("teardrop_sword", new TeardropSwordItem());
    public static final Item WROUGHT_IRON_SWORD = register("wrought_iron_sword", new WroughtIronSwordItem());

    public static final Item AURORA_CAKE = optionalItem("aurora_cake");
    public static final Item AURORA_CAKE_SLICE = optionalItem("aurora_cake_slice");
    public static final Item AURORA_ICE_CREAM = optionalItem("aurora_ice_cream");
    public static final Item AURORA_MILKSHAKE = optionalItem("aurora_milkshake");
    public static final Item GLACIER_CAKE = optionalItem("glacier_cake");
    public static final Item GLACIER_CAKE_SLICE = optionalItem("glacier_cake_slice");
    public static final Item GLACIER_ICE_CREAM = optionalItem("glacier_ice_cream");
    public static final Item GLACIER_MILKSHAKE = optionalItem("glacier_milkshake");
    public static final Item PHYTOCHEMICAL_CAKE = optionalItem("phytochemical_cake");
    public static final Item PHYTOCHEMICAL_CAKE_SLICE = optionalItem("phytochemical_cake_slice");
    public static final Item PHYTOCHEMICAL_ICE_CREAM = optionalItem("phytochemical_ice_cream");
    public static final Item PHYTOCHEMICAL_MILKSHAKE = optionalItem("phytochemical_milkshake");
    public static final Item TORCHBERRY_CAKE = optionalItem("torchberry_cake");
    public static final Item TORCHBERRY_CAKE_SLICE = optionalItem("torchberry_cake_slice");
    public static final Item TORCHBERRY_ICE_CREAM = optionalItem("torchberry_ice_cream");
    public static final Item TORCHBERRY_MILKSHAKE = optionalItem("torchberry_milkshake");
    public static final Item RAINBOW_ICE_CREAM = optionalItem("rainbow_ice_cream");
    public static final Item REFRESHING_ICE_CREAM = optionalItem("refreshing_ice_cream");
    public static final Item TWILIGHT_ICE_CREAM = optionalItem("twilight_ice_cream");

    private TDItems() {
    }

    public static void init() {
        GeminiRegistration.items(TDItems.class);
    }

    public static Map<String, Item> all() {
        return Map.copyOf(ALL);
    }

    private static Item food(String id, FoodKind kind, int nutrition, float saturation, List<FoodEffect> effects) {
        FoodProperties.Builder food = new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation);
        Item.Properties properties = new Item.Properties();
        if (kind.fast) {
            food.fast();
        }
        if (kind.alwaysEdible) {
            food.alwaysEdible();
        }
        if (kind.container != null) {
            food.usingConvertsTo(kind.container);
            properties.craftRemainder(kind.container);
        }
        for (FoodEffect effect : effects) {
            food.effect(effect.instance.get(), effect.chance);
        }
        properties.food(food.build()).stacksTo(kind.stackSize).rarity(kind.rarity);
        if (kind.fireResistant) {
            properties.fireResistant();
        }
        if (kind.tomahawkUses > 0) {
            return register(id, new TomahawkFoodItem(properties, kind.tomahawkUses));
        }
        if (kind.drink) {
            if ("thorn_rose_tea".equals(id)) {
                return register(id, new RoseTeaItem(properties, kind.fast));
            }
            return register(id, new TDDrinkableItem(properties, kind.fast));
        }
        return register(id, new TDFoodItem(properties, kind.fast));
    }

    private static Item register(String id, Item item) {
        ALL.put(id, item);
        return item;
    }

    private static Item optionalItem(String id) {
        return register(id, new Item(new Item.Properties()));
    }

    private static FoodEffect effect(Holder<MobEffect> effect, int duration, int amplifier, float chance) {
        return new FoodEffect(() -> new MobEffectInstance(effect, duration, amplifier, false, false, true), chance);
    }

    private static FoodEffect custom(MobEffect effect, int duration, int amplifier, float chance) {
        return new FoodEffect(() -> new MobEffectInstance(net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect), duration, amplifier, false, false, true), chance);
    }

    private record FoodEffect(Supplier<MobEffectInstance> instance, float chance) {
    }

    private enum FoodKind {
        NONE(64, Rarity.COMMON, false, false, false, null, 0, UseAnim.EAT),
        FAST(64, Rarity.COMMON, true, false, false, null, 0, UseAnim.EAT),
        HYDRA_PIECE(64, Rarity.UNCOMMON, true, false, true, null, 0, UseAnim.EAT),
        TOMAHAWK(1, Rarity.UNCOMMON, true, false, false, null, 3, UseAnim.EAT),
        COOKED_TOMAHAWK(1, Rarity.UNCOMMON, true, false, false, null, 5, UseAnim.EAT),
        STICK(64, Rarity.COMMON, true, false, false, Items.STICK, 0, UseAnim.EAT),
        SAUCE(64, Rarity.COMMON, false, false, false, Items.BOWL, 0, UseAnim.EAT),
        BOWL(16, Rarity.COMMON, false, false, false, Items.BOWL, 0, UseAnim.EAT),
        DRINK(16, Rarity.COMMON, false, true, false, Items.GLASS_BOTTLE, 0, UseAnim.DRINK),
        ROSE(16, Rarity.COMMON, false, true, false, Items.GLASS_BOTTLE, 0, UseAnim.DRINK);

        private final int stackSize;
        private final Rarity rarity;
        private final boolean fast;
        private final boolean alwaysEdible;
        private final boolean fireResistant;
        private final Item container;
        private final int tomahawkUses;
        private final UseAnim useAnim;
        private final boolean drink;

        FoodKind(int stackSize, Rarity rarity, boolean fast, boolean alwaysEdible, boolean fireResistant, Item container, int tomahawkUses, UseAnim useAnim) {
            this.stackSize = stackSize;
            this.rarity = rarity;
            this.fast = fast;
            this.alwaysEdible = alwaysEdible;
            this.fireResistant = fireResistant;
            this.container = container;
            this.tomahawkUses = tomahawkUses;
            this.useAnim = useAnim;
            this.drink = useAnim == UseAnim.DRINK;
        }
    }
}
