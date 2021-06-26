package cz.dubcat.xpboost.gui;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.BoostAPI;
import cz.dubcat.xpboost.api.InternalXPBoostAPI;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.XPBoostInventoryHolder;

public class ShopClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof XPBoostInventoryHolder) {
            Player player = (Player) event.getWhoClicked();
            UUID playerUuid = player.getUniqueId();
            ItemStack clickedItem = event.getCurrentItem();
            int slot = event.getSlot();
            int i = -1;

            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                event.setCancelled(true);
                player.closeInventory();

                if (XPBoostAPI.hasBoost(playerUuid)) {
                    InternalXPBoostAPI.sendMessage(XPBoostMain.getLang().getString("lang.boostactive"), player);
                    return;
                }

                for (String boostId : XPBoostMain.boostCfg.getConfigurationSection("").getKeys(false)) {
                    if (XPBoostMain.boostCfg.getBoolean(boostId + ".enabled") == true) {
                        i++;
                        if (i == slot) {
                            new BoostAPI().buyBoost(player, boostId);
                        }
                    }
                }
            }
        }
    }
}
