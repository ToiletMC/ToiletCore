package net.toiletmc.toiletcore.module.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderHook extends PlaceholderExpansion {
    private final boolean missileWarsMode;
    private final Spark spark;

    public PlaceholderHook(PlaceholderModule module) {
        missileWarsMode = module.getConfig().getBoolean("emoji-setting.world.missile-wars", false);
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

        /*
            资源世界：⛏
            钻石剑：🗡
            TPS小圆：🌑
            世界时间：☀（白天）🌚（黑夜）
        */
        switch (arg1) {
            case "emoji" -> {
                switch (arg2) {
                    case "world" -> {
                        return getWorldEmoji(player);
                    }
                    case "tps" -> {
                        return getTPSEmoji();
                    }
                    case "worldtime" -> {
                        return getWorldtimeEmoji(player);
                    }
                }
            }
        }

        return "";
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

    @SuppressWarnings("deprecation")
    private @NotNull String getNameColor(Player player) {
        return player.getDisplayName().startsWith("§") && !player.getDisplayName().startsWith("§7")
                ? player.getDisplayName().substring(0, 2) : "";
    }

    private String getTPSEmoji() {
        DoubleStatistic<StatisticWindow.TicksPerSecond> tps = spark.tps();
        double tpsLast10Secs = 0;
        if (tps != null) {
            tpsLast10Secs = tps.poll(StatisticWindow.TicksPerSecond.SECONDS_10);
        }
        if (tpsLast10Secs >= 18) {
            return "§a\uD83C\uDF11";
        } else if (tpsLast10Secs >= 15) {
            return "§6\uD83C\uDF11";
        } else if (tpsLast10Secs >= 10) {
            return "§c\uD83C\uDF11";
        } else {
            return "§4\uD83C\uDF11";
        }
    }

    private String getWorldEmoji(Player player) {
        World world = player.getWorld();

        if (missileWarsMode) {
            return "§7️\uD83D\uDDE1" + getNameColor(player);
        } else if (world.getName().equals("resource_world")) {
            return "§7⛏§r";
        } else {
            return "";
        }
    }

    private String getWorldtimeEmoji(Player player) {
        long gameTime = player.getWorld().getTime() % 24000;
        if (gameTime >= 0 && gameTime <= 12000) {
            return "§6☀";
        } else {
            return "§e\uD83C\uDF1A";
        }
    }
}
