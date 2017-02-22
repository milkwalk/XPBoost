package cz.dubcat.xpboost.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.api.MainAPI;

public class guiCmd implements CommandInterface{
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,String commandLabel, String[] args) {
        Player player = (Player) sender;
    	if (player.hasPermission("xpboost.use") || player.hasPermission("xpboost.gui")){
    		MainAPI.openXpBoostShop(player);
    	}
        return false;
    }
}
