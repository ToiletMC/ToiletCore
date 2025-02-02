package net.toiletmc.toiletcore.module.lagalert;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.toiletmc.toiletcore.api.module.SimpleModule;
import org.bukkit.entity.Player;

public class LagAlertModule extends SimpleModule {
    @Getter
    private String message;
    @Getter
    private boolean givePotato;
    @Getter
    private boolean enableWebhook;
    @Getter
    private String webhookUrl;
    @Getter
    private int maxMSPT;

    private MSPTCheckTask checkTask;

    @Override
    public void onEnable() {
        givePotato = getConfig().getBoolean("give-potato", true);
        maxMSPT = getConfig().getInt("max-mspt", 80);
        message = getConfig().getString("lag-broadcast", "当前MSPT过高，请通知管理员填写提示信息。");

        enableWebhook = getConfig().getBoolean("webhook.enable", false);
        webhookUrl = getConfig().getString("webhook.url", "http:127.0.0.1");

        checkTask = new MSPTCheckTask(this);
        checkTask.runTaskTimer(plugin, 20L * 30L, 20L * 30L);
    }

    @Override
    public void onDisable() {
        checkTask.cancel();
    }

    /**
     * @param player 需要查询的玩家
     * @return true 为接收服务器尸体；false 拒绝接收服务器尸体
     */
    public boolean getReceivePotatoStatus(Player player) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        CachedMetaData metaData = luckPerms.getPlayerAdapter(Player.class).getMetaData(player);
        return metaData.getMetaValue("toilet.setting.receive_potato", Boolean::parseBoolean).orElse(true);
    }

    public void setReceivePotatoStatus(Player player, boolean status) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);
        MetaNode metaNode = MetaNode.builder("toilet.setting.receive_potato", Boolean.toString(status)).build();
        user.data().clear(NodeType.META.predicate(mn -> mn.getMetaKey().equals("toilet.setting.receive_potato")));
        user.data().add(metaNode);
        luckPerms.getUserManager().saveUser(user);
    }
}
