package cz.dubcat.xpboost.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;

public class XpboostMainCmd implements CommandInterface{

	
	
    private Main plugin;
    
    public XpboostMainCmd(Main plugin) {
        this.plugin = plugin;
    }

    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,String commandLabel, String[] args) {
    if(sender instanceof Player) {    
	    Player player = (Player) sender;       
	  	MainAPI.sendMessage(Main.getLang().getString("lang.menu.row1"), player);
	  	MainAPI.sendMessage(Main.getLang().getString("lang.menu.row2"), player);
	  	MainAPI.sendMessage(Main.getLang().getString("lang.menu.row3"), player);
	      if (player.hasPermission("xpboost.admin")){
		    	  for(int i = 4; i < 11;i++){
		    		  MainAPI.sendMessage(Main.getLang().getString("lang.menu.row"  +i), player);
		    	  }
	    	  }
    }else{
    	plugin.getLogger().info(Main.getLang().getString("lang.menu.row3"));
    	plugin.getLogger().info(Main.getLang().getString("lang.menu.row4"));
    	plugin.getLogger().info(Main.getLang().getString("lang.menu.row5"));
    	plugin.getLogger().info(Main.getLang().getString("lang.menu.row6"));
    	plugin.getLogger().info(Main.getLang().getString("lang.menu.row7"));
    	plugin.getLogger().info(Main.getLang().getString("lang.menu.row8"));
    	plugin.getLogger().info(Main.getLang().getString("lang.menu.row9"));
    	plugin.getLogger().info(Main.getLang().getString("lang.menu.row10"));
    }
        return false;
    }
}
