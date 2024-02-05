package net.toiletmc.toiletcore.module.impl.debugstick;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.enums.Modules;
import net.toiletmc.toiletcore.module.interfaces.AbstractModule;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.List;

public class DebugStickModule extends AbstractModule implements Listener {
    private final List<String> excludedBlocks = new ArrayList<>();

    public DebugStickModule(ToiletCore plugin, Modules module) {
        super(plugin, module);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        reload();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        GameMode playerGameMode = player.getGameMode();
        Action action = event.getAction();

        if (player.getInventory().getItemInMainHand().getType().equals(Material.DEBUG_STICK) ||
                player.getInventory().getItemInOffHand().getType().equals(Material.DEBUG_STICK)
        ) {
            if (!playerGameMode.equals(GameMode.SURVIVAL)) {
                return;
            }

            //  调试棒会触发BlockPlaceEvent，所以就算不通知，对应的保护插件也会通知。
            if (event.useItemInHand() == Event.Result.DENY) {
                player.sendMessage(Component.text("你不能在这里使用调试棒！").color(NamedTextColor.RED));
                return;
            }

            if (!player.hasPermission("minecraft.debugstick.always")) {
                player.sendMessage("抱歉，只有建筑师可以使用调试棒。");
                event.setUseItemInHand(Event.Result.DENY);
                return;
            }

            Block clickedblock = event.getClickedBlock();
            if (clickedblock != null && action == Action.RIGHT_CLICK_BLOCK) {
                if (excludedBlocks.contains(clickedblock.getType().toString())) {
                    event.setUseItemInHand(Event.Result.DENY);
                    player.sendMessage("你不能对这种类型的方块使用调试棒！");
                }
            }
        }
    }

    @Override
    public void reload() {
        excludedBlocks.clear();
        excludedBlocks.addAll(getConfig().getStringList("excluded_blocks"));

        Bukkit.removeRecipe(new NamespacedKey(plugin, module.name));

        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, module.name), new ItemStack(Material.DEBUG_STICK, 1));
        recipe.shape(
                "  s",
                "np ",
                "pn ");
        recipe.setIngredient('n', Material.NETHERITE_INGOT);
        recipe.setIngredient('s', Material.NETHER_STAR);
        recipe.setIngredient('p', Material.STICK);
        Bukkit.addRecipe(recipe);
    }
}
