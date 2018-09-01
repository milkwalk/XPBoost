package cz.dubcat.xpboost.versions;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import cz.dubcat.xpboost.api.MainAPI;
import cz.dubcat.xpboost.constructors.Debug;
import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class ActionBar1_12 implements ActionbarInterface {

	@Override
	public void sendActionBarMessage(Player player, String message) {
		CraftPlayer p = (CraftPlayer) player;
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		ChatMessageType type = ChatMessageType.a((byte) 2);
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, type);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
		MainAPI.debug("Sent action bar message to " + p.getName(), Debug.ALL);
	}

}
