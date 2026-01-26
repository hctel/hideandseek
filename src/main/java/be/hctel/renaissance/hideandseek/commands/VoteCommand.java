package be.hctel.renaissance.hideandseek.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class VoteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 0) {
				Hide.votesHandler.sendMapChoices(player);
			} else {
				try {
					int a = Utils.convertToInt(args[0]);
					Hide.votesHandler.registerPlayerVote(player, a, 1);
				} catch (NumberFormatException e) {
					player.sendMessage(ChatMessages.NAN.toText() + " (1-6).");
				}
			}
 		}
		return true;
	}

}
