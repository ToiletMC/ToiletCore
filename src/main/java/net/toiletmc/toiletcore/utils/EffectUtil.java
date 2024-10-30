package net.toiletmc.toiletcore.utils;

import net.toiletmc.toiletcore.ToiletCore;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class EffectUtil {

    public static void giveEffects(Player player, Collection<PotionEffect> effectsToGive) {
        effectsToGive.forEach(effectToGive -> {
            PotionEffect potionEffect = player.getPotionEffect(effectToGive.getType());
            if (potionEffect == null || potionEffect.getDuration() < 10) {
                player.addPotionEffect(effectToGive);
            }
        });
    }

    public static void removeEffects(Player player, Collection<PotionEffectType> effectsToRemove) {
        effectsToRemove.forEach(player::removePotionEffect);
    }

    public static void removeHarmfulEffects(Player player) {
        player.getActivePotionEffects().stream()
                .filter(potionEffect -> potionEffect.getType().getEffectCategory() == PotionEffectType.Category.HARMFUL)
                .forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
    }

    public static @Nullable PotionEffectType getEffectType(String string) {
        PotionEffectType potionEffectType;
        try {
            potionEffectType = Registry.EFFECT.get(NamespacedKey.minecraft(string.toLowerCase()));
            return potionEffectType;
        } catch (Exception e) {
            ToiletCore.getInstance().getLogger().log(Level.WARNING, "解析药水类型时出现错误！坐标：" + string, e);
            return null;
        }
    }

    /**
     * @param effectString 格式："药水名称;药水等级;持续时间"
     */
    public static @Nullable PotionEffect getPotionEffect(String effectString) {
        String[] split = effectString.split(";");
        PotionEffect potionEffect;
        try {
            potionEffect = new PotionEffect(getEffectType(split[0]), Integer.parseInt(split[2]), Integer.parseInt(split[1]));
            return potionEffect;
        } catch (Exception e) {
            ToiletCore.getInstance().getLogger().log(Level.WARNING, "解析药水时出现异常：" + effectString, e);
            return null;
        }
    }

    public static List<PotionEffect> getPotionEffects(List<String> effectStrings) {
        return effectStrings.stream()
                .map(EffectUtil::getPotionEffect)
                .filter(Objects::nonNull)
                .toList();
    }
}
