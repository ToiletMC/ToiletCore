package net.toiletmc.toiletcore.module.cdk;

import net.toiletmc.toiletcore.ToiletCore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CDKManager {
    private final List<CDKInstance> cdkInstanceList = new ArrayList<>();
    private final CDKModule module;

    public CDKManager(CDKModule module) {
        this.module = module;
        ConfigurationSection data = module.getData();
        data.getKeys(false).forEach(key ->
                cdkInstanceList.add(new CDKInstance(data.getConfigurationSection(key)))
        );

        ToiletCore.getInstance().getLogger().info("已加载 " + cdkInstanceList.size() + " 个 CDK 实例！");
    }

    private CDKInstance getCDKInstance(String key) {
        return cdkInstanceList.stream()
                .filter(cdkInstance -> cdkInstance.existsKey(key))
                .findFirst().orElse(null);
    }

    public boolean existsKey(String key) {
        return getCDKInstance(key) != null;
    }

    public void consumeKey(String key, Player player) {
        CDKInstance cdkInstance = getCDKInstance(key);
        cdkInstance.consumeKey(key, player);
        cdkInstance.setToDataConfig(module.getData());
        module.saveData();
    }
}
