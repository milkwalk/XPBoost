package cz.dubcat.xpboost.constructors;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.massivecraft.factions.entity.Faction;

import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;

public class XPBoost {

    private UUID uiid;
    private double boost = 1;
    private long endtime;
    private ConcurrentHashMap<Condition, Boolean> conditions = new ConcurrentHashMap<Condition, Boolean>();
    private ConcurrentHashMap<String, BoostOptions> advancedOptions = new ConcurrentHashMap<String, BoostOptions>();
    private int boostTime;
    private Faction faction;

    public XPBoost(UUID id, double boost, long endtime, ConcurrentHashMap<Condition, Boolean> conditions) {
        this(id, boost, endtime);
        this.conditions = conditions;
    }

    public XPBoost(UUID id, double boost, long endtime) {
        this.uiid = id;
        this.boost = boost;
        this.endtime = endtime;
        this.boostTime = (int) ((endtime - System.currentTimeMillis()) / 1000);
    }

    public XPBoost(Faction faction, double boost, long endtime) {
        this.faction = faction;
        this.boost = boost;
        this.endtime = endtime;
        this.boostTime = (int) ((endtime - System.currentTimeMillis()) / 1000);
    }

    /* Player UUID */
    public UUID getUUID() {
        return uiid;
    }

    /* Boost */
    public double getBoost() {
        return boost;
    }

    public void setEndTime(long time) {
        endtime = time;
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
     * @param boost
     *            Boost
     */
    public void setBoost(double boost) {
        this.boost = boost;
    }

    /**
     * @return long Time when boost will end in milliseconds
     */
    public long getEndTime() {
        return endtime;
    }

    public Faction getFaction() {
        return faction;
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

    /* HashMap of all conditions */
    public ConcurrentHashMap<Condition, Boolean> getConditions() {
        return this.conditions;
    }

    /* Duration of the boost in seconds */
    public int getBoostTime() {
        return boostTime;
    }

    public void clearConditions() {
        conditions.clear();
    }

    public ConcurrentHashMap<String, BoostOptions> getAdvancedOptions() {
        return advancedOptions;
    }

    /* Save current boost */
    public void saveBoost() {
        MainAPI.savePlayer(this.uiid);
    }
}
