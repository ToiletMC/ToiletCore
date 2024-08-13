package net.toiletmc.toiletcore.module;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.api.module.Module;
import net.toiletmc.toiletcore.api.module.ToiletModule;
import net.toiletmc.toiletcore.module.authme.AuthmeModule;
import net.toiletmc.toiletcore.module.debugstick.DebugStickModule;
import net.toiletmc.toiletcore.module.effectonblock.EffectOnBlockModule;
import net.toiletmc.toiletcore.module.lagalert.LagAlertModule;
import net.toiletmc.toiletcore.module.placeholder.PlaceholderModule;
import net.toiletmc.toiletcore.module.premium.PremiumModule;
import net.toiletmc.toiletcore.module.shart.ShartModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleManager {
    private final ToiletCore plugin;
    private final List<Module> enabledModules = new ArrayList<>();

    public ModuleManager(ToiletCore plugin) {
        this.plugin = plugin;
    }

    public void enableAllModules() {
        plugin.getLogger().info("正在初始化模块……");
        List<Pair<ModuleEnum, Boolean>> allEnabledModules = new ArrayList<>();
        List<Pair<ModuleEnum, Boolean>> allDisabledModules = new ArrayList<>();
        for (ModuleEnum moduleEnum : ModuleEnum.values()) {
            if (!isEnableInConfig(moduleEnum)) {
                allDisabledModules.add(new ObjectObjectImmutablePair<>(moduleEnum, Boolean.FALSE));
                continue;
            }


            Class<? extends ToiletModule> clazz = moduleEnum.moduleClass;
            try {
                Constructor<? extends ToiletModule> constructor = clazz.getConstructor();
                ToiletModule moduleInstance = constructor.newInstance();
                moduleInstance.init(moduleEnum);
                moduleInstance.onEnable();
                enabledModules.add(moduleInstance);
                allEnabledModules.add(new ObjectObjectImmutablePair<>(moduleEnum, Boolean.TRUE));
            } catch (NoSuchMethodException | InvocationTargetException |
                     InstantiationException | IllegalAccessException e) {
                plugin.getLogger().severe("模块 " + moduleEnum.id + " 初始化时遇到错误⚠️！");
                allDisabledModules.add(new ObjectObjectImmutablePair<>(moduleEnum, Boolean.FALSE));
                throw new RuntimeException(e);
            }
        }

        plugin.getLogger().info("模块初始化完成！模块状态：");
        allEnabledModules.stream()
                .sorted(Comparator.comparing(p -> p.first().id))
                .forEach(p -> plugin.getLogger().info("模块已启用✅ - " + p.first().id));
        allDisabledModules.stream()
                .sorted(Comparator.comparing(p -> p.first().id))
                .forEach(p -> plugin.getLogger().info("模块未启用❎ - " + p.first().id));
    }

    public void disableAllModules() {
        plugin.getLogger().info("正在禁用所有模块……");
        enabledModules.forEach(net.toiletmc.toiletcore.api.module.Module::onDisable);
        enabledModules.clear();
    }

    private boolean isEnableInConfig(ModuleEnum moduleEnum) {
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean("module." + moduleEnum.id, false);
    }

    public enum ModuleEnum {
        AUTHME("authme", AuthmeModule.class, "Authme 密码算法支持"),
        PLACEHOLDER("placeholder-api", PlaceholderModule.class, "PlaceholderAPI 支持"),
        DEBUGSTICK("debug-stick", DebugStickModule.class, "生存模式调试棒"),
        LAGALERT("lag-alert", LagAlertModule.class, "滞后监测程序"),
        SHART("shart", ShartModule.class, "排泄"),
        PREMIUM("premium", PremiumModule.class, "正版玩家奖励"),
        EFFECTONBLOCK("effect-on-block", EffectOnBlockModule.class, "玩家区域内效果");

        public final String id;
        public final Class<? extends ToiletModule> moduleClass;
        public final String description;

        ModuleEnum(String id, Class<? extends ToiletModule> moduleClass, String description) {
            this.id = id;
            this.moduleClass = moduleClass;
            this.description = description;
        }
    }
}
