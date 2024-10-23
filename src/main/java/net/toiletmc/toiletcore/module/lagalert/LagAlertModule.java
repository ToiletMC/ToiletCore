package net.toiletmc.toiletcore.module.lagalert;

import net.toiletmc.toiletcore.api.module.SimpleModule;

public class LagAlertModule extends SimpleModule {
    private MSPTCheckTask checkTask;

    @Override
    public void onEnable() {
        checkTask = new MSPTCheckTask(this);
        checkTask.runTaskTimer(plugin, 20L * 30L, 20L * 30L);
    }

    @Override
    public void onDisable() {
        checkTask.cancel();
    }

}
