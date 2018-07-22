package cz.dubcat.xpboost.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.Debug;
import cz.dubcat.xpboost.constructors.XPBoost;

public class GiveBoostCommand implements CommandInterface {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		//format /xpb give <player/all> <boost> <time> [coditions]
		
		if(sender.hasPermission("xpboost.admin")) {
			if(args.length == 4 || args.length == 5) {
					
			   	int boostDuration = 0;
			   	double boost = 0;
			   	try{
			   		boostDuration = Integer.parseInt(args[3]);
			   	}catch(NumberFormatException e){
				   	MainAPI.sendMessage("This is not an integer: &c" + args[3], sender);    									   		
			   		return true;
			   	}
				
			   	try {
			   		boost = Double.parseDouble(args[2]);
			   	}catch(NumberFormatException e) {
			   		MainAPI.sendMessage("Please provide a number. '" + args[2] + "' is not a number.", sender);    					
			   		return true;
			   	}
			   	
			   	List<Condition> condToApply = new ArrayList<Condition>();
			   	
			   	//conditons
			   	if(args.length == 5) {
			   		String[] potenConditions = args[4].split(",");
			   		
			   		for(String c : potenConditions) {
			   			try{
			   				condToApply.add(Condition.valueOf(c.toUpperCase()));
			   			}catch (IllegalArgumentException e){ 
						   	MainAPI.sendMessage("Parameter '" + c + "' does not exist. Use &aVANILLA,SKILLAPI,MCMMO,RPGME,HEROES,JOBS", sender);    									   		
			   				return true;
			   			}
			   		}
			   		
			   		MainAPI.debug("Found " + condToApply.size() + " conditons. " + condToApply.toString(), Debug.NORMAL);
			   	}
			   	
			   	
				
				if(args[1].equalsIgnoreCase("all")) {
					for(Player player : Bukkit.getOnlinePlayers()) {
						XPBoost xpb = XPBoostAPI.setPlayerBoost(player.getUniqueId(), boost, boostDuration);
						
						for(Condition c : Condition.values()) {
							if(condToApply.contains(c))
								xpb.putCondition(c, true);
							else
								xpb.putCondition(c, false);
						}
					}
					
					MainAPI.sendMessage("You have given &c"+boost+"x Boost &fto &call online players &ffor &c"+boostDuration+" seconds. &6" + condToApply.toString()+"", sender);    	
				} else {
					
					if(Bukkit.getServer().getPlayer(args[1]) == null || !Bukkit.getServer().getPlayer(args[1]).isOnline()){   
						MainAPI.sendMessage("Player " + args[1] + " is not online.", sender);   
						return true;
					}
					
					XPBoost xpb = XPBoostAPI.setPlayerBoost(Bukkit.getPlayer(args[1]).getUniqueId(), boost, boostDuration);
					for(Condition c : Condition.values()) {
						if(condToApply.contains(c))
							xpb.putCondition(c, true);
						else
							xpb.putCondition(c, false);
					}
					MainAPI.sendMessage("You have given &c"+boost+"x Boost &fto &c"+Bukkit.getPlayer(args[1]).getName()+" &ffor &c"+boostDuration+" seconds. &6" + condToApply.toString() + "", sender);  
				}
				
			}else {
				MainAPI.sendMessage("Usage: &c/xpboost give <player/all> <boost> <time> [VANILLA,SKILLAPI,MCMMO,RPGME,HEROES]", sender);	
			}	
		}
		return true;
	}

}
