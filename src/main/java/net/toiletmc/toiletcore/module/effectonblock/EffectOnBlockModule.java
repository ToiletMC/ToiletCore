package net.toiletmc.toiletcore.module.effectonblock;

import lombok.Getter;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.ModuleManager;
import net.toiletmc.toiletcore.api.module.ToiletModule;
import net.toiletmc.toiletcore.api.objects.EOBRegion;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class EffectOnBlockModule extends ToiletModule {
    Set<PotionEffectType> negativeEffects;
    List<EOBRegion> eobRegions;
    BukkitTask bukkitTask;

    @Override
    public void onEnable() {
        negativeEffects = getConfig().getStringList("negative_effects").stream()
                .map(PotionEffectType::getByName)
                .collect(Collectors.toSet());

        ConfigurationSection regions = getConfig().getConfigurationSection("regions");
        eobRegions = regions.getKeys(false).stream()
                .map(key -> new EOBRegion(this, regions.getConfigurationSection(key)))
                .toList();

        bukkitTask = new CheckTask(this).runTaskTimer(plugin, 20L, 2L);
    }

    @Override
    public void onDisable() {
        bukkitTask.cancel();
    }
}
