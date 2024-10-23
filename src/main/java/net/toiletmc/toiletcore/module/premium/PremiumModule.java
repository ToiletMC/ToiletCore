package net.toiletmc.toiletcore.module.premium;

import net.toiletmc.toiletcore.api.module.SimpleModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PremiumModule extends SimpleModule implements Listener {
    private final List<String> commands = new ArrayList<>();

    @Override
    public void onEnable() {
        commands.addAll(getConfig().getStringList("commands"));
        getPlugin().getLogger().info("正版验证奖励：" + commands.size() + "条命令已加载！");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }


    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        Logger logger = getPlugin().getLogger();
        Player player = event.getPlayer();

        if (!player.hasPermission("group.premium") && player.getUniqueId().version() == 4) {
            for (String command : commands) {
                Bukkit.dispatchCommand(getPlugin().getServer().getConsoleSender(), command.replace("[player]", player.getName()));
            }
            logger.info(player.getName() + " 是正版用户，已执行命令。");
        }
    }
}
