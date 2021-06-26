package cz.dubcat.xpboost.api;

import java.util.UUID;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.constructors.Debug;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;

/**
 * @author Qifi
 *
 */
public class XPBoostAPI {

    /**
     * Method for checking whether player has any XPBoost at all. Works only for
     * online players
     * 
     * @param uuid
     * @return boolean
     */
    public static boolean hasBoost(UUID uuid) {
        return XPBoostMain.allplayers.containsKey(uuid);
    }

    /**
     * Method for getting player's XPBoost
     * 
     * @param uuid
     * @return XPBoost Returns existing XPBoost object for the player or null if boost doesn't exist
     */
    public static XPBoost getBoost(UUID uuid) {
        return XPBoostMain.allplayers.get(uuid);
    }

    /**
     * NOTE: If EXP multiplier is the same the duration of the existing boost will
     * be extended, given that the player already has existing boost.
     * 
     * @param uuid
     * @param boost
     *            The multiplier of the boost
     * @param duration
     *            Duration of the boost in seconds
     * @return XPBoost Returns created XPBoost object or modifies existing one
     */
    public static XPBoost setPlayerBoost(UUID uuid, double boost, int duration) {
        XPBoost xpb = getBoost(uuid);
        if (xpb != null) {
            if (xpb.getBoost() == boost) {
                xpb.addTime(duration * 1000);
                InternalXPBoostAPI.debug("Adding boost of " + boost + "x for UUID " + uuid + " endTime: " + (duration * 1000),
                        Debug.NORMAL);
                InternalXPBoostAPI.debug("Player's boost time has been extended UUID " + xpb.getUuid(), Debug.NORMAL);
                return xpb;
            }

            xpb.setBoost(boost);
            xpb.setEndtime(System.currentTimeMillis() + duration * 1000);
            InternalXPBoostAPI.debug("Setting boost of " + boost + "x for UUID " + uuid + " endTime: " + xpb.getEndtime(),
                    Debug.NORMAL);
        } else {
            xpb = new XPBoost(uuid, boost, (System.currentTimeMillis() + duration * 1000));
            XPBoostMain.allplayers.put(uuid, xpb);
            InternalXPBoostAPI.debug("Creating new boost " + boost + "x for UUID " + uuid, Debug.NORMAL);
        }

        return xpb;
    }

    /**
     * Mehtod for getting global boost
     * 
     * @return GlobalBoost The global boost object
     */
    public static GlobalBoost getGlobalBoost() {
        return XPBoostMain.GLOBAL_BOOST;
    }

    /**
     * Getting XPBoost of offline player. This should not be used if player is
     * online.
     * 
     * @param uuid
     * @return XPBoost object for offline players, if boost doesnt exist null is
     *         returned.
     */
    public static XPBoost getOfflineBoost(UUID uuid) {
        return InternalXPBoostAPI.loadPlayer(uuid);
    }

}
