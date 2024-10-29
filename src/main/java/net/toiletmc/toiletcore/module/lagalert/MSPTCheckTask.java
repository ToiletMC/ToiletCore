package net.toiletmc.toiletcore.module.lagalert;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class MSPTCheckTask extends BukkitRunnable {
    private final LagAlertModule module;
    private int broTimes = 0;

    private final int maxMSPT;
    private final String message;
    private final boolean givePatato;

    public MSPTCheckTask(LagAlertModule module) {
        this.module = module;
        this.givePatato = module.getConfig().getBoolean("give-potato", true);
        maxMSPT = module.getConfig().getInt("max-mspt", 80);
        message = module.getConfig().getString("lag-broadcast", "请通知管理员填写提示信息。");
    }


    @Override
    public void run() {
        if (broTimes != 0) {
            broTimes = broTimes >= 15 ? 0 : broTimes + 1;
            return;
        }

        if (ToiletCore.getInstance().getLast1MinMSPT() >= maxMSPT) {
            broadcastMessage();
            if (givePatato) givePotato();
            broTimes++;
        }
    }


    private void broadcastMessage() {
        String willSend = message.replaceAll(
                "%mspt%", String.valueOf((int) ToiletCore.getInstance().getLast1MinMSPT()));
        Bukkit.getServer().sendMessage(MiniMessage.miniMessage().deserialize(willSend));
    }

    private void givePotato() {
        Bukkit.getOnlinePlayers().forEach(player -> PlayerUtil.giveOrDrop(player, getPotato()));
    }

    private ItemStack getPotato() {
        ItemStack itemStack = new ItemStack(Material.BAKED_POTATO);
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.text("服务器尸体")
                .color(NamedTextColor.RED)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
