package net.toiletmc.toiletcore.module.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.misc.DoubleAverageInfo;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import me.lucko.spark.api.statistic.types.GenericStatistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderHook extends PlaceholderExpansion {
    private final Spark spark;

    public PlaceholderHook(PlaceholderModule module) {
        spark = module.getPlugin().getSpark();
    }


    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null || !player.isOnline()) {
            return "";
        }

        String[] split = params.split("_");
        String arg1 = split[0];
        String arg2 = split[1];

        if (arg1.equals("emoji")) {
            switch (arg2) {
                case "world" -> {
                    return getEmojiWorld(player);
                }
                case "tps" -> {
                    return getEmojiTPS();
                }
                case "worldtime" -> {
                    return getEmojiWorldtime(player);
                }
            }
        }

        return null;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "toilet";
    }

    @Override
    public @NotNull String getAuthor() {
        return "TheLittle_Yang";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    private String getEmojiTPS() {
        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();
        double tpsLast10Secs = 0;
        if (tps != null) {
            tpsLast10Secs = tps.poll(StatisticWindow.TicksPerSecond.SECONDS_10);
        }

        if (tpsLast10Secs >= 18) {
            GenericStatistic<DoubleAverageInfo, StatisticWindow.MillisPerTick> mspt = spark.mspt();
            double msptLast1Min = 0;
            if (mspt != null) {
                msptLast1Min = mspt.poll(StatisticWindow.MillisPerTick.SECONDS_10).percentile95th();
            }
            if (msptLast1Min <= 90) {
                return "§a\uD83C\uDF11";
            } else {
                return "§a\uD83C\uDF11§f［伪］";
            }
        } else if (tpsLast10Secs >= 15) {
            return "§6\uD83C\uDF11";
        } else if (tpsLast10Secs >= 10) {
            return "§c\uD83C\uDF11";
        } else if (tpsLast10Secs >= 5) {
            return "§4\uD83C\uDF11";
        } else {
            return "§4\uD83C\uDF11§f［爆］";
        }
    }

    private String getEmojiWorld(Player player) {
        World world = player.getWorld();
        return world.getName().equals("resource_world") ? "§7⛏§r" : "";
    }

    private String getEmojiWorldtime(Player player) {
        long gameTime = player.getWorld().getTime() % 24000;
        if (gameTime >= 0 && gameTime <= 12000) {
            return "§6☀";
        } else {
            return "§e\uD83C\uDF1A";
        }
    }
}
