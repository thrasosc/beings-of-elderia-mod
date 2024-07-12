package net.pixeldreamstudios.beings_of_elderia.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public record TagRegistry() {
    // BIOME TAGS
    public static TagKey<Biome> NETHER_BIOMES = TagKey.create(Registries.BIOME, new ResourceLocation("minecraft", "is_nether"));

}
