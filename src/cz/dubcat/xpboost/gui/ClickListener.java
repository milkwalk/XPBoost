package cz.dubcat.xpboost.gui;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.BoostOptions;
import cz.dubcat.xpboost.constructors.XPBoost;

public class ClickListener implements Listener {

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		if (event.getInventory().getName().equals(MainAPI.colorizeText(Main.getLang().getString("lang.gui")))) {
			UUID id = player.getUniqueId();
			ItemStack clicked = event.getCurrentItem();
			int slot = event.getSlot();
			int i = -1;

			if (clicked != null && clicked.getType() != Material.AIR) {
				event.setCancelled(true);
				player.closeInventory();

				if (XPBoostAPI.hasBoost(id)) {
					MainAPI.sendMessage(Main.getLang().getString("lang.boostactive"), player);
					return;
				}

				for (String key : Main.boostCfg.getConfigurationSection("").getKeys(false)) {
					if (Main.boostCfg.getBoolean(key + ".enabled") == true) {
						i++;
						if (i == slot) {

							if (Main.factions_enabled && Main.factions.getBoolean("settings.allow_one_boost_only")
									&& XPBoostAPI.getFactionBoost(player) != null) {
								MainAPI.sendMessage(Main.getLang().getString("lang.factions_one_boost"), player);
								return;
							}

							if (Main.boostCfg.contains(key + ".permissions")) {
								if (!player.hasPermission(
										Main.boostCfg.getString(key + ".permissions.required_permission"))) {
									String message = Main.boostCfg.getString(key + ".permissions.fail_message")
											.replaceAll("%perm%",
													Main.boostCfg.getString(key + ".permissions.required_permission"));
									MainAPI.sendMessage(message, player);
									return;
								}
							}

							if (Main.economy.has(player, Main.boostCfg.getDouble(key + ".cost"))) {
								int time = Main.boostCfg.getInt(key + ".time");
								double boost = Main.boostCfg.getDouble(key + ".boost");

								String message = Main.getLang().getString("lang.xpbuy").replaceAll("%time%", time + "")
										.replaceAll("%money%", Main.boostCfg.getString(key + ".cost"))
										.replaceAll("%boost%", String.valueOf(boost));
								MainAPI.sendMessage(message, player);
								Main.economy.withdrawPlayer(player, Main.boostCfg.getDouble(key + ".cost"));

								XPBoost xpb = XPBoostAPI.setPlayerBoost(player.getUniqueId(), boost, time);

								if (Main.boostCfg.contains(key + ".behaviour")) {
									for (String cond : Main.boostCfg.getConfigurationSection(key + ".behaviour")
											.getKeys(false)) {
										xpb.putCondition(Condition.valueOf(cond.toUpperCase()),
												Main.boostCfg.getBoolean(key + ".behaviour." + cond));
									}
								}

								if (Main.boostCfg.contains(key + ".advanced")) {

									for (String pluginName : Main.boostCfg.getConfigurationSection(key + ".advanced")
											.getKeys(false)) {
										BoostOptions options = new BoostOptions(pluginName.toUpperCase());

										for (String option : Main.boostCfg
												.getConfigurationSection(key + ".advanced." + pluginName)
												.getKeys(false)) {
											if (option.equalsIgnoreCase("default"))
												options.setEnabledByDefault(Main.boostCfg
														.getBoolean(key + ".advanced." + pluginName + "." + option));
											else
												options.getOptions().put(option.toUpperCase(), Main.boostCfg
														.getBoolean(key + ".advanced." + pluginName + "." + option));
										}

										xpb.getAdvancedOptions().put(pluginName.toUpperCase(), options);
									}
								}

							} else {
								String message = Main.getLang().getString("lang.buyfail");
								message = message.replaceAll("%money%", Main.boostCfg.getString(key + ".cost"));
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
