package net.toiletmc.toiletcore.utils;

import org.bukkit.util.BoundingBox;

public class RegionUtil {
    /**
     * 从 2个 Location 字符串 {@link LocationUtil#fromString(String) 获取 BoundingBox 实例。
     */
    public static BoundingBox fromString(String p1, String p2) {
        return BoundingBox.of(LocationUtil.fromString(p1), LocationUtil.fromString(p2));
    }
}
