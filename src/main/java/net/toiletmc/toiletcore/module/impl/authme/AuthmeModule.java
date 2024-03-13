package net.toiletmc.toiletcore.module.impl.authme;

import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.enums.Module;
import net.toiletmc.toiletcore.module.interfaces.AbstractModule;
import org.bukkit.Bukkit;

public class AuthmeModule extends AbstractModule {
    private final AuthmeHook authmeHook;

    public AuthmeModule(ToiletCore plugin, Module module) {
        super(plugin, module);
        authmeHook = new AuthmeHook();
        Bukkit.getPluginManager().registerEvents(authmeHook, plugin);
        authmeHook.reloadAuthme();
    }
}
