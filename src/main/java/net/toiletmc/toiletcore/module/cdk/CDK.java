package net.toiletmc.toiletcore.module.cdk;

import net.toiletmc.toiletcore.api.module.ToiletModule;

public class CDK extends ToiletModule {
    @Override
    public void onEnable() {
        getLogger().info("test");
    }

    @Override
    public void onDisable() {
        getLogger().info("bye");
    }
}
