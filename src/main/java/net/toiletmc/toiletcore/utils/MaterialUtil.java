package net.toiletmc.toiletcore.utils;

import net.toiletmc.toiletcore.ToiletCore;
import org.bukkit.Material;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MaterialUtil {
    public static Material getMaterial(String string) {
        Material material;

        try {
            material = Material.valueOf(string.toUpperCase());
        } catch (IllegalArgumentException e) {
            ToiletCore.getInstance().getLogger().warning("配置文件中包含不存在的材质：" + string);
            return null;
        }

        return material;
    }

    public static Set<Material> getMaterialSet(List<String> materials) {
        return materials.stream()
                .map(MaterialUtil::getMaterial)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
