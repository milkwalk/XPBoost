package cz.dubcat.xpboost.constructors;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;

public class GlobalBoost {

	private boolean enabled;
	private double boost;
	private long time = 0;

	public GlobalBoost() {
		this.enabled = false;
		this.boost = Main.getPlugin().getConfig().getDouble("settings.globalboost.multiplier");
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public double getGlobalBoost() {

		if (time != 0) {
			if (System.currentTimeMillis() > time) {
				enabled = false;
				time = 0;
				MainAPI.debug("Global boost has been disabled, because time has run out.", Debug.ALL);
			}

		}

		return this.boost;
	}

	public void setBoost(double boost) {
		this.boost = boost;
	}

	public void setEnabled(boolean bol) {
		this.enabled = bol;
	}

	public long getTime() {

		return time;
	}

	public void setTime(int time) {
		long new_time = System.currentTimeMillis() + time * 1000;

		this.time = new_time;
	}

}
