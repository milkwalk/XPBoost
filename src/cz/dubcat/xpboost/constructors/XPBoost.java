package cz.dubcat.xpboost.constructors;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import com.massivecraft.factions.entity.Faction;

import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;

public class XPBoost{
	
	private UUID uiid;
	private double boost = 1;
	private long endtime;
	private ConcurrentHashMap<Condition, Boolean> conditions = new ConcurrentHashMap<Condition, Boolean>();
	private ConcurrentHashMap<String, Boolean> advancedOptions = new ConcurrentHashMap<String, Boolean>();
	private int boostTime;
	private Faction faction;
	
	public XPBoost(UUID id, double boost, long endtime, ConcurrentHashMap<Condition, Boolean> conditions){
		this(id, boost, endtime);
		this.conditions = conditions;
	}
	
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
	
	
	/* Player UUID */
	public UUID getUUID(){
		return uiid;
	}
	
	/* Boost */
	public double getBoost(){
		return boost;
	}
	
	
	/* Add time to boost (in milliseconds) */
	public void addTimeToBoost(long time){	
		endtime += time;
	}
	
	/* Substract time from boost (in milliseconds) */
	public void substractTimeFromBoost(long time){	
		endtime -= time;
	}
	
	/* Add time to boost (in milliseconds) 
	 * */
	public void addBoost(double boost, long time){
		
		if(boost == this.boost)
			endtime += time;
		else
			setBoost(boost, time);
	}
	
	/* Override boost */
	public void setBoost(double boost, long endtime){
		this.boost = boost;
		this.endtime = endtime;		
	}
	
	/* Time when boost will end (in milliseconds) */
	public long getEndTime(){
		return endtime;
	}
	
	public Faction getFaction(){
		return faction;
	}
	
	/* Remove/reset boost */
	public void clear(){
		boost  = 1.0D;
		endtime = 0L;
		conditions.clear();
	}
	
	/* Get time remaining of the boost in seconds */
	public double getTimeRemaining(){
		if(endtime  >= System.currentTimeMillis()){
			return ((endtime - System.currentTimeMillis())/1000);
		}else{			
			return 0;	
		}
	}
	
	/* Add condition */
	public void putCondition(Condition condition, boolean bol){
		this.conditions.put(condition, bol);
	}
	
	/* Check condition */
	public boolean hasCondition(Condition condition){
		if(conditions.containsKey(condition)){
			return conditions.get(condition);
		}else{
			return true;
		}
	}
	
	@Deprecated
	public String getName(){
		if(Bukkit.getServer().getPlayer(this.uiid) != null && Bukkit.getServer().getPlayer(this.uiid).isOnline()){
			return Bukkit.getServer().getPlayer(this.uiid).getName();
		}else{
			return Bukkit.getServer().getOfflinePlayer(this.uiid).getName();
		}
	}
	
	/* HashMap of all conditions */
	public ConcurrentHashMap<Condition, Boolean> getConditions(){
		return this.conditions;
	}
	
	/* Duration of the boost in seconds */
	public int getBoostTime(){
		return boostTime;
	}
	
	public void clearConditions(){
		conditions.clear();
	}
	
	/* Save current boost to a file */
	public void savePlayerFile(){
		MainAPI.saveOfflinePlayer(this.uiid, this);
	}
}
