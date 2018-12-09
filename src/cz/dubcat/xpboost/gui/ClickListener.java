package cz.dubcat.xpboost.gui;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.BoostOptions;
import cz.dubcat.xpboost.constructors.XPBoost;

public class ClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().getName().equals(MainAPI.colorizeText(XPBoostMain.getLang().getString("lang.gui")))) {
            UUID id = player.getUniqueId();
            ItemStack clicked = event.getCurrentItem();
            int slot = event.getSlot();
            int i = -1;

            if (clicked != null && clicked.getType() != Material.AIR) {
                event.setCancelled(true);
                player.closeInventory();

                if (XPBoostAPI.hasBoost(id)) {
                    MainAPI.sendMessage(XPBoostMain.getLang().getString("lang.boostactive"), player);
                    return;
                }

                for (String key : XPBoostMain.boostCfg.getConfigurationSection("").getKeys(false)) {
                    if (XPBoostMain.boostCfg.getBoolean(key + ".enabled") == true) {
                        i++;
                        if (i == slot) {

                            if (XPBoostMain.factions_enabled && XPBoostMain.factions.getBoolean("settings.allow_one_boost_only")
                                    && XPBoostAPI.getFactionBoost(player) != null) {
                                MainAPI.sendMessage(XPBoostMain.getLang().getString("lang.factions_one_boost"), player);
                                return;
                            }

                            if (XPBoostMain.boostCfg.contains(key + ".permissions")) {
                                if (!player.hasPermission(
                                        XPBoostMain.boostCfg.getString(key + ".permissions.required_permission"))) {
                                    String message = XPBoostMain.boostCfg.getString(key + ".permissions.fail_message")
                                            .replaceAll("%perm%",
                                                    XPBoostMain.boostCfg.getString(key + ".permissions.required_permission"));
                                    MainAPI.sendMessage(message, player);
                                    return;
                                }
                            }

                            if (XPBoostMain.economy.has(player, XPBoostMain.boostCfg.getDouble(key + ".cost"))) {
                                int time = XPBoostMain.boostCfg.getInt(key + ".time");
                                double boost = XPBoostMain.boostCfg.getDouble(key + ".boost");

                                String message = XPBoostMain.getLang().getString("lang.xpbuy").replaceAll("%time%", time + "")
                                        .replaceAll("%money%", XPBoostMain.boostCfg.getString(key + ".cost"))
                                        .replaceAll("%boost%", String.valueOf(boost));
                                MainAPI.sendMessage(message, player);
                                XPBoostMain.economy.withdrawPlayer(player, XPBoostMain.boostCfg.getDouble(key + ".cost"));

                                XPBoost xpb = XPBoostAPI.setPlayerBoost(player.getUniqueId(), boost, time);

                                if (XPBoostMain.boostCfg.contains(key + ".behaviour")) {
                                    for (String cond : XPBoostMain.boostCfg.getConfigurationSection(key + ".behaviour")
                                            .getKeys(false)) {
                                        xpb.putCondition(Condition.valueOf(cond.toUpperCase()),
                                                XPBoostMain.boostCfg.getBoolean(key + ".behaviour." + cond));
                                    }
                                }

                                if (XPBoostMain.boostCfg.contains(key + ".advanced")) {

                                    for (String pluginName : XPBoostMain.boostCfg.getConfigurationSection(key + ".advanced")
                                            .getKeys(false)) {
                                        BoostOptions options = new BoostOptions(pluginName.toUpperCase());

                                        for (String option : XPBoostMain.boostCfg
                                                .getConfigurationSection(key + ".advanced." + pluginName)
                                                .getKeys(false)) {
                                            if (option.equalsIgnoreCase("default"))
                                                options.setEnabledByDefault(XPBoostMain.boostCfg
                                                        .getBoolean(key + ".advanced." + pluginName + "." + option));
                                            else
                                                options.getOptions().put(option.toUpperCase(), XPBoostMain.boostCfg
                                                        .getBoolean(key + ".advanced." + pluginName + "." + option));
                                        }

                                        xpb.getAdvancedOptions().put(pluginName.toUpperCase(), options);
                                    }
                                }

                            } else {
                                String message = XPBoostMain.getLang().getString("lang.buyfail");
                                message = message.replaceAll("%money%", XPBoostMain.boostCfg.getString(key + ".cost"));
                                MainAPI.sendMessage(message, player);
                            }

                            break;
                        }
                    }
                }

            }
        }
    }
}
