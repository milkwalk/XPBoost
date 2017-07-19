package cz.dubcat.xpboost.constructors;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import com.massivecraft.factions.entity.Faction;

import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;

public class XPBoost{
	
	private UUID uiid;
	private double boost  =1;
	private long endtime;
	private ConcurrentHashMap<Condition, Boolean> conditions = new ConcurrentHashMap<Condition, Boolean>();
	private int boostTime;
	private Faction faction;

	
	public XPBoost(UUID id, double boost, long endtime){
		this.uiid = id;	
		this.boost = boost;		
		this.endtime = endtime;	
		this.boostTime = (int) ((endtime - System.currentTimeMillis())/1000);
	}
	
	public XPBoost(Faction faction, double boost, long endtime){
		this.faction = faction;
		this.boost = boost;		
		this.endtime = endtime;	
		this.boostTime = (int) ((endtime - System.currentTimeMillis())/1000);
	}
	
	//get uuid
	public UUID getUUID(){
		return this.uiid;
	}
	
	//get boost
	public double getBoost(){
		return this.boost;
	}
	
	
	//add time to an existing boost
	public void addTimeToBoost(long time){	
			this.endtime += time;
	}
	
	//add time to an existing boost
	public void substractTimeFromBoost(long time){	
		this.endtime -= time;
	}
	
	//add time to an existing boost
	public void addBoost(double boost, long time){
		
		if(boost == this.boost)
			endtime += time;
		else
			setBoost(boost, time);
	}
	
	//set boost
	public void setBoost(double boost, long endtime){
		this.boost = boost;
		this.endtime = endtime;		
	}
	
	//Get time when boost ends in miliseconds
	public long getEndTime(){
		return this.endtime;
	}
	
	public Faction getFaction(){
		return this.faction;
	}
	
	//remove/reset boost
	public void clear(){
		this.boost  = 1.0D;
		this.endtime = 0L;
		this.conditions.clear();
	}
	
	//Get time remaining of the boost in seconds
	public double getTimeRemaining(){
		if(endtime  >= System.currentTimeMillis()){
			return ((endtime - System.currentTimeMillis())/1000);
		}else{			
			return 0;	
		}
	}
	
	//Add condition to the player
	public void putCondition(Condition condition, boolean bol){
		this.conditions.put(condition, bol);
	}
	
	//Check whether a player has a condition
	public boolean hasCondition(Condition condition){
		if(conditions.containsKey(condition)){
			return conditions.get(condition);
		}else{
			return true;
		}
	}
	
	//Get a name of a player >.<
	@Deprecated
	public String getName(){
		if(Bukkit.getServer().getPlayer(this.uiid) != null && Bukkit.getServer().getPlayer(this.uiid).isOnline()){
			return Bukkit.getServer().getPlayer(this.uiid).getName();
		}else{
			return Bukkit.getServer().getOfflinePlayer(this.uiid).getName();
		}
	}
	
	//RETURN A HASHMAP WITH A LIST OF ALL INCLUDED CONDITIONS
	public ConcurrentHashMap<Condition, Boolean> getConditions(){
		return this.conditions;
	}
	
	//DURATION OF THE BOOST IN SECONDS
	public int getBoostTime(){
		return this.boostTime;
	}
	
	public void clearCondition(){
		this.conditions.clear();
	}
	
	public void savePlayerFile(){
		MainAPI.saveOfflinePlayer(this.uiid, this);
	}
}
