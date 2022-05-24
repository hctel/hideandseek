package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import be.hctel.api.scoreboard.DynamicScoreboard;
import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameMap;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameTeam;
import be.hctel.renaissance.hideandseek.gamespecific.enums.ItemsManager;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class GameEngine {
	private Plugin plugin;
	private Random r = new Random();
	private GameMap map;
	private int index;
	private Location seekerSpawn;
	private Location hiderSpawn;
	private TauntManager tauntManager;
	
	private int timer = 330;
	private boolean warmup = true;
	public static boolean isPlaying = false;
	public static boolean isGameFinished = false;
	
	private ArrayList<Player> hiders = new ArrayList<Player>();
	private ArrayList<Player> seekers = new ArrayList<Player>();
	private ArrayList<Player> queuedSeekers;
	
	private HashMap<Player, Integer> hiderKills = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> seekerKills = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> deaths = new HashMap<Player, Integer>();
	
	private HashMap<Player, DynamicScoreboard> sidebars = new HashMap<Player, DynamicScoreboard>();
	public HashMap<Player, DisguiseBlockManager> disguises = new HashMap<Player, DisguiseBlockManager>();
	public HashMap<Player, Integer> durability = new HashMap<Player, Integer>();
	
	public GameEngine(Plugin plugin, GameMap map) {
		this.plugin = plugin;
		this.map = map;
		index = Hide.votesHandler.currentGameMaps.indexOf(map);
	}
	
	public void start() {
		this.queuedSeekers = Hide.preGameTimer.seekerQueue;
		this.hiderSpawn = new Location(Bukkit.getWorld("TEMPWORLD" + index), map.getHiderStart().getX(), map.getHiderStart().getY(), map.getHiderStart().getZ(), map.getHiderStart().getYaw(), map.getHiderStart().getPitch());
		this.seekerSpawn = new Location(Bukkit.getWorld("TEMPWORLD" + index), map.getSeekerStart().getX(), map.getSeekerStart().getY(), map.getSeekerStart().getZ(), map.getSeekerStart().getYaw(), map.getSeekerStart().getPitch());
		isPlaying = true;
		this.tauntManager = new TauntManager(plugin);
		for(Player p : Bukkit.getOnlinePlayers()) {
			sidebars.put(p, new DynamicScoreboard(Utils.randomString(16), "§b§lHide§a§lAnd§e§lSeek", Bukkit.getScoreboardManager()));
			sidebars.get(p).setLine(14, "§e§lTime left");
			sidebars.get(p).setLine(13, "0:30");
			sidebars.get(p).setLine(12, "     ");
			sidebars.get(p).setLine(11, "§bHiders");
			sidebars.get(p).setLine(10, "0 ");
			sidebars.get(p).setLine(9, "      ");
			sidebars.get(p).setLine(8, "§aSeekers");
			sidebars.get(p).setLine(7, "0");
			sidebars.get(p).setLine(6, "       ");
			sidebars.get(p).setLine(5, "§7Points: §f0");
			sidebars.get(p).setLine(4, "§7Kills: §f0");
			sidebars.get(p).setLine(3, "    ");
			sidebars.get(p).setLine(2, "§7----------");
			sidebars.get(p).setLine(1, "§bhctel§f.§anet         ");
			sidebars.get(p).addReceiver(p);
			Utils.sendActionBarMessage(p, "");
			Hide.stats.addGame(p);
			hiders.add(p);
			p.setGameMode(GameMode.ADVENTURE);
			deaths.put(p, 0);
			seekerKills.put(p, 0);
			hiderKills.put(p, 0);
			p.getInventory().clear();
			p.setBedSpawnLocation(hiderSpawn, true);
		}
		Player firstSeeker = getNewSeeker();
		hiders.remove(firstSeeker);
		seekers.add(firstSeeker);
		
		for(Player p : hiders) {
			p.teleport(hiderSpawn);
			Utils.sendCenteredMessage(p, "§6§m--------------------------------");
			Utils.sendCenteredMessage(p, "§b§lYou are a §f§lHIDER! (" + Utils.getUserItemName(Hide.blockPicker.playerBlock.get(p)) + ")");
			Utils.sendCenteredMessage(p, "§aFind a hiding spot before the seeker's released!");
			Utils.sendCenteredMessage(p, "§cThe seeker will be released in §l30 seconds!");
			Utils.sendCenteredMessage(p, "§6§m--------------------------------");
			disguises.put(p, new DisguiseBlockManager(p, Hide.blockPicker.playerBlock.get(p), plugin));
		}
		for(Player p : seekers) {
			p.teleport(seekerSpawn);
			p.setGameMode(GameMode.SURVIVAL);
			p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
			p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
			p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
			Utils.sendCenteredMessage(p, "§c§m---------------------------------------------------");
			Utils.sendCenteredMessage(p, "§6§lYou are a §c§lSEEKER!");
			Utils.sendCenteredMessage(p, "§eIt's your job to find hidden block and KILL THEM!");
			Utils.sendCenteredMessage(p, "You will be released in §l30 seconds!");
			Utils.sendCenteredMessage(p, "§c§m---------------------------------------------------");
			durability.put(p, 20);
		}
		
		//Task running every second (timer decrease, give items, ...)
		new BukkitRunnable() {
			@Override
			public void run() {
				if(isPlaying) {
					if(timer > 0) {
						if(timer < 304 && timer > 300) {
							Bukkit.broadcastMessage(Hide.header + "§eStarting in §f" + (timer-300));
							for(Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
							}
						}
						if(timer == 300) {
							Bukkit.broadcastMessage(Hide.header + "§c§lReady or not, here they come!");
							for(Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1.0f, 1.0f);
							}
							for(Player p : seekers) {
								p.teleport(hiderSpawn);
								p.playSound(p.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
								p.setGameMode(GameMode.SURVIVAL);
							}
							warmup = false;
						}
						if(timer == 240) {
							for(Player p : hiders) {
								p.getInventory().setItem(0, ItemsManager.hidersSword());
							}
						}
						if(timer == 30) {
							for(Player p : Bukkit.getOnlinePlayers()) p.sendTitle("§c30 seconds remaining", "", 10, 70, 20);
						}
						if(timer < 6 && timer > 0) {
							for(Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1.0f, 1.0f);
							}
							Bukkit.broadcastMessage(Hide.header + "§eEnding in §f" + timer);
						}
						if(warmup) {
							for(Player p : sidebars.keySet()) {
								sidebars.get(p).setLine(13, Utils.formatSeconds(timer-300));
								sidebars.get(p).setLine(10, hiders.size() + " ");
								sidebars.get(p).setLine(7, seekers.size() + "");
							}
						} else {
							for(Player p : sidebars.keySet()) {
								sidebars.get(p).setLine(13, Utils.formatSeconds(timer));
								sidebars.get(p).setLine(10, hiders.size() + " ");
								sidebars.get(p).setLine(7, seekers.size() + "");
							}
						}
						for(Player P : durability.keySet()) {
							if(durability.get(P) < 20) durability.replace(P, durability.get(P)+1);
							Utils.sendActionBarMessage(P, "§6Durability: " + durability.get(P));
						}
						timer--;
					} else if (timer == 0) {
						endGame(GameTeam.HIDER);
						timer--;
					}
				}
			}			
		}.runTaskTimer(plugin, 0L, 20L);
		new BukkitRunnable() {

			@Override
			public void run() {
				if(isPlaying) {
					for(Player p : hiders) {
						disguises.get(p).tick();
					}
				}
			}
			
		}.runTaskTimer(plugin, 0L, 1L);
		
	}
	public boolean areSameTeam(Player a, Player b) {
		return (hiders.contains(a) && hiders.contains(b)) || (seekers.contains(a) && seekers.contains(b));
	}
	public boolean isSeeker(Player player) {
		return seekers.contains(player);
	}
	public boolean isHider(Player player) {
		return hiders.contains(player);
	}
	public void addKill(Player player, Player killed, boolean seekerKill) {
		killed.spigot().respawn();
		if(player == null) {
			if(seekerKill) {
				deaths.replace(killed, deaths.get(killed)+1);
				Bukkit.broadcastMessage(Hide.header + "§6Hider " + Hide.rankManager.getRankColor(killed) + killed.getName() + " §6has died.");
				hiders.remove(killed);
				disguises.get(killed).kill();
				disguises.remove(killed);
				killed.teleport(Hide.votesHandler.currentGameMaps.get(Hide.votesHandler.voted).getSeekerStart());
				Player p = killed;
				p.teleport(seekerSpawn);
				p.setGameMode(GameMode.SURVIVAL);
				p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
				p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
				p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
				Utils.sendCenteredMessage(p, "§c§m---------------------------------------------------");
				Utils.sendCenteredMessage(p, "§6§lYou are a §c§lSEEKER!");
				Utils.sendCenteredMessage(p, "§eIt's your job to find hidden block and KILL THEM!");
				Utils.sendCenteredMessage(p, "You will be released in §l30 seconds!");
				Utils.sendCenteredMessage(p, "§c§m---------------------------------------------------");
				new BukkitRunnable() {
					@Override
					public void run() {
						p.teleport(hiderSpawn);
						p.playSound(p.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
					}
					
				}.runTaskLater(plugin, 30*20L);
			} else {
				deaths.replace(killed, deaths.get(killed)+1);
				Bukkit.broadcastMessage(Hide.header + "§6Seeker " + Hide.rankManager.getRankColor(killed) + killed.getName() + " §6has died.");
				killed.teleport(hiderSpawn);
			}
		} else {
			if(seekerKill) {
				seekerKills.replace(player, seekerKills.get(player)+1);
				deaths.replace(killed, deaths.get(killed)+1);
				Bukkit.broadcastMessage(Hide.header + "§6Hider " + Hide.rankManager.getRankColor(killed) + killed.getName() + " §6was killed by " + Hide.rankManager.getRankColor(player) + player.getName());
				hiders.remove(killed);
				seekers.add(killed);
				disguises.get(killed).kill();
				disguises.remove(player);
				killed.teleport(Hide.votesHandler.currentGameMaps.get(Hide.votesHandler.voted).getSeekerStart());
				sidebars.get(player).setLine(4, "§7Kills: §f" + seekerKills.get(player));
				Player p = killed;
				p.teleport(seekerSpawn);
				p.getInventory().clear();
				p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
				p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
				p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
				Utils.sendCenteredMessage(p, "§c§m---------------------------------------------------");
				Utils.sendCenteredMessage(p, "§6§lYou are a §c§lSEEKER!");
				Utils.sendCenteredMessage(p, "§eIt's your job to find hidden block and KILL THEM!");
				Utils.sendCenteredMessage(p, "You will be released in §l10 seconds!");
				Utils.sendCenteredMessage(p, "§c§m---------------------------------------------------");
				durability.put(p, 20);
				new BukkitRunnable() {
					@Override
					public void run() {
						p.teleport(hiderSpawn);
						p.playSound(p.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
						p.setGameMode(GameMode.SURVIVAL);
					}
					
				}.runTaskLater(plugin, 10*20L);
				checkForHidersRemaining();
			} else {
				hiderKills.replace(player, hiderKills.get(player)+1);
				deaths.replace(killed, deaths.get(killed)+1);
				Bukkit.broadcastMessage(Hide.header + "§6Seeker " + Hide.rankManager.getRankColor(killed) + killed.getName() + " §6was killed by " + Hide.rankManager.getRankColor(player) + player.getName());
				killed.teleport(hiderSpawn);
				sidebars.get(player).setLine(4, "§7Kills: §f" + hiderKills.get(player));
			}
		}
	}
	public void disconnect(Player player) {
		if(hiders.contains(player)) {
			disguises.get(player).kill();
			disguises.remove(player);
			if(!isGameFinished) Bukkit.broadcastMessage(Hide.header + "§6Hider " + Hide.rankManager.getRankColor(player) + player.getName() + " §6has died.");
			hiders.remove(player);
		} else if(seekers.contains(player)) {
			seekers.remove(player);
			if(!isGameFinished) Bukkit.broadcastMessage(Hide.header + "§6Seeker " + Hide.rankManager.getRankColor(player) + player.getName() + " §6has left.");
		}
	}
	private Player getNewSeeker() {
		if(queuedSeekers.isEmpty()) {
			return hiders.get(r.nextInt(hiders.size()));
		} else {
			return queuedSeekers.get(r.nextInt(queuedSeekers.size()));
		}
	}
	
	public GameTeam getTeam(Player player) {
		if(hiders.contains(player)) return GameTeam.HIDER;
		else if(seekers.contains(player)) return GameTeam.SEEKER;
		else return GameTeam.SPECTATOR;
	}
	
	private void endGame(GameTeam winners) {
		isGameFinished = true;
		if(winners == GameTeam.HIDER) {
			new BukkitRunnable() {
				@Override
				public void run() {
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
						p.sendTitle("§c§l<< GAME OVER >>", "§eThe Hiders won the game.", 0, 25, 70);
						p.sendMessage(Hide.header + "§cGame Over! §3You will be sent to hub in 10 seconds.");
					}
				}
			}.runTaskAsynchronously(plugin);
			for(Player p : hiders) {
				Hide.stats.addVictory(p);
			}
		} else if(winners == GameTeam.SEEKER) {
			new BukkitRunnable() {
				@Override
				public void run() {
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
						p.sendTitle("§c§l<< GAME OVER >>", "§eThe Seekers won the game.", 0, 25, 70);
						p.sendMessage(Hide.header + "§cGame Over! §3You will be sent to hub in 10 seconds.");
					}
				}
			}.runTaskAsynchronously(plugin);
			for(Player p : seekers) {
				Hide.stats.addVictory(p);
			}
		}
		for(Player P : sidebars.keySet()) sidebars.get(P).removeReceiver(P);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Player P : Bukkit.getOnlinePlayers()) Hide.bm.sendToServer(P, "LOBBY02");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
			}
		}.runTaskLater(plugin, 10*20L);
	}
	private void checkForHidersRemaining() {
		if(hiders.size() < 1) {
			endGame(GameTeam.SEEKER);
		}
	}
	public TauntManager getTauntManager() {
		return tauntManager;
	}
}
