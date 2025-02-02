package net.toiletmc.toiletcore;

import lombok.Getter;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.SparkProvider;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import net.toiletmc.toiletcore.http.request.MSPTRequest;
import net.toiletmc.toiletcore.module.ModuleManager;
import net.toiletmc.toiletcore.module.hook.Hook;
import net.toiletmc.toiletcore.module.hook.HookGroup;

import net.toiletmc.toiletcore.module.lagalert.LagAlertModule;
import net.toiletmc.toiletcore.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;

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
            if (sender.hasPermission("toiletcore.admin")) {
                MessageUtil.sendRedText(sender, "未知的指令");
                return true;
            }
            switch (args[0]) {
                case "reload" -> {
                    reloadPlugin();
                    MessageUtil.sendNormalText(sender, "插件已重载！");
                }
                case "debug" -> {
                    String url = moduleManager.getModuleInstance(LagAlertModule.class).getWebhookUrl();
                    HttpHelper.sendHttpRequest(url, new MSPTRequest((int) getLast1MinMSPT(), "debug test"));
                    MessageUtil.sendNormalText(sender, "LagAlert消息已上报！");
                }
            }
        } else {
            if (args[0].equals("toggle")) {
                Player player = sender instanceof Player ? (Player) sender : null;

                if (args[1].equals("potato")) {
                    LagAlertModule lagAlert = moduleManager.getModuleInstance(LagAlertModule.class);
                    if (lagAlert == null) {
                        MessageUtil.sendNormalText(sender, "土豆功能还未在服务器中启用！");
                    } else {
                        if (player == null) {
                            MessageUtil.sendRedText(sender, "只有游戏中的玩家可以执行该命令！");
                        } else {
                            lagAlert.setReceivePotatoStatus(player, !lagAlert.getReceivePotatoStatus(player));
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("reload", "debug", "toggle").sorted().filter(s -> s.startsWith(args[0])).toList();
        } else if (args.length == 2) {
            if (args[0].equals("toggle")) {
                return Stream.of("potato").sorted().filter(s -> s.startsWith(args[1])).toList();
            }
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
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Spark 服务异常，请排查错误！", e);
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

    public void debugLog(String message) {
        if (debugMode) {
            getLogger().warning(message);
        }
    }
}
