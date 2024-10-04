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
                var builder = Component.empty()
                                .toBuilder()
                                .append(
                                                Component.text("=== ToiletCore Hook Manager ===")
                                                                .color(NamedTextColor.GRAY))
                                .appendNewline()
                                .append(
                                                Component
                                                                .text("[创建]")
                                                                .color(NamedTextColor.GREEN)
                                                                .decorate(TextDecoration.BOLD)
                                                                .hoverEvent(HoverEvent.showText(
                                                                                Component.text("Create a new hook")))
                                                                .clickEvent(ClickEvent
                                                                                .suggestCommand("/hook create ")));
                for (var hook : hooks) {
                        builder.appendNewline()
                                        .append(
                                                        Component
                                                                        .text("-"))
                                        .appendSpace()
                                        .append(Component.text(hook.getName())
                                                        .clickEvent(ClickEvent.runCommand(
                                                                        "/hook manager " + hook.getName())));
                }
                builder.appendNewline()
                                .append(
                                                Component.text("==================")
                                                                .color(NamedTextColor.GRAY));
                return builder.build();
        }

        public static Component hookPage(Hook hook) {
                var builder = Component.empty()
                                .toBuilder()
                                .append(
                                                Component.text("===== 编辑 Hook " + hook.getName() + " =====")
                                                                .color(NamedTextColor.GRAY))
                                .appendNewline()
                                .append(
                                                Component.text("[运行]")
                                                                .color(NamedTextColor.GREEN)
                                                                .decorate(TextDecoration.BOLD)
                                                                .hoverEvent(HoverEvent
                                                                                .showText(Component.text("运行这个Hook")))
                                                                .clickEvent(ClickEvent.runCommand(
                                                                                "/hook run " + hook.getName())))
                                .appendSpace()
                                .append(
                                                Component.text("[添加组]")
                                                                .color(NamedTextColor.GREEN)
                                                                .decorate(TextDecoration.BOLD)
                                                                .hoverEvent(HoverEvent.showText(
                                                                                Component.text("添加一个组")))
                                                                .clickEvent(ClickEvent.suggestCommand("/hook addgroup "
                                                                                + hook.getName() + " ")))
                                .appendSpace()
                                .append(
                                                Component.text("[删除]")
                                                                .color(NamedTextColor.RED)
                                                                .decorate(TextDecoration.BOLD)
                                                                .hoverEvent(HoverEvent
                                                                                .showText(Component.text("删除Hook")))
                                                                .clickEvent(ClickEvent
                                                                                .suggestCommand("/hook deletehook "
                                                                                                + hook.getName())));
                for (var group : hook.getGroups()) {
                        builder
                                        .appendNewline()
                                        .append(
                                                        Component.text("=> 组 " + group.getName())
                                                                        .color(NamedTextColor.LIGHT_PURPLE))
                                        .appendSpace()
                                        .append(
                                                        Component.text("[编辑命令]")
                                                                        .color(NamedTextColor.GREEN)
                                                                        .decorate(TextDecoration.BOLD)
                                                                        .hoverEvent(HoverEvent.showText(
                                                                                        Component.text("设置这个组的命令，当前命令：\n"
                                                                                                        + group.getCommand())))
                                                                        .clickEvent(
                                                                                        ClickEvent.suggestCommand(
                                                                                                        "/hook editcmd "
                                                                                                                        + hook.getName()
                                                                                                                        + " "
                                                                                                                        + group.getName()
                                                                                                                        + " ")))
                                        .appendSpace()
                                        .append(
                                                        Component.text("[删除]")
                                                                        .color(NamedTextColor.RED)
                                                                        .decorate(TextDecoration.BOLD)
                                                                        .hoverEvent(HoverEvent.showText(
                                                                                        Component.text("删除这个组")))
                                                                        .clickEvent(ClickEvent.suggestCommand(
                                                                                        "/hook deletegroup " + hook
                                                                                                        .getName())));
                }
                builder.appendNewline()
                                .append(
                                                Component.text("==================")
                                                                .color(NamedTextColor.GRAY));
                return builder.build();
        }
}
