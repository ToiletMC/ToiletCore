package net.toiletmc.toiletcore.utils;

import net.toiletmc.toiletcore.ToiletCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtil {
    /**
     * 从坐标字符串生成坐标，例如 "world;23;53;-23"
     *
     * @param string 坐标的字符串
     * @return 返回一个 Bukkit Location
     */
    public static Location fromString(String string) {
        String[] split = string.split(";");
        Location location = null;
        try {
            location = new Location(
                    Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    Double.parseDouble(split[2]),
                    Double.parseDouble(split[3])
            );
            return location;
        } catch (Exception e) {
            ToiletCore.getInstance().getLogger().warning("解析坐标时出现错误！坐标：" + string);
            throw new RuntimeException(e.getMessage());
        }
    }
}