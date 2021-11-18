package be.hctel.renaissance.hideandseek.gamespecific.objects;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MiscDisguise;

public class DisguiseBlockManager {
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
	@SuppressWarnings("deprecation")
	public DisguiseBlockManager(Player player, ItemStack block, Plugin plugin) {
		this.player = player;
		this.block = block;
		this.plugin = plugin;
		this.lastLocation = player.getLocation();
		disguise = new MiscDisguise(DisguiseType.FALLING_BLOCK, block.getType(), block.getData().getData());
		startDisguise();
	}
	
	public void tick() {
		if(isAlive) {
			if(Utils.locationComparator(player.getLocation(), lastLocation)) timeInLocation++;
			else timeInLocation = 0;
			
			
			if(timeInLocation == 0 && isSolid) {
				isSolid = false;
				player.sendTitle("§6You are now §cvisible!", "", 5, 25, 20);
				player.sendMessage(Hide.header + "§aYou are now visible. §7Be careful!");
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
			player.sendTitle("§aYou are now solid", "", 5, 60, 20);
			solidLocation = lastLocation;
			solidLocation.getBlock().setType(block.getType());
			solidLocation.getBlock().setData(block.getData().getData(), false);
			player.sendBlockChange(solidLocation, Material.AIR, (byte) 0);
			player.getWorld().playEffect(player.getLocation(), Effect.COLOURED_DUST, 0);
			player.sendMessage(Hide.header + "§6You are now §ahidden");
			b = solidLocation.getBlock();
			fakeEntityId = Utils.testSpawnFakeBlockEntityNMS(player, solidLocation, block.getType(), block.getData().getData());
			Utils.sendBlockChange(player, Material.AIR, solidLocation);
			isSolid = true;
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.hidePlayer(plugin, player);
			}
		} else {
			player.sendMessage(Hide.header + "§cYou can't go solid here!");
			player.sendTitle("§c§l❌", "§6You can't go solid here", 0, 20, 70);
		}
	}
	private void makeUnsolid() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.showPlayer(plugin, player);
		}
		startDisguise();
		solidLocation.getBlock().setType(Material.AIR);
		Utils.testEntotyDestroyNMS(player, fakeEntityId);
	}
	
	private void startDisguise() {
		disguise.setEntity(player);
		disguise.startDisguise();
	}
	private void stopDisguise() {
		disguise.stopDisguise();
	}
	
	public void kill() {
		isAlive = false;
		stopDisguise();
		
	}
	
	public Block getBlock() {
		return b;
	}
}
