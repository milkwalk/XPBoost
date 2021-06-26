package cz.dubcat.xpboost.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.api.InternalXPBoostAPI;
import cz.dubcat.xpboost.api.XPBoostAPI;
import cz.dubcat.xpboost.constructors.Debug;
import cz.dubcat.xpboost.constructors.XPBoost;

public class ClearCommand implements CommandInterface {

    private static final String MESSAGE = "You have successfully removed boost from &a";

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (sender.hasPermission("xpboost.admin")) {
            if (args.length == 2) {
                XPBoost xpb = null;

                if (Bukkit.getServer().getPlayer(args[1]) != null && Bukkit.getServer().getPlayer(args[1]).isOnline()) {
                    Player hrac = Bukkit.getPlayer(args[1]);
                    UUID id = hrac.getUniqueId();
                    xpb = XPBoostAPI.getBoost(id);

                    if (xpb == null)
                        return true;

                    xpb.clear();
                    InternalXPBoostAPI.sendMessage(MESSAGE + hrac.getName(), sender);
                } else if (Bukkit.getServer().getOfflinePlayer(args[1]) != null) {
                    OfflinePlayer p = Bukkit.getServer().getOfflinePlayer(args[1]);
                    UUID id = p.getUniqueId();
                    xpb = XPBoostAPI.getOfflineBoost(p.getUniqueId());

                    if (xpb == null)
                        return true;

                    xpb.clear();
                    xpb.saveBoost();

                    InternalXPBoostAPI.debug("XPBoost clear CMD for offlineplayer ID " + id, Debug.NORMAL);
                    InternalXPBoostAPI.sendMessage(MESSAGE + p.getName(), sender);
                }
            } else {
                InternalXPBoostAPI.sendMessage("Usage: &c/xpboost clear <player>", sender);
            }
        }
        return true;
    }
}
