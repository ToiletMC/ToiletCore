package net.toiletmc.toiletcore.module.hook;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class HookManagerUI {
  public static Component mainPage(FileConfiguration config) {
    @SuppressWarnings("unchecked")
    var hooks = (List<Hook>) config.getList("hooks", List.of());
    var builder = Component.text("=== ToiletCore Hook Manager ===")
        .toBuilder()
        .appendNewline()
        .append(
            Component
                .text("[Create]")
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD)
                .hoverEvent(HoverEvent.showText(Component.text("Create a new hook")))
                .clickEvent(ClickEvent.suggestCommand("/hook create ")));
    for (var hook : hooks) {
      builder.appendNewline()
          .append(
              Component
                  .text("-"))
          .appendSpace()
          .append(Component.text(hook.getName())
              .clickEvent(ClickEvent.runCommand("/hook manager " + hook.getName())));
    }
    builder.appendNewline()
        .append(Component.text("===== By zty012 ====="));
    return builder.build();
  }

  public static Component hookPage(Hook hook) {
    var builder = Component.text("===== Edit " + hook.getName() + " =====")
        .toBuilder()
        .appendNewline()
        .append(
            Component.text("[Run]")
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD)
                .hoverEvent(HoverEvent.showText(Component.text("Run this hook")))
                .clickEvent(ClickEvent.runCommand("/hook run " + hook.getName())))
        .appendSpace()
        .append(
            Component.text("[Add]")
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD)
                .hoverEvent(HoverEvent.showText(Component.text("Add a new group to this hook")))
                .clickEvent(ClickEvent.runCommand("/hook run " + hook.getName())))
        .appendSpace()
        .append(
            Component.text("[X]")
                .color(NamedTextColor.RED)
                .decorate(TextDecoration.BOLD)
                .hoverEvent(HoverEvent.showText(Component.text("Delete this hook")))
                .clickEvent(ClickEvent.suggestCommand("/hook delete hook " + hook.getName())));
    for (var group : hook.getGroups()) {
      builder
          .appendNewline()
          .append(Component.text("===== Group " + group.getName() + " ====="))
          .appendNewline()
          .append(
              Component.text("[Configure]")
                  .color(NamedTextColor.GREEN)
                  .decorate(TextDecoration.BOLD)
                  .hoverEvent(HoverEvent.showText(Component.text("Set command for this group")))
                  .clickEvent(
                      ClickEvent.suggestCommand("/hook configure " + hook.getName() + " " + group.getName() + " ")))
          .append(Component.text("[X]"));
    }
    return builder.build();
  }
}
