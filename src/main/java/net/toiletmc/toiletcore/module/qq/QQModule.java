package net.toiletmc.toiletcore.module.qq;

import net.toiletmc.toiletcore.api.module.SimpleModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QQModule extends SimpleModule implements CommandExecutor, TabCompleter {

    @Override
    public void onEnable() {
        getPlugin().getCommand("qq").setExecutor(this);
        getPlugin().getCommand("qq").setTabCompleter(this);
    }

    @Override
    public void onDisable() {
        getPlugin().getCommand("qq").setExecutor(null);
        getPlugin().getCommand("qq").setTabCompleter(null);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("bind");
        }

        return null;
    }
}
