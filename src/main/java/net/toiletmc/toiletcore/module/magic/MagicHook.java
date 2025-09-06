package net.toiletmc.toiletcore.module.magic;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.Modules.PlayerOptions.PlayerOption;
import com.Zrips.CMI.Modules.PlayerOptions.PlayerOptionsManager;
import com.elmakers.mine.bukkit.api.action.CastContext;
import com.elmakers.mine.bukkit.api.event.CastEvent;
import com.elmakers.mine.bukkit.api.event.PreCastEvent;
import com.elmakers.mine.bukkit.api.event.StartCastEvent;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import com.elmakers.mine.bukkit.api.spell.SpellResult;
import com.elmakers.mine.bukkit.spell.builtin.MountSpell;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class MagicHook implements Listener {
    MagicAPI getMagicAPI() {
        Plugin magicPlugin = Bukkit.getPluginManager().getPlugin("Magic");
        if (!(magicPlugin instanceof MagicAPI)) {
            return null;
        }
        return (MagicAPI) magicPlugin;
    }

    @EventHandler
    private void eee(StartCastEvent event) {
        System.out.println(event.getSpell().getTargetEntity());
        System.out.println(event.getSpell().getName());
//        event.getSpell().cancel();
        event.getMage().cancelPending();
        System.out.println("已取消施法");
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
}
