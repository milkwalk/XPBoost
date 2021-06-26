package cz.dubcat.xpboost.tasks;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.InternalXPBoostAPI;
import cz.dubcat.xpboost.constructors.XPBoost;

public class ActionBarTask extends BukkitRunnable {
    private final String reminderOptionsPath = "settings.activeBoostReminderOptions";
    
    @Override
    public void run() {
        if (XPBoostMain.getPlugin().getConfig().getBoolean(reminderOptionsPath + ".enabled")) {
            Map<UUID, XPBoost> mp = XPBoostMain.allplayers;
            Iterator<Entry<UUID, XPBoost>> it = mp.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<UUID, XPBoost> pair = it.next();
                XPBoost xpb = pair.getValue();

                if (xpb.getTimeRemaining() > 0) {
                    String message = InternalXPBoostAPI.colorizeText(XPBoostMain.getLang().getString("lang.actionbar")
                            .replaceAll("%boost%", String.valueOf(xpb.getBoost()))
                            .replaceAll("%timeleft%", String.valueOf((int) xpb.getTimeRemaining())));
                    
                    Bukkit.getScheduler().scheduleSyncDelayedTask(XPBoostMain.getPlugin(), () -> {
                        XPBoostMain.getPlugin().getExperienceNotifier().reminderNotification(Bukkit.getPlayer(pair.getKey()), message);
                    });
                }
            }
        }
    }

}
