package cz.dubcat.xpboost.cmds;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;

public class ReloadCommand implements CommandInterface {

	private Main plugin;

	public ReloadCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (sender.hasPermission("xpboost.admin")) {
			plugin.reloadConfig();

			Main.debug = plugin.reloadDebug();

			if (Main.getPlugin().getConfig().contains("settings.language")) {
				Main.langFile = new File(Main.getPlugin().getDataFolder() + "/lang/lang_"
						+ Main.getPlugin().getConfig().getString("settings.language") + ".yml");
				Main.lang = YamlConfiguration.loadConfiguration(Main.langFile);
			} else {
				Main.langFile = new File(Main.getPlugin().getDataFolder() + "/lang/lang_ENG.yml");
				Main.lang = YamlConfiguration.loadConfiguration(Main.langFile);
			}

			Main.factions_file = new File(Main.getPlugin().getDataFolder() + "/factions.yml");
			Main.factions = YamlConfiguration.loadConfiguration(Main.factions_file);

			Main.boostFile = new File(Main.getPlugin().getDataFolder() + "/boosts.yml");
			Main.boostCfg = YamlConfiguration.loadConfiguration(Main.boostFile);

			MainAPI.sendMessage(Main.getLang().getString("lang.reload"), sender);
		}
		return true;
	}

}
