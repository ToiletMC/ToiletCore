package net.toiletmc.toiletcore.module.magic;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.Modules.PlayerOptions.PlayerOption;
import com.elmakers.mine.bukkit.api.action.CastContext;
import com.elmakers.mine.bukkit.api.event.CastEvent;
import com.elmakers.mine.bukkit.api.event.PreCastEvent;
import com.elmakers.mine.bukkit.api.event.PreLoadEvent;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.protection.EntityTargetingManager;
import com.elmakers.mine.bukkit.api.spell.SpellResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class MagicHook implements EntityTargetingManager, Listener {
    MagicAPI getMagicAPI() {
        Plugin magicPlugin = Bukkit.getPluginManager().getPlugin("Magic");
        if (!(magicPlugin instanceof MagicAPI)) {
            return null;
        }
        return (MagicAPI) magicPlugin;
    }

    @EventHandler
    private void eee(PreCastEvent event) {
        System.out.println("PreCastEvent Start---------------------");
        System.out.println(event.getSpell().getTargetLocation());
        System.out.println(event.getSpell().getEntity());
        System.out.println(event.getSpell().getTargetEntity());
        System.out.println(event.getSpell().getName());
        event.setCancelled(true);
        System.out.println("已取消施法");
        System.out.println("PreCastEvent End---------------------");
    }


//    @EventHandler
    private void onSpellCast(CastEvent event) {


        CastContext context = event.getContext();
        String spellName = context.getSpell().getName();
        Entity targetEntity = context.getTargetEntity();

        if (!("Mount".equals(spellName))) return;

        if (targetEntity instanceof Sheep) {
            event.getContext().setResult(SpellResult.CANCELLED);
            System.out.println("已取消绵羊施法");
            return;
        }

        if (targetEntity instanceof Player player) {
            CMIUser cmiUser = CMI.getInstance().getPlayerManager().getUser(player);
            Boolean optionState = cmiUser.getOptionState(PlayerOption.rideMe);

            System.out.println("选项状态：" + optionState);

            if (!optionState) {
                event.getContext().setResult(SpellResult.CANCELLED);
                System.out.println("已取消施法");
            }
        }
    }

    @EventHandler
    private void onPreLoadEvent(PreLoadEvent event) {
        event.registerEntityTargetingManager(this);
    }

    @Override
    public boolean canTarget(Entity source, Entity target) {
        return false;
    }
}
