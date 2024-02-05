package net.toiletmc.toiletcore.module.impl.placeholder;

import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.enums.Modules;
import net.toiletmc.toiletcore.module.interfaces.AbstractModule;

public class PlaceholderModule extends AbstractModule {
    private final PlaceholderHook placeholderHook;

    public PlaceholderModule(ToiletCore plugin, Modules module) {
        super(plugin, module);
        placeholderHook = new PlaceholderHook(this);
        placeholderHook.register();
    }

    @Override
    public void reload() {
        placeholderHook.reload();
    }
}
