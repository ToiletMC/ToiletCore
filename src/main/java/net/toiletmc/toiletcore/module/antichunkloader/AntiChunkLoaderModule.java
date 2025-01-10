package net.toiletmc.toiletcore.module.antichunkloader;

import net.toiletmc.toiletcore.api.module.SimpleModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;

public class AntiChunkLoaderModule extends SimpleModule implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPortal(EntityPortalEnterEvent event) {
        if (isVehicle(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    private boolean isVehicle(Entity entity) {
        String type = entity.getType().toString();
        return type.contains("MINECART") || type.contains("BOAT");
    }
}
