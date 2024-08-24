package net.toiletmc.toiletcore.module.shart;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ScatterShartTask extends BukkitRunnable {
    final private ShartModule module;

    final private Material[] rainbowSmallItems = {
            Material.RED_DYE, Material.ORANGE_DYE, Material.YELLOW_DYE,
            Material.LIME_DYE, Material.MAGENTA_DYE, Material.PURPLE_DYE
    };
    final private Material[] rainbowBigItems = {
            Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL,
            Material.LIME_WOOL, Material.MAGENTA_WOOL, Material.PURPLE_WOOL
    };
    final private Color[] rainbowColors = {
            Color.fromRGB(176, 46, 38),
            Color.fromRGB(249, 128, 29),
            Color.fromRGB(254, 216, 61),
            Color.fromRGB(128, 199, 31),
            Color.fromRGB(199, 78, 189),
            Color.fromRGB(137, 50, 184)
    };
    final private Color poopColor = Color.fromRGB(102, 51, 0);


    private int i = 0;
    final private Player player;
    final private int maxShart;
    final private String source;
    final private boolean rainbow;

    public ScatterShartTask(ShartModule module, Player player, boolean rainbow) {
        this.module = module;
        this.player = player;
        this.rainbow = rainbow;
        maxShart = module.getConfig().getInt("sharts", 25);
        source = module.getConfig().getString("source", "butt");
        this.runTaskTimer(module.getPlugin(), 1, 2);
    }

    @Override
    public void run() {
        i++;
        if (!player.isOnline() || i > maxShart) {
            this.cancel();
            return;
        }

        Random random = new Random();

        // 拉小💩
        Item smallItem;
        if (rainbow) {
            smallItem = dropItem(player, new ItemStack(rainbowSmallItems[random.nextInt(rainbowSmallItems.length)]));
        } else {
            smallItem = dropItem(player, new ItemStack(Material.COCOA_BEANS));
        }

        if (source.equals("butt")) {
            smallItem.setVelocity(player.getLocation().getDirection().normalize().multiply(-0.5));
        } else {
            smallItem.setVelocity(new Vector(random.nextFloat() * 2 - 1, 0.5, random.nextFloat() * 2 - 1));
        }
        smallItem.setMetadata("shart", new FixedMetadataValue(module.getPlugin(), true));
        smallItem.setPickupDelay(Integer.MAX_VALUE);

        if (rainbow) {
            spawnParticle(smallItem, rainbowColors[random.nextInt(rainbowColors.length)]);
        } else {
            spawnParticle(smallItem, poopColor);
        }
        new DeleteItemTask(module, smallItem, 40);

        // 拉大💩
        if (i % 5 == 0) {
            Item bigitem;
            if (rainbow) {
                bigitem = dropItem(player, new ItemStack(rainbowBigItems[random.nextInt(rainbowBigItems.length)], 2));
            } else {
                bigitem = dropItem(player, new ItemStack(Material.BROWN_WOOL, 2));

            }

            if (source.equals("butt")) {
                bigitem.setVelocity(player.getLocation().getDirection().normalize().multiply(-0.1));
            }
            bigitem.setMetadata("shart", new FixedMetadataValue(module.getPlugin(), true));
            bigitem.setPickupDelay(Integer.MAX_VALUE);
            new DeleteItemTask(module, bigitem, 120);
        }
    }

    private Item dropItem(Player player, ItemStack itemStack) {
        return player.getLocation().getWorld().dropItem(player.getLocation().add(0.0, 0.3, 0.0).subtract(player.getLocation().getDirection().normalize().multiply(0.3)), itemStack);
    }

    private void spawnParticle(Item smallItem, Color color) {
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE,
                smallItem.getLocation().add(0.0, 0.3, 0.0),
                1,
                new Particle.DustOptions(color, 1F)
        );
    }
}
