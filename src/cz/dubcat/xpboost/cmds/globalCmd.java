package cz.dubcat.xpboost.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;

public class globalCmd implements CommandInterface{
	
    private Main plugin;
    
    public globalCmd(Main plugin) {
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
			
			if (args[1] != null){  
			   	double globalboost= Double.parseDouble(args[1]);
			   	
			   	Main.GLOBAL_BOOST.setBoost(globalboost);
			   	
			   	plugin.getConfig().set("settings.globalboost.multiplier", globalboost);
			   	plugin.saveConfig();
			   	
			    if(sender instanceof Player) {
			    	player = (Player) sender;
			    	MainAPI.sendMSG("You have successfully set global boost to &a" + globalboost, player);
			    	if(!Main.GLOBAL_BOOST.isEnabled())
			    		MainAPI.sendMSG("To enable &cGlobal Boost &fwrite &a/xpboost on", player);
			    }else{
			    	plugin.getLogger().info("You have successfully set a global boost to " + globalboost);
			    }
			}
		}else{
			if(sender instanceof Player) {
				player = (Player) sender;
				MainAPI.sendMSG( "Usage: &c/xpboost global <boost>", player);
			}else{
				plugin.getLogger().info("Usage: /xpboost global <boost>");
			}
		}
    	
        return false;
    }

}
