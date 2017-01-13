package cz.dubcat.xpboost.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;

public class reloadCmd implements CommandInterface{
	
    private Main plugin;
    
    public reloadCmd(Main plugin) {
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
    	
		plugin.reloadConfig();
		
		if(sender instanceof Player) {
			player = (Player) sender;		   	
			MainAPI.sendMSG( plugin.getConfig().getString("lang.reload"), player);
		}else{
			plugin.getLogger().info(plugin.getConfig().getString("lang.reload"));   
		}
		
        return false;
    }

}
