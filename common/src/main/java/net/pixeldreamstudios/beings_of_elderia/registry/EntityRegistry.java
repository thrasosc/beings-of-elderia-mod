package net.pixeldreamstudios.beings_of_elderia.registry;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;
import net.pixeldreamstudios.beings_of_elderia.entity.DemonEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.ImpEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.ImpGuardEntity;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BeingsOfElderia.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<DemonEntity>> DEMON = ENTITIES.register("demon", () ->
            EntityType.Builder.of(DemonEntity::new, MobCategory.MONSTER)
                    .sized(0.9f, 2.0f)
                    .build(new ResourceLocation(BeingsOfElderia.MOD_ID, "demon").toString()));

    public static final RegistrySupplier<EntityType<ImpEntity>> IMP = ENTITIES.register("imp", () ->
            EntityType.Builder.of(ImpEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.25f)
                    .build(new ResourceLocation(BeingsOfElderia.MOD_ID, "imp").toString()));

    public static final RegistrySupplier<EntityType<ImpGuardEntity>> IMP_GUARD = ENTITIES.register("imp_guard", () ->
            EntityType.Builder.of(ImpGuardEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 1.25f)
                    .build(new ResourceLocation(BeingsOfElderia.MOD_ID, "imp_guard").toString()));

    private static void initSpawns() {
        BiomeModifications.addProperties(b -> b.hasTag(TagRegistry.NETHER_BIOMES), (ctx, b) -> b.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(DEMON.get(), 5, 1, 5)));
        BiomeModifications.addProperties(b -> b.hasTag(TagRegistry.NETHER_BIOMES), (ctx, b) -> b.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(IMP.get(), 5, 1, 5)));
        BiomeModifications.addProperties(b -> b.hasTag(TagRegistry.NETHER_BIOMES), (ctx, b) -> b.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(IMP_GUARD.get(), 5, 1, 5)));
    }

    private static void initAttributes() {
        EntityAttributeRegistry.register(DEMON, DemonEntity::createAttributes);
        EntityAttributeRegistry.register(IMP, ImpEntity::createAttributes);
        EntityAttributeRegistry.register(IMP_GUARD, ImpGuardEntity::createAttributes);
    }

    public static void init() {
        ENTITIES.register();
        initAttributes();
        initSpawns();
    }
}
