package net.salkcoding.evatunaloreeffect;

import net.salkcoding.evatunaloreeffect.effect.CommandEffect;
import net.salkcoding.evatunaloreeffect.listener.InteractListener;
import org.bukkit.plugin.java.JavaPlugin;

public class EvaTunaLoreEffect extends JavaPlugin {

    private static EvaTunaLoreEffect instance;

    public static EvaTunaLoreEffect getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        if (instance != null)
            throw new IllegalStateException();
        instance = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        CommandEffect.load(getConfig());

        getServer().getPluginManager().registerEvents(new InteractListener(), this);

        getLogger().info("Plugin is enabled");
    }

    @Override
    public void onDisable() {

        getLogger().info("Plugin is disabled");
    }

}
