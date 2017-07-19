package cz.dubcat.xpboost.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;


import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.constructors.XPBoost;

public class MainAPI {
	
	public enum Debug{
		OFF,NORMAL,ALL;
	}
	
	public enum Condition{
		VANILLA,SKILLAPI,MCMMO,RPGME,HEROES,ALL;
		public static Condition[] CONDITIONS = new Condition[] {VANILLA,SKILLAPI,MCMMO,RPGME,HEROES};
	}
	
    public static File playersyml;
    public static FileConfiguration players;
    
    public static File factionsyml;
    public static FileConfiguration factions;
		
	public static void loadPlayer(Player p){
		setPlayerFile(p.getUniqueId());
		
		double boost = 0;
		long endtime = 0;
		
		if(players.contains("xp.boost")){
			boost = (double) getPlayerVariable("xp.boost");
		}
		
		if(players.contains("xp.endtime")){
			try{
				endtime = (long) getPlayerVariable("xp.endtime");
			} catch(ClassCastException e){
				return;
			}
		}
		
		if(endtime > System.currentTimeMillis()){
			XPBoost xpb = new XPBoost(p.getUniqueId(), boost,endtime);	
			Main.allplayers.put(p.getUniqueId(), xpb);			
		}
	}
	
	public static void savePlayer(Player p){
		UUID id = p.getUniqueId();
		if(xpbAPI.hasBoost(id)){
			XPBoost xpb = xpbAPI.getBoost(id);
			
			setPlayerFile(id);
			//RESET EVERYTHIGN
			setPlayerVariable("xp", "");
			
			//SAVE NEW INFO >.<
			setPlayerVariable("xp.boost", xpb.getBoost());
			setPlayerVariable("xp.endtime", xpb.getEndTime());		
			
			
		   Iterator<Entry<Condition, Boolean>> it = xpb.getConditions().entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<Condition, Boolean> pair = (Map.Entry<Condition, Boolean>)it.next();
		    	setPlayerVariable("xp.condition." + pair.getKey(), pair.getValue());
		        it.remove(); 
		    }
		}	
	}
	
	public static boolean loadAllFactions(){
		File[] files = new File(Main.getPlugin().getDataFolder() + "/factions/").listFiles();
		
		if(files == null || files.length == 0)
			return false;
		
	    for (File file : files) {
	        if (!file.isDirectory()) {
	        	loadFaction(file.getName().replaceAll(".yml", ""));
	        }
	    }
		return true;
	}
	
	public static void loadFaction(String faction){
		Faction f = FactionColl.get().getByName(faction);
		File file = setFactionFile(f);
		
		double boost = 0;
		long endtime = 0;
		
		if(factions.contains("xp.boost")){
			boost = (double) getFactionVariable("xp.boost");
		}
		
		if(factions.contains("xp.endtime")){
			try{
				endtime = (long) getFactionVariable("xp.endtime");
			} catch(ClassCastException e){
				return;
			}
		}
		
		if(endtime > System.currentTimeMillis()){
			XPBoost xpb = new XPBoost(f, boost,endtime);	
			Main.factions_boost.put(f, xpb);			
		}else{
			file.delete();
		}
	}
	
	public static void saveFaction(Faction f, XPBoost xpb){
		setFactionFile(f);
		
		if(xpbAPI.hasFactionBoost(f)){
			
			setFactionFile(f);
			//RESET EVERYTHIGN
			setFactionVariable("xp", "");
			
			//SAVE NEW INFO >.<
			setFactionVariable("xp.boost", xpb.getBoost());
			setFactionVariable("xp.endtime", xpb.getEndTime());		
			
			
		   Iterator<Entry<Condition, Boolean>> it = xpb.getConditions().entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<Condition, Boolean> pair = (Map.Entry<Condition, Boolean>)it.next();
		    	setFactionVariable("xp.condition." + pair.getKey(), pair.getValue());
		        it.remove(); 
		    }
		}		
	}
	
	public static void saveOfflinePlayer(UUID id, XPBoost xpb){
		
		setPlayerFile(id);
		//RESET EVERYTHIGN
		setPlayerVariable("xp", "");
		
		//SAVE NEW INFO >.<
		setPlayerVariable("xp.boost", xpb.getBoost());
		setPlayerVariable("xp.endtime", xpb.getEndTime());		
		
		
	   Iterator<Entry<Condition, Boolean>> it = xpb.getConditions().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Condition, Boolean> pair = (Map.Entry<Condition, Boolean>)it.next();
	    	setPlayerVariable("xp.condition." + pair.getKey(), pair.getValue());
	        it.remove(); 
	    }
	    
	    savePlayerProfile();
	}
	
	
	public static File setPlayerFile(UUID uuid){
        playersyml = new File(Main.getPlugin().getDataFolder()+"/players/"+uuid+".yml");
        players = YamlConfiguration.loadConfiguration(playersyml);	
        
        return playersyml;
	}
	
	public static void setPlayerVariable(String cesta, Object variable){
		 players.set(cesta, variable);
		 saveCustomYml(players, playersyml);
	}
	
	public static void savePlayerProfile(){
		  saveCustomYml(players, playersyml);
	}
	
	public static Object getPlayerVariable(String cesta){
		return players.get(cesta);
	}
	
	public static File setFactionFile(Faction faction){
        factionsyml = new File(Main.getPlugin().getDataFolder()+"/factions/"+faction.getName()+".yml");
        factions = YamlConfiguration.loadConfiguration(factionsyml);
        
        return factionsyml;
	}
	
	public static void setFactionVariable(String cesta, Object variable){
		factions.set(cesta, variable);
		saveFactionFile();
	}
	
	public static void saveFactionFile(){
		  saveCustomYml(factions, factionsyml);
	}
	
	public static Object getFactionVariable(String cesta){
		return factions.get(cesta);
	}
	
    public static void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
    	try {
    		ymlConfig.save(ymlFile);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public static void debug(String srt, Debug val){   	
    	if(Main.debug == val){
    		Main.getLog().info(srt);
    	}   	
    }
    
	public static void sendMSG(String string, UUID player){
		Bukkit.getServer().getPlayer(player).sendMessage(colorizeText(Main.getLang().getString("lang.prefix") + string));
	}
    
    
	public static void sendMSG(String string, Player player){
		player.sendMessage(colorizeText(Main.getLang().getString("lang.prefix") + string));
	}
    
    
    public static String colorizeText(String string) {
    	return ChatColor.translateAlternateColorCodes('&', string);
    }
    
	public static String stripColours(String string) {
		string = string.replaceAll(ChatColor.BLACK + "", "");
		string = string.replaceAll(ChatColor.DARK_BLUE + "", "");
		string = string.replaceAll(ChatColor.DARK_GREEN + "", "");
		string = string.replaceAll(ChatColor.DARK_AQUA + "", "");
		string = string.replaceAll(ChatColor.DARK_RED + "", "");
		string = string.replaceAll(ChatColor.DARK_PURPLE + "", "");
		string = string.replaceAll(ChatColor.GOLD + "", "");
		string = string.replaceAll(ChatColor.GRAY + "", "");
		string = string.replaceAll(ChatColor.DARK_GRAY + "", "");
		string = string.replaceAll(ChatColor.BLUE + "", "");
		string = string.replaceAll(ChatColor.GREEN + "", "");
		string = string.replaceAll(ChatColor.AQUA + "", "");
		string = string.replaceAll(ChatColor.RED + "", "");
		string = string.replaceAll(ChatColor.LIGHT_PURPLE + "", "");
		string = string.replaceAll(ChatColor.YELLOW + "", "");
		string = string.replaceAll(ChatColor.WHITE + "", "");
		string = string.replaceAll(ChatColor.BOLD + "", "");
		string = string.replaceAll(ChatColor.WHITE + "", "");
		string = string.replaceAll(ChatColor.STRIKETHROUGH + "", "");
		return string;
	}
    
    
    public static void createDisplay(Material material, Inventory inv, int Slot, String name, String lore){
    	ItemStack item = new ItemStack(material);
    	ItemMeta meta = item.getItemMeta();
    	meta.setDisplayName(name);
    	//ArrayList<String> Lore = new ArrayList<String>();
    	List<String> itemlore = new ArrayList<String>();
    	//Lore.add(lore);
    	
    	if (lore.contains("%newline%")){
    		String parts[] = lore.split("%newline%");
	    	for (String s: parts)
	    		itemlore.add(s);
    	}else{
    		itemlore.add(lore);
    	}
    	
    	meta.setLore(itemlore);
    	item.setItemMeta(meta);		    	 
    	inv.setItem(Slot, item); 
    }
    
    public static Faction getPlayerFaction(Player p){  	
    	return MPlayer.get(p).getFaction();
    }
    
    public static void openFactionBoostShop(Player player){
		int i = -1;
		
		int amount = 1;
		
		Faction faction = getPlayerFaction(player);
		
		if(faction.isNone()){
			sendMSG(Main.getLang().getString("lang.factions_nofaction"),player);
			return;
		}
		
		for(String key : Main.factions.getConfigurationSection("boost").getKeys(false)){
			if (Main.factions.getBoolean("boost." + key + ".enabled") == true){
				amount++;
			}
		}
		
		if(amount > 9 && amount <= 18){
			amount = 18;
		}else if(amount > 18){
			amount = 27;
		}else{
			amount = 9;
		}
		
      	Inventory GUI = Bukkit.createInventory(null, amount, colorizeText(Main.getLang().getString("lang.factions_gui_name")));
      	
		for(String key : Main.factions.getConfigurationSection("boost").getKeys(false)){
			if (Main.factions.getBoolean("boost." + key + ".enabled") == true){
				i++;
            	int cost = Main.getPlugin().getConfig().getInt("boost." + key + ".cost");
            	int time = Main.getPlugin().getConfig().getInt("boost." + key + ".time");
            	double boost = Main.getPlugin().getConfig().getDouble("boost." + key + ".boost");
            	Material mat = Material.EXP_BOTTLE;
            	
            	if(Main.getPlugin().getConfig().contains("boost." + key + ".item_type")){
            		mat = Material.valueOf(Main.getPlugin().getConfig().getString("boost." + key + ".item_type"));
            	}
            	
            	MainAPI.createDisplay(mat,GUI,i,(Main.getPlugin().getConfig().getString("boost." + key + ".title") != null) ? MainAPI.colorizeText(Main.getPlugin().getConfig().getString("boost." + key + ".title")).replaceAll("%boost%", boost+"").replaceAll("%money%", cost + "").replaceAll("%time%", time + "") : MainAPI.colorizeText(Main.getLang().getString("lang.xptitle").replaceAll("%boost%", boost+"")),MainAPI.colorizeText(Main.getLang().getString("lang.xplore").replaceAll("%time%", time + "").replaceAll("%money%", cost + "").replaceAll("%boost%", boost + "")));
	            player.openInventory(GUI);
			}
		}
    }
    
    public static void openXpBoostShop(Player player){
		int i = -1;
		
		int amount = 1;
		for(String key : Main.getPlugin().getConfig().getConfigurationSection("boost").getKeys(false)){
			if (Main.getPlugin().getConfig().getBoolean("boost." + key + ".enabled") == true){
				amount++;
			}
		}
		
		if(amount > 9 && amount <= 18){
			amount = 18;
		}else if(amount > 18){
			amount = 27;
		}else{
			amount = 9;
		}
		
      	Inventory GUI = Bukkit.createInventory(null, amount, colorizeText(Main.getLang().getString("lang.gui")));
      	
		for(String key : Main.getPlugin().getConfig().getConfigurationSection("boost").getKeys(false)){
			if (Main.getPlugin().getConfig().getBoolean("boost." + key + ".enabled") == true){
				i++;
            	int cost = Main.getPlugin().getConfig().getInt("boost." + key + ".cost");
            	int time = Main.getPlugin().getConfig().getInt("boost." + key + ".time");
            	double boost = Main.getPlugin().getConfig().getDouble("boost." + key + ".boost");
            	Material mat = Material.EXP_BOTTLE;
            	
            	if(Main.getPlugin().getConfig().contains("boost." + key + ".item_type")){
            		mat = Material.valueOf(Main.getPlugin().getConfig().getString("boost." + key + ".item_type"));
            	}
            	
            	MainAPI.createDisplay(mat,GUI,i,(Main.getPlugin().getConfig().getString("boost." + key + ".title") != null) ? MainAPI.colorizeText(Main.getPlugin().getConfig().getString("boost." + key + ".title")).replaceAll("%boost%", boost+"").replaceAll("%money%", cost + "").replaceAll("%time%", time + "") : MainAPI.colorizeText(Main.getLang().getString("lang.xptitle").replaceAll("%boost%", boost+"")),MainAPI.colorizeText(Main.getLang().getString("lang.xplore").replaceAll("%time%", time + "").replaceAll("%money%", cost + "").replaceAll("%boost%", boost + "")));
	            player.openInventory(GUI);
			}
		}
    }
}
