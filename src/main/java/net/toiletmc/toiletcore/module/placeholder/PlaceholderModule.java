package net.toiletmc.toiletcore.module.placeholder;

import net.toiletmc.toiletcore.api.module.ToiletModule;

public class PlaceholderModule extends ToiletModule {
    private PlaceholderHook placeholderHook;

    @Override
    public void onEnable() {
        placeholderHook = new PlaceholderHook(this);
        placeholderHook.register();
    }

    @Override
    public void onDisable() {
        placeholderHook.unregister();
    }

}
