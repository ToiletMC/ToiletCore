package net.toiletmc.toiletcore;

import net.toiletmc.toiletcore.module.ModuleManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToiletCore extends JavaPlugin {
    private ModuleManager moduleManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        moduleManager = new ModuleManager(this);
    }

    @Override
    public void onDisable() {

    }

    public void reload() {
        reloadConfig();
        moduleManager.reload();
    }
}
