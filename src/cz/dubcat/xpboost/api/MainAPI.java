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

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.constructors.XPBoost;

public class MainAPI {
	
	public enum Debug{
		OFF,NORMAL,ALL;
	}
	
	public enum Condition{
		VANILLA,SKILLAPI,MCMMO,RPGME,HEROES;
		public static Condition[] CONDITIONS = new Condition[] {VANILLA,SKILLAPI,MCMMO,RPGME,HEROES};
	}
	
    public static File playersyml;
    public static FileConfiguration players;
		
	public static void loadPlayer(Player p){
		setPlayerFile(p.getUniqueId());
		
		double boost = 0;
		long endtime = 0;
		
		if(players.contains("xp.boost")){
			boost = (double) getPlayerVariable("xp.boost");
		}
		
		if(players.contains("xp.endtime")){
			endtime = (long) getPlayerVariable("xp.endtime");
		}
		
		if(boost != 0 || endtime != 0){
			XPBoost xpb = new XPBoost(p, boost,endtime);	
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
	
	
	public static void setPlayerFile(UUID uuid){
        playersyml = new File(Main.getPlugin().getDataFolder()+"/players/"+uuid+".yml");
        players = YamlConfiguration.loadConfiguration(playersyml);	
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
	
    public static void saveCustomYml(FileConfiguration ymlConfig, File ymlFile) {
    	try {
    		ymlConfig.save(ymlFile);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public static void debug(String srt, Debug val){
    	
    	if(Main.debug == Debug.OFF){
    		return;
    	}
    	
    	if(Main.debug == val){
    		Main.getLog().info(srt);
    	}
    	
    }
    
	public static void sendMSG(String string, UUID player){
		Bukkit.getServer().getPlayer(player).sendMessage(colorizeText(Main.getPlugin().getConfig().getString("lang.prefix") + string));
	}
    
    
	public static void sendMSG(String string, Player player){
		player.sendMessage(colorizeText(Main.getPlugin().getConfig().getString("lang.prefix") + string));
	}
    
    
    public static String colorizeText(String string) {
        string = string.replaceAll("&0", ChatColor.BLACK+"");
        string = string.replaceAll("&1", ChatColor.DARK_BLUE+"");
        string = string.replaceAll("&2", ChatColor.DARK_GREEN+"");
        string = string.replaceAll("&3", ChatColor.DARK_AQUA+"");
        string = string.replaceAll("&4", ChatColor.DARK_RED+"");
        string = string.replaceAll("&5", ChatColor.DARK_PURPLE+"");
        string = string.replaceAll("&6", ChatColor.GOLD+"");
        string = string.replaceAll("&7", ChatColor.GRAY+"");
        string = string.replaceAll("&8", ChatColor.DARK_GRAY+"");
        string = string.replaceAll("&9", ChatColor.BLUE+"");
        string = string.replaceAll("&a", ChatColor.GREEN+"");
        string = string.replaceAll("&b", ChatColor.AQUA+"");
        string = string.replaceAll("&c", ChatColor.RED+"");
        string = string.replaceAll("&d", ChatColor.LIGHT_PURPLE+"");
        string = string.replaceAll("&e", ChatColor.YELLOW+"");
        string = string.replaceAll("&f", ChatColor.WHITE+"");
        string = string.replaceAll("&l", ChatColor.BOLD+"");
        string = string.replaceAll("&r", ChatColor.WHITE+"");
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
		
      	Inventory GUI = Bukkit.createInventory(null, amount, colorizeText(Main.getPlugin().getConfig().getString("lang.gui")));
      	
		for(String key : Main.getPlugin().getConfig().getConfigurationSection("boost").getKeys(false)){
			if (Main.getPlugin().getConfig().getBoolean("boost." + key + ".enabled") == true){
				i++;
            	int cost = Main.getPlugin().getConfig().getInt("boost." + key + ".cost");
            	int time = Main.getPlugin().getConfig().getInt("boost." + key + ".time");
            	double boost = Main.getPlugin().getConfig().getDouble("boost." + key + ".boost");
            	
            	
            	MainAPI.createDisplay(Material.EXP_BOTTLE,GUI,i,(Main.getPlugin().getConfig().getString("boost." + key + ".title") != null) ? MainAPI.colorizeText(Main.getPlugin().getConfig().getString("boost." + key + ".title")).replaceAll("%boost%", boost+"").replaceAll("%money%", cost + "").replaceAll("%time%", time + "") : MainAPI.colorizeText(Main.getPlugin().getConfig().getString("lang.xptitle").replaceAll("%boost%", boost+"")),MainAPI.colorizeText(Main.getPlugin().getConfig().getString("lang.xplore").replaceAll("%time%", time + "").replaceAll("%money%", cost + "").replaceAll("%boost%", boost + "")));
	            player.openInventory(GUI);
			}
		}
    }
}
