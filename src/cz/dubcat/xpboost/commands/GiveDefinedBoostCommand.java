package cz.dubcat.xpboost.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.api.BoostAPI;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.exceptions.BoostNotFoundException;

public class GiveDefinedBoostCommand implements CommandInterface {
    private BoostAPI boostApi = new BoostAPI();
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender.hasPermission("xpboost.admin")) {
            if(args.length >= 3) {
                String target = args[1];
                String boostId = args[2];
                Integer duration = null;
                
                if(args.length >= 4) {
                    try {
                        duration = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        MainAPI.sendMessage("This is not an integer: &c" + args[3], sender);
                        return true;
                    }
                }
                
                if(target.equalsIgnoreCase("all")) {
                    for(Player p : Bukkit.getOnlinePlayers()) {
                        if(!this.giveBoost(p, boostId, sender, duration)) {
                            return true;
                        }
                    }
                } else {
                    Player targetPlayer = Bukkit.getServer().getPlayer(target);
                    if (targetPlayer == null || !targetPlayer.isOnline()) {
                        MainAPI.sendMessage("Player " + target + " is not online.", sender);
                        
                        return true;
                    }
                    
                    if(!this.giveBoost(targetPlayer, boostId, sender, duration)) {
                        return true;
                    }
                }
                
                MainAPI.sendMessage("&aYou have successfully given boost " + boostId + " to " + target + (duration != null ? " for " + duration + " seconds" : ""), sender);
            } else {
                MainAPI.sendMessage(
                        "Usage: &c/xpboost giveDefinedBoost <player/all> <boostId>",
                        sender);
            }
        }
        
        return true;
    }
    
    private boolean giveBoost(Player player, String boostId, CommandSender caller, Integer durationInSeconds) {
        try {
            boostApi.giveBoost(player, boostId, durationInSeconds);
        } catch (BoostNotFoundException e) {
            MainAPI.sendMessage("&cBoost " + boostId + " does not exist", caller);
            
            return false;
        }
        
        return true;
    }

}
