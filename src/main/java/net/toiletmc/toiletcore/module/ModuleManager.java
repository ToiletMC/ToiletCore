package net.toiletmc.toiletcore.module;

import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.api.module.AutoEnable;
import net.toiletmc.toiletcore.api.module.SimpleModule;
import net.toiletmc.toiletcore.module.antichunkloader.AntiChunkLoaderModule;
import net.toiletmc.toiletcore.module.antienderman.AntiEndermanModule;
import net.toiletmc.toiletcore.module.authme.AuthmeModule;
import net.toiletmc.toiletcore.module.betterdrops.BetterDropsModule;
import net.toiletmc.toiletcore.module.cdk.CDKModule;
import net.toiletmc.toiletcore.module.debugstick.DebugStickModule;
import net.toiletmc.toiletcore.module.effectonblock.EffectOnBlockModule;
import net.toiletmc.toiletcore.module.hook.HookModule;
import net.toiletmc.toiletcore.module.lagalert.LagAlertModule;
import net.toiletmc.toiletcore.module.magic.MagicModule;
import net.toiletmc.toiletcore.module.premium.PremiumModule;
import net.toiletmc.toiletcore.module.qq.QQModule;
import net.toiletmc.toiletcore.module.shart.ShartModule;
import net.toiletmc.toiletcore.module.tpguard.TpGuardModule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Level;

public class ModuleManager {
    private final ToiletCore plugin;
    private final List<SimpleModule> enabledModules = new ArrayList<>();

    public ModuleManager(ToiletCore plugin) {
        this.plugin = plugin;
    }

    public void enableAllModules() {
        plugin.getLogger().info("正在初始化模块……");
        Set<ModuleEnum> allEnabledModules = new HashSet<>();
        Set<ModuleEnum> allDisabledModules = new HashSet<>();

        // 清理配置文件，删除不在插件中的模块
        ConfigurationSection modules = plugin.getConfig().getConfigurationSection("module");
        if (modules != null) {
            List<String> stringList = Arrays.stream(ModuleEnum.values()).map(ModuleEnum -> ModuleEnum.id).toList();
            modules.getKeys(false).forEach(key -> {
                if (!stringList.contains(key)) {
                    plugin.getConfig().set("module." + key, null);
                    plugin.saveConfig();
                }
            });
        }

        for (ModuleEnum moduleEnum : ModuleEnum.values()) {
            ModuleStatus moduleStatus = getConfigStatus(moduleEnum);

            if (moduleStatus == ModuleStatus.DISABLED) {
                allDisabledModules.add(moduleEnum);
                continue;
            }

            try {
                SimpleModule moduleInstance = moduleEnum.moduleClass.getDeclaredConstructor().newInstance();
                moduleInstance.init(moduleEnum);

                if (moduleStatus == ModuleStatus.AUTO) {
                    boolean whetherEnable = ((AutoEnable) moduleInstance).whetherEnable();
                    if (!whetherEnable) {
                        allDisabledModules.add(moduleEnum);
                        plugin.getLogger().info(moduleEnum.id + " 不符合启动条件，已自动禁用。");
                        continue;
                    } else {
                        plugin.getLogger().info(moduleEnum.id + " 符合启动条件，已自动启用。");
                    }
                }

                moduleInstance.onEnable();
                enabledModules.add(moduleInstance);
                allEnabledModules.add(moduleEnum);
            } catch (NoClassDefFoundError err) {
                allDisabledModules.add(moduleEnum);
                plugin.getLogger().log(Level.SEVERE, "模块 " + moduleEnum.id + " 所需类缺失（可能未安装依赖或未正确打包）⚠️：" + err.getMessage());
            } catch (Exception e) {
                allDisabledModules.add(moduleEnum);
                plugin.getLogger().log(Level.SEVERE, "模块 " + moduleEnum.id + " 初始化时遇到错误⚠️！", e);
            }
        }

        plugin.getLogger().info("模块初始化完成！");
        allEnabledModules.stream()
                .map(p -> p.id)
                .sorted(String::compareTo)
                .reduce((a, b) -> a + ", " + b)
                .ifPresent(str -> plugin.getLogger().info("✅ 启用的模块：" + str));

        allDisabledModules.stream()
                .map(p -> p.id)
                .sorted(String::compareTo)
                .reduce((a, b) -> a + ", " + b)
                .ifPresent(str -> plugin.getLogger().info("🚫 未启用的模块：" + str));
    }

    public void disableAllModules() {
        plugin.getLogger().info("正在禁用所有模块……");
        enabledModules.forEach(SimpleModule::onDisable);
        enabledModules.forEach(SimpleModule::disabled);
        enabledModules.clear();
    }

    public ModuleStatus getConfigStatus(ModuleEnum moduleEnum) {
        FileConfiguration config = plugin.getConfig();
        String status = config.getString("module." + moduleEnum.id);

        if (status == null) {
            config.set("module." + moduleEnum.id, moduleEnum.defaultStatus.name().toLowerCase());
            plugin.saveConfig();
            return moduleEnum.defaultStatus;
        } else {
            return ModuleStatus.valueOf(status.toUpperCase());
        }
    }

    public <T extends SimpleModule> T getModuleInstance(Class<T> moduleClass) {
        return enabledModules.stream()
                .filter(moduleClass::isInstance)
                .findFirst()
                .map(moduleClass::cast) // 安全类型转换
                .orElse(null);
    }

    public enum ModuleEnum {
        AUTHME(AuthmeModule.class, "Authme 密码算法支持", ModuleStatus.AUTO),
        DEBUG_STICK(DebugStickModule.class, "生存模式调试棒", ModuleStatus.DISABLED),
        LAG_ALERT(LagAlertModule.class, "滞后监测程序", ModuleStatus.DISABLED),
        SHART(ShartModule.class, "排泄", ModuleStatus.ENABLED),
        PREMIUM(PremiumModule.class, "正版玩家奖励", ModuleStatus.DISABLED),
        EFFECT_ON_BLOCK(EffectOnBlockModule.class, "玩家区域内效果", ModuleStatus.DISABLED),
        HOOK(HookModule.class, "???", ModuleStatus.DISABLED),
        ANTI_ENDERMAN(AntiEndermanModule.class, "末影人计划生育", ModuleStatus.DISABLED),
        ANTI_CHUNK_LOADER(AntiChunkLoaderModule.class, "禁止区块加载器", ModuleStatus.DISABLED),
        CDK(CDKModule.class, "CDK", ModuleStatus.DISABLED),
        QQ(QQModule.class, "QQ模块", ModuleStatus.DISABLED),
        TP_GUARD(TpGuardModule.class, "巡逻守卫者", ModuleStatus.DISABLED),
        BETTER_DROPS(BetterDropsModule.class, "更好的掉落物", ModuleStatus.ENABLED),
        MAGIC(MagicModule.class, "魔法兼容", ModuleStatus.DISABLED),
        ;

        public final String id;
        public final Class<? extends SimpleModule> moduleClass;
        public final String description;
        public final ModuleStatus defaultStatus;

        ModuleEnum(Class<? extends SimpleModule> moduleClass, String description, ModuleStatus defaultStatus) {
            this.id = this.name().replace('_', '-').toLowerCase();
            this.moduleClass = moduleClass;
            this.description = description;
            this.defaultStatus = defaultStatus;
        }
    }

    public enum ModuleStatus {
        ENABLED,
        DISABLED,
        AUTO
    }
}
