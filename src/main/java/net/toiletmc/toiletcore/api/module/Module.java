package net.toiletmc.toiletcore.api.module;

import org.bukkit.configuration.ConfigurationSection;

public interface Module {
    void onEnable();

    void onDisable();

    ConfigurationSection getConfig();

    void saveConfig();
}
