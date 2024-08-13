package net.toiletmc.toiletcore.module.effectonblock;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckTask extends BukkitRunnable {
    private final EffectOnBlockModule module;

    public CheckTask(EffectOnBlockModule module) {
        this.module = module;
    }

    public void run() {
        module.getEobRegions().forEach(eobRegion ->
                Bukkit.getOnlinePlayers().forEach(eobRegion::tick));
    }
}