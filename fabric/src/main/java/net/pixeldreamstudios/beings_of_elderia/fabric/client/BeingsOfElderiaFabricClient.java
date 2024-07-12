package net.pixeldreamstudios.beings_of_elderia.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.pixeldreamstudios.beings_of_elderia.BeingsOfElderia;

public final class BeingsOfElderiaFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BeingsOfElderia.initClient();
    }
}
