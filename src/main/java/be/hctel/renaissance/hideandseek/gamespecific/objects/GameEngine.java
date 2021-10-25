package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.text.SimpleDateFormat;
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

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameMap;
import be.hctel.renaissance.hideandseek.gamespecific.enums.ItemsManager;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import be.hctel.renaissance.hideandseek.nongame.utils.thirdparty.samagames.ObjectiveSign;

public class GameEngine {
	private Plugin plugin;
	private Random r = new Random();
	private GameMap map;
	private int index;
	private Location seekerSpawn;
	private Location hiderSpawn;
	
	private int timer = 330;
	private boolean warmup = true;
	public static boolean isPlaying = false;
	public static boolean isGameFinished = false;
	
	private ArrayList<Player> hiders = new ArrayList<Player>();
	private ArrayList<Player> seekers = new ArrayList<Player>();
	private ArrayList<Player> queuedSeekers = new ArrayList<Player>();
	
	private HashMap<Player, ObjectiveSign> sidebars = new HashMap<Player, ObjectiveSign>();
	
	public GameEngine(Plugin plugin, GameMap map) {
		this.plugin = plugin;
		this.map = map;
		index = Hide.votesHandler.currentGameMaps.indexOf(map);
	}
	
	public void start() {
		this.hiderSpawn = new Location(Bukkit.getWorld("TEMPWORLD" + index), map.getHiderStart().getX(), map.getHiderStart().getY(), map.getHiderStart().getZ(), map.getHiderStart().getYaw(), map.getHiderStart().getPitch());
		this.seekerSpawn = new Location(Bukkit.getWorld("TEMPWORLD" + index), map.getSeekerStart().getX(), map.getSeekerStart().getY(), map.getSeekerStart().getZ(), map.getSeekerStart().getYaw(), map.getSeekerStart().getPitch());
		isPlaying = true;
		for(Player p : Bukkit.getOnlinePlayers()) {
			sidebars.put(p, new ObjectiveSign(p.getName(), "§b§lHide§a§lAnd§e§lSeek"));
			
			Utils.sendActionBarMessage(p, "");
			Hide.stats.addGame(p);
			hiders.add(p);
			p.setGameMode(GameMode.ADVENTURE);
		}
		Player firstSeeker = getNewSeeker();
		hiders.remove(firstSeeker);
		seekers.add(firstSeeker);
		
		for(Player p : hiders) {
			p.teleport(hiderSpawn);
			Utils.sendCenteredMessage(p, "§6§m————————————————————————————————");
			Utils.sendCenteredMessage(p, "§b§lYou are a §f§lHIDER!");
			Utils.sendCenteredMessage(p, "§aFind a hiding spot before the seeker's released!");
			Utils.sendCenteredMessage(p, "§cThe seeker will be released in §l30 seconds!");
			Utils.sendCenteredMessage(p, "§6§m————————————————————————————————");
		}
		for(Player p : seekers) {
			p.teleport(seekerSpawn);
			p.setGameMode(GameMode.SURVIVAL);
			p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
			p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
			p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
			Utils.sendCenteredMessage(p, "§c§m———————————————————————————————————————————————————");
			p.sendMessage("");
			Utils.sendCenteredMessage(p, "§6§lYou are a §c§lSEEKER!");
			Utils.sendCenteredMessage(p, "§eIt's your job to find hidden block and KILL THEM!");
			p.sendMessage("");
			Utils.sendCenteredMessage(p, "You will be released in §l30 seconds!");
			Utils.sendCenteredMessage(p, "§c§m———————————————————————————————————————————————————");
			sidebars.get(p).setLine(11, hiders.size() + "");
			sidebars.get(p).setLine(8, seekers.size() + "");
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
							}
							warmup = false;
						}
						if(timer == 240) {
							for(Player p : hiders) {
								p.getInventory().setItem(4, ItemsManager.hidersSword());
							}
						}
						if(timer == 10) {
							
						}
						if(timer < 6 && timer > 0) {
							
						}
						timer--;
					} else if (timer == 0) {
						//Endgame
						timer--;
					}
				}
			}			
		}.runTaskTimer(plugin, 0L, 20L);
		
	}
	
	private Player getNewSeeker() {
		if(queuedSeekers.isEmpty()) {
			return hiders.get(r.nextInt(hiders.size()));
		} else {
			return queuedSeekers.get(r.nextInt(queuedSeekers.size()));
		}
	}
}
