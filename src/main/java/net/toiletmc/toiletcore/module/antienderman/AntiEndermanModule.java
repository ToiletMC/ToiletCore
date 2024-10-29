package net.toiletmc.toiletcore.module.antienderman;

import net.toiletmc.toiletcore.api.module.SimpleModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Enderman;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class AntiEndermanModule extends SimpleModule implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEndermanPutMushroom(EntityChangeBlockEvent event) {
        if (!(event.getEntity() instanceof Enderman)) {
            return;
        }
        if (isMushroom(event.getTo()) || isMushroom(event.getBlock().getType())) {
            event.setCancelled(true);
            Enderman enderman = (Enderman) event.getEntity();
            enderman.setCarriedBlock(null);
        }
    }

    private boolean isMushroom(Material type) {
        return type == Material.BROWN_MUSHROOM
                || type == Material.RED_MUSHROOM
                || type == Material.CRIMSON_FUNGUS
                || type == Material.WARPED_FUNGUS;
    }
}
