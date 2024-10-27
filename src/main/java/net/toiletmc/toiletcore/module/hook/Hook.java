package net.toiletmc.toiletcore.module.hook;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Hook implements ConfigurationSerializable {
    private String name;
    private List<HookGroup> groups;

    @Override
    public @NotNull Map<String, Object> serialize() {
        return Map.of("name", name, "groups", groups);
    }

    public static Hook deserialize(Map<String, Object> map) {
        String name = (String) map.get("name");
        List<HookGroup> groups = (List<HookGroup>) map.get("groups");

        return new Hook(name, groups);
    }
}
