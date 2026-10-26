package dev.arakiel.twilightdelight.effect;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.arakiel.twilightdelight.TwilightDelightFabric;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;

public final class TwilightTransformationMaps
        implements SimpleSynchronousResourceReloadListener {
    private static final ResourceLocation ID =
            TwilightDelightFabric.id("transformation_powder");
    private static final String DIRECTORY = "data_maps/entity_type";
    private static volatile Map<EntityType<?>, EntityType<?>> transformations =
            Map.of();

    private TwilightTransformationMaps() {
    }

    public static void init() {
        ResourceManagerHelper.get(PackType.SERVER_DATA)
                .registerReloadListener(new TwilightTransformationMaps());
    }

    public static EntityType<?> target(EntityType<?> source) {
        return transformations.get(source);
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        Map<ResourceLocation, ResourceLocation> mappings = new LinkedHashMap<>();
        Map<ResourceLocation, Resource> files = manager.listResources(
                DIRECTORY,
                id -> id.getPath().endsWith("/transformation_powder.json")
        );
        for (Map.Entry<ResourceLocation, Resource> entry : files.entrySet()) {
            readMappings(entry.getKey(), entry.getValue(), mappings);
        }

        Map<EntityType<?>, EntityType<?>> loaded = new LinkedHashMap<>();
        mappings.forEach((sourceId, targetId) -> {
            EntityType<?> source = BuiltInRegistries.ENTITY_TYPE
                    .getOptional(sourceId)
                    .orElse(null);
            EntityType<?> target = BuiltInRegistries.ENTITY_TYPE
                    .getOptional(targetId)
                    .orElse(null);
            if (source != null && target != null) {
                loaded.put(source, target);
            }
        });
        transformations = Map.copyOf(loaded);
    }

    private static void readMappings(
            ResourceLocation file,
            Resource resource,
            Map<ResourceLocation, ResourceLocation> mappings
    ) {
        try (Reader reader = resource.openAsReader()) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject values = root.getAsJsonObject("values");
            if (values == null) {
                return;
            }
            for (Map.Entry<String, JsonElement> entry : values.entrySet()) {
                JsonObject value = entry.getValue().getAsJsonObject();
                if (!value.has("transform_to")) {
                    continue;
                }
                ResourceLocation source = ResourceLocation.tryParse(entry.getKey());
                ResourceLocation target = ResourceLocation.tryParse(
                        value.get("transform_to").getAsString()
                );
                if (source != null && target != null) {
                    mappings.put(source, target);
                }
            }
        } catch (IOException | RuntimeException exception) {
            TwilightDelightFabric.LOGGER.warn(
                    "Could not read transformation data from {}",
                    file,
                    exception
            );
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return ID;
    }
}
