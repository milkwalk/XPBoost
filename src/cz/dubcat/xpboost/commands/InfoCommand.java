package cz.dubcat.xpboost.commands;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.Condition;
import cz.dubcat.xpboost.api.InternalXPBoostAPI;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.XPBoost;

public class InfoCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("xpboost.use") || player.hasPermission("xpboost.info")) {
            UUID id = player.getUniqueId();

            if (XPBoostAPI.hasBoost(id)) {
                XPBoost xpb = XPBoostAPI.getBoost(id);
                InternalXPBoostAPI.sendMessage(XPBoostMain.getLang().getString("lang.info_command.boost")
                        .replaceAll("%boost%", String.valueOf(xpb.getBoost())), player);
                InternalXPBoostAPI.sendMessage(XPBoostMain.getLang().getString("lang.boostcountdown") + xpb.getTimeRemaining(), player);
                if (xpb.getConditions().size() > 0) {
                    InternalXPBoostAPI.sendMessage(XPBoostMain.getLang().getString("lang.info_command.boost_type"), player);
                    for (Entry<Condition, Boolean> set : xpb.getConditions().entrySet()) {
                        if (set.getValue())
                            InternalXPBoostAPI.sendMessage("  &a" + set.getKey().name(), player);
                    }
                }
            } else {
                InternalXPBoostAPI.sendMessage(XPBoostMain.getLang().getString("lang.boostinfodeny"), player);
            }
        }

        return true;
    }

}
