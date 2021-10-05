package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import be.hctel.renaissance.hideandseek.gamespecific.enums.GameMap;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import net.minecraft.server.v1_12_R1.Slot;

public class GameEngine {
	private Plugin plugin;
	private BukkitScheduler scheduler;
	private Random r = new Random();
	private GameMap map;
	
	private int timer = 330;
	private int ticksTimer = 6600;
	private boolean warmup = true;
	
	private ArrayList<Player> hiders = new ArrayList<Player>();
	private ArrayList<Player> seekers = new ArrayList<Player>();
	private ArrayList<Player> queuedSeekers = new ArrayList<Player>();
	
	public GameEngine(Plugin plugin, GameMap map) {
		this.plugin = plugin;
		this.scheduler = Bukkit.getServer().getScheduler();
		this.map = map;
	}
	
	public void start() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			hiders.add(p);
		}
		Player firstSeeker = getNewSeeker();
		hiders.remove(firstSeeker);
		seekers.add(firstSeeker);
		
		for(Player p : hiders) {
			p.teleport(new Location(Bukkit.getWorld("TEMPWORLD"), map.getHiderStart().getX(), map.getHiderStart().getY(), map.getHiderStart().getZ(), map.getHiderStart().getYaw(), map.getHiderStart().getPitch()));
			Utils.sendCenteredMessage(p, "ｧ6ｧm覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧");
			Utils.sendCenteredMessage(p, "ｧbｧlYou are a ｧfｧlHIDER!");
			Utils.sendCenteredMessage(p, "ｧaFind a hiding spot before the seeker's released!");
			Utils.sendCenteredMessage(p, "ｧcThe seeker will be released in ｧl30 seconds!");
			Utils.sendCenteredMessage(p, "ｧ6ｧm覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧");
		}
		for(Player p : seekers) {
			p.teleport(new Location(Bukkit.getWorld("TEMPWORLD"), map.getSeekerStart().getX(), map.getSeekerStart().getY(), map.getSeekerStart().getZ(), map.getSeekerStart().getYaw(), map.getSeekerStart().getPitch()));
			p.setGameMode(GameMode.SURVIVAL);
			p.getInventory().setItem(0, new ItemStack(Material.DIAMOND_SWORD));
			p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
			p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
			Utils.sendCenteredMessage(p, "ｧcｧl覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧�");
			p.sendMessage("");
			Utils.sendCenteredMessage(p, "ｧ6ｧlYou are a ｧcｧlSEEKER!");
			Utils.sendCenteredMessage(p, "ｧeIt's your job to find hidden block and KILL THEM!");
			p.sendMessage("");
			Utils.sendCenteredMessage(p, "You will be released in ｧl30 seconds!");
			Utils.sendCenteredMessage(p, "ｧcｧl覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧覧�");
		}
	}
	
	private Player getNewSeeker() {
		if(queuedSeekers.isEmpty()) {
			return hiders.get(r.nextInt(hiders.size()));
		} else {
			return queuedSeekers.get(r.nextInt(queuedSeekers.size()));
		}
	}
}
