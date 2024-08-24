package net.toiletmc.toiletcore.module.eggrespawn;

import net.toiletmc.toiletcore.api.module.ToiletModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class EggRespawnModule extends ToiletModule implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        final World world = e.getEntity().getLocation().getWorld();

        if (e.getEntityType() != EntityType.ENDER_DRAGON)
            return;

        if (world.getEnvironment() != World.Environment.THE_END)
            return;

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Location loc = new Location(world, 0.0D, 0.0D, 0.0D);
            loc.setY(loc.getWorld().getHighestBlockYAt(0, 0));
            Block highestBlock = world.getBlockAt(loc);

            loc.setY((loc.getWorld().getHighestBlockYAt(0, 0) - 1));
            Block secondHighestBlock = world.getBlockAt(loc);

            if (highestBlock.getType().equals(Material.BEDROCK)) {
                loc.setY((loc.getWorld().getHighestBlockYAt(0, 0) + 1));
                generateChest(loc);
            } else if (secondHighestBlock.getType().equals(Material.BEDROCK)) {
                generateChest(highestBlock.getLocation());
            } else {
                plugin.getLogger().warning("错误，无法在世界重生龙蛋：" + loc.getWorld().getName());
                return;
            }

            plugin.getLogger().info("龙蛋在世界重生：" + loc.getWorld().getName());
        }, 200L);
    }

    private void generateChest(Location location) {
        Block block = location.getBlock();
        if (block.getType() != Material.CHEST) {
            block.setType(Material.CHEST);
        }

        Chest chest = (Chest) block.getState();
        Inventory chestInventory = chest.getInventory();
        HashMap<Integer, ItemStack> map = chestInventory.addItem(new ItemStack(Material.DRAGON_EGG));
        if (!map.isEmpty()) {
            chestInventory.setItem(0, map.get(0));
        }
    }
}
