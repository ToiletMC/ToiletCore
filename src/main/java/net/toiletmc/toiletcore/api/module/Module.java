package net.toiletmc.toiletcore.api.module;

import org.bukkit.configuration.ConfigurationSection;

public interface Module {
    void onEnable();

    void onDisable();

    ConfigurationSection getConfig();

    void saveConfig();

    ConfigurationSection getData();

    void saveData();

    void info(String logInfo);

    void warning(String logWarning);

    void exception(String description, Exception exception);

    void error(String logError);

    void debug(String logDebug);
}
