package cz.dubcat.xpboost.cmds;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
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
		
	    if(Main.getPlugin().getConfig().contains("settings.language")){
	    	 Main.lang_file = new File(Main.getPlugin().getDataFolder() + "/lang/lang_"+Main.getPlugin().getConfig().getString("settings.language")+".yml");
	    	 Main.lang = YamlConfiguration.loadConfiguration(Main.lang_file);
	    }else{
	    	Main.lang_file = new File(Main.getPlugin().getDataFolder() + "/lang/lang_ENG.yml");
	    	Main.lang = YamlConfiguration.loadConfiguration(Main.lang_file);
	    }
		
		if(sender instanceof Player) {
			player = (Player) sender;		   	
			MainAPI.sendMSG( Main.getLang().getString("lang.reload"), player);
		}else{
			plugin.getLogger().info(Main.getLang().getString("lang.reload"));   
		}
		
        return false;
    }

}
