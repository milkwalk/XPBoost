package cz.dubcat.xpboost.constructors;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import cz.dubcat.xpboost.api.MainAPI.Condition;

public class XPBoost {
	
	private UUID uiid;
	private double boost  =1;
	private long endtime;
	private String name;
	private ConcurrentHashMap<Condition, Boolean> conditions = new ConcurrentHashMap<Condition, Boolean>();
	private int boostTime;

	
	public XPBoost(Player p, double boost, long endtime){
		this.uiid = p.getUniqueId();
		this.boost = boost;
		this.endtime = endtime;
		this.name = p.getName();
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
	
	//remove/reset boost
	public void clear(){
		this.boost  = 1;
		this.endtime = 0;
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
	public String getName(){
		return this.name;
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
}
