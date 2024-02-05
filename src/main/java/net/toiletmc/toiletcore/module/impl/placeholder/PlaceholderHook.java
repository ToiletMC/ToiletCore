package net.toiletmc.toiletcore.module.impl.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lucko.spark.api.Spark;
import me.lucko.spark.api.statistic.StatisticWindow;
import me.lucko.spark.api.statistic.types.DoubleStatistic;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.toiletmc.toiletcore.module.interfaces.Reloadable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderHook extends PlaceholderExpansion implements Reloadable {
    private final PlaceholderModule module;
    private boolean missileWarsMode;
    private String footer;

    public PlaceholderHook(PlaceholderModule module) {
        this.module = module;
        reload();
    }


    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null || !player.isOnline()) {
            return "";
        }

        World world = player.getWorld();

        /*
            资源世界：⛏
            钻石剑：🗡
            TPS小圆：🌑
            世界时间：☀（白天）🌚（黑夜）
        */
        switch (params) {
            case "prefix" -> {
                if (missileWarsMode) {
                    return "§7️\uD83D\uDDE1§r" + getNameColor(player);
                } else if (world.getName().equals("resource_world")) {
                    return "§7⛏§r";
                } else {
                    return "";
                }
            }
            case "tps" -> {
                DoubleStatistic<StatisticWindow.TicksPerSecond> tps = module.getPlugin().getSpark().tps();
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
            case "world_time" -> {
                long gameTime = player.getWorld().getTime() % 24000;
                if (gameTime >= 0 && gameTime <= 12000) {
                    return "§6☀";
                } else {
                    return "§e\uD83C\uDF1A";
                }
            }
            case "footer" -> {
                return footer;
            }
            default -> {
                return "";
            }
        }
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

    @Override
    public void reload() {
        missileWarsMode = module.getConfig().getBoolean("prefix.missile-wars", false);
        footer = LegacyComponentSerializer.legacySection().serialize(
                MiniMessage.miniMessage().deserialize(
                        module.getConfig().getString("footer", "请修改页脚内容！")
                )
        );
    }
}
