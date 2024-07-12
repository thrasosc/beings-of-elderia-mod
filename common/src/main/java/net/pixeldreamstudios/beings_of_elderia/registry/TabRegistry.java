package net.pixeldreamstudios.beings_of_elderia.registry;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;

public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BeingsOfElderia.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static final RegistrySupplier<CreativeModeTab> MOBS_OF_MYTHOLOGY_TAB = TABS.register(
            "beings_of_elderia_tab", // Tab ID
            () -> CreativeTabRegistry.create(
                    Component.translatable("category.beings_of_elderia"), // Tab Name
                    () -> new ItemStack(ItemRegistry.DEMON_SPAWN_EGG.get()) // Icon
            )
    );

    public static void init() {
        TABS.register();
    }
}
