package net.toiletmc.toiletcore.module.impl.lagalert;

import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.enums.Module;
import net.toiletmc.toiletcore.module.interfaces.AbstractModule;

public class LagAlertModule extends AbstractModule {
    private final MSPTCheckTask checkTask;

    protected LagAlertModule(ToiletCore plugin, Module module) {
        super(plugin, module);
        checkTask = new MSPTCheckTask(this);
        checkTask.runTaskTimer(plugin, 20L * 30L, 20L * 30L);
    }

    @Override
    public void reload() {
        super.reload();
        checkTask.reload();
    }
}
