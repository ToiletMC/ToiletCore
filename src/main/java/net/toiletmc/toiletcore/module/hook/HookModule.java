package net.toiletmc.toiletcore.module.hook;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.toiletmc.toiletcore.api.module.ToiletModule;

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
    var hooks = (List<Hook>) config.getList("hooks", List.of());
    if (args[0].equalsIgnoreCase("manager") && sender.hasPermission("toiletcore.admin")) {
      if (args.length == 1) {
        sender.sendMessage(HookManagerUI.mainPage(this.config));
        return true;
      }
      if (args.length == 2) {
        for (var hook : hooks) {
          if (hook.getName().equals(args[1])) {
            sender.sendMessage(HookManagerUI.hookPage(hook));
            sender.sendMessage(hook.getName());
            return true;
          }
        }
        sender.sendMessage("没有这个项目，而且谁让你用内部命令了:(");
        return false;
      }
    }
    // create子命令，需要权限
    if (args[0].equalsIgnoreCase("create") && sender.hasPermission("toiletcore.admin")) {
      if (args.length == 2) {
        var name = Arrays.asList(args).subList(1, args.length).stream().reduce("", (a, b) -> a + " " + b).trim();
        var prev = new ArrayList<>(hooks);
        // h检查name是否重复
        for (var hook : prev) {
          if (hook.getName().equals(name)) {
            sender.sendMessage("你说的对，但是这个list已经存在了");
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
    sender.sendMessage("加入项目: /hook join <项目名>");
    sender.sendMessage("离开项目: /hook leave <项目名>");
    if (sender.hasPermission("toiletcore.admin")) {
      sender.sendMessage("管理: /hook manager");
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
