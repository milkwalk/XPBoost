package cz.dubcat.xpboost.constructors;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.InternalXPBoostAPI;
import lombok.Data;

@Data
public class GlobalBoost {

    private boolean enabled;
    private double boost;
    private long time = 0;

    public GlobalBoost() {
        this.enabled = false;
        this.boost = XPBoostMain.getPlugin().getConfig().getDouble("settings.globalboost.multiplier");
    }

    public double getGlobalBoost() {
        if (time != 0) {
            if (System.currentTimeMillis() > time) {
                enabled = false;
                time = 0;
                InternalXPBoostAPI.debug("Global boost has been disabled, because time has run out.", Debug.ALL);
            }

        }
        
        return this.boost;
    }

    public void setTime(int time) {
        long new_time = System.currentTimeMillis() + time * 1000;
        this.time = new_time;
    }

}
