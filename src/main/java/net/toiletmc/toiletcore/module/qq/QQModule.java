package net.toiletmc.toiletcore.module.qq;

import net.toiletmc.toiletcore.HttpHelper;
import net.toiletmc.toiletcore.api.module.SimpleModule;
import net.toiletmc.toiletcore.http.request.BindQQRequest;
import net.toiletmc.toiletcore.utils.MsgUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QQModule extends SimpleModule implements CommandExecutor, TabCompleter {
    private String bindUrl;

    @Override
    public void onEnable() {
        getPlugin().getCommand("qq").setExecutor(this);
        getPlugin().getCommand("qq").setTabCompleter(this);
        bindUrl = getPlugin().getConfig().getString("webhook.bind-url");
    }

    @Override
    public void onDisable() {
        getPlugin().getCommand("qq").setExecutor(null);
        getPlugin().getCommand("qq").setTabCompleter(null);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player && args.length == 2 && args[0].equalsIgnoreCase("qq")) {
            String bindCode = args[1];
            HttpHelper.sendHttpRequest(bindUrl, new BindQQRequest(bindCode, player.getUniqueId().toString()));
            MsgUtil.sendNormalText(player, "已请求服务器，请留意QQ消息。");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("bind");
        }

        return null;
    }
}
