package be.hctel.renaissance.hideandseek.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import be.hctel.renaissance.hideandseek.Hide;

public class UtilsCommands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			if(cmd.getName().equalsIgnoreCase("ping")) {
				Player player = (Player) sender;
				ArrayList<Integer> pings = new ArrayList<>();
				player.sendMessage(Hide.header + "§aPong. Calculating your ping. Un momento per favor...");
				new BukkitRunnable() {
					@Override
					public void run() {
						pings.add(player.getPing());
						if(pings.size() == 4) {
							int sum = 0;
							for(int P : pings) {
								sum += P;
							}
							player.sendMessage(Hide.header + "§aYour ping is " + sum/pings.size() + " ms.");
							this.cancel();
						}
					}
					
				}.runTaskTimerAsynchronously(Hide.plugin, 0L, 20L);
			}
		}
		if(cmd.getName().equalsIgnoreCase("whereami")) {
			sender.sendMessage(Hide.header + "§6You are currently playing on §a" + Hide.bm.getServerName());
		}
		return true;
	}

}
