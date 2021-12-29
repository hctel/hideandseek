package be.hctel.renaissance.hideandseek.gamespecific.objects;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.ItemsManager;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;

public class DisguiseBlockManager2 {
	Player player;
	ItemStack block;
	Block b = null;
	Plugin plugin;
	Location lastLocation;
	Location solidLocation;
	int timeInLocation = 0;
	public boolean isSolid = false;
	MiscDisguise disguise;
	public boolean isAlive = true;
	int fakeEntityId = 0;
	BukkitRunnable run;
	@SuppressWarnings("deprecation")
	public DisguiseBlockManager2(Player player, ItemStack block, Plugin plugin) {
		this.player = player;
		this.block = block;
		this.plugin = plugin;
		this.lastLocation = player.getLocation();
		disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, block.getType(), block.getData().getData());
		startDisguise();
		run = new BukkitRunnable() {
			@Override
			public void run() {
				if(isSolid && isAlive) for(Player P : Bukkit.getOnlinePlayers()) if(P != player) P.sendBlockChange(solidLocation, block.getType(), block.getData().getData());
			}
		};
		run.runTaskTimer(plugin, 0L, 20L);
	}
	
	public void tick() {
		if(isAlive) {
			if(Utils.locationComparator(player.getLocation(), lastLocation)) timeInLocation++;
			else timeInLocation = 0;
			
			
			if(timeInLocation == 0 && isSolid) {
				makeUnsolid();
			}
			else if(timeInLocation == 20) {
				player.sendTitle("", "§a»§8»»»»", 0, 25, 20);
			}
			else if(timeInLocation == 40) {
				player.sendTitle("", "§a»»§8»»»", 0, 25, 20);
			}
			else if(timeInLocation == 60) {
				player.sendTitle("", "§a»»»§8»»", 0, 25, 20);
			}
			else if(timeInLocation == 80) {
				player.sendTitle("", "§a»»»»§8»", 0, 25, 20);
			}
			else if(timeInLocation == 100) {
				makeSolid();
			}
			lastLocation = player.getLocation();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void makeSolid() {
		if(lastLocation.getBlock().getType() == Material.AIR) {
			stopDisguise();
			isSolid = true;
			player.sendTitle("§aYou are now solid", "", 5, 60, 20);
			player.getInventory().setItem(4, ItemsManager.tauntButton);
			solidLocation = Utils.locationFlattenner(lastLocation);
			//solidLocation.getBlock().setType(block.getType());
			//solidLocation.getBlock().setData(block.getData().getData(), false);
			//solidLocation.getBlock().getState().update();
			player.getWorld().playEffect(player.getLocation(), Effect.COLOURED_DUST, 0, 2);
			player.sendMessage(Hide.header + "§6You are now §ahidden");
			b = solidLocation.getBlock();
			fakeEntityId = Utils.spawnBlockTestFGDSHGDFSQGFD(player, solidLocation, block.getTypeId(), block.getData().getData());
			//Utils.sendBlockChange(player, Material.AIR, solidLocation);
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.hidePlayer(plugin, player);
			}
		} else {
			player.sendMessage(Hide.header + "§cYou can't go solid here!");
			player.sendTitle("§c§l✖", "§6You can't go solid here", 0, 20, 70);
		}
	}
	@SuppressWarnings("deprecation")
	public void makeUnsolid() {
		isSolid = false;
		timeInLocation = 0;
		player.sendTitle("§6You are now §cvisible!", "", 5, 25, 20);
		player.sendMessage(Hide.header + "§aYou are now visible. §7Be careful!");
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.showPlayer(plugin, player);
			p.sendBlockChange(solidLocation, Material.AIR, (byte) 0);
		}
		startDisguise();
		//solidLocation.getBlock().setType(Material.AIR);
		Utils.testEntotyDestroyNMS(player, fakeEntityId);
	}
	
	private void startDisguise() {
		disguise.setEntity(player);
		disguise.startDisguise();
	}
	private void stopDisguise() {
		DisguiseAPI.undisguiseToAll(player);
	}
	
	public void kill() {
		isAlive = false;
		stopDisguise();
		run.cancel();
	}
	
	public Block getBlock() {
		return b;
	}
}
