package net.toiletmc.toiletcore.utils;

import me.lucko.helper.serialize.Position;
import me.lucko.helper.serialize.Region;

public class RegionUtil {
    /**
     * 从2个Location字符串获取Region实例。
     */
    public static Region fromString(String p1, String p2) {
        return Region.of(Position.of(LocationUtil.fromString(p1)), Position.of(LocationUtil.fromString(p2)));
    }
}
