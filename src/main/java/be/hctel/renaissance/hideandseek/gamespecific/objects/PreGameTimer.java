package be.hctel.renaissance.hideandseek.gamespecific.objects;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.commonclass.VotesHandler;
import be.hctel.renaissance.hideandseek.gamespecific.enums.ItemsManager;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class PreGameTimer {
	Plugin plugin;
	int minPlayers = 2;
	int timer = 36;
	BukkitScheduler scheduler;
	
	public boolean mapSelected = false;
	public boolean gameStarted = false;
	public boolean choosingBlock = false;
	
	public PreGameTimer(Plugin plugin) {
		this.plugin = plugin;
		
		scheduler = Bukkit.getServer().getScheduler();
		
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
						} else if(timer <= 21 && timer > 16) {
							if(timer == 21) {
								Hide.votesHandler.endVotes();
								Bukkit.broadcastMessage(Hide.header + "§3Voting has ended. §bThe map §f" + Hide.votesHandler.voted.getMapName() + " §bhas won.");
								Hide.mapLoader.loadWorldToTempWorld(Hide.votesHandler.voted);
								Hide.blockPicker = new BlockPicker(Hide.votesHandler.voted, Hide.stats);
							}
							for(Player p : Bukkit.getOnlinePlayers()) {
								Utils.sendActionBarMessage(p, "§aStarting in §c§l" + (timer - 16));
								p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
							}
						} else if(timer <= 16 && timer > 5) {
							if(timer == 16) {
								for(Player p : Bukkit.getOnlinePlayers()) {
									p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1.0f, 1.0f);
									Utils.sendActionBarMessage(p, "§eChoose your Block! §8| §aStarting in §l" + timer);
									Hide.blockPicker.buildBlockSelector(p);
									p.getInventory().clear();
									p.getInventory().setItem(4, ItemsManager.blockSelector());
								}
							} else {
								for(Player p : Bukkit.getOnlinePlayers()) {
									Utils.sendActionBarMessage(p, "§eChoose your Block! §8| §aStarting in §l" + timer);
								}
							}
						} else if(timer <= 5 && timer != 0) {
							for(Player p : Bukkit.getOnlinePlayers()) {
								Utils.sendActionBarMessage(p, "§eChoose your Block! §8| §aStarting in §c§l" + timer);
								p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
							}
						} else if(timer == 0) {
							//GameManager Start
						}
						if(timer != 0) {
							timer--;
						}
					}
				}
				
			}
		}, 0L, 20L);
	}
}
