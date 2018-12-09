package cz.dubcat.xpboost.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI;

public class MainCommand implements CommandInterface {

    @SuppressWarnings("unchecked")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        for (String menu : (List<String>) XPBoostMain.getLang().getList("lang.pluginmenu")) {
            MainAPI.sendMessage(menu, sender);
        }

        return true;
    }
}
