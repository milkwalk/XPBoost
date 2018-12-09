package cz.dubcat.xpboost.events;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.Debug;

public class ClickListener_18 implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public void rightClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

            Player player = event.getPlayer();
            if (player.getItemInHand() != null && player.getItemInHand().getType() == Material
                    .getMaterial(XPBoostMain.getPlugin().getConfig().getString("settings.itemmaterial"))) {
                ItemStack item = player.getItemInHand();
                if (item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null
                        && item.getItemMeta().getLore() != null) {
                    String nazev = item.getItemMeta().getDisplayName();
                    List<String> lore = item.getItemMeta().getLore();

                    int i = 0;
                    double boost = 0;
                    Integer time = 0;

                    try {
                        for (String l : lore) {
                            String str = MainAPI.stripColours(l);
                            str = str.replaceAll("[^\\d.]", "");
                            if (i == 0) {
                                boost = Double.parseDouble(str);
                            } else if (i == 1) {
                                time = Integer.parseInt(str);
                                break;
                            }
                            i++;
                        }
                    } catch (NumberFormatException ex) {
                        MainAPI.debug("Cannot recognise XPBoost item.", Debug.NORMAL);
                        return;
                    }

                    String name = MainAPI.colorizeText(XPBoostMain.getLang().getString("lang.itemname")
                            .replace("%boost%", String.valueOf(boost)).replace("%time%", String.valueOf(time)));

                    if (nazev.equals(name) || nazev.contains(MainAPI.stripColours(name)) || nazev.contains(name)
                            || nazev.equals(name)) {

                        if (XPBoostAPI.hasBoost(player.getUniqueId())) {
                            MainAPI.sendMessage(XPBoostMain.getLang().getString("lang.boostactive"), player);
                            event.setCancelled(true);
                            return;
                        }

                        XPBoostAPI.setPlayerBoost(player.getUniqueId(), boost, time);

                        MainAPI.sendMessage(XPBoostMain.getLang().getString("lang.xpbuy").replace("%boost%", "" + boost)
                                .replace("%time%", "" + time).replace("%money%", ""), player);

                        player.setItemInHand(null);

                        event.setCancelled(true);

                    }
                }

            }

        }
    }

}
