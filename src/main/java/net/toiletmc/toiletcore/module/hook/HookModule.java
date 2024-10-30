package net.toiletmc.toiletcore.module.hook;

import java.util.Collections;
import java.util.List;
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

import net.toiletmc.toiletcore.api.module.SimpleModule;

public class HookModule extends SimpleModule implements CommandExecutor, TabCompleter {
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
        var hooks = getHooks();
        debug("onCommand: " + hooks);

        if (args.length == 0) {
            return false;
        }

        // 管理面板
        if (handleManagerCommand(sender, args, hooks))
            return true;

        if (sender.hasPermission("toiletcore.admin")) {
            switch (args[0].toLowerCase()) {
                case "create":
                    return handleCreateCommand(sender, args, hooks);
                case "addgroup":
                    return handleAddGroupCommand(sender, args, hooks);
                case "editcmd":
                    return handleEditCmdCommand(sender, args, hooks);
                case "deletegroup":
                    return handleDeleteGroupCommand(sender, args, hooks);
                case "delete":
                    return handleDeleteCommand(sender, args, hooks);
                case "run":
                    return handleRunCommand(sender, args, hooks);
            }
        }

        switch (args[0].toLowerCase()) {
            case "join":
            case "leave":
                return handleJoinLeaveCommands(sender, args, hooks);
        }

