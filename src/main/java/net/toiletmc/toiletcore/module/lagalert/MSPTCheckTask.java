package net.toiletmc.toiletcore.module.lagalert;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.toiletmc.toiletcore.HttpHelper;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.http.request.MSPTRequest;
import net.toiletmc.toiletcore.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class MSPTCheckTask extends BukkitRunnable {
    private final LagAlertModule module;
    private int broTimes = 0;

    public MSPTCheckTask(LagAlertModule module) {
        this.module = module;
    }

    @Override
    public void run() {
        if (broTimes != 0) {
            broTimes = broTimes >= 15 ? 0 : broTimes + 1;
            return;
        }

        if (ToiletCore.getInstance().getLast1MinMSPT() >= module.getMaxMSPT()) {
            broadcastMessage();
            if (module.isGivePotato()) givePotato();
            if (module.isEnableWebhook()) handleWebhook();
            broTimes++;
        }
    }


    private void broadcastMessage() {
        String willSend = module.getMessage().replaceAll(
                "%mspt%", String.valueOf((int) ToiletCore.getInstance().getLast1MinMSPT()));
        Bukkit.getServer().sendMessage(MiniMessage.miniMessage().deserialize(willSend));
    }

    private void givePotato() {
        Bukkit.getOnlinePlayers().stream()
                .filter(module::getReceivePotatoStatus)
                .forEach(player -> PlayerUtil.giveOrDrop(player, getPotato()));
    }

    private void handleWebhook() {
        HttpHelper.sendHttpRequest(module.getWebhookUrl(), new MSPTRequest((int) ToiletCore.getInstance().getLast1MinMSPT(), ""));
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
