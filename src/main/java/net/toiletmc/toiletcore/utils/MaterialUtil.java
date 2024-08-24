package net.toiletmc.toiletcore.utils;

import net.toiletmc.toiletcore.ToiletCore;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MaterialUtil {
    /**
     * 将字符串转换为 Material
     *
     * @return 材料不存在时将返回 null
     */
    public static @Nullable Material getMaterial(String string) {
        Material material;

        try {
            material = Material.valueOf(string.toUpperCase());
            return material;
        } catch (IllegalArgumentException e) {
            ToiletCore.getInstance().getLogger().warning("解析材质时出现异常：" + string);
            ToiletCore.getInstance().getLogger().warning(e.getMessage());
            return null;
        }
    }


    /**
     * 将字符串转换为 Material 集合，材料不存在时将会跳过。
     */
    public static Set<Material> getMaterialSet(List<String> materials) {
        return materials.stream()
                .map(MaterialUtil::getMaterial)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
