package net.toiletmc.toiletcore.module.lagalert;

import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class MSPTCheckTask extends BukkitRunnable {
    private final LagAlertModule module;
    private int broTimes = 0;
    private boolean skip = false;

    private final int maxMSPT;
    private final String message;

    public MSPTCheckTask(LagAlertModule module) {
        this.module = module;
        maxMSPT = module.getConfig().getInt("max-mspt", 80);
        message = module.getConfig().getString("lag-broadcast", "请通知管理员填写提示信息。");
    }


    @Override
    public void run() {
        if (skip) {
            skip = false;
            return;
        }

        if (broTimes != 0) {
            broTimes = broTimes >= 15 ? 0 : broTimes + 1;
            return;
        }

        if (getMspt() >= maxMSPT) {
            broadcastMessage();
            broTimes++;
        }
    }


    private double getMspt() {
        GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> msptInfo = module.getPlugin().getSpark().mspt();
        DoubleAverageInfo msptLastMin = msptInfo.poll(StatisticWindow.MillisPerTick.MINUTES_1);
        return msptLastMin.percentile95th();
    }

    private void broadcastMessage() {
        String willSend = message.replaceAll(
                "%mspt%", String.valueOf((int) getMspt()));
        Bukkit.getServer().sendMessage(MiniMessage.miniMessage().deserialize(willSend));

    }
}
