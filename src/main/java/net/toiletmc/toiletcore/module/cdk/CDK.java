package net.toiletmc.toiletcore.module.cdk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.toiletmc.toiletcore.api.module.ToiletModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CDK extends ToiletModule implements CommandExecutor {
    @Override
    public void onEnable() {
        getLogger().info("test");
    }

    @Override
    public void onDisable() {
        getLogger().info("bye");
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("该指令仅限玩家使用！").color(NamedTextColor.RED));
                return true;
            }

            player.sendMessage("CDK错误，请核对密钥后重试。");
            getLogger().info(player.getName() + "(" + player.getUniqueId() + ")" + "使用了CDK：" + "cdk");

            return true;
        }

        return true;
    }
}
