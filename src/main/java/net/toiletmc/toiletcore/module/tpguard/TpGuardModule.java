package net.toiletmc.toiletcore.module.tpguard;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import net.toiletmc.toiletcore.api.module.SimpleModule;
import net.toiletmc.toiletcore.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Set;

public class TpGuardModule extends SimpleModule implements Listener {
    private final Set<String> commandList = Set.of("/tp", "/patrol", "/cmi tp", "/cmi patrol");

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    private void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("toiletcore.bypass.tpguard") ||
                !commandList.contains(event.getMessage().toLowerCase())) {
            return;
        }

        CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);
        boolean vanished = user.isVanished();
        if (!vanished) {
            event.setCancelled(true);
            MessageUtil.sendRedText(player, "请在隐身状态下传送玩家。");
        }
    }
}
