package net.toiletmc.toiletcore.api.module;

import org.bukkit.configuration.ConfigurationSection;

import java.util.logging.Logger;

public interface Module {
    void onEnable();

    void onDisable();

    ConfigurationSection getConfig();

    void saveConfig();

    ConfigurationSection getData();

    void saveData();

    Logger getLogger();

    void info(String logInfo);

    void warning(String logWarning);

    void error(String logError);

    void debug(String logDebug);
}
