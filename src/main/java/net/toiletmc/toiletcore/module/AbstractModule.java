package net.toiletmc.toiletcore.module;

import net.toiletmc.toiletcore.ToiletCore;

public abstract class AbstractModule {
    protected ToiletCore plugin;

    public AbstractModule(ToiletCore plugin) {
        this.plugin = plugin;
    }

    public abstract void enable();

    public abstract void disable();
}
