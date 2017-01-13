package cz.dubcat.xpboost.constructors;

import cz.dubcat.xpboost.Main;

public class GlobalBoost {
	
	private boolean enabled;
	private double boost;
	
	public GlobalBoost(){
		this.enabled = false;
		this.boost = Main.getPlugin().getConfig().getDouble("settings.globalboost.multiplier");
	}
	
	public boolean isEnabled(){
		return this.enabled;
	}
	
	public double getGlobalBoost(){
		return this.boost;
	}
	
	public void setBoost(double boost){
		this.boost = boost; 
	}
	
	public void setEnabled(boolean bol){
		this.enabled = bol;
	}
}
