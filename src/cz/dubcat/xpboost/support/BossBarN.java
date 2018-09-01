package cz.dubcat.xpboost.support;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBar;
import org.inventivetalent.bossbar.BossBarAPI;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.XPBoost;
import net.md_5.bungee.api.chat.TextComponent;

public class BossBarN extends BukkitRunnable {

	@SuppressWarnings("deprecation")
	@Override
	public void run() {

		if (!Main.getPlugin().getConfig().getBoolean("settings.bossbar.bossbarmsg")) {
			return;
		}

		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			UUID id = p.getUniqueId();
			if (XPBoostAPI.hasBoost(id)) {
				XPBoost xpb = XPBoostAPI.getBoost(id);

				for (BossBar s : BossBarAPI.getBossBars(p)) {
					s.removePlayer(p);
				}

				int lenght = xpb.getBoostTime();
				float calc1 = ((float) xpb.getTimeRemaining()) / (float) (lenght);

				String message = MainAPI.colorizeText(
						Main.getLang().getString("lang.bossbar").replaceAll("%boost%", xpb.getBoost() + ""));

				BossBarAPI.addBar(p, // The receiver of the BossBar
						new TextComponent(message), // Displayed message
						BossBarAPI.Color.valueOf(Main.getPlugin().getConfig().getString("settings.bossbar.color")), // Color
																													// of
																													// the
																													// bar
						BossBarAPI.Style.valueOf(Main.getPlugin().getConfig().getString("settings.bossbar.style")), // Bar
																													// style
						calc1, // Progress (0.0 - 1.0)
						lenght, // Timeout
						20); // Timeout-interval
			} else if (BossBarAPI.hasBar(p)) {
				BossBarAPI.removeBar(p);
			}
		}
	}

}
