package cz.dubcat.xpboost.cmds;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.xpbAPI;

public class clearCmd implements CommandInterface{
	
    private Main plugin;
    private static String msg = "You have successfully removed boost from &a";
    
    public clearCmd(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,String commandLabel, String[] args) {
    	 	Player player;
    	   if(sender instanceof Player) {
    		   player = (Player) sender;
    		   if (!(player.hasPermission("xpboost.admin"))){
    			   return false;
    		   }
    	   }
    	   
    		if(args.length == 2){  			
    			if (Bukkit.getServer().getPlayer(args[1]) != null && Bukkit.getServer().getPlayer(args[1]).isOnline()){  
				   	Player hrac = Bukkit.getPlayer(args[1]);
				   	UUID id = hrac.getUniqueId();
				   	
				   	
				   	xpbAPI.clearBoost(id);
				    if(sender instanceof Player) {
				    	player = (Player) sender;
				    	MainAPI.sendMSG(msg + hrac.getName(), player);
				    }else{
				    	plugin.getLogger().info(msg + hrac.getName());
				    }
    			}
    		}else{
    			if(sender instanceof Player) {
    				player = (Player) sender;
    				MainAPI.sendMSG("Usage: &c/xpboost clear <player>", player);
    			}else{
    				plugin.getLogger().info("Usage: /xpboost clear <player>");
    			}
    		}
        return false;
    }
}
