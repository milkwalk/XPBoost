package cz.dubcat.xpboost.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.XPBoost;

public class FactionsClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory GUI = Bukkit.createInventory(null, 9,
                MainAPI.colorizeText(XPBoostMain.getLang().getString("lang.factions_gui_name")));

        if (event.getInventory().getName().equals(GUI.getName())) {

            ItemStack clicked = event.getCurrentItem();
            int slot = event.getSlot();
            int i = -1;

            if (clicked != null && clicked.getType() != Material.AIR) {
                event.setCancelled(true);
                player.closeInventory();

                MPlayer fplayer = MPlayer.get(player);

                Faction faction = MainAPI.getPlayerFaction(player);

                for (String key : XPBoostMain.getPlugin().getConfig().getConfigurationSection("boost").getKeys(false)) {
                    if (XPBoostMain.factions.contains("boost." + key + ".enabled")
                            && XPBoostMain.factions.getBoolean("boost." + key + ".enabled") == true) {
                        i++;
                        if (i == slot) {

                            if (XPBoostAPI.hasFactionBoost(faction)) {
                                MainAPI.sendMessage(XPBoostMain.getLang().getString("lang.faction_active_boost"), player);
                                return;
                            }

                            if (XPBoostMain.getPlugin().getConfig().getBoolean("boost." + key + ".permission.use")) {

                                if (!player.hasPermission(
                                        XPBoostMain.getPlugin().getConfig().getString("boost." + key + ".permission.perm"))) {
                                    String message = XPBoostMain.getPlugin().getConfig()
                                            .getString("boost." + key + ".permission.msg");
                                    message = message.replaceAll("%perm%", XPBoostMain.getPlugin().getConfig()
                                            .getString("boost." + key + ".permission.perm"));
                                    MainAPI.sendMessage(message, player);
                                    return;
                                }
                            }

                            if (XPBoostMain.factions.getBoolean("boost." + key + ".onlyowners")
                                    && !faction.getLeader().equals(fplayer)) {
                                MainAPI.sendMessage(XPBoostMain.getLang().getString("lang.factions_only_owners"), player);
                                return;
                            }

                            @SuppressWarnings("deprecation")
                            boolean money = XPBoostMain.economy.has(player.getName(),
                                    XPBoostMain.getPlugin().getConfig().getDouble("boost." + key + ".cost"));

                            if (money) {

                                int time = XPBoostMain.getPlugin().getConfig().getInt("boost." + key + ".time");

                                String message = XPBoostMain.getLang().getString("lang.factions_buy")
                                        .replaceAll("%player%", player.getName()).replaceAll("%time%", time + "")
                                        .replaceAll("%money%",
                                                XPBoostMain.getPlugin().getConfig().getString("boost." + key + ".cost"))
                                        .replaceAll("%boost%",
                                                XPBoostMain.getPlugin().getConfig().getString("boost." + key + ".boost"));

                                for (MPlayer p : faction.getMPlayers()) {
                                    if (Bukkit.getServer().getPlayer(p.getUuid()) != null
                                            && Bukkit.getServer().getPlayer(p.getUuid()).isOnline())
                                        MainAPI.sendMessage(message, p.getUuid());
                                }

                                XPBoostMain.economy.withdrawPlayer(player,
                                        XPBoostMain.getPlugin().getConfig().getDouble("boost." + key + ".cost"));

                                XPBoostAPI.setFactionBoost(faction,
                                        XPBoostMain.getPlugin().getConfig().getDouble("boost." + key + ".boost"),
                                        XPBoostMain.getPlugin().getConfig().getInt("boost." + key + ".time"));

                                XPBoost xpb = XPBoostAPI.getFactionBoost(faction);

                                if (XPBoostMain.getPlugin().getConfig().contains("boost." + key + ".behaviour")) {
                                    for (String cond : XPBoostMain.getPlugin().getConfig()
                                            .getConfigurationSection("boost." + key + ".behaviour").getKeys(false)) {
                                        xpb.putCondition(Condition.valueOf(cond.toUpperCase()), XPBoostMain.getPlugin()
                                                .getConfig().getBoolean("boost." + key + ".behaviour." + cond));
                                    }
                                }
                            } else {
                                String message = XPBoostMain.getLang().getString("lang.buyfail");
                                message = message.replaceAll("%money%",
                                        XPBoostMain.getPlugin().getConfig().getString("boost." + key + ".cost"));
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
