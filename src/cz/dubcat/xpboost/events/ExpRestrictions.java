package cz.dubcat.xpboost.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.xpbAPI;

public class ExpRestrictions implements Listener{
	
	@EventHandler
	public void onExpBottle(ExpBottleEvent event){
		if(Main.getPlugin().getConfig().getInt("settings.xpbottlemode") == 2){
			event.setExperience(0);
		}
	}	

	@SuppressWarnings("deprecation")
	@EventHandler
	public void rightClick(PlayerInteractEvent event){
        
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK  || event.getAction() == Action.RIGHT_CLICK_AIR)&& Main.getPlugin().getConfig().getInt("settings.xpbottlemode") == 3 && xpbAPI.hasBoost(event.getPlayer().getUniqueId())) {
            Player player = event.getPlayer();   
            if(player.getItemInHand() != null && player.getItemInHand().getType() == Material.EXP_BOTTLE){
            	event.setCancelled(true);                  	
            }
        }
	}
}
