package cz.dubcat.xpboost.tasks;

import java.util.Calendar;

import org.bukkit.scheduler.BukkitRunnable;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.constructors.GlobalBoost;

public class BoostTaskCheck extends BukkitRunnable {

    @Override
    public void run() {
        GlobalBoost gl = XPBoostMain.GLOBAL_BOOST;

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        gl.setEnabled(false);

        switch (day) {
        case 2:
            if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.day.monday"))
                gl.setEnabled(true);
            break;
        case 3:
            if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.day.tuesday"))
                gl.setEnabled(true);
            break;
        case 4:
            if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.day.wednesday"))
                gl.setEnabled(true);
            break;
        case 5:
            if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.day.thursday"))
                gl.setEnabled(true);
            break;
        case 6:
            if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.day.friday"))
                gl.setEnabled(true);
            break;
        case 7:
            if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.day.saturday"))
                gl.setEnabled(true);
            break;
        case 1:
            if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.day.sunday"))
                gl.setEnabled(true);
            break;
        }
    }

}
