package cz.dubcat.xpboost.support;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gamingmesh.jobs.api.JobsExpGainEvent;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.Condition;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;

public class JobsReborn implements Listener {

    private static GlobalBoost gl = XPBoostMain.GLOBAL_BOOST;
    private static final Condition CONDITION_NAME = Condition.JOBS;

    @EventHandler
    public void jobsExp(JobsExpGainEvent event) {
        Player player = (Player) event.getPlayer();

        String job = event.getJob().getName();
        UUID id = player.getUniqueId();

        double exp = event.getExp();
        double newExp = 0;

        if (XPBoostAPI.hasBoost(id)) {
            XPBoost xpb = XPBoostAPI.getBoost(id);
            if (xpb.hasCondition(CONDITION_NAME)) {
                if (xpb.getAdvancedOptions().containsKey("JOBS")) {
                    if (xpb.getAdvancedOptions().get("JOBS")
                            .isAllowedType(event.getJob().getShortName().toUpperCase())) {
                        newExp += (int) Math.round(exp * xpb.getBoost());
                    }
                } else {
                    newExp += Math.round(exp * xpb.getBoost());
                }
            }
        }

        if (gl.isEnabled()) {
            newExp += Math.round(exp * gl.getGlobalBoost());
        }

        if (newExp > 0) {
            event.setExp(newExp);

            if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.jobs.msg.enabled")) {
                String message = XPBoostMain.getPlugin().getConfig().getString("settings.jobs.msg.msg");
                message = message.replaceAll("%newexp%", String.valueOf(newExp));
                message = message.replaceAll("%oldexp%", String.valueOf(exp));
                message = message.replaceAll("%job%", job);
                XPBoostMain.getPlugin().getExperienceNotifier().experienceGainedNotification(player, message);
            }
        }
    }

}
