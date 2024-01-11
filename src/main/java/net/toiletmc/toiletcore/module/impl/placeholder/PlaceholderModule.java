package net.toiletmc.toiletcore.module.impl.placeholder;

import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.interfaces.Module;

public class PlaceholderModule implements Module {

    private final ToiletCore plugin;
    private final PlaceholderHook placeholderHook;

    public PlaceholderModule(ToiletCore plugin) {
        this.plugin = plugin;
        placeholderHook = new PlaceholderHook();
    }

    @Override
    public void reload() {

    }
}
