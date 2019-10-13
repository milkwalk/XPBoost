package cz.dubcat.xpboost.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import cz.dubcat.xpboost.XPBoostMain;

public class XpBoostItemListener_1_8_R3 implements Listener {
    
    @EventHandler
    public void rightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            @SuppressWarnings("deprecation")
            ItemStack item = player.getItemInHand();
            
            if (item != null && item.getType() == Material.getMaterial(XPBoostMain.getPlugin().getConfig().getString("settings.itemmaterial"))) {
                new XpBoostItemListener().processPlayerInteractEvent(event);
            }
        }
    }
}
