package be.hctel.renaissance.hideandseek.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import be.hctel.renaissance.hideandseek.Hide;

public class DevCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args[0].equalsIgnoreCase("inv")) {
				for(Player p : Bukkit.getOnlinePlayers()) {
					p.hidePlayer(Hide.plugin, player);
				}
			}
			if(args[0].equalsIgnoreCase("checkrawjson")) {
				player.sendMessage(""+Hide.stats.getRawBlockExperience(player, new ItemStack(Material.BEACON)));
			}
		}
		if(args[0].equalsIgnoreCase("ver")) {
			sender.sendMessage(Hide.version);
		}
		return true;
	}
	

}
