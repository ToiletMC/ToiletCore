package net.toiletmc.toiletcore.module.lagalert;

import lombok.Getter;
import net.toiletmc.toiletcore.api.module.SimpleModule;

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

}
