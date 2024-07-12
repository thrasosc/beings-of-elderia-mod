package net.pixeldreamstudios.beings_of_elderia.registry;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;
import net.pixeldreamstudios.beings_of_elderia.entity.DemonEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.ImpEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.ImpGuardEntity;
import net.pixeldreamstudios.beings_of_elderia.entity.abstraction.AbstractDemonEntity;

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
        SpawnPlacementsRegistry.register(EntityRegistry.DEMON, SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractDemonEntity::checkAnyLightMonsterSpawnRules);
        BiomeModifications.addProperties(b -> b.hasTag(BiomeTags.IS_NETHER), (ctx, b) -> b.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(DEMON.get(), 20, 1, 6)));

        SpawnPlacementsRegistry.register(EntityRegistry.IMP, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractDemonEntity::checkAnyLightMonsterSpawnRules);
        BiomeModifications.addProperties(b -> b.hasTag(BiomeTags.IS_NETHER), (ctx, b) -> b.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(IMP.get(), 10, 1, 6)));

        SpawnPlacementsRegistry.register(EntityRegistry.IMP_GUARD, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractDemonEntity::checkAnyLightMonsterSpawnRules);
        BiomeModifications.addProperties(b -> b.hasTag(BiomeTags.IS_NETHER), (ctx, b) -> b.getSpawnProperties().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(IMP_GUARD.get(), 10, 1, 6)));
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
