package dev.arakiel.twilightdelight.registry;

import dev.arakiel.gemini.registration.GeminiRegistration;
import dev.arakiel.gemini.registration.annotations.AssignedName;
import dev.arakiel.gemini.registration.annotations.RegistryNamespace;
import dev.arakiel.gemini.registration.container.BlockRegistryContainer;
import dev.arakiel.twilightdelight.TwilightDelightFabric;
import dev.arakiel.twilightdelight.block.FieryCookingPotBlock;
import dev.arakiel.twilightdelight.block.FieryCookingPotItem;
import dev.arakiel.twilightdelight.block.FiddleheadClumpBlock;
import dev.arakiel.twilightdelight.block.FiddleheadClumpUpperBlock;
import dev.arakiel.twilightdelight.block.MazeStoveBlock;
import dev.arakiel.twilightdelight.block.MazeStoveBlockEntity;
import dev.arakiel.twilightdelight.block.TwilightCakeBlock;
import dev.arakiel.twilightdelight.block.TwilightRichSoilBlock;
import dev.arakiel.twilightdelight.block.TwilightRichSoilFarmlandBlock;
import dev.arakiel.twilightdelight.block.TwilightFeastBlock;
import dev.arakiel.twilightdelight.item.TwilightCakeItem;
import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.tags.TagKey;
import twilightforest.block.DarkLeavesBlock;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.item.MushroomColonyItem;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

@RegistryNamespace(TwilightDelightFabric.MOD_ID)
public final class TDBlocks implements BlockRegistryContainer {
    public static BlockEntityType<MazeStoveBlockEntity> MAZE_STOVE_ENTITY;
    private static final ResourceKey<ConfiguredFeature<?, ?>> IRONWOOD_TREE =
            ResourceKey.create(Registries.CONFIGURED_FEATURE, TwilightDelightFabric.id("tree/ironwood_tree"));

