package net.toiletmc.toiletcore.module.impl.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.toiletmc.toiletcore.ToiletCore;
import net.toiletmc.toiletcore.module.interfaces.Module;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderHook extends PlaceholderExpansion {

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        return super.onPlaceholderRequest(player, params);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "toilet";
    }

    @Override
    public @NotNull String getAuthor() {
        return "TheLittle_Yang";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.0.0";
    }

}
