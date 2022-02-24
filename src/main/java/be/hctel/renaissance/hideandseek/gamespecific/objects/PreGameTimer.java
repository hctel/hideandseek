package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.mojang.authlib.GameProfile;

import be.hctel.api.fakeentities.FakePlayer;
import be.hctel.api.runnables.ArgumentRunnable;
import be.hctel.api.scoreboard.DynamicScoreboard;
import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.ItemsManager;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class PreGameTimer {
	Plugin plugin;
	int minPlayers = 2;
	int timer = 36;
	BukkitScheduler scheduler;
	FakePlayer seekerQueueNPC;
	
	public boolean mapSelected = false;
	public boolean gameStarted = false;
	public boolean choosingBlock = false;
	
	private HashMap<Player, DynamicScoreboard> sidebars = new HashMap<Player, DynamicScoreboard>();
	public final ArrayList<Player> seekerQueue = new ArrayList<Player>();
	
	public PreGameTimer(Plugin plugin) {
		this.plugin = plugin;
		scheduler = Bukkit.getServer().getScheduler();
		
		seekerQueueNPC = new FakePlayer(((CraftWorld) Bukkit.getWorld("HIDE_Lobby")).getHandle(), new GameProfile(UUID.fromString("fef039ef-e6cd-4987-9c84-26a3e6134277"), "§bSeeker queue"), new Location(Bukkit.getWorld("HIDE_Lobby"), -75.5, 90.01, 65.5, 135.0f, 0.0f), plugin);
		seekerQueueNPC.setSkin(UUID.fromString("4c13b583-2355-465f-aad8-d202d621176b"));
		seekerQueueNPC.setOnRightClickTask(new ArgumentRunnable() {
			@Override
			public void run(Object o) {
				System.out.println("start runnable");
				if(o instanceof String) {
					Player p = Bukkit.getPlayer((String) o);
					new BukkitRunnable() {
						@Override
						public void run() {
							int a = 0;
							if(a < 1) {
								System.out.println("cast");
								if(!seekerQueue.contains(p)) {
									p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
									System.out.println("sound");
									seekerQueue.add(p);
								} else {
									p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1.0f, 0.675f);
									seekerQueue.remove(p);
								}	
							}
						}
					}.runTaskAsynchronously(plugin);
				}
			}
		});
			
		scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				
				if(!gameStarted) {
					if(Bukkit.getOnlinePlayers().size() < minPlayers) {
						if(minPlayers - Bukkit.getOnlinePlayers().size() == 1) {
							for(Player p : Bukkit.getOnlinePlayers()) {
								Utils.sendActionBarMessage(p, "§e" + (minPlayers - Bukkit.getOnlinePlayers().size()) + " player needed to start...");
							}
						} else {
							for(Player p : Bukkit.getOnlinePlayers()) {
								Utils.sendActionBarMessage(p, "§e" + (minPlayers - Bukkit.getOnlinePlayers().size()) + " players needed to start...");
							}
						}
					} else if(Bukkit.getOnlinePlayers().size() < minPlayers && timer != 36) {
						Bukkit.broadcastMessage(Hide.header + ChatMessages.STARTCANCELLED.toText());
						timer = 36;
						for(Player p : Bukkit.getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 0.5f);
						}
					} else if(Bukkit.getOnlinePlayers().size() >= minPlayers) {
						if(timer > 21) {
							for(Player p : Bukkit.getOnlinePlayers()) {
								Utils.sendActionBarMessage(p, "§aStarting in §l" + (timer - 16));
							}
							if(timer == 31) {
								Hide.votesHandler.sendMapChoices();
							}
						} else if(timer <= 21 && timer > 16) {
							if(timer == 21) {
								Hide.votesHandler.endVotes();
								Bukkit.broadcastMessage(Hide.header + "§3Voting has ended. §bThe map §f" + Hide.votesHandler.currentGameMaps.get(Hide.votesHandler.voted).getMapName() + " §bhas won.");
								Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
									public void run() {
									}
								});
								Hide.blockPicker = new BlockPicker(Hide.votesHandler.currentGameMaps.get(Hide.votesHandler.voted), Hide.stats);
							}
							for(Player p : Bukkit.getOnlinePlayers()) {
								Utils.sendActionBarMessage(p, "§aStarting in §c§l" + (timer - 16));
								p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
							}
						} else if(timer <= 16 && timer > 5) {
							if(timer == 16) {
								choosingBlock = true;
								for(Player p : Bukkit.getOnlinePlayers()) {
									p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1.0f, 1.0f);
									Utils.sendActionBarMessage(p, "§eChoose your Block! §8| §aStarting in §l" + timer);
									Hide.blockPicker.buildBlockSelector(p);
									p.getInventory().clear();
									p.getInventory().setItem(4, ItemsManager.blockSelector());
								}
								Hide.gameEngine = new GameEngine(plugin, Hide.votesHandler.currentGameMaps.get(Hide.votesHandler.voted));
							} else {
								for(Player p : Bukkit.getOnlinePlayers()) {
									Utils.sendActionBarMessage(p, "§eChoose your Block! §8| §aStarting in §l" + timer);
								}
							}
						} else if(timer <= 5 && timer > 0) {
							for(Player p : Bukkit.getOnlinePlayers()) {
								Utils.sendActionBarMessage(p, "§eChoose your Block! §8| §aStarting in §c§l" + timer);
								p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
							}
						} else if(timer == 0) {
							for(Player p : sidebars.keySet()) sidebars.get(p).removeReceiver(p);
							Hide.gameEngine.start();
							gameStarted = true;
						}
						if(timer > -1) {
							timer--;
						}
					}
				}
				
			}
		}, 0L, 20L);
	}
	public void loadPlayer(Player player) {
		sidebars.put(player, new DynamicScoreboard(player.getName(), "§eYour HIDE stats", Bukkit.getScoreboardManager()));
		sidebars.get(player).setLine(Hide.stats.getPoints(player), "§bPoints", false);
		sidebars.get(player).setLine(Hide.cosmeticManager.getTokens(player), "§aTokens", false);
		sidebars.get(player).setLine(Hide.stats.getGamesPlayed(player), "§bGames Played", false);
		sidebars.get(player).setLine(Hide.stats.getDeaths(player), "§bTotal Deaths", false);
		sidebars.get(player).setLine(Hide.stats.getKills(player), "§bTotal Kills", false);
		sidebars.get(player).setLine(Hide.stats.getKilledHiders(player), "§bKills as Seeker", false);
		sidebars.get(player).setLine(Hide.stats.getVictories(player), "§bVictories", false);
		sidebars.get(player).setLine(Hide.stats.getKilledSeekers(player), "§bKills as Hider", false);
		sidebars.get(player).addReceiver(player);
		seekerQueueNPC.spawnFor(player);
	}
}
