package cz.dubcat.xpboost.versions;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitScheduler;

import cz.dubcat.xpboost.Main;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.api.MainAPI.Debug;
import cz.dubcat.xpboost.constructors.XPBoost;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;



public class ActionBar_1_8 implements ActionbarInterface{
	
	
	private Main plugin;
    
    public ActionBar_1_8(Main plugin) {
        this.plugin = plugin;
    }
    
    public void shedul(){ 	
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
            	ConcurrentHashMap<UUID, XPBoost> mp = Main.allplayers;
            	Iterator<Entry<UUID, XPBoost>> it = mp.entrySet().iterator();
            	
            	
            	while (it.hasNext()) {      			
					Map.Entry<UUID, XPBoost> pair = it.next();
        			XPBoost xpb = pair.getValue();

        			if(xpb.getTimeRemaining() > 0){
            	        if(!Main.getPlugin().getConfig().getBoolean("settings.actionbarmsg")){
            	        	return;
            	        }
            	        
            	    	CraftPlayer p = (CraftPlayer) Bukkit.getPlayer(xpb.getUUID());
            	    	String message = MainAPI.colorizeText(Main.getLang().getString("lang.actionbar").replaceAll("%boost%", xpb.getBoost()+""));
            	    	message = message.replaceAll("%timeleft%",xpb.getTimeRemaining()+"");
            	        IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
            	        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
            	        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
            	        
            	        MainAPI.debug("Sent action bar message to " + p.getName(), Debug.ALL);
            	        
        			}
            	}
            }
        }, 0, plugin.getConfig().getInt("settings.actiondelay"));  
    }

}
