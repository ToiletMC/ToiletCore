package net.toiletmc.toiletcore.module.hook;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Hook {
  private String name;
  private List<HookGroup> groups;
}
