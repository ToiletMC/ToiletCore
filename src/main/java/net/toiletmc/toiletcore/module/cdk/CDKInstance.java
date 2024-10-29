package net.toiletmc.toiletcore.module.cdk;

import lombok.Getter;
import lombok.ToString;
import net.toiletmc.toiletcore.ToiletCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
public class CDKInstance {
    @Getter
    private String id;
    private List<String> commands;
    private Set<String> keys;

    public CDKInstance(String id, List<String> commands, Set<String> keys) {
        this.id = id;
        this.commands = commands;
        this.keys = keys;
    }

    public CDKInstance(ConfigurationSection dataConfig) {
        this(
                dataConfig.getName(),
                dataConfig.getStringList("commands"),
                new HashSet<>(dataConfig.getStringList("keys"))
        );
    }

    public boolean existsKey(String key) {
        return keys.contains(key);
    }

    private void executeCommands(Player player) {
        commands.stream()
                .map(cmd -> cmd.replace("{player}", player.getName()))
                .forEach(cmd -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd));
    }

    public void consumeKey(String key, Player player) {
        if (!existsKey(key)) {
            CDKModule cdkModule = ToiletCore.getInstance().getModuleManager().getModuleInstance(CDKModule.class);
            cdkModule.getLogger().warning(player.getName() + " 尝试使用不存在的CDK：" + key);
        } else {
            executeCommands(player);
            keys.remove(key);
        }
    }

    public void setToDataConfig(ConfigurationSection dataConfig) {
        dataConfig.set(id + ".commands", commands);
        dataConfig.set(id + ".keys", new ArrayList<>(keys));
    }
}

