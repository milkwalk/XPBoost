package cz.dubcat.xpboost.cmds;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandInterface {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);
}
