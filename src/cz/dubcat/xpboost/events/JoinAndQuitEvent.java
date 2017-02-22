package cz.dubcat.xpboost.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.constructors.GlobalBoost;

public class JoinAndQuitEvent implements Listener{

    @EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player player = e.getPlayer();
		
		if(Main.getPlugin().getConfig().getBoolean("settings.globalboost.notification")){
			GlobalBoost gl = Main.GLOBAL_BOOST;
			if(gl.isEnabled())
				MainAPI.sendMSG(Main.getLang().getString("lang.joinnotmsg").replaceAll("%boost%", gl.getGlobalBoost()+""), player);	
	    }
		
		MainAPI.loadPlayer(player);	
	}
    
    @EventHandler
	public void onQuit(PlayerQuitEvent e){
    	Player player = e.getPlayer();
    	
    	MainAPI.savePlayer(player);
    	
    	Main.allplayers.remove(player.getUniqueId());
    }
    
    
}
