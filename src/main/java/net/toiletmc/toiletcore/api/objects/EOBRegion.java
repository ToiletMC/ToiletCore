package net.toiletmc.toiletcore.api.objects;

import net.toiletmc.toiletcore.module.effectonblock.EffectOnBlockModule;
import net.toiletmc.toiletcore.utils.LocationUtil;
import net.toiletmc.toiletcore.utils.PotionEffectUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;
import java.util.stream.Collectors;

public class EOBRegion {
    private final EffectOnBlockModule module;
    private final boolean needInWater;
    private final boolean removeNegative;
    private final int giveExpPoints;
    private final Set<PotionEffectType> giveEffects;
    private final CuboidRegion cuboidRegion;

    public EOBRegion(EffectOnBlockModule module, ConfigurationSection config) {
        this.module = module;

        Location loc1 = LocationUtil.fromString(config.getString("location.loc1", "Not Found"));
        Location loc2 = LocationUtil.fromString(config.getString("location.loc2", "Not Found"));
        giveEffects = config.getStringList("give-effects").stream()
                .map(PotionEffectType::getByName)
                .collect(Collectors.toUnmodifiableSet());

        this.cuboidRegion = new CuboidRegion(loc1, loc2);
        this.needInWater = config.getBoolean("need-in-water", false);
        this.removeNegative = config.getBoolean("remove-negative", false);
        this.giveExpPoints = config.getInt("give-exp-points", 1);
    }

    public void tick(Player player) {
        if (!cuboidRegion.isInRegion(player) ||
                (needInWater && !player.isInWater())) {
            return;
        }

        player.giveExp(giveExpPoints);

        if (removeNegative) {
            PotionEffectUtil.removeAllNegativePotions(player, module.getNegativeEffects());
        }

        PotionEffectUtil.givePotionEffects(player, giveEffects);
    }
}
