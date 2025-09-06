package net.toiletmc.toiletcore.module.magic;

import net.toiletmc.toiletcore.api.module.AutoEnable;
import net.toiletmc.toiletcore.api.module.SimpleModule;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class MagicModule extends SimpleModule implements AutoEnable {
    private MagicHook magicHook;

    @Override
    public void onEnable() {
        magicHook = new MagicHook();
        Bukkit.getPluginManager().registerEvents(magicHook, plugin);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(magicHook);
    }

    @Override
    public boolean whetherEnable() {
        return Bukkit.getPluginManager().getPlugin("Magic") != null &&
                Bukkit.getPluginManager().getPlugin("CMI") != null;
    }
}
