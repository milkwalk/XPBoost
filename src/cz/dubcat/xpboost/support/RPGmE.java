package cz.dubcat.xpboost.support;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.Condition;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;
import net.flamedek.rpgme.events.SkillExpGainEvent;

public class RPGmE implements Listener {

    private static GlobalBoost gl = XPBoostMain.GLOBAL_BOOST;
    private static final Condition CONDITION_NAME = Condition.RPGME;

    @EventHandler
    public void expEvent(SkillExpGainEvent e) {

        Player player = e.getPlayer();

        String skill = e.getSkill().toString();
        UUID id = player.getUniqueId();

        float exp = e.getExp();
        float expnew = 0;

        if (XPBoostAPI.hasBoost(id)) {
            XPBoost xpb = XPBoostAPI.getBoost(id);
            if (xpb.hasCondition(CONDITION_NAME)) {
                if (xpb.getAdvancedOptions().containsKey("RPGME")) {
                    if (xpb.getAdvancedOptions().get("RPGME").isAllowedType(e.getSkill().name())) {
                        expnew += (int) Math.round(exp * xpb.getBoost());
                    }
                } else {
                    expnew += Math.round(exp * xpb.getBoost());
                }
            }
        }

        if (gl.isEnabled()) {
            expnew += Math.round(exp * gl.getGlobalBoost());
        }

        if (expnew > 0) {
            e.setExp(expnew);

            if (!skill.equals("STAMINA")) {
                if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.rpgme.msg.enabled")) {
                    String message = XPBoostMain.getPlugin().getConfig().getString("settings.rpgme.msg.msg");
                    message = message.replaceAll("%newexp%", expnew + "");
                    message = message.replaceAll("%oldexp%", exp + "");
                    message = message.replaceAll("%skill%", skill + "");
                    XPBoostMain.getPlugin().getExperienceNotifier().experienceGainedNotification(player, message);
                }
            }
        }
    }
}
