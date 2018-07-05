package cz.dubcat.xpboost.cmds;

import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.xpbAPI;
import cz.dubcat.xpboost.constructors.XPBoost;

public class InfoCmd implements CommandInterface{
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,String commandLabel, String[] args) {
        Player player = (Player) sender;
    	if (player.hasPermission("xpboost.use") || player.hasPermission("xpboost.info")){
    		UUID id = player.getUniqueId();
    		
    		if(xpbAPI.hasBoost(id)){
    			XPBoost xpb = xpbAPI.getBoost(id);
    			MainAPI.sendMessage(Main.getLang().getString("lang.boostcountdown") + xpb.getTimeRemaining(), player);
    			if(xpb.getConditions().size() > 0) {
    				StringBuilder sb = new StringBuilder("Boost type: ");
    				for(Entry<Condition, Boolean> set : xpb.getConditions().entrySet()) {
    					if(set.getValue())
    						sb.append("&a" + set.getKey().name() + " ");
    					else
    						sb.append("&c" + set.getKey().name() + " ");
    				}
    				
    				MainAPI.sendMessage(MainAPI.colorizeText(sb.toString()), player);
    			}
    		}else{
    			MainAPI.sendMessage(Main.getLang().getString("lang.boostinfodeny"), player);
    		}
    	}
        return false;
    }

}
