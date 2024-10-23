package net.toiletmc.toiletcore.module.shart;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.toiletmc.toiletcore.api.module.SimpleModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShartModule extends SimpleModule implements CommandExecutor, Listener, TabCompleter {
    @Override
    public void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getCommand("shart").setExecutor(this);
        plugin.getCommand("shart").setTabCompleter(this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        plugin.getCommand("shart").setExecutor(null);
        plugin.getCommand("shart").setTabCompleter(null);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        if (args.length == 0 && sender instanceof Player player) {
            new ScatterShartTask(this, player, false);
            return true;
        }

        if (sender.hasPermission("toiletcore.shart.others") && args.length >= 1) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(Component.text(args[0] + " 当前并不在线！").color(NamedTextColor.RED));
                return true;
            }
            if (args.length == 1) {
                new ScatterShartTask(this, player, false);
            } else {
                new ScatterShartTask(this, player, true);
            }
        } else {
            sender.sendMessage(Component.text("未知的指令").color(NamedTextColor.RED));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("toiletcore.shart.others")) {
            return null;
        }

        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.startsWith(args[0]))
                    .toList();
        }

        if (args.length == 2) {
            return List.of("rainbow");
        }

        return null;
    }

    @EventHandler
    public void onMerge(ItemMergeEvent event) {
        if (event.getEntity().hasMetadata("shart"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPickUp(InventoryPickupItemEvent event) {
        if (event.getItem().hasMetadata("shart"))
            event.setCancelled(true);
    }
}
