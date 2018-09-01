package cz.dubcat.xpboost.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;

public class FactionCommand implements CommandInterface {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = (Player) sender;
		if (player.hasPermission("xpboost.use") || player.hasPermission("xpboost.factions")) {
			if (!Main.factions_enabled)
				return true;

			MainAPI.openFactionBoostShop(player);
		}
		return false;
	}

}
