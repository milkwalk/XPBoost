package cz.dubcat.xpboost.api;

import org.bukkit.entity.Player;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.constructors.BoostOptions;
import cz.dubcat.xpboost.constructors.XPBoost;

public class BoostAPI {

    public boolean buyBoost(Player player, String boostId) {
        
        if(!XPBoostMain.boostCfg.contains(boostId)) {
            return false;
        }
        
        if (XPBoostMain.boostCfg.getBoolean(boostId + ".enabled")) {
            if (XPBoostMain.boostCfg.contains(boostId + ".permissions")) {
                if (!player.hasPermission(
                        XPBoostMain.boostCfg.getString(boostId + ".permissions.required_permission"))) {
                    String message = XPBoostMain.boostCfg.getString(boostId + ".permissions.fail_message")
                            .replaceAll("%perm%",
                                    XPBoostMain.boostCfg.getString(boostId + ".permissions.required_permission"));
                    MainAPI.sendMessage(message, player);
                    
                    return true;
                }
            }

            if (XPBoostMain.economy.has(player, XPBoostMain.boostCfg.getDouble(boostId + ".cost"))) {
                int time = XPBoostMain.boostCfg.getInt(boostId + ".time");
                double boost = XPBoostMain.boostCfg.getDouble(boostId + ".boost");

                String message = XPBoostMain.getLang().getString("lang.xpbuy").replaceAll("%time%", time + "")
                        .replaceAll("%money%", XPBoostMain.boostCfg.getString(boostId + ".cost"))
                        .replaceAll("%boost%", String.valueOf(boost));
                MainAPI.sendMessage(message, player);
                XPBoostMain.economy.withdrawPlayer(player, XPBoostMain.boostCfg.getDouble(boostId + ".cost"));

                XPBoost xpb = XPBoostAPI.setPlayerBoost(player.getUniqueId(), boost, time);

                if (XPBoostMain.boostCfg.contains(boostId + ".behaviour")) {
                    for (String cond : XPBoostMain.boostCfg.getConfigurationSection(boostId + ".behaviour")
                            .getKeys(false)) {
                        xpb.putCondition(Condition.valueOf(cond.toUpperCase()),
                                XPBoostMain.boostCfg.getBoolean(boostId + ".behaviour." + cond));
                    }
                }

                if (XPBoostMain.boostCfg.contains(boostId + ".advanced")) {
                    for (String pluginName : XPBoostMain.boostCfg.getConfigurationSection(boostId + ".advanced").getKeys(false)) {
                        BoostOptions options = new BoostOptions(pluginName.toUpperCase());

                        for (String option : XPBoostMain.boostCfg.getConfigurationSection(boostId + ".advanced." + pluginName).getKeys(false)) {
                            if (option.equalsIgnoreCase("default")) {
                                options.setEnabledByDefault(XPBoostMain.boostCfg
                                        .getBoolean(boostId + ".advanced." + pluginName + "." + option));
                            } else {
                                options.getOptions().put(option.toUpperCase(), XPBoostMain.boostCfg
                                        .getBoolean(boostId + ".advanced." + pluginName + "." + option));
                            }
                        }

                        xpb.getAdvancedOptions().put(pluginName.toUpperCase(), options);
                    }
                }

            } else {
                String message = XPBoostMain.getLang().getString("lang.buyfail");
                message = message.replaceAll("%money%", XPBoostMain.boostCfg.getString(boostId + ".cost"));
                MainAPI.sendMessage(message, player);
            }
            
            return true;
        }
        
        return true;
    }
}
