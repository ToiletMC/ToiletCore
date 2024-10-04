package net.toiletmc.toiletcore.module.hook;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HookGroup implements ConfigurationSerializable {
  private String name;
  private List<UUID> players;
  private String command;

  @Override
  public @NotNull Map<String, Object> serialize() {
    return Map.of(
        "name", name,
        "players", players,
        "command", command);
  }
}