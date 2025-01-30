package net.toiletmc.toiletcore.module.effectonblock;

import net.toiletmc.toiletcore.utils.EffectUtil;
import net.toiletmc.toiletcore.utils.RegionUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BoundingBox;

import java.util.List;

public class EOBRegion {
    private final boolean mustInWater;
    private final boolean removeHarmfulNegative;
    private final boolean needPermission;
    private final String permission;
    private final int giveExp;
    private final List<PotionEffect> giveEffects;
    private final BoundingBox region;

    public EOBRegion(ConfigurationSection config) {
        region = RegionUtil.fromString(config.getString("region.min"),
                config.getString("region.max"));

        giveEffects = EffectUtil.getPotionEffects(config.getStringList("give.effects"));
        this.giveExp = config.getInt("give.exp", 1);

        this.mustInWater = config.getBoolean("requirement.must-in-water", false);
        this.permission = config.getString("requirement.permission", "NONE");
        this.needPermission = !permission.equalsIgnoreCase("NONE");

        this.removeHarmfulNegative = config.getBoolean("options.remove-harmful-effects", false);
    }

    /**
     * tick 玩家，并给予奖励。
     *
     * @return 是否 tick 命中。
     */
    public boolean tick(Player player) {
        if (!region.contains(player.getLocation().toVector()) ||
                (mustInWater && !player.isInWater()) ||
                (needPermission && !player.hasPermission(permission))
        ) {
            return false;
        }

        player.giveExp(giveExp);
        if (removeHarmfulNegative) {
            EffectUtil.removeHarmfulEffects(player);
        }
        EffectUtil.giveEffects(player, giveEffects);

        return true;
    }
}
