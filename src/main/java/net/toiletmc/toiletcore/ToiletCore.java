package net.toiletmc.toiletcore;

import net.toiletmc.toiletcore.module.AbstractModule;
import net.toiletmc.toiletcore.module.Modules;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public final class ToiletCore extends JavaPlugin {
    private Set<AbstractModule> modules;

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig();
        for (Modules module : Modules.values()) {
            modules.add()
            if (config.getBoolean("modules." + module.name())) {

            }
        }

    }

    @Override
    public void onDisable() {

    }
}
