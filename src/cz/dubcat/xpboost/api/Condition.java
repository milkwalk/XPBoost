package cz.dubcat.xpboost.api;

import cz.dubcat.xpboost.XPBoostMain;

public enum Condition {
    VANILLA, SKILLAPI, MCMMO, RPGME, HEROES, JOBS, MYPET;
    public static Condition[] CONDITIONS = new Condition[] { VANILLA, SKILLAPI, MCMMO, RPGME, HEROES, JOBS, MYPET };
    
    public String getConditionTranslation() {
        return XPBoostMain.getLang().getString("conditions." + this.name());
    }
}
