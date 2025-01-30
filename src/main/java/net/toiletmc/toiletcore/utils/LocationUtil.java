package net.toiletmc.toiletcore.utils;

import net.toiletmc.toiletcore.ToiletCore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class LocationUtil {
    /**
     * 从坐标字符串生成坐标，例如 "world;23;53;-23"
     *
     * @param string 坐标的字符串，例如 "world;23;53;-23"
     * @return 返回一个 Bukkit Location
     */
    public static @NotNull Location fromString(String string) {
        String[] split = string.split(";");
        try {
            return new Location(
                    Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    Double.parseDouble(split[2]),
                    Double.parseDouble(split[3])
            );
        } catch (Exception e) {
            ToiletCore.getInstance().getLogger().log(Level.WARNING, "解析坐标时出现错误：" + string, e);
        }
        return Bukkit.getWorlds().getFirst().getSpawnLocation();
    }
}