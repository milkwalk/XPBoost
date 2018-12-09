package cz.dubcat.xpboost.tasks;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.constructors.Database;
import cz.dubcat.xpboost.constructors.Database.DType;
import cz.dubcat.xpboost.constructors.DbUtils;
import cz.dubcat.xpboost.constructors.Debug;
import cz.dubcat.xpboost.constructors.XPBoost;

public class XPBoostTask extends BukkitRunnable {

    private static String MESSAGE;

    public XPBoostTask() {
        MESSAGE = XPBoostMain.getLang().getString("lang.boostfisnish");
    }

    @Override
    public void run() {
        ConcurrentHashMap<UUID, XPBoost> mp = XPBoostMain.allplayers;
        Iterator<Entry<UUID, XPBoost>> it = mp.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<UUID, XPBoost> pair = it.next();
            XPBoost xpb = pair.getValue();

            if (xpb.getTimeRemaining() <= 0) {

                // SEND MESSAGE
                Bukkit.getScheduler().scheduleSyncDelayedTask(XPBoostMain.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getPlayer(xpb.getUUID()).playSound(Bukkit.getPlayer(xpb.getUUID()).getLocation(),
                                Sound.BLOCK_TRIPWIRE_CLICK_OFF, 5f, 5f);
                        MainAPI.sendMessage(MESSAGE, xpb.getUUID());
                    }
                });

                if (Database.type == DType.FILE) {
                    File file = MainAPI.setPlayerFile(xpb.getUUID());
                    file.delete();
                } else {
                    PreparedStatement ps = null;
                    try {
                        // readding new value
                        ps = Database.getConnection().prepareStatement("DELETE FROM xpboost WHERE uuid=?");
                        ps.setString(1, xpb.getUUID().toString());
                        ps.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DbUtils.closeQuietly(ps);
                    }
                }

                MainAPI.debug("Removed boost from UUID " + xpb.getUUID(), Debug.NORMAL);
                mp.remove(xpb.getUUID());
            }
        }
    }

}
