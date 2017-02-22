package cz.dubcat.xpboost.cmds;

import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cz.dubcat.xpboost.api.MainAPI;

public class CommandHandler implements CommandExecutor
{

  private static HashMap<String, CommandInterface> commands = new HashMap<String, CommandInterface>();

  public void register(String name, CommandInterface cmd) {

      commands.put(name, cmd);
  }

  public boolean exists(String name) {

      return commands.containsKey(name);
  }

  public CommandInterface getExecutor(String name) {

      return commands.get(name);
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {


          if(args.length == 0) {
              try {
				getExecutor("xpboost").onCommand(sender, cmd, commandLabel, args);
			} catch (SQLException e) {
				e.printStackTrace();
			}
              return true;
          }

          if(args.length > 0) {

              if(exists(args[0])){

                  try {
					getExecutor(args[0]).onCommand(sender, cmd, commandLabel, args);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                  return true;
              } else {
                  MainAPI.sendMSG("This command doesnt exist.", (Player) sender);
                  return true;
              }
          }
      return false;
  }

}