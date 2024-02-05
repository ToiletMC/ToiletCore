package net.toiletmc.toiletcore.module.interfaces;

import lombok.Getter;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.enums.Modules;
import org.bukkit.configuration.ConfigurationSection;

import java.util.logging.Logger;

@Getter
public abstract class AbstractModule implements Module {
    protected final ToiletCore plugin;
    protected final Modules module;

    public AbstractModule(ToiletCore plugin, Modules module) {
        this.plugin = plugin;
        this.module = module;
    }

    @Override
    public ConfigurationSection getConfig() {
        return plugin.getConfig().getConfigurationSection("setting." + module.name);
    }
}
