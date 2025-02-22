package net.toiletmc.toiletcore.module.betterdrops;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.toiletmc.toiletcore.api.module.SimpleModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BetterDropsModule extends SimpleModule implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(ignoreCancelled = true)
    private void onItemFrameBreak(HangingBreakEvent event) {
        Hanging entity = event.getEntity();
        if (entity instanceof ItemFrame itemFrame) {
            if (!itemFrame.isVisible()) {
                event.setCancelled(true);

                entity.remove();

                World world = entity.getWorld();
                world.dropItem(entity.getLocation(), getInvisibleItemFrame());
                if (!itemFrame.getItem().isEmpty()) {
                    world.dropItem(entity.getLocation(), itemFrame.getItem());
                }
            }
        }
    }

    private ItemStack getInvisibleItemFrame() {
        ItemStack invisibleItemFrame = new ItemStack(Material.ITEM_FRAME);
        ItemMeta itemMeta = invisibleItemFrame.getItemMeta();
        itemMeta.displayName(Component.text("隐形物品展示框").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        invisibleItemFrame.setItemMeta(itemMeta);

        NBT.modifyComponents(invisibleItemFrame, component -> {
            ReadWriteNBT compound = component.getOrCreateCompound("minecraft:entity_data");
            compound.setString("id", "minecraft:item_frame");
            compound.setByte("Invisible", (byte) 1);
        });

        return invisibleItemFrame;
    }
}
