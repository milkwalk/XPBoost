package cz.dubcat.xpboost.cmds;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;
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
				MainAPI.sendMessage("Boost: &6" + xpb.getBoost(), player);
				MainAPI.sendMessage(Main.getLang().getString("lang.boostcountdown") + xpb.getTimeRemaining(), player);
				if (xpb.getConditions().size() > 0) {
					MainAPI.sendMessage("Boost type: ", player);
					for (Entry<Condition, Boolean> set : xpb.getConditions().entrySet()) {
						if (set.getValue())
							MainAPI.sendMessage("  &a" + set.getKey().name(), player);
					}
				}
			} else {
				MainAPI.sendMessage(Main.getLang().getString("lang.boostinfodeny"), player);
			}
		}

		return true;
	}

}
