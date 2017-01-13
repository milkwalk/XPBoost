package cz.dubcat.xpboost.events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.xpbAPI;
import cz.dubcat.xpboost.api.MainAPI.Debug;

public class CommandListener implements Listener{
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
	    Player p = e.getPlayer();
	    
	    if(!xpbAPI.hasBoost(p.getUniqueId()))
	    	return;
	    
	    if(!Main.getPlugin().getConfig().getBoolean("settings.disabledcommands.enabled"))
	    	return;
	    
	    @SuppressWarnings("unchecked")
	    List<String> cmdlist  = (List<String>) Main.getPlugin().getConfig().getList("settings.disabledcommands.list");

	    String[] command = e.getMessage().split(" ");
	    String finalcmd = command[0];
	    
	    for(String cmd : cmdlist){
	    	if(cmd.equalsIgnoreCase(finalcmd)){
	    		MainAPI.sendMSG(Main.getPlugin().getConfig().getString("lang.cmdblock").replace("%cmd%", finalcmd), p);
	    		
	    		MainAPI.debug("Command " + finalcmd + " have been blocked for player " + p.getName(), Debug.NORMAL);
	    		
				e.setCancelled(true);
	    	}
	    }
	 
	}

}
