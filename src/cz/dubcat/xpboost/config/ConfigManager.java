package cz.dubcat.xpboost.config;

import java.io.IOException;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Debug;

public class ConfigManager {
	
	public void setLine(String key, String content){
		if(!Main.lang.contains(key))
			setLine(key, content);
	}
	
	public void loadLangFile(){
		
		setLine("lang.prefix", "&f[&aXPBoost&f] ");
		setLine("lang.doublexpnot", "&fYou got &a%newexp% XP &finstead of &a%oldexp% XP");
		setLine("lang.actionbar", "&a%boost%x XP boost is active");
		setLine("lang.bossbar", "&a%boost%x XP boost is active");
		setLine("lang.motd", "&c%boost%x &aXP Multiplier is enabled! Join NOW! \n &f&lThis is second line! &c%players%/%max%");
		setLine("lang.boostfisnish", "&cYour boost has expired.");
		setLine("lang.boostcountdown", "&fSeconds remaining: &6");
		setLine("lang.boostinfodeny", "&fYou dont have any active boosts.");
		setLine("lang.xpbuy", "&c%boost%x &fXP boost &fis on for &a%time% seconds &c(%money%$).");
		setLine("lang.gui", "&cXP Boost Shop.");
		setLine("lang.xptitle", "&a%boost%x XP Boost.");
		setLine("lang.xplore", "&c%boost%x XP Boost for %time% seconds%newline%&aPrice: &f&l%money%$");
		setLine("lang.buyfail", "&cYou dont have enough money to purchase this boost! &a(%money%$)");
		setLine("lang.boostactive", "&cYour boost is still active!");
		setLine("lang.joinnotmsg", "&f&lGlobal XP Boost of %boost%x is currently &f&aenabled &f&lfor everyone!");
		setLine("lang.cmdblock", "&cYou are not allowed to execute &a%cmd% &ccommand while boost is active :(");
		setLine("lang.itemname", "&a%boost%x XPBoost");
		
		setLine("lang.item.lore1", "&fBoost: &a%boost%x");
		setLine("lang.item.lore2", "&fDuration: &a%time% Seconds");
		setLine("lang.item.lore3", "&8Right-Click to gain boost.");
		setLine("lang.reload", "&fConfig has been reloaded");
		
		setLine("lang.menu.row1", "/xpboost gui &f- Opens up GUI");
		setLine("lang.menu.row2", "/xpboost info &f- Shows time remaining");
		setLine("lang.menu.row3", "/xpboost on/off &f - Enables/disables global XP Boost");
		setLine("lang.menu.row4", "/xpboost give <name/all> <boost> <time> [VANILLA,SKILLAPI,MCMMO,RPGME,HEROES]&f - Gives player boost");
		setLine("lang.menu.row5", "/xpboost clear <name> &f - Clears active boost");
		setLine("lang.menu.row6", "/xpboost item <player> <boost> <time>&f - Gives an XPboost item to a player");
		setLine("lang.menu.row7", "/xpboost global <boost> - Changes global boost multiplier");
		setLine("lang.menu.row8", "/xpboost reload &f - Reloads config");
		setLine("lang.menu.row9", "What ever is inside [] is &coptional");
		
		
		try {
			Main.lang.save(Main.lang_file);
		} catch (IOException e) {
			MainAPI.debug("Could not save language file", Debug.NORMAL);
			e.printStackTrace();
		}
		
		
		
	}

}
