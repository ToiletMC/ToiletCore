package net.toiletmc.toiletcore;

import lombok.Getter;
import me.lucko.spark.api.Spark;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.toiletmc.toiletcore.module.ModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class ToiletCore extends JavaPlugin {
    @Getter
    private static ToiletCore instance;
    @Getter
    private Spark spark;
    private ModuleManager moduleManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        initSpark();
        initModuleManager();
    }

    @Override
    public void onDisable() {
        moduleManager.disableAllModules();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadPlugin();
            sender.sendMessage(Component.text("插件已重载！").color(NamedTextColor.GREEN));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reload");
        }
        return null;
    }

    public void reloadPlugin() {
        reloadConfig();
        moduleManager.disableAllModules();
        moduleManager.enableAllModules();
    }

    private void initSpark() {
        RegisteredServiceProvider<Spark> provider = Bukkit.getServicesManager().getRegistration(Spark.class);
        if (provider != null) {
            spark = provider.getProvider();
            getLogger().info("已挂钩到 Spark");
        } else {
            spark = null;
            getLogger().severe("Spark 服务异常，请排查错误！");
        }
    }

    private void initModuleManager() {
        moduleManager = new ModuleManager(this);
        moduleManager.enableAllModules();
    }
}
