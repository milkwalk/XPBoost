package cz.dubcat.xpboost.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;

public class JoinAndQuitEvent implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		if (Main.getPlugin().getConfig().getBoolean("settings.globalboost.notification")) {
			GlobalBoost gl = Main.GLOBAL_BOOST;
			if (gl.isEnabled())
				MainAPI.sendMessage(
						Main.getLang().getString("lang.joinnotmsg").replaceAll("%boost%", gl.getGlobalBoost() + ""),
						player);
		}

		XPBoost boost = MainAPI.loadPlayer(player.getUniqueId());

		if (boost != null)
			Main.allplayers.put(player.getUniqueId(), boost);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		MainAPI.savePlayer(player.getUniqueId());
		Main.allplayers.remove(player.getUniqueId());
	}

}
