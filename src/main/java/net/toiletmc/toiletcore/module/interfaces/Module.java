package net.toiletmc.toiletcore.module.interfaces;

import org.bukkit.configuration.ConfigurationSection;

public interface Module extends Reloadable {
    ConfigurationSection getConfig();

    @Override
    default void reload() {

    }
}
