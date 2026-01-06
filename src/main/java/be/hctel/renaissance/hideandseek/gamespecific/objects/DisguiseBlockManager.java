package be.hctel.renaissance.hideandseek.gamespecific.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftFallingBlock;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import be.hctel.api.disguises.FallingBlockDisguise;
import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameAchievement;
import be.hctel.renaissance.hideandseek.gamespecific.enums.ItemsManager;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;

public class DisguiseBlockManager {
	Player player;
	public ItemStack block;
	Block b = null;
	Plugin plugin;
	Location lastLocation;
	Location solidLocation;
	int timeInLocation = 0;
	int timesWentSolid = 0;
	public boolean isSolid = false;
	boolean isDisguise = false;
	FallingBlockDisguise disguise;
	public boolean isAlive = true;
	private boolean sentMessage = false;
	int fakeEntityId = 0;
	BukkitRunnable run;
	CraftFallingBlock solidPlayerBlock;

	public DisguiseBlockManager(Player player, ItemStack block, Plugin plugin) {
		this.player = player;
		this.block = block;
		this.plugin = plugin;
		this.lastLocation = player.getLocation();
		startDisguise();
		run = new BukkitRunnable() {
			@Override
			public void run() {
				if(isSolid && isAlive) for(Player P : Bukkit.getOnlinePlayers()) if(P != player) P.sendBlockChange(solidLocation, block.getType().createBlockData());
			}
		};
		run.runTaskTimer(plugin, 0L, 20L);
		for(Player P : Bukkit.getOnlinePlayers()) P.hidePlayer(plugin, P);
		
	}
	
	public void tick() {
		if(isAlive) {
			if(Utils.locationComparator(player.getLocation(), lastLocation)) timeInLocation++;
			else timeInLocation = 0;
			if(timeInLocation == 0 && isSolid) {
				makeUnsolid();
			}
			else if(timeInLocation == 20) {
				player.sendTitle("", "§a»§8»»»»", 0, 25, 0);
			}
			else if(timeInLocation == 40) {
				player.sendTitle("", "§a»»§8»»»", 0, 25, 0);
			}
			else if(timeInLocation == 60) {
				player.sendTitle("", "§a»»»§8»»", 0, 25, 0);
			}
			else if(timeInLocation == 80) {
				player.sendTitle("", "§a»»»»§8»", 0, 25, 0);
			}
			else if(timeInLocation >= 100 && !isSolid) {
				makeSolid();
			}
			lastLocation = player.getLocation();
		}
	}
	
	private void makeSolid() {
		if(isAllowedBlock()) {
			stopDisguise(true);
			isSolid = true;
			player.sendTitle("§aYou are now solid", "", 5, 60, 20);
			player.getInventory().setItem(4, ItemsManager.tauntButton);
			solidLocation = Utils.locationFlattenner(lastLocation);
			// player.getWorld().playEffect(player.getLocation(), Effect.COLOURED_DUST, 0, 2);
			player.sendMessage(Hide.header + "§6You are now §ahidden");
			b = solidLocation.getBlock();
			plugin.getLogger().info(player.getName() + " went solid at " + solidLocation.getBlockX() + ", " + solidLocation.getBlockY() + ", " + solidLocation.getBlockZ() + " as a " + block.getType().toString());
			PacketPlayOutEntityDestroy ed = new PacketPlayOutEntityDestroy(player.getEntityId());
			for(Player P : Bukkit.getOnlinePlayers()) if(P != player) ((CraftPlayer) P).getHandle().g.sendPacket(ed);
			solidPlayerBlock = Utils.spawnFakeBlockEntity(player, solidLocation.add(0.5, 0, 0.5), block.getType());
			sentMessage = false;
			timesWentSolid++;
		} else if(!sentMessage) {
			sentMessage = true;
			player.sendMessage(Hide.header + "§cYou can't go solid here!");
			player.sendTitle("§c§l✖", "§6You can't go solid here", 0, 20, 70);
		}
	}
	public void makeUnsolid() {
		isSolid = false;
		timeInLocation = 0;
		player.sendTitle("§6You are now §cvisible!", "", 5, 25, 20);
		player.sendMessage(Hide.header + "§aYou are now visible. §7Be careful!");
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendBlockChange(solidLocation, Material.AIR.createBlockData());
		}
		startDisguise();
		solidPlayerBlock.remove();
	}
	
	private void startDisguise() {
		if(isDisguise) {
			plugin.getLogger().warning("Tried to start the falling block disguise while it was already started!");
			return;
		}
		disguise = new FallingBlockDisguise(plugin, player, block.getType());
		System.out.println(disguise);
		isDisguise = true;
	}
	
	public void stopDisguise(boolean stayVanished) {
		if(!isDisguise) {
			plugin.getLogger().warning("Tried to strop the falling block disguise while it was already stopped!");
			return;
		}
		disguise.cancel(stayVanished);
		isDisguise = false;
	}
	
	public void kill() {
		disguise.cancel(false);
		isSolid = false;
		isAlive = false;
		run.cancel();
		System.out.println("Stopped disguise");
	}
	
	public Block getBlock() {
		return b;
	}
	
	public ItemStack getHideAs() {
		return block;
	}
	
	public void resendDisguise(Player to) {
		if(disguise != null && !to.equals(player)) disguise.sendTo(to);
	}
	
	public void showTo(Player to) {
		if(disguise != null) disguise.cancelFor(to);
	}
	
	public void endGameChecks() {
		if(timesWentSolid == 1) {
			Hide.gameEngine.unlockAch(player, GameAchievement.SETINPLACE);
		}
		kill();
	}
	
	public boolean isAllowedBlock() {
		return lastLocation.getBlock().getType().toString().contains("AIR") || lastLocation.getBlock().getType().toString().contains("FENCE") || lastLocation.getBlock().getType().equals(Material.WATER) || lastLocation.getBlock().getType().equals(Material.LAVA);
	}
	
	
}
