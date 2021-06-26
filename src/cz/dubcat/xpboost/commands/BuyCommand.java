package cz.dubcat.xpboost.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.api.BoostAPI;
import cz.dubcat.xpboost.api.InternalXPBoostAPI;

public class BuyCommand implements CommandInterface {
    
    private BoostAPI boostApi = new BoostAPI();
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("xpboost.use") || player.hasPermission("xpboost.gui")) {
                if(args.length == 2) {
                    String boostId = args[1];
                    if(!boostApi.buyBoost(player, boostId)) {
                        InternalXPBoostAPI.sendMessage("Boost &c" + boostId + " &fdoenst exist.", sender);
                    }
                } else {
                    InternalXPBoostAPI.sendMessage( "Usage: &c/xpboost buy <boostName>", sender);
                }
            }
        }
        return true;
    }

}
