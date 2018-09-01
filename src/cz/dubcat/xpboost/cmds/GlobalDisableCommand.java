package cz.dubcat.xpboost.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;

public class GlobalDisableCommand implements CommandInterface {

	private Main plugin;

	public GlobalDisableCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (!(player.hasPermission("xpboost.admin")) && !(player.hasPermission("xpboost.off"))) {
				return false;
			}
		}

		Main.GLOBAL_BOOST.setEnabled(false);

		if (sender instanceof Player) {
			player = (Player) sender;
			MainAPI.sendMessage("Global &c" + Main.GLOBAL_BOOST.getGlobalBoost() + "x Boost&f is now OFF.", player);
		} else {
			plugin.getLogger().info("Global " + Main.GLOBAL_BOOST.getGlobalBoost() + "x Boost is now OFF.");
		}

		return false;
	}

}
