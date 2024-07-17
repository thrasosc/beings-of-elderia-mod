package net.pixeldreamstudios.beings_of_elderia;

import mod.azure.azurelib.config.Config;
import mod.azure.azurelib.config.Configurable;

@Config(id = BeingsOfElderia.MOD_ID)
public class BeingsOfElderiaConfig {
    @Configurable
    @Configurable.Synchronized
    public double demonHealth = 30.0;
    @Configurable
    @Configurable.Synchronized
    public double demonAttackDamage = 8.0;

    @Configurable
    @Configurable.Synchronized
    public double impHealth = 10.0;
    @Configurable
    @Configurable.Synchronized
    public double impAttackDamage = 2.0;

    @Configurable
    @Configurable.Synchronized
    public double impGuardHealth = 20.0;
    @Configurable
    @Configurable.Synchronized
    public double impGuardAttackDamage = 4.0;
}
