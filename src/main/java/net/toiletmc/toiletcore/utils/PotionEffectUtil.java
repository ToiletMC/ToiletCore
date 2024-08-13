package net.toiletmc.toiletcore.utils;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PotionEffectUtil {

    public static void givePotionEffects(Player player, Set<PotionEffectType> newEffectTypes) {
        for (PotionEffectType newEffectType : newEffectTypes) {
            Set<PotionEffectType> effectTypes = player.getActivePotionEffects()
                    .stream()
                    .map(PotionEffect::getType)
                    .collect(Collectors.toUnmodifiableSet());
            if (!effectTypes.contains(newEffectType)) {
                player.addPotionEffect(new PotionEffect(newEffectType, 60, 1, true));
            } else {
                Optional<PotionEffect> effectOptional = player.getActivePotionEffects()
                        .stream()
                        .filter(potionEffect -> potionEffect.getType().equals(newEffectType))
                        .findAny();
                if (effectOptional.isPresent()) {
                    PotionEffect effect = effectOptional.get();
                    int remainingDuration = effect.getDuration();
                    if (remainingDuration < 10) {
                        player.removePotionEffect(newEffectType);
                        player.addPotionEffect(new PotionEffect(newEffectType, 60, 1, true));
                    }
                }
            }
        }

    }

    public static void removeAllNegativePotions(Player player, Set<PotionEffectType> negativeEffects) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (negativeEffects.contains(effect.getType())) {
                player.removePotionEffect(effect.getType());
            }
        }

    }
}
