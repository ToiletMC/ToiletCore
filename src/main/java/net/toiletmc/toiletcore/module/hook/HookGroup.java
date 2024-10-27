package net.toiletmc.toiletcore.module.hook;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HookGroup implements ConfigurationSerializable {
    private String name;
    private List<String> players;
    private String command;

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of(
                "name", name,
                "players", players,
                "command", command);
    }

    public static HookGroup deserialize(Map<String, Object> map) {
        List<String> players = (List<String>) map.get("players");
        String command = (String) map.get("command");
        String name = (String) map.get("name");

        return new HookGroup(name, players, command);
    }
}