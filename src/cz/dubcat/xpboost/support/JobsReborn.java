package cz.dubcat.xpboost.support;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gamingmesh.jobs.api.JobsExpGainEvent;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.xpbAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.MainAPI.Debug;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;

public class JobsReborn implements Listener{
	
	
	private static GlobalBoost gl = Main.GLOBAL_BOOST;
	private static final Condition CONDITION_NAME = Condition.JOBS;
	
	@EventHandler
	public void jobsExp(JobsExpGainEvent event) {
		Player player = (Player) event.getPlayer();
		
	    String job = event.getJob().getName();
    	UUID id = player.getUniqueId();
    	
    	
    	double exp = event.getExp();
    	double newExp = 0;
		 
		if(xpbAPI.hasBoost(id)){			
			XPBoost xpb = xpbAPI.getBoost(id);
			if(xpb.hasCondition(CONDITION_NAME))
				newExp = Math.round(exp * xpb.getBoost());
			else
				return;
		}
		
		
		if(xpbAPI.getFactionBoost(player) != null){
			XPBoost faction_boost = xpbAPI.getFactionBoost(player);
			newExp += (int) Math.round(exp * faction_boost.getBoost());
			MainAPI.debug("Faction boost of " + faction_boost.getBoost() + "x has been applied to JobsXP, Player: " + player.getName(), Debug.ALL);
		}
		
		if(gl.isEnabled()){
			newExp += Math.round(exp * gl.getGlobalBoost());
		}
		
		if(newExp > 0){
			event.setExp(newExp);
		

			if (Main.getPlugin().getConfig().getBoolean("settings.jobs.msg.enabled")){
				String message = Main.getPlugin().getConfig().getString("settings.jobs.msg.msg");
			    message = message.replaceAll("%newexp%", String.valueOf(newExp));
			    message = message.replaceAll("%oldexp%", String.valueOf(exp));
			    message = message.replaceAll("%job%", job);
			    MainAPI.sendMessage(message, player);
			}
		}
	}
	

}
