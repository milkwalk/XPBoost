package cz.dubcat.xpboost;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Debug;
import cz.dubcat.xpboost.constructors.XPBoost;

public class XPBoostTask implements Runnable{
	
	public BukkitTask id;
	private static String MESSAGE;

	public void setId(BukkitTask id){
		 this.id = id;	
		 MESSAGE = Main.getLang().getString("lang.boostfisnish");
	}
		

	@Override
	public void run() {
    	ConcurrentHashMap<UUID, XPBoost> mp = Main.allplayers;
    	Iterator<Entry<UUID, XPBoost>> it = mp.entrySet().iterator();    	
    	
    	while (it.hasNext()) {      			
			Map.Entry<UUID, XPBoost> pair = it.next();
			XPBoost xpb = pair.getValue();

			if(xpb.getTimeRemaining() == 0){    	
    	    	//SEND MESSAGE
    	    	Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
		    		@Override
		    		public void run() {
		    	    	MainAPI.sendMessage(MESSAGE, xpb.getUUID());	
		    		}
		    	});
		    	
    	    	MainAPI.debug("Removed boost from UUID " + xpb.getUUID(), Debug.NORMAL);
				mp.remove(xpb.getUUID());
				
				File file = MainAPI.setPlayerFile(xpb.getUUID());
				file.delete();
			}
    	}		
	}

}
