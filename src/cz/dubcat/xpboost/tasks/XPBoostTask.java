package cz.dubcat.xpboost.tasks;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.InternalXPBoostAPI;
import cz.dubcat.xpboost.constructors.Database;
import cz.dubcat.xpboost.constructors.Database.DType;
import cz.dubcat.xpboost.utils.DbUtils;
import cz.dubcat.xpboost.constructors.Debug;
import cz.dubcat.xpboost.constructors.XPBoost;

public class XPBoostTask extends BukkitRunnable {

    private static String MESSAGE;

    public XPBoostTask() {
        MESSAGE = XPBoostMain.getLang().getString("lang.boostfisnish");
    }

    @Override
    public void run() {
        Map<UUID, XPBoost> mp = XPBoostMain.allplayers;
        Iterator<Entry<UUID, XPBoost>> it = mp.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<UUID, XPBoost> pair = it.next();
            XPBoost xpb = pair.getValue();

            if (xpb.getTimeRemaining() <= 0) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(XPBoostMain.getPlugin(), () -> {
                        Bukkit.getPlayer(xpb.getUuid()).playSound(
                                Bukkit.getPlayer(xpb.getUuid()).getLocation(),
                                Sound.valueOf(XPBoostMain.getPlugin().getConfig().getString("settings.boostEndSound").toUpperCase()), 
                                5f, 5f);
                        InternalXPBoostAPI.sendMessage(MESSAGE, xpb.getUuid());
                });

                if (Database.getDatabaseType() == DType.FILE) {
                    File file = InternalXPBoostAPI.setPlayerFile(xpb.getUuid());
                    file.delete();
                } else {
                    PreparedStatement ps = null;
                    try (Connection conn = Database.getConnection()){
                        // readding new value
                        ps = conn.prepareStatement("DELETE FROM xpboost WHERE uuid=?");
                        ps.setString(1, xpb.getUuid().toString());
                        ps.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DbUtils.closeQuietly(ps);
                    }
                }

                InternalXPBoostAPI.debug("Removed boost from UUID " + xpb.getUuid(), Debug.NORMAL);
                mp.remove(xpb.getUuid());
            }
        }
    }

}
