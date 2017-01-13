package cz.dubcat.xpboost.cmds;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.xpbAPI;
import cz.dubcat.xpboost.constructors.XPBoost;

public class infoCmd implements CommandInterface{
    private Main plugin;
    
    public infoCmd(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,String commandLabel, String[] args) {
        Player player = (Player) sender;
    	if (player.hasPermission("xpboost.use")){
    		UUID id = player.getUniqueId();
    		
    		if(xpbAPI.hasBoost(id)){
    			XPBoost xpb = xpbAPI.getBoost(id);
    			MainAPI.sendMSG(plugin.getConfig().getString("lang.boostcountdown") + xpb.getTimeRemaining(), player);			
    		}else{
    			MainAPI.sendMSG(plugin.getConfig().getString("lang.boostinfodeny"), player);
    		}
    	}
        return false;
    }

}
