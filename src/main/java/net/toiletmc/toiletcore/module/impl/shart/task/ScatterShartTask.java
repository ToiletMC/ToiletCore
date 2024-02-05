package net.toiletmc.toiletcore.module.impl.shart.task;

import net.toiletmc.toiletcore.module.impl.shart.ShartModule;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ScatterShartTask extends BukkitRunnable {
    final private ShartModule module;
    private int i = 0;
    final private Player player;
    final private ItemStack is = new ItemStack(Material.COCOA_BEANS);
    final private int maxShart;
    final private String source;

    public ScatterShartTask(ShartModule module, Player player) {
        this.module = module;
        this.player = player;
        maxShart = module.getConfig().getInt("sharts", 25);
        source = module.getConfig().getString("source", "butt");
        this.runTaskTimer(module.getPlugin(), 1, 2);
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            this.cancel();
            return;
        }

        i++;
        if (i > maxShart) {
            this.cancel();
            return;
        }

        Random random = new Random();

        // 拉小💩
        var item = player.getLocation().getWorld().dropItem(player.getLocation().add(0.0, 0.3, 0.0).subtract(player.getLocation().getDirection().normalize().multiply(0.3)), is);
        if (source.equals("butt")) {
            item.setVelocity(player.getLocation().getDirection().normalize().multiply(-0.5));
        } else {
            item.setVelocity(new Vector(random.nextFloat() * 2 - 1, 0.5, random.nextFloat() * 2 - 1));
        }
        item.setMetadata("shart", new FixedMetadataValue(module.getPlugin(), true));
        item.setPickupDelay(Integer.MAX_VALUE);
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, item.getLocation().add(0.0, 0.3, 0.0), 1, new Particle.DustOptions(Color.fromRGB(102, 51, 0), 1F));
        new DeleteItemTask(module, item, 40);

        // 拉大💩
        if (i % 5 == 0) {
            var bigitem = player.getLocation().getWorld().dropItem(player.getLocation().add(0.0, 0.3, 0.0).subtract(player.getLocation().getDirection().normalize().multiply(0.3)), new ItemStack(Material.BROWN_WOOL, 2));
            if (source.equals("butt")) {
                bigitem.setVelocity(player.getLocation().getDirection().normalize().multiply(-0.1));
            }
            bigitem.setMetadata("shart", new FixedMetadataValue(module.getPlugin(), true));
            bigitem.setPickupDelay(Integer.MAX_VALUE);
            new DeleteItemTask(module, bigitem, 120);
        }
    }
}
