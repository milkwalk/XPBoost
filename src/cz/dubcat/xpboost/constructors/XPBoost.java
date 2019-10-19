package cz.dubcat.xpboost.constructors;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import cz.dubcat.xpboost.api.Condition;
import cz.dubcat.xpboost.api.MainAPI;
import lombok.Data;

@Data
public class XPBoost {

    private UUID uuid;
    private double boost = 1;
    private long endtime;
    private ConcurrentHashMap<Condition, Boolean> conditions = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, BoostOptions> advancedOptions = new ConcurrentHashMap<>();
    private int boostTime;

    public XPBoost(UUID id, double boost, long endtime, ConcurrentHashMap<Condition, Boolean> conditions) {
        this(id, boost, endtime);
        this.conditions = conditions;
    }

    public XPBoost(UUID id, double boost, long endtime) {
        this.uuid = id;
        this.boost = boost;
        this.endtime = endtime;
        this.boostTime = (int) ((endtime - System.currentTimeMillis()) / 1000);
    }

    /* Adds time to the boost (in milliseconds) */
    public void addTime(long time) {
        endtime += time;
    }

    /* Substracts time from the boost (in milliseconds) */
    public void substractTime(long time) {
        endtime -= time;
    }

    /**
     * This method overrides boost's boost and duration
     * 
     * @param boost Boost
     */
    public void setBoost(double boost) {
        this.boost = boost;
    }

    /* Remove/reset boost */
    public void clear() {
        boost = 1.0D;
        endtime = 0L;
        conditions.clear();
    }

    /* Get time remaining of the boost in seconds */
    public double getTimeRemaining() {
        if (endtime >= System.currentTimeMillis()) {
            return ((endtime - System.currentTimeMillis()) / 1000);
        } else {
            return 0;
        }
    }

    /* Add condition */
    public void putCondition(Condition condition, boolean bol) {
        this.conditions.put(condition, bol);
    }

    /* Check condition */
    public boolean hasCondition(Condition condition) {
        if (conditions.containsKey(condition)) {
            return conditions.get(condition);
        }

        return true;
    }

    public void clearConditions() {
        conditions.clear();
    }

    /* Save current boost */
    public void saveBoost() {
        MainAPI.savePlayer(this.uuid);
    }
}
