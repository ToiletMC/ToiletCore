package net.toiletmc.toiletcore.module.impl.authme;

import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.interfaces.Module;
import org.bukkit.Bukkit;

public class AuthmeModule implements Module {

    private final ToiletCore plugin;
    private final AuthmeHook authmeHook;

    public AuthmeModule(ToiletCore plugin) {
        this.plugin = plugin;
        authmeHook = new AuthmeHook();
        Bukkit.getPluginManager().registerEvents(authmeHook, plugin);
        authmeHook.reloadAuthme();
    }

    @Override
    public void reload() {

    }
}
