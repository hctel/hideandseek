package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import be.hctel.api.scoreboard.DynamicScoreboard;
import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameAchievement;
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
	long tick = 0;
	long every2second = 0;
	long every2secondplus05 = 0;
	
	private int timer = 330;
	private boolean warmup = true;
	public boolean isPlaying = false;
	public boolean isGameFinished = false;
	public boolean blocksLeftGiven = false;
	
	private ArrayList<Player> hiders = new ArrayList<Player>();
	private ArrayList<Player> seekers = new ArrayList<Player>();
	private ArrayList<Player> spectators = new ArrayList<Player>();
	private ArrayList<Player> heartbeat = new ArrayList<Player>();
	private ArrayList<Player> queuedSeekers;
	
	private HashMap<Player, Integer> hiderKills = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> seekerKills = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> deaths = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> points = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> secondsAlive = new HashMap<Player, Integer>();
	
	private HashMap<Player, DynamicScoreboard> sidebars = new HashMap<Player, DynamicScoreboard>();
	public HashMap<Player, DisguiseBlockManager> disguises = new HashMap<Player, DisguiseBlockManager>();
	
	public HashMap<Material, Integer> blocksLeft = new HashMap<Material, Integer>();
	int prevBlockLeftKey = -1;
	public HashMap<Material, ItemStack> blocksLeftItem = new HashMap<Material, ItemStack>();
	
	public HashMap<Player, Integer> durability = new HashMap<Player, Integer>();
	
	private PotionEffect hbeft = new PotionEffect(PotionEffectType.SPEED, 5, 1, false, false);
	
	BukkitTask everySecondTask;
	BukkitTask everyTickTask;
	
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
		blocksLeft.put(Material.STONE, 0);
		this.tauntManager = new TauntManager(plugin);
		for(Player p : Bukkit.getOnlinePlayers()) {
			seekerKills.put(p, 0);
			hiderKills.put(p, 0);
			points.put(p,0);
			deaths.put(p, 0);
			sidebars.put(p, new DynamicScoreboard(Utils.randomString(16), "§b§lHide§a§lAnd§e§lSeek", Bukkit.getScoreboardManager()));
			sidebars.get(p).setLine(14, "§e§lTime left");
			sidebars.get(p).setLine(13, "§8Waiting...");
			sidebars.get(p).setLine(12, "     ");
			sidebars.get(p).setLine(11, "§bHiders");
			sidebars.get(p).setLine(10, "§8Waiting...");
			sidebars.get(p).setLine(9, "      ");
			sidebars.get(p).setLine(8, "§aSeekers");
			sidebars.get(p).setLine(7, "§8Waiting...");
			sidebars.get(p).setLine(6, "       ");
			sidebars.get(p).setLine(5, "§7Points: §f0");
			sidebars.get(p).setLine(4, "§7Kills: §f0");
			sidebars.get(p).setLine(3, "    ");
			sidebars.get(p).setLine(2, "§7§m-------------------");
			sidebars.get(p).setLine(1, "§bhctel§f.§anet         ");
			sidebars.get(p).addReceiver(p);
			Utils.sendActionBarMessage(p, "");
			Hide.stats.addGame(p);
			hiders.add(p);
			p.setGameMode(GameMode.ADVENTURE);
			p.getInventory().clear();
			p.setBedSpawnLocation(hiderSpawn, true);
			
		}
		Player firstSeeker = getNewSeeker();
		if(!queuedSeekers.contains(Bukkit.getPlayer("hctel"))) {
			while(firstSeeker == Bukkit.getPlayer("hctel")) firstSeeker = getNewSeeker();
		}
		hiders.remove(firstSeeker);
		seekers.add(firstSeeker);
		
		unlockAch(firstSeeker, GameAchievement.THECHOSENONE);
		for(Material M : Material.values()) {
			blocksLeft.put(M, 0);
		}
		
		for(Player p : hiders) {
			p.teleport(hiderSpawn);
			if(Hide.blockPicker.playerBlock.get(p) == null) {
				plugin.getLogger().severe(String.format("Had to replace a null block for %s as stone", p.getName()));
				Hide.blockPicker.playerBlock.put(p, new ItemStack(Material.STONE));
			}
			Utils.sendCenteredMessage(p, "§6§m--------------------------------");
			Utils.sendCenteredMessage(p, "§b§lYou are a §f§lHIDER! (" + Utils.getUserItemName(Hide.blockPicker.playerBlock.get(p)) + ")");
			Utils.sendCenteredMessage(p, "§aFind a hiding spot before the seeker's released!");
			Utils.sendCenteredMessage(p, "§cThe seeker will be released in §l30 seconds!");
			Utils.sendCenteredMessage(p, "§6§m--------------------------------");
			disguises.put(p, new DisguiseBlockManager(p, Hide.blockPicker.playerBlock.get(p), plugin));
			blocksLeft.put(Hide.blockPicker.playerItem.get(p), blocksLeft.get(Hide.blockPicker.playerItem.get(p))+1);
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
		everySecondTask = new BukkitRunnable() {
			@Override
			public void run() {
				if(isPlaying) {
					if(timer > 0) {
						for(Player P : hiders) {
							Utils.sendActionBarMessage(P, Hide.stats.getBigLevelProgressBar(P, Hide.blockPicker.playerBlock.get(P)));
							if(Utils.doubleContains(P.getNearbyEntities(5.0, 5.0, 5.0), seekers)) {
								heartbeat.add(P);
								//Utils.sendRedVignette(P);
							} else {
								heartbeat.remove(P);
								//Utils.normalVignette(P);
							}
						}
						if(timer < 304 && timer > 300) {
							Bukkit.broadcastMessage(Hide.header + "§eStarting in §f" + (timer-300));
							for(Player p : Bukkit.getOnlinePlayers()) {
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0f, 1.0f);
							}
						}
						if(timer == 300) {
							Bukkit.broadcastMessage(Hide.header + "§c§lReady or not, here they come!");
							for(Player p : hiders) {
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
							if(hiders.size() > 1 && seekers.size() < 2) {
								Player p = getNewSeeker();
								while(seekers.contains(p)) p = getNewSeeker(); //INFINITE LOOP HERE
								addKill(null, p, true);
								p.sendMessage(Hide.header + "§6You have been selected to help out the first seeker.");
								Bukkit.broadcastMessage(Hide.header + Hide.rankManager.getRankColor(p) + p.getName() + " §6has been selected to help out the first seeker §8§o[Seeker Struggling]");
							}
						}
						if(timer == 230) {
							for(Player p : hiders) {
								p.getInventory().setItem(0, ItemsManager.hidersSword());
							}
						}
						if(timer == 300) {
							blocksLeftGiven = true;
						}
						if(timer == 30) {
							for(Player p : Bukkit.getOnlinePlayers()) p.sendTitle("§c30 seconds remaining", "", 10, 70, 20);
						}
						if(timer < 4 && timer > 0) {
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
								sidebars.get(p).setLine(5, "§7Points: §f" + points.get(p));
								sidebars.get(p).setLine(4, "§7Kills: §f" + (seekerKills.get(p) + hiderKills.get(p)));
							}
						} else {
							for(Player p : sidebars.keySet()) {
								sidebars.get(p).setLine(13, Utils.formatSeconds(timer));
								sidebars.get(p).setLine(10, hiders.size() + " ");
								sidebars.get(p).setLine(7, seekers.size() + "");
								sidebars.get(p).setLine(5, "§7Points: §f" + points.get(p));
								sidebars.get(p).setLine(4, "§7Kills: §f" + (seekerKills.get(p) + hiderKills.get(p)));
							}
						}
						for(Player P : durability.keySet()) {
							if(durability.get(P) < 20) durability.replace(P, durability.get(P)+1);
							Utils.sendActionBarMessage(P, "§6Durability: " + durability.get(P));
						}
						updateBlocksLeftItem();
						timer--;
					} else if (timer == 0) {
						endGame(GameTeam.HIDER);
						timer--;
					}
				}
			}			
		}.runTaskTimer(plugin, 0L, 20L);
		everyTickTask = new BukkitRunnable() {

			@Override
			public void run() {
				if(isPlaying) {
					tick++;
					for(Player p : hiders) {
						disguises.get(p).tick();
					}
					for(Player P : heartbeat) {
						if(every2second-tick > 40) {
							P.addPotionEffect(hbeft);
							P.playSound(P.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 2.0f, 0.8f);
						}
						if(every2second-tick > 45) {
							P.removePotionEffect(PotionEffectType.SPEED);
							P.playSound(P.getLocation(), Sound.BLOCK_NOTE_BASEDRUM, 2.0f, 0.8f);
							every2second = tick-5;
						}
					}
					if(seekers.size() < 1) {
						Player picked = getNewSeeker();
						addKill(null,picked,true);
						picked.sendMessage(Hide.header + "§6You have been selected to become a seeker as all seekers have left!.");
					}
					if(hiders.size() < 1) {
						endGame(GameTeam.SEEKER);
					}
				}
			}
			
		}.runTaskTimer(plugin, 0L, 1L);		
	}
	public boolean areSameTeam(Player a, Player b) {
		return getTeam(a) == getTeam(b);
	}
	public boolean isSeeker(Player player) {
		return seekers.contains(player);
	}
	public boolean isHider(Player player) {
		return hiders.contains(player);
	}
	
	/**
	 * Registers a kill
	 * @param player the killer
	 * @param killed the {@link Player} that was killed
	 * @param seekerKill if the kill was made by a seeker; true if {@link killed} was a hider
	 */
	public void addKill(Player player, Player killed, boolean seekerKill) {
		killed.spigot().respawn();
		for(DisguiseBlockManager D : disguises.values()) {
			D.resendDisguise(killed);
		}
		if(player == null) {
			if(seekerKill) {
				//heartbeat.remove(killed);
				Utils.normalVignette(killed);
				Hide.stats.addDeath(killed);
				Hide.cosmeticManager.addTokens(player, 15);
				deaths.replace(killed, deaths.get(killed)+1);
				Bukkit.broadcastMessage(Hide.header + Hide.stats.getBlockLevelString(killed, Hide.blockPicker.playerBlock.get(killed)) + "§6 " + Utils.getUserItemName(disguises.get(killed).block.getType()) + " " + Hide.rankManager.getRankColor(killed) + killed.getName() + " §6has died");
				hiders.remove(killed);
				seekers.add(killed);
				disguises.get(killed).kill();
				disguises.remove(killed);
				killed.teleport(Hide.votesHandler.currentGameMaps.get(Hide.votesHandler.voted).getSeekerStart());
				Player p = killed;
				int aliveTime = 300-timer;
				secondsAlive.put(p, aliveTime);
				if(timer < 300) {
					p.sendMessage(Hide.header + String.format("§aYou gained §r%d points §aand §b%d tokens §a for surviving §e%d seconds", (int) (aliveTime*0.6), (int) (aliveTime*0.15), aliveTime));
					Hide.stats.addPoints(p, (int) (aliveTime*0.6));
					points.replace(p, points.get(p)+(int) (aliveTime*0.6));
					Hide.cosmeticManager.addTokens(p, (int) (aliveTime*0.15));
				}
				p.teleport(seekerSpawn);
				p.getInventory().clear();
				p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
				p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
				p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
				p.setGameMode(GameMode.SURVIVAL);
				new BukkitRunnable() {
					@Override
					public void run() {
						p.teleport(hiderSpawn);
						p.playSound(p.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.0f);
					}
					
				}.runTaskLater(plugin, 10*20L);
				blocksLeft.put(Hide.blockPicker.playerItem.get(killed), blocksLeft.get(Hide.blockPicker.playerItem.get(killed))-1);
			} else {
				Hide.stats.addDeath(killed);
				deaths.replace(killed, deaths.get(killed)+1);
				Bukkit.broadcastMessage(Hide.header + "§6Seeker " + Hide.rankManager.getRankColor(killed) + killed.getName() + " §6has died.");
				killed.teleport(hiderSpawn);
			}
		} else {
			addPoints(player, 30);
			if(seekerKill) {
				try {
					switch(disguises.get(killed).getBlock().getType()) {
					case FURNACE:
						unlockAch(player, GameAchievement.FURNACE);
						break;
					case ICE:
						unlockAch(player, GameAchievement.ICE);
						break;
					case FLOWER_POT:
						unlockAch(player, GameAchievement.PLANT);
						break;
					case LEAVES:
						unlockAch(player, GameAchievement.LEAF);
						break;
					case ANVIL:
						unlockAch(player, GameAchievement.ANVIL);
						break;
					case BEACON:
						unlockAch(player, GameAchievement.BEACON);
						break;
					case SNOW:
						unlockAch(player, GameAchievement.SNOW);
						break;
					default:
						break;
					
					}
				} catch (NullPointerException e) {
					plugin.getLogger().warning("Error in switch: getBlock().getType() == null!!");
				}
				if(seekers.size() == 1) unlockAch(killed, GameAchievement.PEEKABOO);
				//heartbeat.remove(killed);
				Utils.normalVignette(killed);
				seekerKills.replace(player, seekerKills.get(player)+1);
				Hide.stats.addKilledHider(player);
				Hide.stats.addPoints(player, 30);
				Hide.stats.addDeath(killed);
				Hide.cosmeticManager.addTokens(player, 15);
				player.sendMessage(Hide.header + "§6You gained §b30 points §6and §a15 Tokens §6for killing " + Hide.rankManager.getRankColor(killed) + killed.getName() + "§6.");
				deaths.replace(killed, deaths.get(killed)+1);
				Bukkit.broadcastMessage(Hide.header + Hide.stats.getBlockLevelString(killed, Hide.blockPicker.playerBlock.get(killed)) + "§6 " + Utils.getUserItemName(disguises.get(killed).block.getType()) + " " + Hide.rankManager.getRankColor(killed) + killed.getName() + " §6was killed by " + Hide.rankManager.getRankColor(player) + player.getName());
				hiders.remove(killed);
				seekers.add(killed);
				disguises.get(killed).kill();
				disguises.remove(killed);
				killed.teleport(Hide.votesHandler.currentGameMaps.get(Hide.votesHandler.voted).getSeekerStart());
				sidebars.get(player).setLine(4, "§7Kills: §f" + seekerKills.get(player));
				Player p = killed;
				int aliveTime = 300-timer;
				secondsAlive.put(p, aliveTime);
				if(timer < 300) {
					p.sendMessage(Hide.header + String.format("§aYou gained §r%d points §aand §b%d tokens §a for surviving §e%d seconds", (int) (aliveTime*0.6), (int) (aliveTime*0.15), aliveTime));
					Hide.stats.addPoints(p, (int) (aliveTime*0.6));
					Hide.cosmeticManager.addTokens(p, (int) (aliveTime*0.15));
				}
				p.teleport(seekerSpawn);
				p.getInventory().clear();
				p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
				p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
				p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
				p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
				p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
				if(seekerKills.get(player) == 10) unlockAch(player, GameAchievement.DAREALMVP);
				switch(Hide.stats.getKilledHiders(player)) {
				case 1:
					unlockAch(player, GameAchievement.HIDER1);
					break;
				case 50:
					unlockAch(player, GameAchievement.HIDER50);
					break;
				case 250:
					unlockAch(player, GameAchievement.HIDER250);
					break;
				case 500:
					unlockAch(player, GameAchievement.HIDER500);
					break;
				case 1000:
					unlockAch(player, GameAchievement.HIDER1000);
					break;
				}
				
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
				blocksLeft.put(Hide.blockPicker.playerItem.get(killed), blocksLeft.get(Hide.blockPicker.playerItem.get(killed))-1);
			} else {
				Hide.stats.addKilledSeeker(player);
				Hide.stats.addPoints(player, 30);
				Hide.stats.addDeath(killed);
				Hide.stats.addBlockExperience(player, 30, Hide.blockPicker.playerBlock.get(player));
				Hide.cosmeticManager.addTokens(player, 15);
				player.sendMessage(Hide.header + "§6You gained §b30 points §6and §a15 Tokens §6for killing " + Hide.rankManager.getRankColor(killed) + killed.getName() + "§6.");
				hiderKills.replace(player, hiderKills.get(player)+1);
				deaths.replace(killed, deaths.get(killed)+1);
				Bukkit.broadcastMessage(Hide.header + "§6Seeker " + Hide.rankManager.getRankColor(killed) + killed.getName() + " §6was killed by " + Hide.rankManager.getRankColor(player) + player.getName());
				killed.teleport(hiderSpawn);
				sidebars.get(player).setLine(4, "§7Kills: §f" + hiderKills.get(player));
				switch(Hide.stats.getKilledSeekers(player)) {
				case 1:
					unlockAch(player, GameAchievement.SEEKER1);
					break;
				case 25:
					unlockAch(player, GameAchievement.SEEKER25);
					break;
				}
			}
		}
		
	}
	public void disconnect(OfflinePlayer offlinePlayer) {
		if(hiders.contains(offlinePlayer)) {
			disguises.get(offlinePlayer).kill();
			disguises.remove(offlinePlayer);
			if(!isGameFinished) Bukkit.broadcastMessage(Hide.header + "§6Hider " + Hide.rankManager.getRankColor(offlinePlayer) + offlinePlayer.getName() + " §6has died.");
			hiders.remove(offlinePlayer);
		} else if(seekers.contains(offlinePlayer)) {
			seekers.remove(offlinePlayer);
			if(seekers.size() == 0) {
				Player o = getNewSeeker();
				o.sendMessage(Hide.header + "§7you have been picked to be a seeker as all the previous seekers left");
				addKill(null, o, true);
			}
			if(!isGameFinished) Bukkit.broadcastMessage(Hide.header + "§6Seeker " + Hide.rankManager.getRankColor(offlinePlayer) + offlinePlayer.getName() + " §6has left.");
		}
	}
	private Player getNewSeeker() {
		if(queuedSeekers.size() < 2) {
			return hiders.get(r.nextInt(hiders.size()));
		} else {
			return queuedSeekers.get(r.nextInt(queuedSeekers.size()));
		}
	}
	
	public GameTeam getTeam(Player player) {
		if(hiders.contains(player)) return GameTeam.HIDER;
		else if(seekers.contains(player)) return GameTeam.SEEKER;
		else if(spectators.contains(player)) return GameTeam.SPECTATOR;
		else return GameTeam.SPECTATOR;
	}
	
	public void addSpectator(Player player) {
		spectators.add(player);
		player.setGameMode(GameMode.SPECTATOR);
		player.teleport(map.getHiderStart());	
	}
	
	private void endGame(GameTeam winners) {
		isGameFinished = true;
		isPlaying = false;
		blocksLeftGiven = false;
		everySecondTask.cancel();
		everyTickTask.cancel();
		
		
		
		if(winners == GameTeam.HIDER) {
			new BukkitRunnable() {
				@Override
				public void run() {
					List<Entry<Player, Integer>> seekersByKillsDesc = Utils.entriesSortedByValues(seekerKills);
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
						p.sendTitle("§c§l<< GAME OVER >>", "§eThe Hiders won the game.", 0, 25, 70);
						Utils.sendCenteredMessage(p,"§a§m-------------------------------");
						p.sendMessage("");
						Utils.sendCenteredMessage(p, "§c§lGAME OVER! §eHiders WIN!");
						p.sendMessage("");
						Utils.sendCenteredMessage(p, "§lRemaining Hiders");
						for(Player H : hiders) {
							DisguiseBlockManager mgr = disguises.get(H);
							ItemStack block = mgr.block;
							Utils.sendCenteredMessage(p, String.format("%s%s §7(%s §7%s)", Hide.rankManager.getRankColor(H), H.getName(), Hide.stats.getBlockLevelString(H, block), Utils.getUserItemName(block)));
							mgr.endGameChecks();
							disguises.remove(H);
						}
						String worker = "";
						for(int i = 0; i < seekersByKillsDesc.size(); i++) {
							if(i == 3) break;
							Entry<Player, Integer> entry = seekersByKillsDesc.get(i);
							Player player = entry.getKey();
							int amount = entry.getValue();
							worker += Hide.rankManager.getRankColor(player) + player.getName() + String.format("§6 (%d)§7, ", amount);
						}
						worker = worker.substring(0, worker.length()-2);
						Utils.sendCenteredMessage(p, "§lTop Seekers by Kills");
						Utils.sendCenteredMessage(p, worker);
						p.sendMessage("");
						Utils.sendCenteredMessage(p,"§a§m-------------------------------");
						p.sendMessage(Hide.header + "§3You will be sent to hub in 10 seconds.");
					}
				}
			}.runTask(plugin);
			for(Player p : hiders) {
				Hide.stats.addVictory(p);
				Hide.cosmeticManager.addGoldMedal(p);
				p.sendMessage(Hide.header + "§6You have gained §e50 points§6 and §b30 tokens§6 for winning as a Hider!");
				p.sendMessage(Hide.header + "§e✯ §6Won as hider! §eGold Medal Awarded! §8[" + Hide.cosmeticManager.getGoldMedals(p) + " Total]");
				Hide.stats.addPoints(p, 50);
				Hide.cosmeticManager.addTokens(p, 30);
			}
			if(hiders.size() == 1) {
				for(Player S : seekers) {
					unlockAch(S, GameAchievement.SOCLOSEYETSOFAR);
				}
			}
		} else if(winners == GameTeam.SEEKER) {
			List<Entry<Player, Integer>> seekersByKillsDesc = Utils.entriesSortedByValues(seekerKills);
			List<Entry<Player, Integer>> hidersByHideTime = Utils.entriesSortedByValues(secondsAlive);
			new BukkitRunnable() {
				@Override
				public void run() {
					for(Player p : Bukkit.getOnlinePlayers()) {
						p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_DEATH, 1.0f, 1.0f);
						p.sendTitle("§c§l<< GAME OVER >>", "§eThe Seekers won the game.", 0, 25, 70);
						Utils.sendCenteredMessage(p,"§a§m-------------------------------");
						p.sendMessage("");
						Utils.sendCenteredMessage(p, "§c§lGAME OVER! §cSeekers WIN!");
						p.sendMessage("");
						Utils.sendCenteredMessage(p, "§lTop Seekers by Kills");
						String worker = "";
						for(int i = 0; i < seekersByKillsDesc.size(); i++) {
							if(i == 3) break;
							Entry<Player, Integer> entry = seekersByKillsDesc.get(i);
							Player player = entry.getKey();
							int amount = entry.getValue();
							worker += Hide.rankManager.getRankColor(player) + player.getName() + String.format("§6 (%d)§7, ", amount);
						}
						worker = worker.substring(0, worker.length()-2);
						Utils.sendCenteredMessage(p, worker);
						Utils.sendCenteredMessage(p, "§lTop Hiders by Seconds Alive");
						worker = "";
						for(int i = 0; i < hidersByHideTime.size(); i++) {
							if(i == 3) break;
							Entry<Player, Integer> entry = hidersByHideTime.get(i);
							Player player = entry.getKey();
							int amount = entry.getValue();
							worker += Hide.rankManager.getRankColor(player) + player.getName() + String.format("§6 (%d)§7, ", amount);
						}
						worker = worker.substring(0, worker.length()-2);
						Utils.sendCenteredMessage(p, worker);
						p.sendMessage("");
						Utils.sendCenteredMessage(p,"§a§m-------------------------------");
						p.sendMessage(Hide.header + "§3You will be sent to hub in 10 seconds.");
						try {
							Hide.stats.saveAll();
							Hide.cosmeticManager.saveAll();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}.runTask(plugin);
			Player topSeeker = seekersByKillsDesc.get(0).getKey();
			for(Player p : seekers) {
				Hide.stats.addVictory(p);
				if(p == topSeeker) Hide.cosmeticManager.addGoldMedal(p);
				p.sendMessage(Hide.header + "§6You have gained §e50 points§6 and §b30 tokens§6 for winning as a Seeker!");
				if(p == topSeeker) p.sendMessage(Hide.header + "§e✯ §6Top seeker! §eGold Medal Awarded! §8[" + Hide.cosmeticManager.getGoldMedals(p) + " Total]");
				Hide.stats.addPoints(p, 50);
				Hide.cosmeticManager.addTokens(p, 30);
			}
		}
		for(Player P : sidebars.keySet()) sidebars.get(P).removeReceiver(P);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Hide.bm.send("ServerClose");
				Hide.bm.sendForward("Close");
				for(Player P : Bukkit.getOnlinePlayers()) {
					Hide.bm.sendToServer(P, "HUB01");
				}
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
				try {
					Hide.stats.saveAll();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.runTaskLater(plugin, 195L);
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
			}
		}.runTaskLater(plugin, 250L);
	}
	
	public TauntManager getTauntManager() {
		return tauntManager;
	}
	
	private void updateBlocksLeft() {
		if(!blocksLeftGiven) return;
		if(disguises.isEmpty()) return;
		for(Material M : blocksLeft.keySet()) {
			if(blocksLeft.get(M) > 0) {
				blocksLeftItem.put(M, new ItemStack(M, blocksLeft.get(M)));		
			} else {
				blocksLeftItem.remove(M);
			}
		}
	}
	private void updateBlocksLeftItem() {
		updateBlocksLeft();
		if(blocksLeftGiven && blocksLeftItem.size() != 0) {
			if(prevBlockLeftKey >= blocksLeftItem.size()-1) prevBlockLeftKey = -1;
			prevBlockLeftKey++;
			for(Player P : seekers) {
				P.getInventory().setItem(8, blocksLeftItem.get(blocksLeftItem.keySet().toArray()[prevBlockLeftKey]));
				P.updateInventory();
			}
		}
	}
	/**
	 * 
	 * @param player
	 * @param achievement
	 * @return true if the achievement was added, false if the player already unlocked the achievement
	 */
	public boolean unlockAch(Player player, GameAchievement achievement) {
		if(Hide.stats.getAchievements(player).contains(achievement)) {
			if(Hide.stats.getAchievementProgress(player, achievement) == achievement.getUnlockProgress()) {
				return false;
			}			
		}
		Utils.sendCenteredMessage(player, "§e§m---------------------------------------------------");
		player.sendMessage("");
		Utils.sendCenteredMessage(player, "§lAchievement Unlocked!");
		player.sendMessage("");
		Utils.sendCenteredMessage(player, "§6§l" + achievement.getName());
		Utils.sendCenteredMessage(player, "§7" + achievement.getDescription());
		player.sendMessage("");
		Utils.sendCenteredMessage(player, "§e§m---------------------------------------------------");
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
		Hide.stats.completeAchievement(player, achievement);
		return true;
	}
	
	public void addPoints(Player player, int points) {
		this.points.put(player, this.points.get(player) + points);
	}
	
	public void showHiders(Player player) {
		for(Player p : hiders) {
			if(disguises.get(p).isAlive) {
				plugin.getLogger().info("Showing " + p.getName());
				disguises.get(p).showTo(player);
			}
		}
	}
}
