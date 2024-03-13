package net.toiletmc.toiletcore.module;

import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.enums.Module;
import net.toiletmc.toiletcore.module.interfaces.Reloadable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager implements Reloadable {
    private final ToiletCore plugin;
    private final List<net.toiletmc.toiletcore.module.interfaces.Module> enabledModules = new ArrayList<>();

    public ModuleManager(ToiletCore plugin) {
        this.plugin = plugin;
        initModuleInstance();
    }

    @Override
    public void reload() {
        enabledModules.forEach(Reloadable::reload);
    }

    private void initModuleInstance() {
        for (Module module : Module.values()) {
            if (!isEnable(module.name)) {
                plugin.getLogger().info("模块未启用❎ - " + module.name);
                continue;
            }

            Class<? extends net.toiletmc.toiletcore.module.interfaces.Module> clazz = module.moduleClass;
            try {
                Constructor<? extends net.toiletmc.toiletcore.module.interfaces.Module> constructor = clazz.getConstructor(ToiletCore.class, Module.class);
                enabledModules.add(constructor.newInstance(plugin, module));
            } catch (NoSuchMethodException | InvocationTargetException |
                     InstantiationException | IllegalAccessException e) {
                plugin.getLogger().severe("模块 " + module.name + " 初始化时遇到错误⚠️！");
                throw new RuntimeException(e);
            }
            plugin.getLogger().info("模块已启用✅ - " + module.name);
        }
    }

    private boolean isEnable(String Module) {
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean("module." + Module, false);
    }
}
