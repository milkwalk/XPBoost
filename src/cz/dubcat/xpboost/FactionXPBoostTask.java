package cz.dubcat.xpboost;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.constructors.Debug;
import cz.dubcat.xpboost.constructors.XPBoost;

public class FactionXPBoostTask implements Runnable {

	public BukkitTask id;

	public void setId(BukkitTask id) {
		this.id = id;
	}

	@Override
	public void run() {
		ConcurrentHashMap<Faction, XPBoost> mp = Main.factions_boost;
		Iterator<Entry<Faction, XPBoost>> it = mp.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<Faction, XPBoost> pair = it.next();
			XPBoost xpb = pair.getValue();
			Faction faction = pair.getKey();

			if (xpb.getTimeRemaining() == 0) {

				for (MPlayer p : faction.getMPlayers()) {
					String message = Main.getLang().getString("lang.factions_expire");

					// SEND MESSAGE
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
						@Override
						public void run() {
							if (Bukkit.getServer().getPlayer(p.getUuid()) != null
									&& Bukkit.getServer().getPlayer(p.getUuid()).isOnline())
								MainAPI.sendMessage(message, p.getUuid());
						}
					}, 0L);
				}

				MainAPI.debug("Removed boost from Faction " + faction.getName(), Debug.NORMAL);
				mp.remove(xpb.getFaction());

				File file = MainAPI.setFactionFile(xpb.getFaction());
				file.delete();
			}
		}
	}

}
