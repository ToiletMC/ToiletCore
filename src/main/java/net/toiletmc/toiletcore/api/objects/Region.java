package net.toiletmc.toiletcore.api.objects;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Region {
    boolean isInRegion(Location l);

    default boolean isInRegion(Player player) {
        if (player == null || !player.isOnline()) {
            return false;
        } else {
            return isInRegion(player.getLocation());
        }
    }
}
