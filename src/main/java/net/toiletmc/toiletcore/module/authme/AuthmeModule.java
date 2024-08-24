package net.toiletmc.toiletcore.module.authme;

import net.toiletmc.toiletcore.api.module.ToiletModule;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class AuthmeModule extends ToiletModule {
    private AuthmeHook authmeHook;

    @Override
    public void onEnable() {
        authmeHook = new AuthmeHook();
        Bukkit.getPluginManager().registerEvents(authmeHook, plugin);
        authmeHook.reloadAuthme();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(authmeHook);
        authmeHook.reloadAuthme();
    }
}
