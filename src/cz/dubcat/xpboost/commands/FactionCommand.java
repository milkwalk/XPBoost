package cz.dubcat.xpboost.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI;

public class FactionCommand implements CommandInterface {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("xpboost.use") || player.hasPermission("xpboost.factions")) {
            if (!XPBoostMain.factions_enabled)
                return true;

            MainAPI.openFactionBoostShop(player);
        }
        return false;
    }

}
