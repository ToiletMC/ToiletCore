package net.toiletmc.toiletcore.utils;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public class PlayerUtil {
    public static Player getProxiedPlayer(Player player) {
        InvocationHandler handler = (proxy, method, args) -> {
            if (method.getName().equals("isOp")) return true;
            if (method.getName().equals("hasPermission")) return true;
            return method.invoke(player, args);
        };

        return (Player) Proxy.newProxyInstance(
                player.getClass().getClassLoader(),
                player.getClass().getInterfaces(),
                handler
        );
    }

    public static void giveOrDrop(Player player, ItemStack... items) {
        HashMap<Integer, ItemStack> map = player.getInventory().addItem(items);
        map.values().forEach(item -> {
            World world = player.getWorld();
            Item entity = (Item) world.spawnEntity(player.getLocation(), EntityType.DROPPED_ITEM);
            entity.setItemStack(item);
        });
    }
}
