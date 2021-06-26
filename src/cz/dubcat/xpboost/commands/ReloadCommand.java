package cz.dubcat.xpboost.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.InternalXPBoostAPI;

public class ReloadCommand implements CommandInterface {

    private XPBoostMain plugin;

    public ReloadCommand(XPBoostMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender.hasPermission("xpboost.admin")) {
            plugin.reloadConfig();

            XPBoostMain.debug = plugin.reloadDebug();

            if (XPBoostMain.getPlugin().getConfig().contains("settings.language")) {
                XPBoostMain.langFile = new File(XPBoostMain.getPlugin().getDataFolder() + "/lang/lang_"
                        + XPBoostMain.getPlugin().getConfig().getString("settings.language") + ".yml");
                XPBoostMain.lang = YamlConfiguration.loadConfiguration(XPBoostMain.langFile);
            } else {
                XPBoostMain.langFile = new File(XPBoostMain.getPlugin().getDataFolder() + "/lang/lang_ENG.yml");
                XPBoostMain.lang = YamlConfiguration.loadConfiguration(XPBoostMain.langFile);
            }

            XPBoostMain.boostFile = new File(XPBoostMain.getPlugin().getDataFolder() + "/boosts.yml");
            XPBoostMain.boostCfg = YamlConfiguration.loadConfiguration(XPBoostMain.boostFile);

            InternalXPBoostAPI.sendMessage(XPBoostMain.getLang().getString("lang.reload"), sender);
        }
        return true;
    }

}
