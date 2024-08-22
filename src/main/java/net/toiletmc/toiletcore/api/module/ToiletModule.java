package net.toiletmc.toiletcore.api.module;

import lombok.Getter;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.ModuleManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter
public abstract class ToiletModule implements Module {
    protected ToiletCore plugin;
    protected ModuleManager.ModuleEnum moduleEnum;
    protected FileConfiguration config;
    private boolean init = false;

    public void init(ModuleManager.ModuleEnum moduleEnum) {
        if (init) {
            ToiletCore.getInstance().getLogger().severe(moduleEnum.id + " 重复初始化！");
            return;
        }

        this.moduleEnum = moduleEnum;
        this.plugin = ToiletCore.getInstance();
        loadConfig();
        init = true;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public ConfigurationSection getConfig() {
        return this.config;
    }

    @Override
    public void saveConfig() {
        try {
            config.save(new File(plugin.getDataFolder(), "settings/" + moduleEnum.id + ".yml"));
        } catch (Exception e) {
            plugin.getLogger().warning("模块 " + moduleEnum.id + " 保存配置文件时出错！");
            throw new RuntimeException(e);
        }
    }

    private void loadConfig() {
        File configFile = new File(plugin.getDataFolder(), "settings/" + moduleEnum.id + ".yml");
        if (!configFile.exists()) {
            plugin.saveResource("settings/" + moduleEnum.id + ".yml", false);
            configFile = new File(plugin.getDataFolder(), "settings/" + moduleEnum.id + ".yml");
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
