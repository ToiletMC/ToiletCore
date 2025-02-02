package net.toiletmc.toiletcore.module;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.api.module.SimpleModule;
import net.toiletmc.toiletcore.module.antichunkloader.AntiChunkLoaderModule;
import net.toiletmc.toiletcore.module.antienderman.AntiEndermanModule;
import net.toiletmc.toiletcore.module.authme.AuthmeModule;
import net.toiletmc.toiletcore.module.cdk.CDKModule;
import net.toiletmc.toiletcore.module.debugstick.DebugStickModule;
import net.toiletmc.toiletcore.module.effectonblock.EffectOnBlockModule;
import net.toiletmc.toiletcore.module.eggrespawn.EggRespawnModule;
import net.toiletmc.toiletcore.module.hook.HookModule;
import net.toiletmc.toiletcore.module.lagalert.LagAlertModule;
import net.toiletmc.toiletcore.module.premium.PremiumModule;
import net.toiletmc.toiletcore.module.qq.QQModule;
import net.toiletmc.toiletcore.module.shart.ShartModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ModuleManager {
    private final ToiletCore plugin;
    private final List<SimpleModule> enabledModules = new ArrayList<>();

    public ModuleManager(ToiletCore plugin) {
        this.plugin = plugin;
    }

    public void enableAllModules() {
        plugin.getLogger().info("正在初始化模块……");
        List<Pair<ModuleEnum, Boolean>> allEnabledModules = new ArrayList<>();
        List<Pair<ModuleEnum, Boolean>> allDisabledModules = new ArrayList<>();
        for (ModuleEnum moduleEnum : ModuleEnum.values()) {
            if (!isEnabledInConfig(moduleEnum)) {
                allDisabledModules.add(new ObjectObjectImmutablePair<>(moduleEnum, Boolean.FALSE));
                continue;
            }

            try {
                SimpleModule moduleInstance = moduleEnum.moduleClass.getDeclaredConstructor().newInstance();
                moduleInstance.init(moduleEnum);
                moduleInstance.onEnable();
                enabledModules.add(moduleInstance);
                allEnabledModules.add(new ObjectObjectImmutablePair<>(moduleEnum, Boolean.TRUE));
            } catch (Exception e) {
                allDisabledModules.add(new ObjectObjectImmutablePair<>(moduleEnum, Boolean.FALSE));
                plugin.getLogger().log(Level.SEVERE, "模块 " + moduleEnum.id + " 初始化时遇到错误⚠️！", e);
            }
        }

        plugin.getLogger().info("模块初始化完成！");
        allEnabledModules.stream()
                .map(p -> p.first().id)
                .sorted(String::compareTo)
                .reduce((a, b) -> a + ", " + b)
                .ifPresent(str -> plugin.getLogger().info("✅ 启用的模块：" + str));

        allDisabledModules.stream()
                .map(p -> p.first().id)
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

    public boolean isEnabledInConfig(ModuleEnum moduleEnum) {
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean("module." + moduleEnum.id, false);
    }

    public <T extends SimpleModule> T getModuleInstance(Class<T> moduleClass) {
        return enabledModules.stream()
                .filter(moduleClass::isInstance)
                .findFirst()
                .map(moduleClass::cast) // 安全类型转换
                .orElse(null);
    }

    public enum ModuleEnum {
        AUTHME(AuthmeModule.class, "Authme 密码算法支持"),
        DEBUG_STICK(DebugStickModule.class, "生存模式调试棒"),
        LAG_ALERT(LagAlertModule.class, "滞后监测程序"),
        SHART(ShartModule.class, "排泄"),
        PREMIUM(PremiumModule.class, "正版玩家奖励"),
        EFFECT_ON_BLOCK(EffectOnBlockModule.class, "玩家区域内效果"),
        HOOK(HookModule.class, "???"),
        EGG_RESPAWN(EggRespawnModule.class, "龙蛋重生计划"),
        ANTI_ENDERMAN(AntiEndermanModule.class, "末影人计划生育"),
        ANTI_CHUNK_LOADER(AntiChunkLoaderModule.class, "禁止区块加载器"),
        CDK(CDKModule.class, "CDK"),
        QQ(QQModule.class, "QQ模块");

        public final String id;
        public final Class<? extends SimpleModule> moduleClass;
        public final String description;

        ModuleEnum(Class<? extends SimpleModule> moduleClass, String description) {
            this.id = this.name().replace('_', '-').toLowerCase();
            this.moduleClass = moduleClass;
            this.description = description;
        }
    }
}
