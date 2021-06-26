package cz.dubcat.xpboost.events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.InternalXPBoostAPI;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.Debug;

public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();

        if (!XPBoostAPI.hasBoost(p.getUniqueId())) {
            return;
        }

        if (!XPBoostMain.getPlugin().getConfig().getBoolean("settings.disabledcommands.enabled")) {
            return;
        }

        List<String> cmdlist = XPBoostMain.getPlugin().getConfig().getStringList("settings.disabledcommands.list");

        String[] command = e.getMessage().split(" ");
        String finalcmd = command[0];

        if (cmdlist.contains(finalcmd)) {
            InternalXPBoostAPI.sendMessage(XPBoostMain.getLang().getString("lang.cmdblock").replace("%cmd%", finalcmd), p);
            InternalXPBoostAPI.debug("Command " + finalcmd + " have been blocked for player " + p.getName(), Debug.NORMAL);
            e.setCancelled(true);
        }
    }

}
