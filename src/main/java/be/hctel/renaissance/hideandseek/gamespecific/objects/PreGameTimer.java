package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.mojang.authlib.GameProfile;

import be.hctel.api.fakeentities.FakePlayer;
import be.hctel.api.runnables.ArgumentRunnable;
import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.ItemsManager;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class PreGameTimer {
	Plugin plugin;
	int minPlayers = Hide.minPlayers;
	int timer = 36;
	BukkitScheduler scheduler;
	FakePlayer seekerQueueNPC;
	
	public boolean mapSelected = false;
	public boolean gameStarted = false;
	public boolean choosingBlock = false;
	
	public final ArrayList<Player> seekerQueue = new ArrayList<Player>();
	
	public PreGameTimer(Plugin plugin) {
		this.plugin = plugin;
		scheduler = Bukkit.getServer().getScheduler();
		
		seekerQueueNPC = new FakePlayer( new Location(Bukkit.getWorld("HIDE_Lobby"), -75.5, 90.01, 65.5, 135.0f, 0.0f), plugin, new GameProfile(UUID.randomUUID(), "§b§lSeeker queue"));
		seekerQueueNPC.setOnRightClickTask(new ArgumentRunnable() {
			@Override
			public void run(Object o) {
				if(o instanceof Player) {
					Player p = (Player) o;
					new BukkitRunnable() {
						@Override
						public void run() {
								if(!seekerQueue.contains(p)) {
									p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
									seekerQueue.add(p);
									Utils.sendCenteredMessage(p, "§e§m-----------------------------");
									p.sendMessage("");
									Utils.sendCenteredMessage(p, "§a§lJoined seeker queue");
									Utils.sendCenteredMessage(p, "§7You now have a chance at starting out as the Seeker.");
									p.sendMessage("");
									Utils.sendCenteredMessage(p, "§e§m-----------------------------");
								} else {
									p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.8f);
									seekerQueue.remove(p);
									Utils.sendCenteredMessage(p, "§e§m-----------------------------");
									p.sendMessage("");
									Utils.sendCenteredMessage(p, "§c§lLeft seeker queue");
									Utils.sendCenteredMessage(p, "§7You left the queue to become the starting Seeker.");
									p.sendMessage("");
									Utils.sendCenteredMessage(p, "§e§m-----------------------------");
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
								Bukkit.broadcastMessage(Hide.header + "§3Voting has ended. §bThe map §f" + Hide.votesHandler.currentGameMaps.get(Hide.votesHandler.voted).getName() + " §bhas won.");
								
								Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
									public void run() {
									}
								});
								Hide.blockPicker = new BlockPicker(Hide.votesHandler.currentGameMaps.get(Hide.votesHandler.voted), plugin);
							}
							for(Player p : Bukkit.getOnlinePlayers()) {
								Utils.sendActionBarMessage(p, "§aStarting in §c§l" + (timer - 16));
								p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
							}
						} else if(timer <= 16 && timer > 5) {
							if(timer == 16) {
								choosingBlock = true;
								for(Player p : Bukkit.getOnlinePlayers()) {
									p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
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
							gameStarted = true;
							Hide.gameEngine.start();
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
		seekerQueueNPC.spawnFor(player);
	}
}
