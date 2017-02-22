package cz.dubcat.xpboost.support;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.PlayerExperienceGainEvent;
import com.sucy.skill.api.player.PlayerData;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.xpbAPI;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;

public class SkillApi implements Listener{	
	
	private static GlobalBoost gl = Main.GLOBAL_BOOST;
	private static Condition CONDITION_NAME = Condition.SKILLAPI;
	
    private boolean expbug = false;
    
    @EventHandler
    public void onExpGain(PlayerExperienceGainEvent event) {
		Player player = event.getPlayerData().getPlayer();
		UUID id = player.getUniqueId();
    		
   		 double exp = event.getExp();
   		 double expnew = 0;
   		 
   		 if (expbug) return;
   		 
   		if(xpbAPI.hasBoost(id)){			
   			XPBoost xpb = xpbAPI.getBoost(id);
   			if(xpb.hasCondition(CONDITION_NAME))
   				expnew =  Math.round(exp * xpb.getBoost());
			else
				return;
   		}
   		
   		if(gl.isEnabled()){
   			expnew += Math.round(exp * gl.getGlobalBoost());
   		}
   		
   		if(expnew > 0){
	   		expbug = true;
	   		PlayerData data = SkillAPI.getPlayerData(player);
	   		data.giveExp(expnew, event.getSource());
	   		expbug = false;
   		}
    }

}
