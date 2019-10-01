package cz.dubcat.xpboost.support;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;
import de.Keyle.MyPet.api.event.MyPetExpEvent;

public class MyPet implements Listener {
    
    private GlobalBoost globalBoost;
    private final Condition condition;
    
    public MyPet() {
        this.globalBoost = XPBoostMain.GLOBAL_BOOST;
        this.condition = Condition.MYPET;
    }
    
    @EventHandler
    public void petExperienceGain(MyPetExpEvent e) {
        Player player = e.getOwner().getPlayer();
        UUID uuid = e.getOwner().getPlayerUUID();
        double exp = e.getExp();
        double newExp = 0;
        
        if (XPBoostAPI.hasBoost(uuid)) {
            XPBoost xpb = XPBoostAPI.getBoost(uuid);

            if (xpb.hasCondition(condition)) {
                newExp += Math.round(exp * xpb.getBoost());
            }
        }
        
        if (globalBoost.isEnabled()) {
            newExp += Math.round(exp * globalBoost.getGlobalBoost());
        }
        
        if(newExp > 0) {
            e.setExp(newExp);
            if (XPBoostMain.getPlugin().getConfig().getBoolean("settings.mypet.msg.enabled")) {
                String message = XPBoostMain.getPlugin().getConfig().getString("settings.mypet.msg.msg");
                message = message.replaceAll("%newexp%", String.valueOf(newExp));
                message = message.replaceAll("%oldexp%", String.valueOf(exp));
                XPBoostMain.getPlugin().getExperienceNotifier().experienceGainedNotification(player, message);
            }
        }
    }
}
