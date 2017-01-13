package cz.dubcat.xpboost;

import java.util.Calendar;

import org.bukkit.scheduler.BukkitRunnable;

import cz.dubcat.xpboost.constructors.GlobalBoost;

public class BoostTaskCheck extends BukkitRunnable{

	@Override
	public void run() {
		GlobalBoost gl = Main.GLOBAL_BOOST;
    	
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        
        if (Main.getPlugin().getConfig().getBoolean("settings.day.monday") && day == 2){
        	gl.setEnabled(true);
        }
        
        if (Main.getPlugin().getConfig().getBoolean("settings.day.tuesday")&& day == 3){
        	gl.setEnabled(true);
        }
        
    	if (Main.getPlugin().getConfig().getBoolean("settings.day.wednesday") && day == 4){
        	gl.setEnabled(true);
    	}
    	
    	if (Main.getPlugin().getConfig().getBoolean("settings.day.thursday") && day == 5){
        	gl.setEnabled(true);
    	}
    	
    	if (Main.getPlugin().getConfig().getBoolean("settings.day.friday") && day == 6){
        	gl.setEnabled(true);
    	}

    	if (Main.getPlugin().getConfig().getBoolean("settings.day.saturday") && day == 7){
        	gl.setEnabled(true);
    	} 
    	
    	if (Main.getPlugin().getConfig().getBoolean("settings.day.sunday") && day == 1){
        	gl.setEnabled(true);
    	}
		
	}

}
