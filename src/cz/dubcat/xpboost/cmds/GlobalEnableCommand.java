package cz.dubcat.xpboost.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;

public class GlobalEnableCommand implements CommandInterface {

	private Main plugin;

	public GlobalEnableCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
			if (!(player.hasPermission("xpboost.admin")) && !(player.hasPermission("xpboost.on"))) {
				return false;
			}
		}

		Main.GLOBAL_BOOST.setEnabled(true);

		if (sender instanceof Player) {
			MainAPI.sendMessage("Global &c" + Main.GLOBAL_BOOST.getGlobalBoost() + "x Boost&f is now ON.", player);
		} else {
			plugin.getLogger().info("Global " + Main.GLOBAL_BOOST.getGlobalBoost() + "x Boost is now ON.");
		}

		return false;
	}

}
