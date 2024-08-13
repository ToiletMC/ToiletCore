package net.toiletmc.toiletcore.module.lagalert;

import net.toiletmc.toiletcore.api.module.ToiletModule;

public class LagAlertModule extends ToiletModule {
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
