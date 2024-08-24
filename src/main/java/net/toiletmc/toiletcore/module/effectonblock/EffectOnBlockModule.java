package net.toiletmc.toiletcore.module.effectonblock;

import lombok.Getter;
import net.toiletmc.toiletcore.api.module.ToiletModule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;

@Getter
public class EffectOnBlockModule extends ToiletModule {
    List<EOBRegion> eobRegions;
    BukkitTask bukkitTask;

    @Override
    public void onEnable() {
        ConfigurationSection regions = getConfig().getConfigurationSection("regions");
        eobRegions = regions.getKeys(false).stream()
                .map(key -> new EOBRegion(regions.getConfigurationSection(key)))
                .toList();
        int taskInterval = getConfig().getInt("setting.task-interval", 2);
        bukkitTask = new EOBTask(this).runTaskTimer(plugin, 20L, taskInterval);
    }

    @Override
    public void onDisable() {
        bukkitTask.cancel();
    }
}
