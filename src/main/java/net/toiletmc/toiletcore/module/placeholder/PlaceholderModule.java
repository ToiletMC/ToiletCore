package net.toiletmc.toiletcore.module.placeholder;

import net.toiletmc.toiletcore.api.module.SimpleModule;

public class PlaceholderModule extends SimpleModule {
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
