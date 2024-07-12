package net.pixeldreamstudios.beings_of_elderia;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import mod.azure.azurelib.AzureLib;
import net.pixeldreamstudios.beings_of_elderia.entity.client.renderer.DemonRenderer;
import net.pixeldreamstudios.beings_of_elderia.entity.client.renderer.ImpGuardRenderer;
import net.pixeldreamstudios.beings_of_elderia.entity.client.renderer.ImpRenderer;
import net.pixeldreamstudios.beings_of_elderia.registry.EntityRegistry;
import net.pixeldreamstudios.beings_of_elderia.registry.ItemRegistry;
import net.pixeldreamstudios.beings_of_elderia.registry.TabRegistry;

public final class BeingsOfElderia {
    public static final String MOD_ID = "beings_of_elderia";

    public static void init() {
        AzureLib.initialize();
        EntityRegistry.init();
        ItemRegistry.init();
        TabRegistry.init();
    }
    public static void initClient() {
        EntityRendererRegistry.register(EntityRegistry.DEMON, DemonRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMP, ImpRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.IMP_GUARD, ImpGuardRenderer::new);
    }
}
