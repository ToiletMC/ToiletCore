package net.toiletmc.toiletcore.module.effectonblock;

import me.lucko.helper.serialize.Region;
import net.toiletmc.toiletcore.utils.EffectUtil;
import net.toiletmc.toiletcore.utils.RegionUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public class EOBRegion {
    private final boolean needInWater;
    private final boolean removeHarmfulNegative;
    private final boolean needPermission;
    private final String permission;
    private final int giveExpPoints;
    private final List<PotionEffect> giveEffects;
    private final Region region;

    public EOBRegion(ConfigurationSection config) {

        giveEffects = EffectUtil.getPotionEffects(config.getStringList("give-effects"));

        this.needInWater = config.getBoolean("need-in-water", false);
        this.removeHarmfulNegative = config.getBoolean("remove-harmful-effects", false);
        this.giveExpPoints = config.getInt("give-exp-points", 1);
        this.permission = config.getString("need-permission", "NONE");
        this.needPermission = !permission.equalsIgnoreCase("NONE");

        region = RegionUtil.fromString(config.getString("region.min"),
                config.getString("region.max"));
    }

    /**
     * tick 玩家，并给予奖励。
     *
     * @return 是否 tick 命中。
     */
    public boolean tick(Player player) {
        if (!region.inRegion(player.getLocation()) ||
                (needInWater && !player.isInWater()) ||
                (needPermission && !player.hasPermission(permission))
        ) {
            return false;
        }

        player.giveExp(giveExpPoints);
        if (removeHarmfulNegative) {
            EffectUtil.removeHarmfulEffects(player);
        }
        EffectUtil.giveEffects(player, giveEffects);

        return true;
    }
}
