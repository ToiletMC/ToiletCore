package net.toiletmc.toiletcore;

import lombok.Getter;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import net.toiletmc.toiletcore.module.ModuleManager;
import net.toiletmc.toiletcore.module.hook.Hook;
import net.toiletmc.toiletcore.module.hook.HookGroup;

import net.toiletmc.toiletcore.utils.MsgUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public final class ToiletCore extends JavaPlugin {
    @Getter
    private boolean debugMode;
    @Getter
    private static ToiletCore instance;
    private Spark spark = null;
    @Getter
    private ModuleManager moduleManager;

    @Override
    public void onLoad() {
        ConfigurationSerialization.registerClass(Hook.class);
        ConfigurationSerialization.registerClass(HookGroup.class);
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        handleConfig();

        // Paper 的 spark 不知道在时候加载，暂时先这样。
        // https://github.com/PaperMC/Paper/issues/11416
        new SparkInitTask().runTaskTimer(this, 20L, 20L);

        initModuleManager();
    }

    @Override
    public void onDisable() {
        moduleManager.disableAllModules();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (args.length == 1) {
            switch (args[0]) {
                case "reload" -> {
                    reloadPlugin();
                    MsgUtil.sendNormalText(sender, "插件已重载！");
                }
                case "debug" -> {
                    sender.sendMessage("Bukkit.getTickTimes() 执行结果：" + Arrays.toString(Bukkit.getTickTimes()));
                    sender.sendMessage("Bukkit.getCurrentTick() 执行结果：" + Bukkit.getCurrentTick());
                    sender.sendMessage("Bukkit.getTPS() 执行结果：" + Arrays.toString(Bukkit.getTPS()));
                    sender.sendMessage("Bukkit.getAverageTickTime() 执行结果：" + Bukkit.getAverageTickTime());
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reload", "debug");
        }
        return null;
    }

    public void reloadPlugin() {
        reloadConfig();
        handleConfig();
        moduleManager.disableAllModules();
        moduleManager.enableAllModules();
    }

    private void handleConfig() {
        debugMode = getConfig().getBoolean("debug-mode", false);
    }

    @Deprecated
    private class SparkInitTask extends BukkitRunnable {
        @Override
        public void run() {
            initSpark();
            if (spark != null) {
                this.cancel();
            }
        }
    }

    private void initSpark() {
        try {
            spark = SparkProvider.get();
            getLogger().info("已挂钩到 Spark");
        } catch (NoClassDefFoundError e) {
            getLogger().severe("Spark 服务异常，请排查错误！");
        }
    }

    private void initModuleManager() {
        moduleManager = new ModuleManager(this);
        moduleManager.enableAllModules();
    }

    public double getLast10SecsTPS() {
        if (spark == null) {
            return 0;
        }

        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();
        return tps.poll(StatisticWindow.TicksPerSecond.SECONDS_10);
    }

    public double getLast10SecsMSPT() {
        if (spark == null) {
            return 0;
        }

        GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> msptInfo = spark.mspt();
        return msptInfo.poll(StatisticWindow.MillisPerTick.SECONDS_10).percentile95th();

    }

    public double getLast1MinMSPT() {
        if (spark == null) {
            return 0;
        }

        GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> msptInfo = spark.mspt();
        return msptInfo.poll(StatisticWindow.MillisPerTick.MINUTES_1).percentile95th();
    }
}
