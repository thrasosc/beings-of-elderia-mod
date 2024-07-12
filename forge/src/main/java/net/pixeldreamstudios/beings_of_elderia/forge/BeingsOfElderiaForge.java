package net.pixeldreamstudios.beings_of_elderia.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;

@Mod(BeingsOfElderia.MOD_ID)
public final class BeingsOfElderiaForge {
    public BeingsOfElderiaForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(BeingsOfElderia.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        BeingsOfElderia.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> BeingsOfElderia::initClient);
    }
}