        if (sender.hasPermission("toiletcore.admin")) {
            sender.sendMessage("/hook create <项目名>");
            sender.sendMessage("/hook addgroup <项目名> <群组名>");
            sender.sendMessage("/hook editcmd <项目名> <群组名> <...命令>");
            sender.sendMessage("/hook deletegroup <项目名> <群组名>");
            sender.sendMessage("/hook delete <项目名>");
            sender.sendMessage("/hook run <项目名> <群组名?>");
            sender.sendMessage("/hook join <项目名> <群组名> <玩家名?>");
            sender.sendMessage("/hook leave <项目名> <群组名> <玩家名?>");
        } else {
            sender.sendMessage("/hook join <项目名> <群组名>");
            sender.sendMessage("/hook leave <项目名> <群组名>");
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Hook> getHooks() {
        return new ArrayList<>((List<Hook>) getConfig().getList("hooks", new ArrayList<>()));
    }

    private boolean handleRunCommand(CommandSender sender, String[] args, ArrayList<Hook> hooks) {
        if (args.length < 2) {
            sender.sendMessage("用法: /hook run <项目名> <群组名?>");
            return true;
        }

        String hookName = args[1];
        String groupName = args.length == 3 ? args[2] : null;

        for (Hook hook : hooks) {
            if (hook.getName().equals(hookName)) {
                if (groupName != null) {
                    // 运行指定群组的命令
                    return hook.getGroups().stream()
                            .filter(group -> group.getName().equals(groupName))
                            .findFirst()
                            .map(group -> {
                                for (var player : group.getPlayers()) {
                                    var cmd = group.getCommand();
                                    cmd = cmd.replaceAll("%player%", player);
                                    cmd = cmd.replaceAll("%uuid%", Bukkit.getPlayerUniqueId(player).toString());
                                    cmd = cmd.replaceAll("%group%", group.getName());
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                                }
                                return true;
                            })
                            .orElseGet(() -> {
                                sender.sendMessage("没有这个群组");
                                return false;
                            });
                } else {
                    // 遍历所有群组，运行命令
                    for (var group : hook.getGroups()) {
                        for (var player : group.getPlayers()) {
                            var cmd = group.getCommand();
                            cmd = cmd.replaceAll("%player%", player);
                            cmd = cmd.replaceAll("%uuid%", Bukkit.getPlayerUniqueId(player).toString());
                            cmd = cmd.replaceAll("%group%", group.getName());
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                        }
                    }
                    return true;
                }
            }
        }
        sender.sendMessage("没有这个项目");
        return false;
    }

    private boolean handleManagerCommand(CommandSender sender, String[] args, ArrayList<Hook> hooks) {
        if (args[0].equalsIgnoreCase("manager")) {
            if (args.length == 1) {
                sender.sendMessage(HookManagerUI.mainPage(hooks));
                return true;
            }
            if (args.length == 2) {
                return hooks.stream()
                        .filter(hook -> hook.getName().equals(args[1]))
                        .findFirst()
                        .map(hook -> {
                            sender.sendMessage(HookManagerUI.hookPage(hook));
                            return true;
                        })
                        .orElseGet(() -> {
                            sender.sendMessage("没有这个项目");
                            return false;
                        });
            }
        }
        return false;
    }

    private boolean handleCreateCommand(CommandSender sender, String[] args, ArrayList<Hook> hooks) {
        if (args.length == 2) {
            String name = args[1];

            if (hooks.stream().anyMatch(hook -> hook.getName().equals(name))) {
                sender.sendMessage("这个hook已经存在了");
                return true;
            }

            hooks.add(new Hook(name, new ArrayList<>()));
            config.set("hooks", hooks);
            saveConfig();
            sender.sendMessage(HookManagerUI.mainPage(hooks));
            return true;
        } else {
            sender.sendMessage("用法: /hook create <项目名>");
            return true;
        }
    }

    private boolean handleAddGroupCommand(CommandSender sender, String[] args, ArrayList<Hook> hooks) {
        if (args.length == 3) {
            String hookName = args[1];
            String groupName = args[2];

            return hooks.stream()
                    .filter(hook -> hook.getName().equals(hookName))
                    .findFirst()
                    .map(hook -> {
                        var groups = new ArrayList<>(hook.getGroups());
                        groups.add(new HookGroup(groupName, new ArrayList<>(), ""));
                        hook.setGroups(groups);
                        config.set("hooks", hooks);
                        saveConfig();
                        sender.sendMessage(HookManagerUI.hookPage(hook));
                        return true;
                    })
                    .orElseGet(() -> {
                        sender.sendMessage("没有这个项目");
                        return false;
                    });
        } else {
            sender.sendMessage("用法: /hook addgroup <项目名> <群组名>");
            return true;
        }
    }

    private boolean handleEditCmdCommand(CommandSender sender, String[] args, ArrayList<Hook> hooks) {
        if (args.length >= 4) {
            String hookName = args[1];
            String groupName = args[2];
            String cmd = String.join(" ", Arrays.copyOfRange(args, 3, args.length));

            return hooks.stream()
                    .filter(hook -> hook.getName().equals(hookName))
                    .flatMap(hook -> hook.getGroups().stream()
                            .filter(group -> group.getName().equals(groupName)))
                    .findFirst()
                    .map(group -> {
                        group.setCommand(cmd);
                        config.set("hooks", hooks);
                        saveConfig();
                        sender.sendMessage(HookManagerUI
                                .hookPage(hooks.stream().filter(h -> h.getName().equals(hookName)).findFirst().get()));
                        return true;
                    })
                    .orElseGet(() -> {
                        sender.sendMessage("没有这个项目或群组");
                        return false;
                    });
        } else {
            sender.sendMessage("用法: /hook editcmd <项目名> <群组名> <...命令>");
            return true;
        }
    }

    private boolean handleDeleteGroupCommand(CommandSender sender, String[] args, ArrayList<Hook> hooks) {
        if (args.length == 3) {
            String hookName = args[1];
            String groupName = args[2];

            return hooks.stream()
                    .filter(hook -> hook.getName().equals(hookName))
                    .findFirst()
                    .map(hook -> {
                        var groups = new ArrayList<>(hook.getGroups());
                        groups.removeIf(group -> group.getName().equals(groupName));
                        if (groups.size() == hook.getGroups().size()) {
                            sender.sendMessage("没有这个群组");
                            return false;
                        }
                        hook.setGroups(groups);
                        config.set("hooks", hooks);
                        saveConfig();
                        sender.sendMessage(HookManagerUI.hookPage(hook));
                        return true;
                    })
                    .orElseGet(() -> {
                        sender.sendMessage("没有这个项目");
                        return false;
                    });
        } else {
            sender.sendMessage("用法: /hook deletegroup <项目名> <群组名>");
            return true;
        }
    }

    private boolean handleDeleteCommand(CommandSender sender, String[] args, ArrayList<Hook> hooks) {
        if (args.length == 2) {
            String hookName = args[1];
            boolean removed = hooks.removeIf(hook -> hook.getName().equals(hookName));
            if (removed) {
                config.set("hooks", hooks);
                saveConfig();
                sender.sendMessage(HookManagerUI.mainPage(hooks));
                return true;
            } else {
                sender.sendMessage("没有这个项目");
                return false;
            }
        } else {
            sender.sendMessage("用法: /hook delete <项目名>");
            return true;
        }
    }

    private boolean handleJoinLeaveCommands(CommandSender sender, String[] args, ArrayList<Hook> hooks) {
        String command = args[0].toLowerCase();
        if (command.equals("join") || command.equals("leave")) {
            String playerName = "";
            if (args.length == 4 && sender.hasPermission("toiletcore.admin")) {
                playerName = args[3];
            } else if (args.length == 3 && sender instanceof Player) {
                playerName = args[2];
            } else {
                sender.sendMessage("用法: /hook " + command + " <项目名> <群组名> <玩家名?>");
                return false;
            }

            String hookName = args[1];
            String groupName = args[2];

            for (var hook : hooks) {
                if (hook.getName().equals(hookName)) {
                    for (var group : hook.getGroups()) {
                        if (group.getName().equals(groupName)) {
                            var players = new ArrayList<>(group.getPlayers());
                            if (command.equals("join")) {
                                players.add(playerName);
                            } else {
                                players.remove(playerName);
                            }
                            group.setPlayers(players);
                            config.set("hooks", hooks);
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
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return List.of("create", "manager", "addgroup", "editcmd", "deletegroup", "delete", "join", "leave");
        }
        return null;
    }
}
