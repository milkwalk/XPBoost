package cz.dubcat.xpboost.config;

import java.io.IOException;
import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.constructors.Debug;

public class ConfigManager {

    public void setLine(String key, Object content, FileConfiguration cfg) {
        if (!cfg.contains(key))
            cfg.set(key, content);
    }

    public void forceSetLine(String key, Object content, FileConfiguration cfg) {
        cfg.set(key, content);
    }

    public void loadDefaultConfig() {
        XPBoostMain.getPlugin().getConfig().addDefault("database.type", "file");
        XPBoostMain.getPlugin().getConfig().addDefault("database.host", "localhost");
        XPBoostMain.getPlugin().getConfig().addDefault("database.port", 3306);
        XPBoostMain.getPlugin().getConfig().addDefault("database.user", "root");
        XPBoostMain.getPlugin().getConfig().addDefault("database.password", "root");
    }

    public void loadLangFile() {
        FileConfiguration cfg = XPBoostMain.lang;

        setLine("lang.prefix", "&f[&aXPBoost&f] ", cfg);
        setLine("lang.doublexpnot", "&fYou got &a%newexp% XP &finstead of &a%oldexp% XP", cfg);
        setLine("lang.actionbar", "&a%boost%x XP boost is active", cfg);
        setLine("lang.bossbar", "&a%boost%x XP boost is active", cfg);
        setLine("lang.motd",
                "&c%boost%x &aXP Multiplier is enabled! Join NOW! \n &f&lThis is second line! &c%players%/%max%", cfg);
        setLine("lang.boostfisnish", "&cYour boost has expired.", cfg);
        setLine("lang.boostcountdown", "&fSeconds remaining: &6", cfg);
        setLine("lang.boostinfodeny", "&fYou dont have any active boosts.", cfg);
        setLine("lang.xpbuy", "&c%boost%x &fXP boost &fis on for &a%time% seconds &c(%money%$).", cfg);
        setLine("lang.gui", "&cXPBoost Shop", cfg);
        setLine("lang.xptitle", "&a%boost%x XP Boost.", cfg);
        setLine("lang.boostlore", Arrays.asList("&c%boost%x XP Boost for %time% seconds", "&aPrice: &f&l%money%$"),
                cfg);
        setLine("lang.buyfail", "&cYou dont have enough money to purchase this boost! &a(%money%$)", cfg);
        setLine("lang.boostactive", "&cYour boost is still active!", cfg);
        setLine("lang.joinnotmsg", "&f&lGlobal XP Boost of %boost%x is currently &f&aenabled &f&lfor everyone!", cfg);
        setLine("lang.cmdblock", "&cYou are not allowed to execute &a%cmd% &ccommand while boost is active :(", cfg);
        setLine("lang.itemname", "&a%boost%x XPBoost", cfg);

        setLine("lang.factions_gui_name", "&cFaction XPBoost Shop", cfg);
        setLine("lang.factions_nofaction", "&cYou are not member of any faction.", cfg);
        setLine("lang.factions_active_boost", "Your faction already has an active boost!", cfg);
        setLine("lang.factions_only_owners", "This boost can purchase only owner of the faction!", cfg);
        setLine("lang.factions_expire", "&cBoost for you faction has expired!", cfg);
        setLine("lang.factions_buy",
                "&c%player% &fhas bought a boost of &c%boost%x &ffor whole faction for &c%time% seconds!", cfg);
        setLine("lang.factions_one_boost", "You cannot purchase any boosts while faction boost is active.", cfg);

        setLine("lang.item.lore1", "&fBoost: &a%boost%x", cfg);
        setLine("lang.item.lore2", "&fDuration: &a%time% Seconds", cfg);
        setLine("lang.item.lore3", "&8Right-Click to gain boost.", cfg);
        setLine("lang.reload", "&fConfig has been reloaded", cfg);

        setLine("lang.pluginmenu", Arrays.asList("/xpboost gui &f- Opens up GUI",
                "/xpboost info &f- Shows time remaining", "/xpboost factions &f- Opens up a factions GUI",
                "/xpboost on/off &f - Enables/disables global XP Boost",
                "/xpboost give <name/all> <boost> <time> [VANILLA,SKILLAPI,MCMMO,RPGME,HEROES]&f - Gives player boost",
                "/xpboost clear <name> &f - Clears active boost",
                "/xpboost item <player> <boost> <time>&f - Gives an XPboost item to a player",
                "/xpboost giveDefinedBoost <player/all> <boostId> [durationInSeconds]&f - Gives a predefined boost",
                "/xpboost global <boost> [time in seconds]- Changes global boost multiplier",
                "/xpboost reload &f - Reloads config", "What ever is inside [] is &coptional"), cfg);

        try {
            XPBoostMain.lang.save(XPBoostMain.langFile);
        } catch (IOException e) {
            e.printStackTrace();
            MainAPI.debug("Could not save language file.", Debug.NORMAL);
        }
    }

}
