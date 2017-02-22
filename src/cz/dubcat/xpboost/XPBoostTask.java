package cz.dubcat.xpboost;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.constructors.XPBoost;

public class XPBoostTask extends BukkitRunnable{
	
	public int id;

	public void setId(int id){
		 this.id = id;	
	}
		

	@Override
	public void run() {
    	ConcurrentHashMap<UUID, XPBoost> mp = Main.allplayers;
    	Iterator<Entry<UUID, XPBoost>> it = mp.entrySet().iterator();    	
    	
    	while (it.hasNext()) {      			
			Map.Entry<UUID, XPBoost> pair = it.next();
			XPBoost xpb = pair.getValue();

			if(xpb.getTimeRemaining() == 0){
    	    	String message = Main.getLang().getString("lang.boostfisnish");
	    	
    	    	//SEND MESSAGE
    	    	Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
		    		@Override
		    		public void run() {
		    	    	MainAPI.sendMSG(message, xpb.getUUID());	
		    		}
		    	}, 0L);
		    	
				mp.remove(xpb.getUUID());
			}
    	}		
	}

}
