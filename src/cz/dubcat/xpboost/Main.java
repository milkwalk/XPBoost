package cz.dubcat.xpboost;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.bstats.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.entity.Faction;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.cmds.ClearCommand;
import cz.dubcat.xpboost.cmds.CommandHandler;
import cz.dubcat.xpboost.cmds.FactionCommand;
import cz.dubcat.xpboost.cmds.GiveBoostCommand;
import cz.dubcat.xpboost.cmds.InfoCommand;
import cz.dubcat.xpboost.cmds.GlobalCommand;
import cz.dubcat.xpboost.cmds.GlobalDisableCommand;
import cz.dubcat.xpboost.cmds.GlobalEnableCommand;
import cz.dubcat.xpboost.cmds.ItemCommand;
import cz.dubcat.xpboost.cmds.MainCommand;
import cz.dubcat.xpboost.cmds.OpenGuiCommand;
import cz.dubcat.xpboost.cmds.ReloadCommand;
import cz.dubcat.xpboost.config.ConfigManager;
import cz.dubcat.xpboost.constructors.Database;
import cz.dubcat.xpboost.constructors.Debug;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;
import cz.dubcat.xpboost.constructors.Database.DType;
import cz.dubcat.xpboost.events.ClickListener_18;
import cz.dubcat.xpboost.events.ClickListener_ALL;
import cz.dubcat.xpboost.events.CommandListener;
import cz.dubcat.xpboost.events.ExpListener;
import cz.dubcat.xpboost.events.ExpRestrictions;
import cz.dubcat.xpboost.events.JoinAndQuitEvent;
import cz.dubcat.xpboost.events.ServerList;
import cz.dubcat.xpboost.events.Signs;
import cz.dubcat.xpboost.gui.ClickListener;
import cz.dubcat.xpboost.gui.FactionsClickListener;
import cz.dubcat.xpboost.support.BossBarN;
import cz.dubcat.xpboost.support.Heroes;
import cz.dubcat.xpboost.support.JobsReborn;
import cz.dubcat.xpboost.support.McMMO;
import cz.dubcat.xpboost.support.RPGmE;
import cz.dubcat.xpboost.support.SkillApi;
import cz.dubcat.xpboost.versions.ActionBar1_01;
import cz.dubcat.xpboost.versions.ActionBar1_11;
import cz.dubcat.xpboost.versions.ActionBar1_12;
import cz.dubcat.xpboost.versions.ActionBar1_9;
import cz.dubcat.xpboost.versions.ActionBar1_94;
import cz.dubcat.xpboost.versions.ActionBar1_3;
import cz.dubcat.xpboost.versions.ActionBar_1_8;
import cz.dubcat.xpboost.versions.ActionbarInterface;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	private Logger log;
	public static ConcurrentHashMap<UUID, XPBoost> allplayers  = new ConcurrentHashMap<UUID, XPBoost>();
	public static ConcurrentHashMap<Faction, XPBoost> factions_boost;
	public static GlobalBoost GLOBAL_BOOST;
    public static Economy economy = null;
    public static Debug debug;  
    private static ActionbarInterface actionbar;
    private static Main plugin;
    public static int config_version = 1;
    private static Database db = new Database();
    private Metrics metrics = null;
    
    //lang    
    public static File langFile;
    public static FileConfiguration lang;
    
    //factions
    public static File factions_file;
    public static FileConfiguration factions;
    
    public static File boostFile;
    public static FileConfiguration boostCfg;
    
    public static boolean factions_enabled = false;
    
    public void registerCommands() {
    	CommandHandler handler = new CommandHandler();
    	
        handler.register("xpboost", new MainCommand());
        
        OpenGuiCommand oCmd = new OpenGuiCommand();        
        handler.register("gui", oCmd);
        handler.register("shop", oCmd);
        handler.register("buy", oCmd);
        
        handler.register("info", new InfoCommand());
        handler.register("reload", new ReloadCommand(this));
        handler.register("give", new GiveBoostCommand());
        handler.register("on", new GlobalEnableCommand(this));
        handler.register("off", new GlobalDisableCommand(this));
        handler.register("clear", new ClearCommand());
        handler.register("item", new ItemCommand(this));
        handler.register("global", new GlobalCommand(this));
    	handler.register("faction", new FactionCommand());
    	handler.register("factions", new FactionCommand());
        
        getCommand("xpboost").setExecutor(handler);
        getCommand("xpb").setExecutor(handler);
    }
	
	@Override
    public void onEnable()
    {
    	plugin = this;
    	this.log = getLogger();
    	ConfigManager cfg = new ConfigManager();
    	cfg.loadDefaultConfig();
	    getConfig().options().copyDefaults(true);
	    saveDefaultConfig();
	    
	    if(Main.getPlugin().getConfig().getString("database.type").equalsIgnoreCase("mysql")) {
	    	if(db.loadMysql())
	    		log.info("Connected to the MySQL database.");
	    	else
	    		return;
	    }
	    
	   File boostFileGen = new File(Main.getPlugin().getDataFolder() + "/boosts.yml");   
	   if(!boostFileGen.exists()) {
		    try {	    	
				FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/boosts.yml"), 
						new File(Main.getPlugin().getDataFolder() + "/boosts.yml"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	   }
	    
	    
	    File targetFile = new File(Main.getPlugin().getDataFolder() + "/lang/lang_NL.yml"); 
	    if(!targetFile.exists()) {
	    	InputStream stream  = getClass().getResourceAsStream("/lang/lang_NL.yml");
		    try {
				FileUtils.copyInputStreamToFile(stream, targetFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
		if(!Main.getPlugin().getConfig().contains("settings.config_version") || Main.getPlugin().getConfig().getInt("settings.config_version") < Main.config_version){
			this.log.warning("&cYou config is out of date, regenerate you config fille or add required keys for the full functionality of the plugin. You version: &a" +Main.getPlugin().getConfig().getInt("settings.config_version") + " &cnewest version: &a" +  Main.config_version);
		}
	    
		//language
	    if(getConfig().contains("settings.language")){
	    	 langFile = new File(Main.getPlugin().getDataFolder() + "/lang/lang_"+getConfig().getString("settings.language").toUpperCase()+".yml");
	    	 lang = YamlConfiguration.loadConfiguration(langFile);
	    }else{
	    	langFile = new File(getDataFolder() + "/lang/lang_ENG.yml");
	    	lang = YamlConfiguration.loadConfiguration(langFile);
	    }
	    
    	factions_file = new File(getDataFolder() + "/factions.yml");
    	factions = YamlConfiguration.loadConfiguration(factions_file);
    	
    	boostFile = new File(getDataFolder() + "/boosts.yml");
    	boostCfg = YamlConfiguration.loadConfiguration(boostFile);
	    
	    //load configs
	    cfg.loadLangFile();
    	cfg.loadFactionsFile();    	    
	    
	    //INITIALIZE GLOBAL BOOST
	    GLOBAL_BOOST = new GlobalBoost();
	    
	    //SETUP VAULT
	    setupEconomy();
	    
	    //LOAD DEBUG
	    debug = reloadDebug();
   
        //METRICS
        if (getConfig().getBoolean("settings.metrics")){
        	metrics = new Metrics(this);
        	metrics.addCustomChart(new Metrics.SimplePie("database_type", () -> Database.type.name()));
        }else{
        	MainAPI.debug("Disabling metrics.", Debug.NORMAL);
        }
	    
	    //SUPPORT ----------------------------------------------------------------------------    
	    //MCMMO
        Plugin mcmmo = this.getServer().getPluginManager().getPlugin("mcMMO");
        
        if(mcmmo != null || getServer().getPluginManager().getPlugin("McMMO") != null) { 	
        	log.info("Found McMMO, enabling support.");           
        	getServer().getPluginManager().registerEvents(new McMMO(this), this);
        	
        	if(metrics != null)
        		metrics.addCustomChart(new Metrics.SimplePie("addons", () -> "McMMO"));
        }
        
	    //Heroes
        Plugin heroes = this.getServer().getPluginManager().getPlugin("Heroes");
        
        if(heroes != null) {
        	log.info("Found Heroes, enabling support.");           
        	getServer().getPluginManager().registerEvents(new Heroes(), this);
        	
        	if(metrics != null)
        		metrics.addCustomChart(new Metrics.SimplePie("addons", () -> "Heroes"));
        }
        
	    //SkillAPI
        Plugin skillapi = this.getServer().getPluginManager().getPlugin("SkillAPI");
        
        if(skillapi != null) {
        	log.info("Found SkillAPI, enabling support");        
        	getServer().getPluginManager().registerEvents(new SkillApi(), this);
        	
        	if(metrics != null)
        		metrics.addCustomChart(new Metrics.SimplePie("addons", () -> "SkillAPI"));
        }
        
        
	    //RpgMe
        Plugin rpgme = this.getServer().getPluginManager().getPlugin("RPGme");
        
        if(rpgme != null) {
        	log.info("Found RPGme, enabling support.");           
        	getServer().getPluginManager().registerEvents(new RPGmE(), this);
        	
        	if(metrics != null)
        		metrics.addCustomChart(new Metrics.SimplePie("addons", () -> "RPGme"));
        }
	    
	    //JobsReborn
        Plugin jobsReborn = this.getServer().getPluginManager().getPlugin("Jobs");
        
        if(jobsReborn != null) {
        	log.info("Found Jobs, enabling support.");           
        	getServer().getPluginManager().registerEvents(new JobsReborn(), this);
        	
        	if(metrics != null)
        		metrics.addCustomChart(new Metrics.SimplePie("addons", () -> "Jobs"));
        }     
	    
        Plugin bossbarapi = this.getServer().getPluginManager().getPlugin("BossBarAPI");
        
        //BOSS BAR
        if(bossbarapi != null) {
        	log.info("Found BossBarAPI, enabling support.");
        	new BossBarN().runTaskTimer(Main.getPlugin(), 0, 100);
        	
        	if(metrics != null)
        		metrics.addCustomChart(new Metrics.SimplePie("addons", () -> "BossBarAPI"));
        }
	    
        Plugin factions = this.getServer().getPluginManager().getPlugin("Factions");
        
        //Factions
        if(factions == null) {
        	log.warning("Factions not found, disabling support.");
        }else if(!Main.factions.getBoolean("settings.enabled")){
        	log.warning("Factions support disabled, if you wish to use it, please set enabled to true in the factions.yml");
        }else {
        
        	log.info("Found Factions, enabling support.");
        	factions_boost  = new ConcurrentHashMap<Faction, XPBoost>();
        	factions_enabled = true;
        	
        	MainAPI.loadAllFactions();
        	
    	    FactionXPBoostTask task = new FactionXPBoostTask();
    		task.setId(Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(this, task, 0, 5));		
        	
        	getServer().getPluginManager().registerEvents(new FactionsClickListener(), this);
        	
        	if(metrics != null)
        		metrics.addCustomChart(new Metrics.SimplePie("addons", () -> "Factions"));
        }
        
	    //END SUPPORT ----------------------------------------------------------------------------
        
        
        //register commands
        registerCommands();
	    
	    //XPBOOST TASK
	    new XPBoostTask().runTaskTimerAsynchronously(this, 0, 5);
	    
	    //SETUP ACTION BAR
        if (!setupActionbar()) {
            getLogger().severe("Server version is not compatible with XPBoost!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
        	new ActionBarTask().runTaskTimerAsynchronously(this, 20, getConfig().getLong("settings.actiondelay"));
        }
	    
	    //register events
	    getServer().getPluginManager().registerEvents(new ExpListener(), this);
	    getServer().getPluginManager().registerEvents(new JoinAndQuitEvent(), this);
	    getServer().getPluginManager().registerEvents(new ServerList(), this);
	    getServer().getPluginManager().registerEvents(new CommandListener(), this);
	    getServer().getPluginManager().registerEvents(new ClickListener(), this);
	    getServer().getPluginManager().registerEvents(new Signs(), this);
	    getServer().getPluginManager().registerEvents(new ExpRestrictions(), this);

	    	    
	    //Auto global boost task
	    if(getConfig().getBoolean("settings.periodicalDayCheck"))
	    	new BoostTaskCheck().runTaskTimer(Main.getPlugin(), 0, 100);
	    
        
        
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        
        switch (day) {
			case 2:
				if(getConfig().getBoolean("settings.day.monday")) {
		        	GLOBAL_BOOST.setEnabled(true);
		        	MainAPI.sendMessage("&2WOHOO! Today is the Monday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!", Bukkit.getConsoleSender());
				}			
				break;
			case 3:
				if (getConfig().getBoolean("settings.day.tuesday")) {
		        	GLOBAL_BOOST.setEnabled(true);
		        	MainAPI.sendMessage("&2WOHOO! Today is the Tuesday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!", Bukkit.getConsoleSender());	
				}
				break;
			case 4:
				if(getConfig().getBoolean("settings.day.wednesday")) {
		        	GLOBAL_BOOST.setEnabled(true);
		        	MainAPI.sendMessage("&2WOHOO! Today is the Wednesday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!", Bukkit.getConsoleSender());				
				}
				break;
			case 5:
				if(getConfig().getBoolean("settings.day.thursday")) {
		        	GLOBAL_BOOST.setEnabled(true);
		        	MainAPI.sendMessage("&2WOHOO! Today is the Thursday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!", Bukkit.getConsoleSender());				
				}				
				break;
			case 6:
				if(getConfig().getBoolean("settings.day.friday")) {
		        	GLOBAL_BOOST.setEnabled(true);
		        	MainAPI.sendMessage("&2WOHOO! Today is the Friday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!", Bukkit.getConsoleSender());				
				}				
				break;
			case 7:
				if(getConfig().getBoolean("settings.day.saturday")) {
		        	GLOBAL_BOOST.setEnabled(true);
		        	MainAPI.sendMessage("&2WOHOO! Today is the Saturday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!", Bukkit.getConsoleSender());				
				}				
				break;
			case 1:
				if(getConfig().getBoolean("settings.day.sunday")) {
					GLOBAL_BOOST.setEnabled(true);
					MainAPI.sendMessage("&2WOHOO! Today is the Sunday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!", Bukkit.getConsoleSender());				
				}				
				break;
		}
        
    	initializePlaceholder(); 	
        getLogger().info("Enabled.");
    }
    
    @Override
    public void onDisable(){
    	
    	getLogger().info("Saving players....");
    	for(Player p : Bukkit.getServer().getOnlinePlayers()){
    		MainAPI.savePlayer(p.getUniqueId());
    	}
    	
    	if(factions_enabled){
	    	getLogger().info("Saving factions....");
	    	for(Entry<Faction, XPBoost> pair : factions_boost.entrySet()){
	    		System.out.println(pair.getKey().getName());    		
	    		MainAPI.saveFaction(pair.getKey(), pair.getValue());
	    		MainAPI.debug("Saving active boost for faction " + pair.getKey().getName(), Debug.NORMAL);
	    	}
	    }
    	
    	
    	if(Database.type == DType.MYSQL) {
	    	try {
				Database.getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	
        getLogger().info("Disabled.");
    }
    
    private void initializePlaceholder() {
        if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {        	
            PlaceholderAPI.registerPlaceholder(this, "xpboost_hasboost", event -> String.valueOf(XPBoostAPI.hasBoost(event.getPlayer().getUniqueId())));
            PlaceholderAPI.registerPlaceholder(this, "xpboost_boost", event -> String.valueOf(XPBoostAPI.getBoost(event.getPlayer().getUniqueId()).getBoost()));
            PlaceholderAPI.registerPlaceholder(this, "xpboost_boost_time", event -> String.valueOf(XPBoostAPI.getBoost(event.getPlayer().getUniqueId()).getBoostTime()));
            PlaceholderAPI.registerPlaceholder(this, "xpboost_timeleft", event -> String.valueOf(XPBoostAPI.getBoost(event.getPlayer().getUniqueId()).getTimeRemaining()));           
            PlaceholderAPI.registerPlaceholder(this, "xpboost_type", event -> String.valueOf(XPBoostAPI.getBoost(event.getPlayer().getUniqueId()).getConditions().toString()));
        }
    }
    
    private boolean setupActionbar() {

        String version;

        try {
            version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
        } catch (ArrayIndexOutOfBoundsException whatVersionAreYouUsingException) {
            return false;
        }

        getLogger().info("Server version " + version);

        if (version.equals("v1_8_R3")) {
            actionbar = new ActionBar_1_8();
            getServer().getPluginManager().registerEvents(new ClickListener_18(), this); 
        } else if (version.equals("v1_9_R1")) {
            actionbar = new ActionBar1_9();
            getServer().getPluginManager().registerEvents(new ClickListener_ALL(), this); 
        }else if (version.equals("v1_9_R2")) {
            actionbar = new ActionBar1_94();
            getServer().getPluginManager().registerEvents(new ClickListener_ALL(), this); 
        }else if (version.equals("v1_10_R1")){
        	actionbar = new ActionBar1_01();
        	getServer().getPluginManager().registerEvents(new ClickListener_ALL(), this); 
        }else if(version.equals("v1_11_R1")){
        	actionbar = new ActionBar1_11();
        	getServer().getPluginManager().registerEvents(new ClickListener_ALL(), this); 
        }else if(version.equals("v1_12_R1")) {
        	actionbar = new ActionBar1_12();
        	getServer().getPluginManager().registerEvents(new ClickListener_ALL(), this); 
        } else if(version.equals("v1_13_R1")) {
        	actionbar = new ActionBar1_3();
        	getServer().getPluginManager().registerEvents(new ClickListener_ALL(), this);       	
        }
        
        return actionbar != null;
    }
    
    public static Plugin getPlugin(){
    	return plugin;
    }
    
    public static FileConfiguration getLang(){
    	return lang;
    }
    
    public static Logger getLog(){
    	return Main.getPlugin().getLogger();
    }
    
    public static ActionbarInterface getActionbar() {
		return actionbar;
	}
    
    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    public Debug reloadDebug(){
    	int debug = Main.getPlugin().getConfig().getInt("settings.debug");
    	
    	if(debug == 0)
    		return Debug.OFF;
    	else if(debug == 1)
    		return Debug.NORMAL;
    	else if(debug == 2)
    		return Debug.ALL;
    	
    	return Debug.OFF;
    }
    
    public static Database getDb() {
		return db;
	}
}
