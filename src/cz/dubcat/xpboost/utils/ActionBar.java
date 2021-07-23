package cz.dubcat.xpboost.utils;

import java.lang.reflect.Constructor;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar {
    
    private Class<?> chatSerializer; //old
    
    private Class<?> chatComponent; //common
    private Class<?> packetActionbar;
    
    private Class<?> chatComponentTextClass; //new
    private Class<?> chatMessageTypeClass;
    private Object chatMessageType;
    private boolean oldVersion = false;
    private boolean passUuidToPacketPlayOutChat = false; // from 1.16.1
    
    private boolean canUseSpigotActionBar = false;

    public ActionBar() {
        String rawVersion = Bukkit.getServer().getClass().getPackage()
                .getName();
        String nmsVersion = rawVersion.substring(rawVersion.lastIndexOf(".") + 1);
        
        canUseSpigotActionBar = !nmsVersion.startsWith("v1_9_R") && !nmsVersion.startsWith("v1_8_R");
        
        if(!canUseSpigotActionBar) {
            try {
                this.chatComponent = getNMSClass("IChatBaseComponent");
                this.packetActionbar = getNMSClass("PacketPlayOutChat");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
                this.chatComponentTextClass = getNMSClass("ChatComponentText");
                this.chatMessageTypeClass = getNMSClass("ChatMessageType");
                Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                for (Object obj : chatMessageTypes) {
                    if (obj.toString().equals("GAME_INFO")) {
                        this.chatMessageType = obj;
                    }
                }
            } catch (ClassNotFoundException e) {
                oldVersion = true;
                try {
                    this.chatSerializer = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0];
                } catch (Exception e1) {
                    e1.printStackTrace();
                } 
            }
            
            try {
                Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance("test");
                packetActionbar.getConstructor(new Class<?>[]{chatComponent, chatMessageTypeClass}).newInstance(chatCompontentText, chatMessageType);
            } catch(Exception e) {
                passUuidToPacketPlayOutChat = true;
            }
        }
    }
    
    @SuppressWarnings("deprecation")
    public void sendActionBar(Player player, String message) {
        if (canUseSpigotActionBar) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));

            return;
        }
        
        try {
            Object packet;
            if(oldVersion) {
                Constructor<?> ConstructorActionbar = packetActionbar.getDeclaredConstructor(chatComponent, byte.class);
                Object actionbar = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{\"text\": \"" + message + "\"}");
                packet = ConstructorActionbar.newInstance(actionbar, (byte) 2);
            } else {
                Object chatCompontentText = chatComponentTextClass.getConstructor(new Class<?>[]{String.class}).newInstance(message);
                if(passUuidToPacketPlayOutChat) {
                    packet = packetActionbar.getConstructor(new Class<?>[]{chatComponent, chatMessageTypeClass, UUID.class}).newInstance(chatCompontentText, chatMessageType, player.getUniqueId());
                } else {
                    packet = packetActionbar.getConstructor(new Class<?>[]{chatComponent, chatMessageTypeClass}).newInstance(chatCompontentText, chatMessageType);
                }
            }
            sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private Class<?> getNMSClass(String name) throws ClassNotFoundException {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        return Class.forName("net.minecraft.server." + version + "." + name);
    }
    
    private void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
