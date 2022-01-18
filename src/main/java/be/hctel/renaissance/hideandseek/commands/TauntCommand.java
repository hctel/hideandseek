package be.hctel.renaissance.hideandseek.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.TauntType;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;

public class TauntCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(Hide.preGameTimer.gameStarted) {
				if(args.length != 1) player.sendMessage(Hide.header + ChatMessages.ARGSMISSING_1);
				else {
					TauntType t = TauntType.getByName(args[0]);
					if(t == null) {
						player.sendMessage(Hide.header + ChatMessages.TAUNTNOTFOUND);
					} else {
						Hide.gameEngine.getTauntManager().tryPerfomTaunt(t, player);
					}
				}
			} else {
				player.sendMessage(Hide.header + ChatMessages.UNAVAILABLE_CMD);
			}
		}
		return true;
	}

}
