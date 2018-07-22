package cz.dubcat.xpboost.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;

public class ServerList implements Listener{
    @EventHandler(priority = EventPriority.HIGH)
    public void onServerListPing(ServerListPingEvent event){
    	if (Main.GLOBAL_BOOST.isEnabled()){
    		if (Main.getPlugin().getConfig().getBoolean("settings.motdon")){    			
    			String message = Main.getLang().getString("lang.motd");
    			message = message.replaceAll("%boost%", String.valueOf(Main.GLOBAL_BOOST.getGlobalBoost()));
    			message = message.replaceAll("%max%", String.valueOf(event.getMaxPlayers()));
    			message = message.replaceAll("%players%", String.valueOf(event.getNumPlayers()));
    			event.setMotd(MainAPI.colorizeText(message));
    		}
    	}
    }

}
