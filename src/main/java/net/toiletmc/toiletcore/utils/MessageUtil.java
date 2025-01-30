package net.toiletmc.toiletcore.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public class MessageUtil {
    public static void sendRedText(CommandSender sender, String message) {
        sender.sendMessage(Component.text(message).color(NamedTextColor.RED));
    }

    public static void sendNormalText(CommandSender sender, String message) {
        sender.sendMessage(Component.text(message).color(NamedTextColor.WHITE));
    }
}
