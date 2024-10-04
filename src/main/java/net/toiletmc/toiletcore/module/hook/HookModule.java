package net.toiletmc.toiletcore.module.hook;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.toiletmc.toiletcore.api.module.ToiletModule;
import net.toiletmc.toiletcore.module.ModuleManager.ModuleEnum;

public class HookModule extends ToiletModule implements CommandExecutor, TabCompleter {
    @Override
    public void onEnable() {
        plugin.getCommand("hook").setExecutor(this);
        plugin.getCommand("hook").setTabCompleter(this);
    }

    @Override
    public void onDisable() {
        saveConfig();
        plugin.getCommand("hook").setExecutor(null);
        plugin.getCommand("hook").setTabCompleter(null);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
            @NotNull String[] args) {
        @SuppressWarnings("unchecked")
        var hooks = (List<Hook>) getConfig().getList("hooks", List.of());
        getLogger().info(hooks.toString());
        if (args[0].equalsIgnoreCase("manager") && sender.hasPermission("toiletcore.admin")) {
            if (args.length == 1) {
                sender.sendMessage(HookManagerUI.mainPage(this.config));
                return true;
            }
            if (args.length == 2) {
                for (var hook : hooks) {
                    if (hook.getName().equals(args[1])) {
                        sender.sendMessage(HookManagerUI.hookPage(hook));
                        return true;
                    }
                }
                sender.sendMessage("没有这个项目");
                return false;
            }
        }
        // create子命令，需要权限
        // /hook create <name>
        if (args[0].equalsIgnoreCase("create") && sender.hasPermission("toiletcore.admin")) {
            if (args.length == 2) {
                var name = Arrays.asList(args).subList(1, args.length).stream().reduce("", (a, b) -> a + " " + b)
                        .trim();
                var prev = new ArrayList<>(hooks);
                // h检查name是否重复
                for (var hook : prev) {
                    if (hook.getName().equals(name)) {
                        sender.sendMessage("这个hook已经存在了");
                        return true;
                    }
                }
                prev.add(new Hook(name, List.of()));
                config.set("hooks", prev);
                saveConfig();
                sender.sendMessage(HookManagerUI.mainPage(config));
                return true;
            } else {
                sender.sendMessage("用法: /hook create <项目名>");
                return true;
            }
        }
        // addgroup子命令，需要权限
        // /hook addgroup <hook> <group>
        if (args[0].equalsIgnoreCase("addgroup") && sender.hasPermission("toiletcore.admin")) {
            if (args.length == 3) {
                var hookName = args[1];
                var groupName = args[2];
                var prev = new ArrayList<>(hooks);
                for (var hook : prev) {
                    if (hook.getName().equals(hookName)) {
                        var groups = new ArrayList<>(hook.getGroups());
                        groups.add(new HookGroup(groupName, List.of(), ""));
                        logger.info(groups.toString());
                        hook.setGroups(groups);
                        config.set("hooks", prev);
                        saveConfig();
                        sender.sendMessage(HookManagerUI.hookPage(hook));
                        return true;
                    }
                }
                sender.sendMessage("没有这个项目");
                return false;
            } else {
                sender.sendMessage("用法: /hook addgroup <项目名> <群组名>");
                return true;
            }
        }
        // editcmd子命令，需要权限
        // /hook editcmd <hook> <group> <...cmd>
        if (args[0].equalsIgnoreCase("editcmd") && sender.hasPermission("toiletcore.admin")) {
            if (args.length >= 4) {
                var hookName = args[1];
                var groupName = args[2];
                var cmd = Arrays.asList(args).subList(3, args.length).stream().reduce("", (a, b) -> a + " " + b)
                        .trim();
                var prev = new ArrayList<>(hooks);
                for (var hook : prev) {
                    if (hook.getName().equals(hookName)) {
                        for (var group : hook.getGroups()) {
                            if (group.getName().equals(groupName)) {
                                group.setCommand(cmd);
                                config.set("hooks", prev);
                                saveConfig();
                                sender.sendMessage(HookManagerUI.hookPage(hook));
                                return true;
                            }
                        }
                    }
                }
                sender.sendMessage("没有这个项目或群组");
                return false;
            } else {
                sender.sendMessage("用法: /hook editcmd <项目名> <群组名> <...命令>");
                return true;
            }
        }
        // deletegroup子命令，需要权限
        // /hook deletegroup <hook> <group>
        if (args[0].equalsIgnoreCase("deletegroup") && sender.hasPermission("toiletcore.admin")) {
            if (args.length == 3) {
                var hookName = args[1];
                var groupName = args[2];
                var prev = new ArrayList<>(hooks);
                for (var hook : prev) {
                    if (hook.getName().equals(hookName)) {
                        var groups = new ArrayList<>(hook.getGroups());
                        for (var group : groups) {
                            if (group.getName().equals(groupName)) {
                                groups.remove(group);
                                hook.setGroups(groups);
                                config.set("hooks", prev);
                                saveConfig();
                                sender.sendMessage(HookManagerUI.hookPage(hook));
                                return true;
                            }
                        }
                    }
                }
                sender.sendMessage("没有这个项目或群组");
                return false;
            } else {
                sender.sendMessage("用法: /hook deletegroup <项目名> <群组名>");
                return true;
            }
        }
        // delete子命令，需要权限
        // /hook delete <hook>
        if (args[0].equalsIgnoreCase("delete") && sender.hasPermission("toiletcore.admin")) {
            if (args.length == 2) {
                var hookName = args[1];
                var prev = new ArrayList<>(hooks);
                for (var hook : prev) {
                    if (hook.getName().equals(hookName)) {
                        prev.remove(hook);
                        config.set("hooks", prev);
                        saveConfig();
                        sender.sendMessage(HookManagerUI.mainPage(config));
                        return true;
                    }
                }
                sender.sendMessage("没有这个项目");
                return false;
            } else {
                sender.sendMessage("用法: /hook delete <项目名>");
                return true;
            }
        }
        // join子命令
        // /hook join <hook> <group> <player?>
        // 注意配置文件里面存的是玩家uuid
        // 如果没有指定玩家，则默认是自己
        // 如果没有指定玩家，并且是从控制台执行命令，就报错
        if (args[0].equalsIgnoreCase("join")) {
            UUID uuid = null;
            if (args.length == 4 && sender.hasPermission("toiletcore.admin")) {
                uuid = Bukkit.getPlayerUniqueId(args[3]);
                if (uuid == null) {
                    sender.sendMessage("玩家不存在");
                    return false;
                }
            }
            if (args.length == 3) {
                if (sender instanceof Player) {
                    uuid = Bukkit.getPlayerUniqueId(sender.getName());
                    if (uuid == null) {
                        sender.sendMessage("玩家不存在");
                        return false;
                    }
                } else {
                    sender.sendMessage("用法: /hook join <项目名> <群组名> <玩家名?>");
                    return false;
                }
            }
            // 把uuid加入到group对象的players列表中
            var hookName = args[1];
            var groupName = args[2];
            var prev = new ArrayList<>(hooks);
            for (var hook : prev) {
                if (hook.getName().equals(hookName)) {
                    for (var group : hook.getGroups()) {
                        if (group.getName().equals(groupName)) {
                            var players = new ArrayList<>(group.getPlayers());
                            players.add(uuid);
                            group.setPlayers(players);
                            config.set("hooks", prev);
                            saveConfig();
                            sender.sendMessage(HookManagerUI.hookPage(hook));
                            return true;
                        }
                    }
                }
            }
            sender.sendMessage("没有这个项目或群组");
            return false;
        }
        // leave子命令
        // /hook leave <hook> <group> <player?>
        // 注意配置文件里面存的是玩家uuid
        // 如果没有指定玩家，则默认是自己
        // 如果没有指定玩家，并且是从控制台执行命令，就报错
        if (args[0].equalsIgnoreCase("leave")) {
            UUID uuid = null;
            if (args.length == 4 && sender.hasPermission("toiletcore.admin")) {
                uuid = Bukkit.getPlayerUniqueId(args[3]);
                if (uuid == null) {
                    sender.sendMessage("玩家不存在");
                    return false;
                }
            }
            if (args.length == 3) {
                if (sender instanceof Player) {
                    uuid = Bukkit.getPlayerUniqueId(sender.getName());
                    if (uuid == null) {
                        sender.sendMessage("玩家不存在");
                        return false;
                    }
                } else {
                    sender.sendMessage("用法: /hook leave <项目名> <群组名> <玩家名?>");
                    return false;
                }
            }
            // 把uuid从group对象的players列表中移除
            var hookName = args[1];
            var groupName = args[2];
            var prev = new ArrayList<>(hooks);
            for (var hook : prev) {
                if (hook.getName().equals(hookName)) {
                    for (var group : hook.getGroups()) {
                        if (group.getName().equals(groupName)) {
                            var players = new ArrayList<>(group.getPlayers());
                            players.remove(uuid);
                            group.setPlayers(players);
                            config.set("hooks", prev);
                            saveConfig();
                            sender.sendMessage(HookManagerUI.hookPage(hook));
                            return true;
                        }
                    }
                }
            }
            sender.sendMessage("没有这个项目或群组");
            return false;
        }
        // run子命令，需要权限
        // /hook run <hook> <group?>
        // 执行指定group的命令，并将命令里面的%player%替换成玩家名
        // 如果没有指定群组，则运行所有群组
        if (args[0].equalsIgnoreCase("run")) {
            if (args.length == 2) {
                var hookName = args[1];
                var prev = new ArrayList<>(hooks);
                for (var hook : prev) {
                    if (hook.getName().equals(hookName)) {
                        for (var group : hook.getGroups()) {
                            var cmd = group.getCommand().replace("%player%", sender.getName());
                            Bukkit.dispatchCommand(sender, cmd);
                        }
                        return true;
                    }
                }
                sender.sendMessage("没有这个项目");
                return false;
            } else if (args.length == 3) {
                var hookName = args[1];
                var groupName = args[2];
                var prev = new ArrayList<>(hooks);
                for (var hook : prev) {
                    if (hook.getName().equals(hookName)) {
                        for (var group : hook.getGroups()) {
                            if (group.getName().equals(groupName)) {
                                var cmd = group.getCommand().replace("%player%", sender.getName());
                                Bukkit.dispatchCommand(sender, cmd);
                                return true;
                            }
                        }
                    }
                }
                sender.sendMessage("没有这个项目或群组");
                return false;
            } else {
                sender.sendMessage("用法: /hook run <项目名> <群组名?>");
                return false;
            }
        }
        sender.sendMessage("用法: /hook <子命令> <参数>");
        sender.sendMessage("加入项目: /hook join <项目名> <群组名>");
        sender.sendMessage("离开项目: /hook leave <项目名> <群组名>");
        if (sender.hasPermission("toiletcore.admin")) {
            sender.sendMessage("管理面板: /hook manager");
            sender.sendMessage("创建项目: /hook create <项目名>");
            sender.sendMessage("添加群组: /hook addgroup <项目名> <群组名>");
            sender.sendMessage("编辑命令: /hook editcmd <项目名> <群组名> <命令>");
            sender.sendMessage("删除群组: /hook deletegroup <项目名> <群组名>");
            sender.sendMessage("删除项目: /hook delete <项目名>");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        if (Arrays.asList(args).stream().anyMatch(s -> s.equalsIgnoreCase("delete"))) {
            return List.of("confirm");
        }
        return null;
    }
}
