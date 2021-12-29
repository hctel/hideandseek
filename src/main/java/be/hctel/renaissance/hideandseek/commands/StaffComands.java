package be.hctel.renaissance.hideandseek.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameMap;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class StaffComands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("hide.staff")) {
				if(cmd.getName().equals("updateprofile")) {
					if(args.length != 3) {
						player.sendMessage(Hide.header + "§cIncorrect syntax. The expected syntax is /updateprofile <player> <category> <value>");
					} else {
						@SuppressWarnings("deprecation")
						OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
						if(t == null) {
							player.sendMessage(Hide.header + ChatMessages.PLAYER_NOTFOUND);
						} else {
							if(args[1].equalsIgnoreCase("points")) {
								try {
									int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
									Hide.stats.addPoints(t, i);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("victories")) {
								try {
									int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("gamesplayed")) {
								try {
									int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("killsasseeker")) {
								try {
									int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("killsashider")) {
								try {
									int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("deaths")) {
								try {
									int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("blockunlock")) {
								try {
									int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].startsWith("blockexperience.")) {
								
							} else if(args[1].startsWith("blocklevel.")) {
								
							} else if(args[1].startsWith("achievements.")) {
								
							} else if(args[1].equalsIgnoreCase("tokens")) {
								try {
									int i = Utils.convertToInt(args[2]);
									if(!Hide.cosmeticManager.isLoaded(t)) Hide.cosmeticManager.loadPlayer(t);
									Hide.cosmeticManager.setTokens(t, i);
									if(!Bukkit.getOnlinePlayers().contains(t)) Hide.cosmeticManager.unloadPlayer(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								} catch (SQLException e) {
									e.printStackTrace();
								}
							} else {
								player.sendMessage(Hide.header + "§cInvalid category! The following categoies were expected:");
								player.sendMessage(Hide.header + "§6points: adds (or removes) points to the specified player");
								player.sendMessage(Hide.header + "§6victories: adds (or removes) victories amount to the specified player");
								player.sendMessage(Hide.header + "§6gamesplayed: adds (or removes) games played amount to the specified player");
								player.sendMessage(Hide.header + "§6killsasseeker: adds (or removes) kills as seeker to the specified player");
								player.sendMessage(Hide.header + "§6killsashider: adds (or removes) kills as hider to the specified player");
								player.sendMessage(Hide.header + "§6deaths: : adds (or removes) deaths to the specified player");
								player.sendMessage(Hide.header + "§6blockunlock: unlocks the specified item ID for the specified player");
								player.sendMessage(Hide.header + "§6blockexperience.<blockid>: sets the specified experience of the specified block for the specified player.");
								player.sendMessage(Hide.header + "§6blocklevel.<blockid>: sets the specified level of the specified block for the specified player.");
								player.sendMessage(Hide.header + "§6achievements.<achievementid>: sets the specified progress of the specified achievement for the specified player.");
								player.sendMessage(Hide.header + "§aRun /blockid to get the blockid of the block/item you're holding. (ALL can be used to edit all blocks at once).");
								player.sendMessage(Hide.header + "§aRun /findachievement <description> to search for achievements matching a description. (ALL can be used to edit all achievements at once)");
							}
						}
					}
				} else if(cmd.getName().equalsIgnoreCase("gotoworld")) {
					if(args.length == 1) {
							if(!args[0].contains("TEMPWORLD")) {
								player.teleport(GameMap.getFromSystemName(args[0]).getHiderStart());
						}
					}
				}
			} else {
				player.sendMessage(Hide.header + ChatMessages.NOPERM);
			}
		}
		return false;
	}

}
