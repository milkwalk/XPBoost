package cz.dubcat.xpboost.support;

import java.util.UUID;

import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.xpbAPI;
import cz.dubcat.xpboost.constructors.XPBoost;
import me.clip.placeholderapi.external.EZPlaceholderHook;

public class Placeholder extends EZPlaceholderHook {
	
    public Placeholder(Main ourPlugin) {
        super(ourPlugin, "xpboost");
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) {
            return "";
        }
    	
    	UUID id = p.getUniqueId();
    	
        
        if (identifier.equals("hasboost")) {
            return xpbAPI.hasBoost(id) ? "yes" : "no";
        }
    	
    	XPBoost xpb = xpbAPI.getBoost(id);
    	
    	if(xpb == null){
    		
            if(identifier.equals("boost_zero")){
            	return String.valueOf(0);
            }
            
            if (identifier.equals("timeleft_zero")) {
            	return String.valueOf(0);
            }
            
            if(identifier.equals("boost_time_zero")){
            	return String.valueOf(0);
            }
            
            return "";
    	}
    	
        if(identifier.equals("boost_zero")){
        	return String.valueOf(xpb.getBoost());
        }
        
        if (identifier.equals("timeleft_zero")) {
        	return String.valueOf(xpb.getTimeRemaining());
        }
        
        if(identifier.equals("boost_time_zero")){
        	return String.valueOf(xpb.getBoostTime());
        }
    	
        if (identifier.equals("timeleft")) {
        	return String.valueOf(xpb.getTimeRemaining());
        }
        
        if(identifier.equals("boost")){
        	return String.valueOf(xpb.getBoost());
        }
        
        if(identifier.equals("boost_time")){
        	return String.valueOf(xpb.getBoostTime());
        }
        
        return null;
    }

}
