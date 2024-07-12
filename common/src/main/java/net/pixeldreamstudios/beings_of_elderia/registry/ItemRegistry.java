package net.pixeldreamstudios.beings_of_elderia.registry;

import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BeingsOfElderia.MOD_ID, Registries.ITEM);

    // SPAWN EGGS
    public static final RegistrySupplier<Item> DEMON_SPAWN_EGG = ITEMS.register("demon_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.DEMON, 0x26120e, 0x682e24, new Item.Properties().arch$tab(TabRegistry.MOBS_OF_MYTHOLOGY_TAB)));
    public static final RegistrySupplier<Item> IMP_SPAWN_EGG = ITEMS.register("imp_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.IMP, 0x7e4134, 0x371c14, new Item.Properties().arch$tab(TabRegistry.MOBS_OF_MYTHOLOGY_TAB)));
    public static final RegistrySupplier<Item> IMP_GUARD_SPAWN_EGG = ITEMS.register("imp_guard_spawn_egg", () -> new ArchitecturySpawnEggItem(EntityRegistry.IMP_GUARD, 0x6e4942, 0x341f1e, new Item.Properties().arch$tab(TabRegistry.MOBS_OF_MYTHOLOGY_TAB)));

    public static void init() {
        ITEMS.register();
    }
}
