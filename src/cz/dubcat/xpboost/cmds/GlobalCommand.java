package cz.dubcat.xpboost.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;

public class GlobalCommand implements CommandInterface {

	private Main plugin;

	public GlobalCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (!(player.hasPermission("xpboost.admin"))) {
				return false;
			}
		}

		if (args.length == 2) {

			if (args[1] != null) {
				double globalboost = Double.parseDouble(args[1]);

				Main.GLOBAL_BOOST.setBoost(globalboost);

				plugin.getConfig().set("settings.globalboost.multiplier", globalboost);
				plugin.saveConfig();

				if (sender instanceof Player) {
					player = (Player) sender;
					MainAPI.sendMessage("You have successfully set global boost to &a" + globalboost, player);
					if (!Main.GLOBAL_BOOST.isEnabled())
						MainAPI.sendMessage("To enable &cGlobal Boost &fwrite &a/xpboost on", player);
				} else {
					plugin.getLogger().info("You have successfully set a global boost to " + globalboost);
				}
			}
		} else if (args.length == 3) {
			if (args[1] != null && args[2] != null) {
				double globalboost = Double.parseDouble(args[1]);
				int time = Integer.parseInt(args[2]);

				Main.GLOBAL_BOOST.setBoost(globalboost);
				Main.GLOBAL_BOOST.setTime(time);

				plugin.getConfig().set("settings.globalboost.multiplier", globalboost);
				plugin.saveConfig();

				if (sender instanceof Player) {
					player = (Player) sender;
					MainAPI.sendMessage("You have successfully set global boost to &a" + globalboost + " &ffor &a"
							+ time + " Seconds", player);
					if (!Main.GLOBAL_BOOST.isEnabled())
						MainAPI.sendMessage("To enable &cGlobal Boost &fwrite &a/xpboost on", player);
				} else {
					plugin.getLogger().info(
							"You have successfully set a global boost to " + globalboost + "for " + time + " Seconds");
				}
			}
		} else {
			if (sender instanceof Player) {
				player = (Player) sender;
				MainAPI.sendMessage("Usage: &c/xpboost global <boost> [time in seconds]", player);
			} else {
				plugin.getLogger().info("Usage: /xpboost global <boost> [time in seconds]");
			}
		}

		return false;
	}

}
