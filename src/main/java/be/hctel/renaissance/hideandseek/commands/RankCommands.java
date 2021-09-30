package be.hctel.renaissance.hideandseek.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import be.hctel.renaissance.ranks.Ranks;

public class RankCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("togglerank")) {
				Hide.rankManager.toggleRank(player);
				player.setDisplayName(Hide.rankManager.getRankColor(player) + player.getName());
			} else if (cmd.getName().equalsIgnoreCase("setrank")) {
				if(args.length == 1) {
					try {
						int rank = Utils.convertToInt(args[0]);
						if(rank < 1 || rank > 9) player.sendMessage(Hide.header + ChatMessages.RANKINDEXINVALID.toText());
						else {
							Hide.rankManager.changeRank(player, Ranks.getRank(rank));
							player.setDisplayName(Hide.rankManager.getRankColor(player) + player.getName());
						}
					} catch (NumberFormatException e) {
						player.sendMessage(Hide.header + ChatMessages.RANKINDEXINVALID.toText());
					}
				} else {
					player.sendMessage(Hide.header + ChatMessages.RANKINDEXINVALID.toText());
				}
			}
		}
		return true;
	}

}
