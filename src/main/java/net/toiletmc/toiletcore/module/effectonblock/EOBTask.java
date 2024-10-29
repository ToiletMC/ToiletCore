package net.toiletmc.toiletcore.module.effectonblock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;

public class EOBTask extends BukkitRunnable {
    private final EffectOnBlockModule module;

    public EOBTask(EffectOnBlockModule module) {
        this.module = module;
    }

    public void run() {
        Collection<? extends Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (EOBRegion region : module.getEobRegions()) {
            // tick 过1次的玩家将不会再被tick。
            onlinePlayers.removeIf(region::tick);
        }
    }
}