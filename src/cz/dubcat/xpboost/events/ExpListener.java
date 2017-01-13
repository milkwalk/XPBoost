package cz.dubcat.xpboost.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.xpbAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.MainAPI.Debug;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;

public class ExpListener implements Listener{

	private static GlobalBoost gl = Main.GLOBAL_BOOST;
	private static Condition CONDITION_NAME = Condition.VANILLA;
	
	@EventHandler
    public void onExpChange(PlayerExpChangeEvent event){
		
		if(Main.getPlugin().getConfig().getBoolean("settings.disablevanillaxp")){
			return;
		}	
		
        Player player = event.getPlayer();
        
        int exp = event.getAmount();
        UUID id = player.getUniqueId(); 
        int expnew = 0;
		
		if(xpbAPI.hasBoost(id)){			
			XPBoost xpb = xpbAPI.getBoost(id);
			if(xpb.hasCondition(CONDITION_NAME))
				expnew =  (int) Math.round(exp * xpb.getBoost());
		}
		
		if(gl.isEnabled()){
			expnew = (int) Math.round(exp * gl.getGlobalBoost());
		}
		
		event.setAmount(expnew);
		
		MainAPI.debug("Player " + player.getName() + " got " + expnew + " XP instead of " + exp + " XP (" + CONDITION_NAME+ ")" , Debug.NORMAL);
		
		if (Main.getPlugin().getConfig().getBoolean("settings.doublexpmsg")){
			String message = Main.getPlugin().getConfig().getString("lang.doublexpnot")
					.replaceAll("%newexp%", expnew+"")
		    		.replaceAll("%oldexp%", exp+"");
			
			MainAPI.sendMSG(message, player);
	    }
    }
}
