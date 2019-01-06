package cz.dubcat.xpboost.tasks;

import java.util.Calendar;

import org.bukkit.scheduler.BukkitRunnable;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.utils.DayUtil;

public class BoostTaskCheck extends BukkitRunnable {

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String stringDay = DayUtil.getDayOfTheWeek(day);
        XPBoostMain.GLOBAL_BOOST.setEnabled(XPBoostMain.getPlugin().getConfig().getBoolean("settings.day." + stringDay));
    }

}
