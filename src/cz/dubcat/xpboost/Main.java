package cz.dubcat.xpboost;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import cz.dubcat.xpboost.GUI.ClickListener;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.xpbAPI;
import cz.dubcat.xpboost.api.MainAPI.Debug;
import cz.dubcat.xpboost.cmds.CommandHandler;
import cz.dubcat.xpboost.cmds.clearCmd;
import cz.dubcat.xpboost.cmds.giveCmd;
import cz.dubcat.xpboost.cmds.globalCmd;
import cz.dubcat.xpboost.cmds.guiCmd;
import cz.dubcat.xpboost.cmds.infoCmd;
import cz.dubcat.xpboost.cmds.itemCommand;
import cz.dubcat.xpboost.cmds.offCmd;
import cz.dubcat.xpboost.cmds.onCmd;
import cz.dubcat.xpboost.cmds.reloadCmd;
import cz.dubcat.xpboost.cmds.xpboostMain;
import cz.dubcat.xpboost.constructors.GlobalBoost;
import cz.dubcat.xpboost.constructors.XPBoost;
import cz.dubcat.xpboost.events.ClickListener_18;
import cz.dubcat.xpboost.events.ClickListener_ALL;
import cz.dubcat.xpboost.events.CommandListener;
import cz.dubcat.xpboost.events.ExpListener;
import cz.dubcat.xpboost.events.ExpRestrictions;
import cz.dubcat.xpboost.events.JoinAndQuitEvent;
import cz.dubcat.xpboost.events.ServerList;
import cz.dubcat.xpboost.events.Signs;
import cz.dubcat.xpboost.support.BossBarN;
import cz.dubcat.xpboost.support.Heroes;
import cz.dubcat.xpboost.support.McMMO;
import cz.dubcat.xpboost.support.RPGmE;
import cz.dubcat.xpboost.support.SkillApi;
import cz.dubcat.xpboost.versions.actionBar;
import cz.dubcat.xpboost.versions.actionBar1_1;
import cz.dubcat.xpboost.versions.actionBar1_11;
import cz.dubcat.xpboost.versions.actionBar1_9;
import cz.dubcat.xpboost.versions.actionBar1_94;
import cz.dubcat.xpboost.versions.actionbarInterface;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	Logger log;
	
	public static ConcurrentHashMap<UUID, XPBoost> allplayers  = new ConcurrentHashMap<UUID, XPBoost>();
	public static GlobalBoost GLOBAL_BOOST;
    public static Economy economy = null;
    
    public static Debug debug;
    
    
    private actionbarInterface actionbar;
    private static Main plugin;
    
    
    public void registerCommands() {
      	 
        CommandHandler handler = new CommandHandler();
 
        handler.register("xpboost", new xpboostMain(this));
            
        handler.register("gui", new guiCmd());
        handler.register("info", new infoCmd(this));
        handler.register("reload", new reloadCmd(this));
        handler.register("give", new giveCmd(this));
        handler.register("on", new onCmd(this));
        handler.register("off", new offCmd(this));
        handler.register("clear", new clearCmd(this));
        handler.register("item", new itemCommand(this));
        handler.register("global", new globalCmd(this));
        getCommand("xpboost").setExecutor(handler);
        getCommand("xpb").setExecutor(handler);
    }
	
    @SuppressWarnings("deprecation")
	@Override
    public void onEnable()
    {
    	plugin = this;
    	this.log = getLogger();
	    getConfig().options().copyDefaults(true);
	    saveDefaultConfig();
	    
	    
	    //INITIALIZE GLOBAL BOOST
	    GLOBAL_BOOST = new GlobalBoost();
	    
	    //SETUP VAULT
	    setupEconomy();
	    
	    //LOAD DEBUG
	    debug = loadDebug();
	    
	    
	    //SUPPORT ----------------------------------------------------------------------------
        
	    
	    //MCMMO
        Plugin mcmmo = this.getServer().getPluginManager().getPlugin("McMMO");
        
        if(mcmmo == null) {
        	log.warning("McMMO not found, disabling support.");
        } else {
        	log.info("Found McMMO, enabling support.");           
        	getServer().getPluginManager().registerEvents(new McMMO(this), this);
        }
        
	    //Heroes
        Plugin heroes = this.getServer().getPluginManager().getPlugin("Heroes");
        
        if(heroes == null) {
        	log.warning("Heroes not found, disabling support.");
        } else {
        	log.info("Found Heroes, enabling support.");           
        	getServer().getPluginManager().registerEvents(new Heroes(), this);
        }
        
	    //SkillAPI
        Plugin skillapi = this.getServer().getPluginManager().getPlugin("SkillAPI");
        
        if(skillapi == null) {
        	log.warning("SkillAPI not found, disabling support.");
        } else {
        	log.info("Found SkillAPI, enabling support.");           
        	getServer().getPluginManager().registerEvents(new SkillApi(), this);
        }
        
        
	    //RpgMe
        Plugin rpgme = this.getServer().getPluginManager().getPlugin("RPGme");
        
        if(rpgme == null) {
        	log.warning("RPGme not found, disabling support.");
        } else {
        	log.info("Found RPGme, enabling support.");           
        	getServer().getPluginManager().registerEvents(new RPGmE(), this);
        }
	    
	    
	    
        Plugin bossbarapi = this.getServer().getPluginManager().getPlugin("BossBarAPI");
        
        //BOSS BAR
        if(bossbarapi == null) {
        	log.warning("BossBarAPI not found, disabling support.");
        } else {
        	log.info("Found BossBarAPI, enabling support.");
        	new BossBarN().runTaskTimer(Main.getPlugin(), 0, 100);
        }
        
	    //END SUPPORT ----------------------------------------------------------------------------
	    
        //register commands
        registerCommands();
	    
	    //REGISTER MAIN XPBOOST TASK
	    XPBoostTask task = new XPBoostTask();
		task.setId(Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(this, task, 0, 5));		
	    
	    //SETUP ACTION BAR
        if (setupActionbar()) {
        	actionbar.shedul();
        } else {
            getLogger().severe("Failed to setup Actionbar!");
            getLogger().severe("Server version is not compatible with XPBoost!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
	    
	    //register
	    getServer().getPluginManager().registerEvents(new ExpListener(), this);
	    getServer().getPluginManager().registerEvents(new JoinAndQuitEvent(), this);
	    getServer().getPluginManager().registerEvents(new ServerList(), this);
	    getServer().getPluginManager().registerEvents(new CommandListener(), this);
	    getServer().getPluginManager().registerEvents(new ClickListener(), this);
	    getServer().getPluginManager().registerEvents(new Signs(), this);
	    getServer().getPluginManager().registerEvents(new ExpRestrictions(), this);

	    
	    
	    //AUTO BOOST ENABLE TASK
	    if(getConfig().getBoolean("settings.periodicalDayCheck"))
	    	new BoostTaskCheck().runTaskTimer(Main.getPlugin(), 0, 100);
	    
	    
        //METRICS
        if (getConfig().getBoolean("settings.metrics")){
	        try {
	            Metrics metrics = new Metrics(this);
	            metrics.start();
	        } catch (IOException e) {
	        	MainAPI.debug("Failed to setup metrics.", Debug.NORMAL);
	        }
        }else{
        	MainAPI.debug("Disabling metrics.", Debug.NORMAL);
        }
        
        
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        
        Bukkit.getConsoleSender().sendMessage("DAY: " + day);
        
        if (getConfig().getBoolean("settings.day.monday") && day == 2){
        	GLOBAL_BOOST.setEnabled(true);
        	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "WOHOO! Today is the Monday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!");
        }
        
        if (getConfig().getBoolean("settings.day.tuesday")&& day == 3){
        	GLOBAL_BOOST.setEnabled(true);
        	Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "WOHOO! Today is the Tuesday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!");
        }
        
    	if (getConfig().getBoolean("settings.day.wednesday") && day == 4){
        	GLOBAL_BOOST.setEnabled(true);
    		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "WOHOO! Today is the Wednesday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!");
    	}
    	
    	if (getConfig().getBoolean("settings.day.thursday") && day == 5){
        	GLOBAL_BOOST.setEnabled(true);
    		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "WOHOO! Today is the Thursday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!");
    	}
    	
    	if (getConfig().getBoolean("settings.day.friday") && day == 6){
        	GLOBAL_BOOST.setEnabled(true);
    		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "WOHOO! Today is the Friday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!");
    	}

    	if (getConfig().getBoolean("settings.day.saturday") && day == 7){
        	GLOBAL_BOOST.setEnabled(true);
    		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "WOHOO! Today is the Saturday! "+GLOBAL_BOOST.getGlobalBoost()+" XP day!");
    	} 
    	
    	if (getConfig().getBoolean("settings.day.sunday") && day == 1){
        	GLOBAL_BOOST.setEnabled(true);
    		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "WOHOO! Today is the SUNDAY! "+GLOBAL_BOOST.getGlobalBoost()+"  XP day!");
    	}
    	
        getLogger().info("Enabled.");
    }
    
    @Override
    public void onDisable(){
    	
    	getLogger().info("Saving players....");
    	//SAVING EVERYONES BOOST
    	for(Player p : Bukkit.getServer().getOnlinePlayers()){
    			MainAPI.savePlayer(p);
    	}
    	
        getLogger().info("Disabled :(");
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
            actionbar = new actionBar(this);
            getServer().getPluginManager().registerEvents(new ClickListener_18(), this); 
        } else if (version.equals("v1_9_R1")) {
            actionbar = new actionBar1_9(this);
            getServer().getPluginManager().registerEvents(new ClickListener_ALL(), this); 
        }else if (version.equals("v1_9_R2")) {
            actionbar = new actionBar1_94(this);
            getServer().getPluginManager().registerEvents(new ClickListener_ALL(), this); 
        }else if (version.equals("v1_10_R1")){
        	actionbar = new actionBar1_1(this);
        	getServer().getPluginManager().registerEvents(new ClickListener_ALL(), this); 
        }else if(version.equals("v1_11_R1")){
        	actionbar = new actionBar1_11(this);
        	getServer().getPluginManager().registerEvents(new ClickListener_ALL(), this); 
        }
        
        return actionbar != null;
    }
    
    public static Plugin getPlugin(){
    	return plugin;
    }
    
    public static Logger getLog(){
    	return Main.getPlugin().getLogger();
    }
    
    private boolean setupEconomy(){
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
    
    private Debug loadDebug(){
    	int debug = Main.getPlugin().getConfig().getInt("settings.debug");
    	
    	if(debug == 0)
    		return Debug.OFF;
    	else if(debug == 1)
    		return Debug.NORMAL;
    	else if(debug == 2)
    		return Debug.ALL;
    	
    	return Debug.OFF;
    }
}
