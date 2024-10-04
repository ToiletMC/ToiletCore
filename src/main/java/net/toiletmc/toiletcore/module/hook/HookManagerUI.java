package net.toiletmc.toiletcore.module.hook;

import java.util.List;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class HookManagerUI {
        public static Component mainPage(List<Hook> hooks) {
                // 检查hooks是否为空
                if (hooks == null) {
                        return Component.text("没有可用的Hook").color(NamedTextColor.RED);
                }

                var builder = Component.empty().toBuilder()
                                .append(Component.text("=== ToiletCore Hook Manager ===").color(NamedTextColor.GRAY))
                                .appendNewline()
                                .append(createComponent("[创建]", NamedTextColor.GREEN,
                                                "Create a new hook", "/hook create "));

                for (var hook : hooks) {
                        builder.appendNewline()
                                        .append(Component.empty()
                                                        .append(Component.text("=> "))
                                                        .appendSpace()
                                                        .append(Component.text(hook.getName())
                                                                        .clickEvent(ClickEvent.runCommand(
                                                                                        "/hook manager " + hook
                                                                                                        .getName())))
                                                        .color(NamedTextColor.LIGHT_PURPLE));
                }
                builder.appendNewline()
                                .append(Component.text("==================").color(NamedTextColor.GRAY));
                return builder.build();
        }

        public static Component hookPage(Hook hook) {
                // 检查hook是否为空
                if (hook == null) {
                        return Component.text("Hook不存在").color(NamedTextColor.RED);
                }

                var builder = Component.empty().toBuilder()
                                .append(Component.text("===== 编辑 Hook " + hook.getName() + " =====")
                                                .color(NamedTextColor.GRAY))
                                .appendNewline()
                                .append(createComponent("[运行]", NamedTextColor.GREEN,
                                                "运行这个Hook", "/hook run " + hook.getName()))
                                .appendSpace()
                                .append(createComponent("[添加组]", NamedTextColor.GREEN,
                                                "添加一个组", "/hook addgroup " + hook.getName() + " "))
                                .appendSpace()
                                .append(createComponent("[删除]", NamedTextColor.RED,
                                                "删除Hook", "/hook deletehook " + hook.getName()));

                for (var group : hook.getGroups()) {
                        if (group != null) {
                                builder.appendNewline()
                                                .append(Component.text("=> 组 " + group.getName())
                                                                .color(NamedTextColor.LIGHT_PURPLE))
                                                .appendSpace()
                                                .append(createComponent("[添加玩家]", NamedTextColor.GREEN, "添加一个玩家",
                                                                "/hook join " + hook.getName() + " " + group.getName()
                                                                                + " "))
                                                .appendSpace()
                                                .append(createComponent("[编辑命令]", NamedTextColor.GREEN,
                                                                "设置这个组的命令，当前命令：\n" + group.getCommand(),
                                                                "/hook editcmd " + hook.getName() + " "
                                                                                + group.getName() + " "))
                                                .appendSpace()
                                                .append(createComponent("[删除]", NamedTextColor.RED,
                                                                "删除这个组", "/hook group " + hook.getName() + " "
                                                                                + group.getName()));
                                for (var player : group.getPlayers()) {
                                        builder.appendNewline()
                                                        .append(Component.text("   => 玩家 " + player)
                                                                        .color(NamedTextColor.YELLOW))
                                                        .appendSpace()
                                                        .append(createComponent("[移除]", NamedTextColor.RED,
                                                                        "把这个玩家移出组",
                                                                        "/hook leave " + hook.getName() + " "
                                                                                        + group.getName() + " "
                                                                                        + group.getName() + " "
                                                                                        + player));
                                }
                        }
                }
                builder.appendNewline()
                                .append(Component.text("==================").color(NamedTextColor.GRAY));
                return builder.build();
        }

        private static Component createComponent(String text, NamedTextColor color, String hoverText, String command) {
                return Component.text(text)
                                .color(color)
                                .decorate(TextDecoration.BOLD)
                                .hoverEvent(HoverEvent.showText(Component.text(hoverText)))
                                .clickEvent(ClickEvent.suggestCommand(command));
        }
}
