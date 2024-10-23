package net.toiletmc.toiletcore.module.cdk;

import net.toiletmc.toiletcore.api.module.SimpleModule;
import net.toiletmc.toiletcore.utils.MsgUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CDKModule extends SimpleModule implements CommandExecutor {
    private CDKManager cdkManager;

    @Override
    public void onEnable() {
        plugin.getCommand("cdk").setExecutor(this);
        cdkManager = new CDKManager(this);
    }

    @Override
    public void onDisable() {
        plugin.getCommand("cdk").setExecutor(null);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                MsgUtil.sendRedText(sender, "该指令仅限游戏中的玩家使用！");
                return true;
            }

            String key = args[0];

            if (cdkManager.existsKey(key)) {
                cdkManager.consumeKey(key, player);
                MsgUtil.sendNormalText(player, "CDK兑换成功！");
                getLogger().info(player.getName() + "(" + player.getUniqueId() + ")" + "使用了CDK：" + "cdk");
            } else {
                MsgUtil.sendRedText(player, "CDK错误，请核对密钥后重试。");
                getLogger().warning(player.getName() + " 尝试了错误的CDK：" + key);
            }

            return true;
        }

        return true;
    }
}