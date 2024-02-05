package net.toiletmc.toiletcore.module.impl.shart.task;

import net.toiletmc.toiletcore.module.impl.shart.ShartModule;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

public class DeleteItemTask extends BukkitRunnable {
    final private ShartModule module;
    final private Item item;

    public DeleteItemTask(ShartModule module, Item item, int time) {
        this.item = item;
        this.module = module;
        this.runTaskLater(module.getPlugin(), time);
    }

    @Override
    public void run() {
        item.remove();
        item.removeMetadata("shart", module.getPlugin());
    }
}
