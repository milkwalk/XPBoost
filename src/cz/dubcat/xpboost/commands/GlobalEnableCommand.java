package cz.dubcat.xpboost.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.XPBoostMain;
import cz.dubcat.xpboost.api.InternalXPBoostAPI;

public class GlobalEnableCommand implements CommandInterface {

    private XPBoostMain plugin;

    public GlobalEnableCommand(XPBoostMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
            if (!(player.hasPermission("xpboost.admin")) && !(player.hasPermission("xpboost.on"))) {
                return false;
            }
        }

        XPBoostMain.GLOBAL_BOOST.setEnabled(true);

        if (sender instanceof Player) {
            InternalXPBoostAPI.sendMessage("Global &c" + XPBoostMain.GLOBAL_BOOST.getGlobalBoost() + "x Boost&f is now ON.", player);
        } else {
            plugin.getLogger().info("Global " + XPBoostMain.GLOBAL_BOOST.getGlobalBoost() + "x Boost is now ON.");
        }

        return false;
    }

}
