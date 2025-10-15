package be.hctel.renaissance.hideandseek.commands;

import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameMap;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.KADetector;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import net.minecraft.server.v1_12_R1.EntityPlayer;

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
									//int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("gamesplayed")) {
								try {
									//int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("killsasseeker")) {
								try {
									//int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("killsashider")) {
								try {
									//int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("deaths")) {
								try {
									//int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].equalsIgnoreCase("blockunlock")) {
								try {
									//int i = Utils.convertToInt(args[2]);
									if(!(Hide.stats.isLoaded(t))) Hide.stats.load(t);
								} catch (NumberFormatException e) {
									player.sendMessage(Hide.header + "§cPlease enter a correct number in the value field.");
								}
							} else if(args[1].contains("blockexperience.")) {
								
							} else if(args[1].contains("blocklevel.")) {
								
							} else if(args[1].contains("achievements.")) {
								
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
							if(args[0] != "TEMPWORLD") {
								if(!args[0].startsWith("HIDE_")) args[0] = "HIDE_" + args[0];
								player.teleport(GameMap.getFromSystemName(args[0]).getHiderStart());
						}
					}
				}
				else if(cmd.getName().equalsIgnoreCase("getskin")) {
					try {
			            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + player.getName());
			            InputStreamReader reader = new InputStreamReader(url.openStream());
			            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

			            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
			            JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
			            String texture = property.get("value").getAsString();
			            String signature = property.get("signature").getAsString();
			            player.sendRawMessage("Skin Data inside console");
			            Hide.plugin.getLogger().info("Skin data: " + texture + " signature: " + signature);
			            
			        } catch (Exception e) {
			            EntityPlayer p = ((CraftPlayer) player).getHandle();
			            GameProfile profile = p.getBukkitEntity().getProfile();
			            Property property = profile.getProperties().get("textures").iterator().next();
			            String texture = property.getValue();
			            String signature = property.getSignature();
			            Hide.plugin.getLogger().info("Skin data: " + texture + " signature: " + signature);
			        }
					return true;
				}
				else if(cmd.getName().equalsIgnoreCase("katest")) {
					KADetector t = new KADetector(Bukkit.getPlayer(args[0]), Hide.plugin);
					t.spawnAndDespawnPlayers();
					player.sendMessage("§aRunning KATest on" + args[0]);
					new BukkitRunnable() {
						@Override
						public void run() {
							player.sendMessage("§aDetected hits: " + t.getPunchedEntities());
						}
					}.runTaskLater(Hide.plugin, 15L);
				} else if(cmd.getName().equalsIgnoreCase("showhiders")) {
					if(Hide.preGameTimer.gameStarted) {
						Hide.gameEngine.showHiders(player);
					}
					return true;
				}
			} else {
				player.sendMessage(Hide.header + ChatMessages.NOPERM);
			}
			
		}
		if(cmd.getName().equalsIgnoreCase("s")) {
			Bukkit.broadcastMessage(Hide.header + "§c§lServer restarting!");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§cYou were sent to a lobby because the server you were previously on was stopped by a staff member");
			for(Player P : Bukkit.getOnlinePlayers()) Hide.bm.sendToServer(P, "HUB01");
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
			try {
				Hide.con.createStatement().execute("UPDATE servers SET status = 'OFF' WHERE name = '" + Hide.bm.getServerName() + "';");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		
		return false;
	}

}
