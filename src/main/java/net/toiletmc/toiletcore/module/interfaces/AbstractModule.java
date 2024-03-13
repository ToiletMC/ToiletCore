package net.toiletmc.toiletcore.module.interfaces;

import lombok.Getter;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.enums.Module;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public abstract class AbstractModule implements net.toiletmc.toiletcore.module.interfaces.Module {
    protected final ToiletCore plugin;
    protected final Module module;
    protected FileConfiguration config;

    public AbstractModule(ToiletCore plugin, Module module) {
        this.plugin = plugin;
        this.module = module;
        loadConfig();
    }

    @Override
    public ConfigurationSection getConfig() {
        return this.config;
    }

    @Override
    public void reload() {
        loadConfig();
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "settings/" + module.name + ".yml");
        if (!configFile.exists()) {
            plugin.saveResource("settings/" + module.name + ".yml", false);
            configFile = new File(plugin.getDataFolder(), "settings/" + module.name + ".yml");
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
