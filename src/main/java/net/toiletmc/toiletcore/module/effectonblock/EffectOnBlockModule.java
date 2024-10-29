package net.toiletmc.toiletcore.module.effectonblock;

import lombok.Getter;
import net.toiletmc.toiletcore.api.module.SimpleModule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

@Getter
public class EffectOnBlockModule extends SimpleModule {
    List<EOBRegion> eobRegions;
    BukkitTask bukkitTask;

    @Override
    public void onEnable() {
        int taskInterval = getConfig().getInt("setting.task-interval", 2);

        ConfigurationSection regions = getConfig().getConfigurationSection("regions");
        eobRegions = regions.getKeys(false).stream()
                .map(key -> new EOBRegion(regions.getConfigurationSection(key)))
                .toList();
        bukkitTask = new EOBTask(this).runTaskTimer(plugin, 20L, taskInterval);
    }

    @Override
    public void onDisable() {
        bukkitTask.cancel();
    }
}
