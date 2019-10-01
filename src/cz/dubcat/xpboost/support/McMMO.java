package cz.dubcat.xpboost.support;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;

public class McMMO implements Listener {

    private static GlobalBoost gl = XPBoostMain.GLOBAL_BOOST;
    private static final Condition CONDITION_NAME = Condition.MCMMO;

    private XPBoostMain plugin;
    private boolean expbug = false;

    public McMMO(XPBoostMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void gainXp(McMMOPlayerXpGainEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        String enumSkillname = null;
        String skillName = null;
        try {
            Object skillType = event.getClass().getMethod("getSkill").invoke(event);
            skillName = (String) skillType.getClass().getMethod("getName").invoke(skillType);
            enumSkillname = (String) skillType.getClass().getMethod("name").invoke(skillType);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
            return;
        }
        if(enumSkillname == null || skillName == null) {
            return;
        }
        int exp = (int) event.getRawXpGained();
        int expnew = 0;
        if (expbug) {
            return;
        }

        if (XPBoostAPI.hasBoost(id)) {
            XPBoost xpb = XPBoostAPI.getBoost(id);
            if (xpb.hasCondition(CONDITION_NAME)) {
                if (xpb.getAdvancedOptions().containsKey("MCMMO")) {
                    if (xpb.getAdvancedOptions().get("MCMMO").isAllowedType(enumSkillname)) {
                        expnew += (int) Math.round(exp * xpb.getBoost());
                    }
                } else {
                    expnew += (int) Math.round(exp * xpb.getBoost());
                }
            }
        }

        if (gl.isEnabled()) {
            expnew += (int) Math.round(exp * gl.getGlobalBoost());
        }

        if (expnew > 0) {
            expbug = true;
            ExperienceAPI.addXP(player, enumSkillname, expnew, "UNKNOWN");
            expbug = false;

            if (plugin.getConfig().getBoolean("settings.mcmmo.msg.enabled")) {
                String message = plugin.getConfig().getString("settings.mcmmo.msg.msg");
                message = message.replaceAll("%newexp%", expnew + "");
                message = message.replaceAll("%oldexp%", exp + "");
                message = message.replaceAll("%skill%", skillName);
                XPBoostMain.getPlugin().getExperienceNotifier().experienceGainedNotification(player, message);
            }
        }
    }

}
