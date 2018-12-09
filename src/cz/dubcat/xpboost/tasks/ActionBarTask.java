package cz.dubcat.xpboost.tasks;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.constructors.XPBoost;

public class ActionBarTask extends BukkitRunnable {

    @Override
    public void run() {
        if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.actionbarmsg")) {
            ConcurrentHashMap<UUID, XPBoost> mp = XPBoostMain.allplayers;
            Iterator<Entry<UUID, XPBoost>> it = mp.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<UUID, XPBoost> pair = it.next();
                XPBoost xpb = pair.getValue();

                if (xpb.getTimeRemaining() > 0) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(XPBoostMain.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            XPBoostMain.getActionbar().sendActionBarMessage(Bukkit.getPlayer(xpb.getUUID()),
                                    MainAPI.colorizeText(XPBoostMain.getLang().getString("lang.actionbar")
                                            .replaceAll("%boost%", xpb.getBoost() + "")));
                        }
                    });
                }
            }
        }
    }

}
