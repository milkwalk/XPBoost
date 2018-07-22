package cz.dubcat.xpboost.api;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.constructors.Debug;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;

/**
 * @author Qifi
 *
 */
public class XPBoostAPI {
	
	
	/**
	 * Method for checking whether player has any XPBoost at all.
	 * Works only for online players
	 * @param uuid
	 * @return boolean 
	 */
	public static boolean hasBoost(UUID uuid){					
		return Main.allplayers.containsKey(uuid);	
	}
	
	
	/**
	 * Method for getting player's XPBoost objects
	 * @param uuid
	 * @return XPBoost Returns existing XPBoost object of the player or null f
	 * deosn't exist
	 */
	public static XPBoost getBoost(UUID uuid){
		if(hasBoost(uuid)){
			return Main.allplayers.get(uuid);
		}else{
			return null;
		}
	}
	
	
	/**
	 * NOTE: If EXP multiplier is the same the duration of the existing boost will be extended, given that the player already has existing boost.
	 * @param uuid
	 * @param boost The multiplier of the boost
	 * @param duration Duration of the boost in seconds
	 * @return XPBoost Returns created XPBoost object or modifies existing one
	 */
	public static XPBoost setPlayerBoost(UUID uuid, double boost, int duration){
		XPBoost xpb = getBoost(uuid);
		if(xpb != null){
			if(xpb.getBoost() == boost){
				xpb.addTime(duration*1000);
				MainAPI.debug("Adding boost of "+boost +"x for UUID " + uuid +" endTime: " + (duration*1000) , Debug.NORMAL);
				MainAPI.debug("Player's boost time has been extended UUID " + xpb.getUUID(), Debug.NORMAL);
				return xpb;
			}
			
			xpb.setBoost(boost);
			xpb.setEndTime(System.currentTimeMillis() + duration*1000);
			MainAPI.debug("Setting boost of "+boost +"x for UUID " + uuid +" endTime: " + xpb.getEndTime(), Debug.NORMAL);
		}else{
			xpb = new XPBoost(uuid, boost, (System.currentTimeMillis() + duration*1000));
			Main.allplayers.put(uuid, xpb);
			MainAPI.debug("Creating new boost "+boost +"x for UUID " + uuid , Debug.NORMAL);
		}
		
		return xpb;
	}
	
	/**
	 * Mehtod for getting global boost
	 * @return GlobalBoost The global boost object
	 */
	public static GlobalBoost getGlobalBoost(){
		return Main.GLOBAL_BOOST;
	}
	
	
	/**
	 * Getting XPBoost of offline player. This should not be used if player is online.
	 * @param uuid
	 * @return XPBoost object for offline players, if boost doesnt exist null is returned.
	 */
	public static XPBoost getOfflineBoost(UUID uuid){	
		return MainAPI.loadPlayer(uuid);
	}
	
	public static boolean hasFactionBoost(Faction faction){
		if(faction.isNone())
			return false;
		
		if(Main.factions_boost.containsKey(faction))
			return true;
		else
			return false;
	}
	
	public static XPBoost getFactionBoost(Player player){
		if(!Main.factions_enabled || MPlayer.get(player).getFaction().isNone() || !hasFactionBoost(MPlayer.get(player).getFaction()) || (Main.factions.getBoolean("settings.allow_one_boost_only") && hasBoost(player.getUniqueId())))
			return null;
		
		return Main.factions_boost.get(MPlayer.get(player).getFaction());
		
	}
	
	public static XPBoost getFactionBoost(Faction faction){
		if(!Main.factions_enabled || faction.isNone() || !hasFactionBoost(faction))
			return null;
		
		return  Main.factions_boost.get(faction);
	}

	public static void setFactionBoost(Faction f, double boost, int time){
		if(f.isNone())
			return;
		
		XPBoost newxpb = new XPBoost(f, boost, (System.currentTimeMillis() + time*1000));
		Main.factions_boost.put(f, newxpb);
		MainAPI.debug("Creating new boost "+boost +"x for Faction " + f.getName() , Debug.NORMAL);
	}

}
