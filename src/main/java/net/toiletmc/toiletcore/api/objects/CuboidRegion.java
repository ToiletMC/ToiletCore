package net.toiletmc.toiletcore.api.objects;

import org.bukkit.Location;

public class CuboidRegion implements Region {
    protected final Location loc1, loc2;

    protected final double minX, minY, minZ, maxX, maxY, maxZ;

    public CuboidRegion(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;

        this.minX = Math.min(loc1.getX(), loc2.getX());
        this.minY = Math.min(loc1.getY(), loc2.getY());
        this.minZ = Math.min(loc1.getZ(), loc2.getZ());
        this.maxX = Math.max(loc1.getX(), loc2.getX());
        this.maxY = Math.max(loc1.getY(), loc2.getY());
        this.maxZ = Math.max(loc1.getZ(), loc2.getZ());
    }

    @Override
    public boolean isInRegion(Location l) {
        return l.getX() >= minX && l.getX() <= maxX &&
                l.getY() >= minY && l.getY() <= maxY &&
                l.getZ() >= minZ && l.getZ() <= maxZ;
    }
}