    public static final Block MAZE_STOVE = new MazeStoveBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS)
                    .lightLevel(state -> state.getValue(StoveBlock.LIT) ? 13 : 0)
    );
    @AssignedName("fiery_cooking_pot")
    public static final Block FIERY_POT = new FieryCookingPotBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(net.minecraft.world.level.material.MapColor.METAL)
                    .strength(0.5F, 6.0F)
                    .sound(SoundType.LANTERN)
    );
    @AssignedName("fiery_snakes_block")
    public static final Block FIERY_SNAKES = feast(
            () -> TDItems.PLATE_OF_FIERY_SNAKES,
            true,
            TwilightFeastBlock.Kind.FIERY_SNAKES
    );
    @AssignedName("lily_chicken_block")
    public static final Block LILY_CHICKEN = feast(
            () -> TDItems.PLATE_OF_LILY_CHICKEN,
            true,
            TwilightFeastBlock.Kind.LILY_CHICKEN
    );
    @AssignedName("meef_wellington_block")
    public static final Block MEEF_WELLINGTON = feast(
            () -> TDItems.PLATE_OF_MEEF_WELLINGTON,
            true,
            TwilightFeastBlock.Kind.MEEF_WELLINGTON
    );
    @AssignedName("saucy_grilled_naga_block")
    public static final Block SAUCY_GRILLED_NAGA = feast(
            () -> TDItems.PLATE_OF_SAUCY_GRILLED_NAGA,
            true,
            TwilightFeastBlock.Kind.SAUCY_GRILLED_NAGA
    );
    @AssignedName("chaos_stew_block")
    public static final Block CHAOS_STEW = feast(
            () -> TDItems.BOWL_OF_CHAOS_STEW,
            true,
            TwilightFeastBlock.Kind.CHAOS_STEW
    );
    @AssignedName("delicious_in_maze")
    public static final Block DELICIOUS_IN_MAZE = feast(
            () -> TDItems.PLATE_OF_DELICIOUS_IN_MAZE,
            true,
            TwilightFeastBlock.Kind.DELICIOUS_IN_MAZE
    );
    @AssignedName("ghastling_mousse_cake")
    public static final Block GHASTLING_MOUSSE_CAKE = feast(
            () -> TDItems.BOWL_OF_GHASTLING_MOUSSE_CAKE,
            true,
            TwilightFeastBlock.Kind.GHASTLING_MOUSSE_CAKE
    );
    public static final Block TORCHBERRIES_CRATE = new Block(
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)
                    .lightLevel(state -> 15)
    );
    public static final Block MUSHGLOOM_COLONY = new MushroomColonyBlock(
            new DeferredItemHolder("twilightforest", "mushgloom"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM)
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .noCollission()
                    .noOcclusion()
                    .randomTicks()
                    .lightLevel(state -> 3)
    );
    public static final Block FIDDLEHEAD_CLUMP = new FiddleheadClumpBlock(
            new DeferredItemHolder("twilightforest", "fiddlehead"),
            BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM)
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .randomTicks()
                    .noCollission()
                    .noOcclusion()
    );
    @BlockRegistryContainer.NoBlockItem
    public static final Block FIDDLEHEAD_CLUMP_UPPER = new FiddleheadClumpUpperBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM)
                    .instabreak()
                    .sound(SoundType.FUNGUS)
                    .noCollission()
                    .noOcclusion()
    );
    public static final Block TWILIGHT_RICH_SOIL = new TwilightRichSoilBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT).randomTicks()
    );
    public static final Block TWILIGHT_RICH_SOIL_FARMLAND = new TwilightRichSoilFarmlandBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)
    );
    public static final Block IRONWOOD_SAPLING = new SaplingBlock(
            new TreeGrower(
                    "ironwood",
                    Optional.empty(),
                    Optional.of(IRONWOOD_TREE),
                    Optional.empty()
            ),
            BlockBehaviour.Properties.of()
                    .mapColor(net.minecraft.world.level.material.MapColor.PLANT)
                    .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY)
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .noCollission()
                    .randomTicks()
    );
    public static final Block IRONWOOD_LOG = new RotatedPillarBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)
                    .strength(10.0F, 10.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final Block IRONWOOD_LEAVES = new DarkLeavesBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(net.minecraft.world.level.material.MapColor.PLANT)
                    .pushReaction(net.minecraft.world.level.material.PushReaction.DESTROY)
                    .sound(SoundType.AZALEA_LEAVES)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)
                    .strength(10.0F, 10.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final Block TWILIGHT_OAK_CABINET = cabinet();
    public static final Block CANOPY_CABINET = cabinet();
    public static final Block DARK_CABINET = cabinet();
    public static final Block MANGROVE_CABINET = cabinet();
    public static final Block MINING_CABINET = cabinet();
    public static final Block SORTING_CABINET = cabinet();
    public static final Block TIME_CABINET = cabinet();
    public static final Block TRANSFORMATION_CABINET = cabinet();
    public static final Block TWILIGHT_CAKE = new TwilightCakeBlock(
            BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)
    );
    public static final Block AURORA_PIE = pie(() -> TDItems.AURORA_PIE_SLICE, 7);
    public static final Block TORCHBERRY_PIE = pie(() -> TDItems.TORCHBERRY_PIE_SLICE, 10);

    private static final Block[] CABINETS = {
            TWILIGHT_OAK_CABINET,
            CANOPY_CABINET,
            DARK_CABINET,
            MANGROVE_CABINET,
            MINING_CABINET,
            SORTING_CABINET,
            TIME_CABINET,
            TRANSFORMATION_CABINET
    };

    private TDBlocks() {
    }

    public static void init() {
        GeminiRegistration.blocks(TDBlocks.class);
        MAZE_STOVE_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                TwilightDelightFabric.id("maze_stove"),
                BlockEntityType.Builder.of(
                        MazeStoveBlockEntity::new,
                        MAZE_STOVE
                ).build(null)
        );
    }

    public static void finishRegistration() {
        ModBlockEntityTypes.COOKING_POT.get().validBlocks.add(FIERY_POT);
        for (Block cabinet : CABINETS) {
            ModBlockEntityTypes.CABINET.get().validBlocks.add(cabinet);
        }
    }

    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        if (block == FIERY_POT) {
            return new FieryCookingPotItem(block, new Item.Properties().stacksTo(1).fireResistant());
        }
        if (block == TWILIGHT_CAKE) {
            return new TwilightCakeItem(block, new Item.Properties());
        }
        if (block == MUSHGLOOM_COLONY || block == FIDDLEHEAD_CLUMP) {
            return new MushroomColonyItem(block, new Item.Properties());
        }
        return new BlockItem(block, new Item.Properties());
    }

    private static Block feast(
            Supplier<Item> serving,
            boolean leftovers,
            TwilightFeastBlock.Kind kind
    ) {
        return new TwilightFeastBlock(serving, leftovers, kind);
    }

    private static Block pie(Supplier<Item> slice, int light) {
        return new PieBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)
                        .lightLevel(state -> light),
                slice
        );
    }

    private static Block cabinet() {
        return new CabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL));
    }

    private static final class DeferredItemHolder implements Holder<Item> {
        private final ResourceKey<Item> key;

        private DeferredItemHolder(String namespace, String path) {
            this.key = ResourceKey.create(
                    Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(namespace, path)
            );
        }

        private Holder<Item> delegate() {
            return BuiltInRegistries.ITEM.getHolderOrThrow(key);
        }

        @Override
        public Item value() {
            return delegate().value();
        }

        @Override
        public boolean isBound() {
            return BuiltInRegistries.ITEM.containsKey(key);
        }

        @Override
        public boolean is(ResourceLocation location) {
            return delegate().is(location);
        }

        @Override
        public boolean is(ResourceKey<Item> key) {
            return delegate().is(key);
        }

        @Override
        public boolean is(Predicate<ResourceKey<Item>> predicate) {
            return delegate().is(predicate);
        }

        @Override
        public boolean is(TagKey<Item> tag) {
            return delegate().is(tag);
        }

        @Override
        @SuppressWarnings("deprecation")
        public boolean is(Holder<Item> holder) {
            return delegate().is(holder);
        }

        @Override
        public Stream<TagKey<Item>> tags() {
            return delegate().tags();
        }

        @Override
        public Either<ResourceKey<Item>, Item> unwrap() {
            return delegate().unwrap();
        }

        @Override
        public Optional<ResourceKey<Item>> unwrapKey() {
            return Optional.of(key);
        }

        @Override
        public Kind kind() {
            return delegate().kind();
        }

        @Override
        public boolean canSerializeIn(HolderOwner<Item> owner) {
            return delegate().canSerializeIn(owner);
        }
    }
}
