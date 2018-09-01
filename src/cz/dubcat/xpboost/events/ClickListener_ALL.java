package cz.dubcat.xpboost.events;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.Debug;

public class ClickListener_ALL implements Listener {

	@EventHandler
	public void rightClick(PlayerInteractEvent event) {

		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			EquipmentSlot e = event.getHand();
			if (!e.equals(EquipmentSlot.HAND)) {
				return;
			}

			Player player = event.getPlayer();

			if (player.getInventory().getItemInMainHand() != null
					&& player.getInventory().getItemInMainHand().getType() == Material
							.getMaterial(Main.getPlugin().getConfig().getString("settings.itemmaterial"))) {
				ItemStack item = player.getInventory().getItemInMainHand();
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

					String name = MainAPI.colorizeText(Main.getLang().getString("lang.itemname")
							.replace("%boost%", String.valueOf(boost)).replace("%time%", String.valueOf(time)));

					if (nazev.equals(name) || nazev.contains(MainAPI.stripColours(name)) || nazev.contains(name)
							|| nazev.equals(name)) {

						if (XPBoostAPI.hasBoost(player.getUniqueId())) {
							MainAPI.sendMessage(Main.getLang().getString("lang.boostactive"), player);
							event.setCancelled(true);
							return;
						}

						XPBoostAPI.setPlayerBoost(player.getUniqueId(), boost, time);

						MainAPI.sendMessage(Main.getLang().getString("lang.xpbuy").replace("%boost%", "" + boost)
								.replace("%time%", "" + time).replace("%money%", ""), player);

						player.getInventory().setItemInMainHand(null);
						player.updateInventory();

						event.setCancelled(true);

					}
				}

			}

		}
	}
}
