package cz.dubcat.xpboost.cmds;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.xpbAPI;
import cz.dubcat.xpboost.constructors.XPBoost;
import cz.dubcat.xpboost.api.MainAPI.Condition;

public class giveCmd implements CommandInterface{	
	
    private Main plugin;
    
    public giveCmd(Main plugin) {
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
    	
	    	if((args.length == 4 || args.length == 5) && args[1].equalsIgnoreCase("all")){ 
	    		
			   	int time;
			   	try{
			   		 time = Integer.parseInt(args[3]);
			   	}catch(NumberFormatException e){
				   	if(sender instanceof Player) {
				   		player = (Player) sender;   
				   		MainAPI.sendMSG("This is not an integer: &c" + args[3], player);    									   		
				   	}else{
				   		plugin.getLogger().info("This is not an integer: &c" + args[3]);
				   	}
			   		return true;
			   	}
			   	
			   	double boost = Double.parseDouble(args[2]);
				for(Player p : Bukkit.getOnlinePlayers()){
					if(xpbAPI.hasBoost(p.getUniqueId())){
						continue;
					}
					
					xpbAPI.setPlayerBoost(p.getUniqueId(),boost,time);			
					
					XPBoost xpb = xpbAPI.getBoost(p.getUniqueId());
					
				   	if(sender instanceof Player) {
				   		player = (Player) sender;
				   		MainAPI.sendMSG( "You have given &c"+boost+"x Boost &fto &call online players &ffor &c"+time+" seconds.", player);    			
				   	}else{
				   		plugin.getLogger().info("You have given "+boost+"x Boost to all online players for "+time+" seconds.");
				   	}
					
				   	///checking for additional boosts
				   	if(args.length == 5){
				   		
				   		if(!args[4].contains(",")){
				   			args[4] += ",";
				   		}
				   		
				   		String boosttypes = args[4].toUpperCase();
				   		String[] spit1 = boosttypes.split(",");
				   		
				   		for(String type : spit1){
				   			Condition con;
				   			try{
				   				con  = Condition.valueOf(type);
				   			}catch (IllegalArgumentException e){
							   	if(sender instanceof Player) {
							   		player = (Player) sender;   
							   		MainAPI.sendMSG("Paremeter " + type + " does not exist. Use &aVANILLA,SKILLAPI,MCMMO,RPGME,HEROES", player);    									   		
							   	}else{
							   		plugin.getLogger().info("Paremeter " + type + "does not exist. Use &aVANILLA,SKILLAPI,MCMMO,RPGME,HEROES" );
							   	}
				   				return true;
				   			}
				   			
						   	if(sender instanceof Player) {
						   		player = (Player) sender;
						   		MainAPI.sendMSG( con + " &aON", player);    			
						   	}else{
						   		plugin.getLogger().info(con + " &aON");
						   	}
			   				xpb.putCondition(con, true);
				   		}
				   		
				   		for(Condition l : MainAPI.Condition.CONDITIONS){
				   			if(!xpb.getConditions().containsKey(l)){
				   				xpb.putCondition(l, false);
				   			}
				   		}
				   	}
					
				}
			}else if(args.length == 4 || args.length == 5){
				
				
				
    			if (Bukkit.getServer().getPlayer(args[1]) != null && Bukkit.getServer().getPlayer(args[1]).isOnline()){   
				   	Player hrac = Bukkit.getPlayer(args[1]);
				   	
				   	int time;
				   	try{
				   		 time = Integer.parseInt(args[3]);
				   	}catch(NumberFormatException e){
					   	if(sender instanceof Player) {
					   		player = (Player) sender;   
					   		MainAPI.sendMSG("This is not an integer: &c" + args[3], player);    									   		
					   	}else{
					   		plugin.getLogger().info("This is not an integer: &c" + args[3]);
					   	}
				   		return true;
				   	}
				   	
				   	double boost = Double.parseDouble(args[2]);
				   	
				   	//Setting boost before applying other conditions
				   	xpbAPI.setPlayerBoost(hrac.getUniqueId(),boost,time);
				   	
				   	XPBoost xpb = xpbAPI.getBoost(hrac.getUniqueId());
				   	
			   		//clearing conditions;  		
			   		xpb.clearCondition();
				   	
				   	if(sender instanceof Player) {
				   		player = (Player) sender;
				   		MainAPI.sendMSG("You have given &c"+boost+"x Boost &fto &c"+hrac.getName()+" &ffor &c"+time+" seconds.", player);    			
				   		
				   	}else{
				   		plugin.getLogger().info("You have given "+boost+"x Boost to "+hrac.getName()+" for "+time+" seconds.");
				   	}				   	
				   	
				   	///checking for additional boosts
				   	if(args.length == 5){
				   		
				   		if(!args[4].contains(",")){
				   			args[4] += ",";
				   		}
				   		
				   		String boosttypes = args[4].toUpperCase();
				   		String[] spit1 = boosttypes.split(",");
				   		
				   		//clearing conditions;  		
				   		xpb.clearCondition();
				   		
				   		for(String type : spit1){
				   			Condition con;
				   			try{
				   				con  = Condition.valueOf(type);
				   			}catch (IllegalArgumentException e){
							   	if(sender instanceof Player) {
							   		player = (Player) sender;   
							   		MainAPI.sendMSG("Paremeter " + type + " does not exist. Use &aVANILLA,SKILLAPI,MCMMO,RPGME,HEROES", player);    									   		
							   	}else{
							   		plugin.getLogger().info("Paremeter " + type + "does not exist. Use &aVANILLA,SKILLAPI,MCMMO,RPGME,HEROES" );
							   	}
				   				return true;
				   			}
				   			
						   	if(sender instanceof Player) {
						   		player = (Player) sender;
						   		MainAPI.sendMSG( con + " &aON", player);    			
						   	}else{
						   		plugin.getLogger().info(con + " &aON");
						   	}
			   				xpb.putCondition(con, true);
				   		}
				   		
				   		for(Condition l : MainAPI.Condition.CONDITIONS){
				   			if(!xpb.getConditions().containsKey(l)){
				   				xpb.putCondition(l, false);
				   			}
				   		}
				   		
				   		return true;
				   	}
    			}else{
				   	if(sender instanceof Player) {
				   		player = (Player) sender;
				   		MainAPI.sendMSG("Player " + args[1] + " is not online.", player);    			
				   	}else{
				   		plugin.getLogger().info("Player " + args[1] + " is not online.");
				   	}
    			}
    		}else{
			   	if(sender instanceof Player) {
			   		player = (Player) sender;
			   		MainAPI.sendMSG( "Usage: &c/xpboost give <player/all> <boost> <time> [VANILLA,SKILLAPI,MCMMO,RPGME,HEROES]", player);
			   	}else{
			   		plugin.getLogger().info("Usage: /xpboost give <player/all> <boost> <time> [VANILLA,SKILLAPI,MCMMO,RPGME,HEROES]");
			   	}
    		}
        return false;
    }

}
