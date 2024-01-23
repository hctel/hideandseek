package be.hctel.renaissance.hideandseek.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.renaissance.hideandseek.Hide;

public class SignCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length < 1) {
				player.sendMessage(Hide.header + "§cIncorrect command syntax!");
			} else {
				StringBuilder brc = new StringBuilder();
				for(String part : args) {
					brc.append(part + " ");
				}
				Hide.signer.addEditor(player, brc.toString());
			}
		}
		return true;
	}

}
