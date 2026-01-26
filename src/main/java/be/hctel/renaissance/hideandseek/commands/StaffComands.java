package be.hctel.renaissance.hideandseek.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;

public class StaffComands implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("hide.staff")) {
				if(cmd.getName().equalsIgnoreCase("gotoworld")) {
					if(args.length == 1) {
							if(args[0] != "TEMPWORLD") {
								if(!args[0].startsWith("HIDE_")) args[0] = "HIDE_" + args[0];
								player.teleport(Hide.mapManager.getMap(Bukkit.getWorld(args[0])).getSpawn());
						}
					}
				}
				else if(cmd.getName().equalsIgnoreCase("showhiders")) {
					if(Hide.preGameTimer.gameStarted) {
						Hide.gameEngine.showHiders(player);
					}
					return true;
				}
			} else {
				player.sendMessage(Hide.header + ChatMessages.NOPERM);
			}
			
		}		
		return false;
	}

}
