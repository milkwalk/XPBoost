package cz.dubcat.xpboost.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.xpbAPI;
import cz.dubcat.xpboost.api.MainAPI.Condition;
import cz.dubcat.xpboost.constructors.XPBoost;

public class FactionsClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {        
	    Player player = (Player) event.getWhoClicked();  
	    
	    Inventory GUI = Bukkit.createInventory(null, 9, MainAPI.colorizeText(Main.getLang().getString("lang.factions_gui_name")));
	    
	    if (event.getInventory().getName().equals(GUI.getName())) {

		    ItemStack clicked = event.getCurrentItem();
		    int slot = event.getSlot();
		    int i = -1;
		    
		    if (clicked != null && clicked.getType() != Material.AIR) {
			    event.setCancelled(true);
			    player.closeInventory();
			    
			    MPlayer fplayer = MPlayer.get(player);
			    
			    Faction faction = MainAPI.getPlayerFaction(player);	
			    
			    
        		for(String key : Main.getPlugin().getConfig().getConfigurationSection("boost").getKeys(false)){
        			if (Main.factions.contains("boost." + key + ".enabled") && Main.factions.getBoolean("boost." + key + ".enabled") == true){
        				i++;
        				if (i == slot){
        					
        					if(xpbAPI.hasFactionBoost(faction)){
    						    MainAPI.sendMSG(Main.getLang().getString("lang.faction_active_boost"), player);
    			        		return;
        					}
        					
        					if(Main.getPlugin().getConfig().getBoolean("boost." + key + ".permission.use")){	        						
        						
        						if(!player.hasPermission(Main.getPlugin().getConfig().getString("boost." + key + ".permission.perm"))){
        							String message = Main.getPlugin().getConfig().getString("boost." + key + ".permission.msg");
        							message = message.replaceAll("%perm%", Main.getPlugin().getConfig().getString("boost." + key + ".permission.perm"));
        							MainAPI.sendMSG(message, player);
        							return;
        						}
        					}
        					
        					if(Main.factions.getBoolean("boost." + key + ".onlyowners") && !faction.getLeader().equals(fplayer)){
        						MainAPI.sendMSG(Main.getLang().getString("lang.factions_only_owners"), player);
        						return;
        					}
        					
						    @SuppressWarnings("deprecation")
							boolean money = Main.economy.has(player.getName(), Main.getPlugin().getConfig().getDouble("boost." + key + ".cost"));
						    
						    if (money){
						    	
							    int time = Main.getPlugin().getConfig().getInt("boost." + key + ".time");
							    
							    String message = Main.getLang().getString("lang.factions_buy")
							    			.replaceAll("%player%", player.getName())
							    			.replaceAll("%time%", time+"")
							    			.replaceAll("%money%", Main.getPlugin().getConfig().getString("boost." + key + ".cost"))
				  							.replaceAll("%boost%", Main.getPlugin().getConfig().getString("boost." + key + ".boost"));
							    
							    for(MPlayer p : faction.getMPlayers()){
					    			if(Bukkit.getServer().getPlayer(p.getUuid()) != null && Bukkit.getServer().getPlayer(p.getUuid()).isOnline())
					    				MainAPI.sendMSG(message, p.getUuid());	
							    }
    						    
				  				Main.economy.withdrawPlayer(player, Main.getPlugin().getConfig().getDouble("boost." + key + ".cost"));
				  				
				  				xpbAPI.setFactionBoost(faction, Main.getPlugin().getConfig().getDouble("boost." + key + ".boost"), Main.getPlugin().getConfig().getInt("boost." + key + ".time"));
				  				
				  				XPBoost xpb = xpbAPI.getFactionBoost(faction);
				  				
				  				if(Main.getPlugin().getConfig().contains("boost." + key + ".behaviour")){
					  				for(String cond : Main.getPlugin().getConfig().getConfigurationSection("boost." + key + ".behaviour").getKeys(false)){
					  					xpb.putCondition(Condition.valueOf(cond.toUpperCase()), Main.getPlugin().getConfig().getBoolean("boost." + key + ".behaviour." + cond));
					  				}
				  				}
						    } else{						    	
				  				String message = Main.getLang().getString("lang.buyfail");
				  				message = message.replaceAll("%money%", Main.getPlugin().getConfig().getString("boost." + key + ".cost"));
    						    MainAPI.sendMSG(message, player);		    	
						    }
						    break;
        				}
        			}
        		}
		        		
		    }
	    }
    }
}
