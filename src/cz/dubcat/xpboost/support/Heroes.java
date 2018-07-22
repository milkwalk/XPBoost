package cz.dubcat.xpboost.support;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
import com.herocraftonline.heroes.characters.classes.HeroClass.ExperienceType;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.Debug;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;

public class Heroes implements Listener{
	
	private static GlobalBoost gl = Main.GLOBAL_BOOST;
	private static final Condition CONDITION_NAME = Condition.HEROES;
	
	@EventHandler
	public void gainXp(ExperienceChangeEvent event){
		Player player = event.getHero().getPlayer();
		double exp = event.getExpChange();
		double expnew = 0;
		
    	ExperienceType skill = event.getSource();
    	UUID id = player.getUniqueId();
    	
		if(XPBoostAPI.hasBoost(id)){			
			XPBoost xpb = XPBoostAPI.getBoost(id);
			
			if(xpb.hasCondition(CONDITION_NAME)) {
				if(xpb.getAdvancedOptions().containsKey("HEROES")) {
					if(xpb.getAdvancedOptions().get("HEROES").isAllowedType(skill.name())) {
						expnew += (int) Math.round(exp * xpb.getBoost());
					}		
				}else {	
					expnew += Math.round(exp * xpb.getBoost());
				}
			}
		}
		
		if(XPBoostAPI.getFactionBoost(player) != null){
			XPBoost faction_boost = XPBoostAPI.getFactionBoost(player);
			expnew += (int) Math.round(exp * faction_boost.getBoost());
			MainAPI.debug("Faction boost of " + faction_boost.getBoost() + "x has been applied to HeroesXP, Player: " + player.getName(), Debug.ALL);
		}
		
		if(gl.isEnabled()){
			expnew += Math.round(exp * gl.getGlobalBoost());
		}
		
		if(expnew > 0){
			event.setExpGain(expnew);
			
			if (Main.getPlugin().getConfig().getBoolean("settings.heroes.msg.enabled")){
				String message = Main.getPlugin().getConfig().getString("settings.heroes.msg.msg");
			    message = message.replaceAll("%newexp%", expnew+"");
			    message = message.replaceAll("%oldexp%", exp+"");
			    message = message.replaceAll("%source%", skill+"");
			    MainAPI.sendMessage(message, player);
		    }     
		}
		
	}

}
