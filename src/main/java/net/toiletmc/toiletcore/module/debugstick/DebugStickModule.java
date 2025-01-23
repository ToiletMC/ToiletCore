package net.toiletmc.toiletcore.module.debugstick;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadableNBT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.toiletmc.toiletcore.api.module.SimpleModule;
import net.toiletmc.toiletcore.utils.MaterialUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class DebugStickModule extends SimpleModule implements Listener {
    private final List<Material> blocklistBlocks = new ArrayList<>();

    @Override
    public void onEnable() {
        addRecipe();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        blocklistBlocks.addAll(MaterialUtil.getMaterialSet(getConfig().getStringList("blacklist-blocks")));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        removeRecipe();
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (player.isOp()) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.DEBUG_STICK) {
            return;
        }

        if (!player.hasPermission("minecraft.debugstick.always")) {
            player.sendActionBar(Component.text("抱歉，只有建筑师可以使用调试棒。").color(NamedTextColor.RED));
            event.setUseItemInHand(Event.Result.DENY);
            return;
        }

        //  调试棒会触发BlockPlaceEvent，所以就算不通知，对应的保护插件也会通知。
        if (event.useItemInHand() == Event.Result.DENY) {
            player.sendActionBar(Component.text("你不能在这里使用调试棒！").color(NamedTextColor.RED));
            return;
        }

        Block clickedblock = event.getClickedBlock();
        if (clickedblock != null && action == Action.RIGHT_CLICK_BLOCK) {
            BlockData blockData = clickedblock.getBlockData();
            if (blocklistBlocks.contains(clickedblock.getType()) ||
                    blockData instanceof Ageable ||
                    blockData instanceof Beehive) {
                event.setUseItemInHand(Event.Result.DENY);
                player.sendActionBar(Component.translatable(clickedblock)
                        .append(Component.text("已被列入调试棒黑名单！")).color(NamedTextColor.RED));
            }

            if (useWaterloggedState(clickedblock, item)) {
                event.setUseItemInHand(Event.Result.DENY);
                player.sendActionBar(Component.text("你不能切换方块的含水状态！").color(NamedTextColor.RED));
            }
        }
    }

    private void addRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, moduleEnum.toString()),
                new ItemStack(Material.DEBUG_STICK, 1));
        recipe.shape(
                "  s",
                "np ",
                "pn ");
        recipe.setIngredient('n', Material.NETHERITE_INGOT);
        recipe.setIngredient('s', Material.NETHER_STAR);
        recipe.setIngredient('p', Material.STICK);
        Bukkit.addRecipe(recipe);
    }

    private void removeRecipe() {
        Bukkit.removeRecipe(new NamespacedKey(plugin, moduleEnum.toString()));
    }

    private boolean useWaterloggedState(Block block, ItemStack debugStick) {
        if (!(block.getBlockData() instanceof Waterlogged)) {
            return false;
        }

        if (debugStick == null || debugStick.getType() != Material.DEBUG_STICK) {
            return false;
        }

        String blockType = block.getType().toString().toLowerCase();
        return NBT.getComponents(debugStick, nbt -> {
            if (nbt.hasTag("minecraft:debug_stick_state")) {
                ReadableNBT debugStickState = nbt.getCompound("minecraft:debug_stick_state");
                if (debugStickState != null) {
                    String currentState = debugStickState.getString("minecraft:" + blockType);
                    return "waterlogged".equals(currentState);
                }
            }
            return false;
        });
    }
}

