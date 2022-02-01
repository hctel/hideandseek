package be.hctel.renaissance.hideandseek.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameAchievement;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class StatCommands implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("stats")) {
				if(args.length == 0) {
					Utils.sendCenteredMessage(player, "§6§m----------------§f Your stats §6§m-------------");
					player.sendMessage(" §3Points: §b" + Hide.stats.getPoints(player));
					player.sendMessage(" §3Games played: §b" + Hide.stats.getGamesPlayed(player));
					player.sendMessage(" §3Victories: §b" + Hide.stats.getVictories(player));
					player.sendMessage(" §3Total Kills: §b" + (Hide.stats.getKilledHiders(player) + Hide.stats.getKilledSeekers(player)));
					player.sendMessage(" §3Total Deaths: §b" + Hide.stats.getDeaths(player));
					player.sendMessage(" §3Kills as Seeker: §b" + Hide.stats.getKilledHiders(player));
					player.sendMessage(" §3Kills as Hider: §b" + Hide.stats.getKilledSeekers(player));
					player.sendMessage(" §3Achievements: §b" + Hide.stats.getCompletedAchievements(player).size() + "/" + GameAchievement.values().length);
					Utils.sendCenteredMessage(player, "§6§m---------------------------");
					
				} else {
					Player t = Bukkit.getPlayer(args[0]);
					if(t == null) {
						@SuppressWarnings("deprecation")
						OfflinePlayer ot = Bukkit.getOfflinePlayer(args[0]);
						if(!(Hide.stats.isLoaded(ot))) Hide.stats.load(ot);
						Utils.sendCenteredMessage(player, "§6§m---------------- §f§r " + ot.getName() +  "'s stats §6§m-------------");
						player.sendMessage(" §3Points: §b" + Hide.stats.getPoints(ot));
						player.sendMessage(" §3Games played: §b" + Hide.stats.getGamesPlayed(ot));
						player.sendMessage(" §3Victories: §b" + Hide.stats.getVictories(ot));
						player.sendMessage(" §3Total Kills: §b" + (Hide.stats.getKilledHiders(ot) + Hide.stats.getKilledSeekers(ot)));
						player.sendMessage(" §3Total Deaths: §b" + Hide.stats.getDeaths(ot));
						player.sendMessage(" §3Kills as Seeker: §b" + Hide.stats.getKilledHiders(ot));
						player.sendMessage(" §3Kills as Hider: §b" + Hide.stats.getKilledSeekers(ot));
						player.sendMessage(" §3Achievements: §b" + Hide.stats.getCompletedAchievements(ot).size() + "/" + GameAchievement.values().length);
						Utils.sendCenteredMessage(player, "§6§m---------------------------");
					} else {
						Utils.sendCenteredMessage(player, "§6§m---------------- §f§r " + t.getName() +  "'s stats §6§m-------------");
						player.sendMessage(" §3Points: §b" + Hide.stats.getPoints(t));
						player.sendMessage(" §3Games played: §b" + Hide.stats.getGamesPlayed(t));
						player.sendMessage(" §3Victories: §b" + Hide.stats.getVictories(t));
						player.sendMessage(" §3Total Kills: §b" + (Hide.stats.getKilledHiders(t) + Hide.stats.getKilledSeekers(t)));
						player.sendMessage(" §3Total Deaths: §b" + Hide.stats.getDeaths(t));
						player.sendMessage(" §3Kills as Seeker: §b" + Hide.stats.getKilledHiders(t));
						player.sendMessage(" §3Kills as Hider: §b" + Hide.stats.getKilledSeekers(t));
						player.sendMessage(" §3Achievements: §b" + Hide.stats.getCompletedAchievements(t).size() + "/" + GameAchievement.values().length);
						Utils.sendCenteredMessage(player, "§6§m---------------------------");
					}
				}
			}
		} else {
			
		}
		return true;
	}
}
	
