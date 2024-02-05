package net.toiletmc.toiletcore.module.impl.premium;

import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.enums.Modules;
import net.toiletmc.toiletcore.module.interfaces.AbstractModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class PremiumModule extends AbstractModule implements Listener {
    private final List<String> commands = new ArrayList<>();

    public PremiumModule(ToiletCore plugin, Modules module) {
        super(plugin, module);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        reload();
    }

    @EventHandler
    public void OnJoin(PlayerJoinEvent event) {
        Logger logger = getPlugin().getLogger();

        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            UUID uuid = player.getUniqueId();
            int type = uuid.version();
            if (type == 4) {
                for (String command : commands) {
                    Bukkit.dispatchCommand(getPlugin().getServer().getConsoleSender(), command.replace("[player]", player.getName()));
                }
                logger.info(player.getName() + " 是正版用户，已执行命令。");
            }
        }
    }

    @Override
    public void reload() {
        commands.clear();
        commands.addAll(getConfig().getStringList("commands"));
        getPlugin().getLogger().info("正版验证奖励：" + commands.size() + "条命令已加载！");
    }
}
