package cz.dubcat.xpboost.tasks;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.constructors.XPBoost;
import cz.dubcat.xpboost.utils.ActionBar;

public class ActionBarTask extends BukkitRunnable {
    private ActionBar actionBarUtil;
    
    public ActionBarTask() {
        actionBarUtil = new ActionBar();
    }
    
    @Override
    public void run() {
        if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.actionbarmsg")) {
            Map<UUID, XPBoost> mp = XPBoostMain.allplayers;
            Iterator<Entry<UUID, XPBoost>> it = mp.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<UUID, XPBoost> pair = it.next();
                XPBoost xpb = pair.getValue();

                if (xpb.getTimeRemaining() > 0) {
                    String message = MainAPI.colorizeText(XPBoostMain.getLang().getString("lang.actionbar")
                            .replaceAll("%boost%", String.valueOf(xpb.getBoost()))
                            .replaceAll("%timeleft%", String.valueOf((int) xpb.getTimeRemaining())));
                    
                    Bukkit.getScheduler().scheduleSyncDelayedTask(XPBoostMain.getPlugin(), () -> {
                        actionBarUtil.sendActionBar(Bukkit.getPlayer(pair.getKey()), message);
                    });
                }
            }
        }
    }

}
