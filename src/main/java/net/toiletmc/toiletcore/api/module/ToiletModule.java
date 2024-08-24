package net.toiletmc.toiletcore.api.module;

import lombok.Getter;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.ModuleManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Getter
public abstract class ToiletModule implements Module {
    protected ToiletCore plugin;
    protected ModuleManager.ModuleEnum moduleEnum;
    protected FileConfiguration config = null;
    protected FileConfiguration data = null;
    protected Logger logger = null;
    protected FileHandler logFileHandler = null;
    private boolean init = false;

    public void init(ModuleManager.ModuleEnum moduleEnum) {
        if (init) {
            ToiletCore.getInstance().getLogger().severe(moduleEnum.id + " 重复初始化！");
            return;
        }

        this.moduleEnum = moduleEnum;
        this.plugin = ToiletCore.getInstance();

        init = true;
    }

    public void disabled() {
        saveConfig();
        saveData();
        closeLogger();
    }

    @Override
    public @NotNull ConfigurationSection getConfig() {
        if (config == null) {
            loadConfig();
        }
        return this.config;
    }

    @Override
    public void saveConfig() {
        if (config != null) {
            try {
                config.save(new File(plugin.getDataFolder(), "settings/" + moduleEnum.id + ".yml"));
            } catch (Exception e) {
                plugin.getLogger().warning("模块 " + moduleEnum.id + " 保存配置文件时出错！");
                plugin.getLogger().warning(e.getMessage());
            }
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

    @Override
    public @NotNull ConfigurationSection getData() {
        if (data == null) {
            loadData();
        }

        return this.data;
    }

    @Override
    public void saveData() {
        if (data != null) {
            try {
                data.save(new File(plugin.getDataFolder(), "data/" + moduleEnum.id + ".yml"));
            } catch (Exception e) {
                plugin.getLogger().warning("模块 " + moduleEnum.id + " 保存数据文件时出错！");
                plugin.getLogger().warning(e.getMessage());
            }
        }
    }

    private void loadData() {
        File dataFile = new File(plugin.getDataFolder(), "data/" + moduleEnum.id + ".yml");
        if (!dataFile.exists()) {
            plugin.saveResource("data/" + moduleEnum.id + ".yml", false);
            dataFile = new File(plugin.getDataFolder(), "data/" + moduleEnum.id + ".yml");
        }

        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    @Override
    public @NotNull Logger getLogger() {
        if (this.logger == null) {
            loadLogger();
        }

        return this.logger;
    }

    private void closeLogger() {
        if (this.logFileHandler != null) {
            this.logFileHandler.close();
        }
    }

    private void loadLogger() {
        this.logger = Logger.getLogger("ToiletCore-" + getPascalCaseName());
        try {
            File logDir = new File(plugin.getDataFolder(), "logs");
            if (!logDir.exists()) {
                logDir.mkdirs(); // 如果目录不存在，则创建
            }

            File logFile = new File(plugin.getDataFolder(), "logs/" + moduleEnum.id + ".log");
            if (!logFile.exists()) logFile.createNewFile();

            this.logFileHandler = new FileHandler(logFile.getAbsolutePath(), true);
            Formatter formatter = new LogFormatter();
            this.logger.addHandler(this.logFileHandler);
            this.logFileHandler.setFormatter(formatter);
        } catch (IOException e) {
            plugin.getLogger().warning("模块 " + moduleEnum.id + " 加载日志时出错！");
            plugin.getLogger().warning(e.getMessage());
        }
    }

    public String getPascalCaseName() {
        String input = moduleEnum.id;

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (c == '-' || c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }
}
