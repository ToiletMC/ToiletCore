package net.toiletmc.toiletcore.module.hook;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HookGroup {
  private String name;
  private List<UUID> players;
  private String command;
}