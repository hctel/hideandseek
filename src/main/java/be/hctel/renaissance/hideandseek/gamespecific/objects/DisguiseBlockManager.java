package be.hctel.renaissance.hideandseek.gamespecific.objects;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import be.hctel.api.disguies.FallingBlockDisguise;
import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.ItemsManager;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import net.minecraft.server.v1_12_R1.EntityFallingBlock;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;

public class DisguiseBlockManager {
	Player player;
	public ItemStack block;
	Block b = null;
	Plugin plugin;
	Location lastLocation;
	Location solidLocation;
	int timeInLocation = 0;
	public boolean isSolid = false;
	FallingBlockDisguise disguise;
	public boolean isAlive = true;
	int fakeEntityId = 0;
	BukkitRunnable run;
	EntityFallingBlock solidPlayerBlock;
	@SuppressWarnings("deprecation")
	public DisguiseBlockManager(Player player, ItemStack block, Plugin plugin) {
		this.player = player;
		this.block = block;
		this.plugin = plugin;
		this.lastLocation = player.getLocation();
		player.setPlayerListName(null);
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
				ListenerPriority.NORMAL,
				PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
			@Override
			public void onPacketSending(PacketEvent e) {
				if(e.getPlayer() != player && isAlive) {
					WrapperPlayServerNamedEntitySpawn pk = new WrapperPlayServerNamedEntitySpawn(e.getPacket());
					if(pk.getEntityID() == player.getEntityId()) {
						e.setCancelled(true);
					}
				}
			}
		});
		startDisguise();
		run = new BukkitRunnable() {
			@Override
			public void run() {
				if(isSolid && isAlive) for(Player P : Bukkit.getOnlinePlayers()) if(P != player) P.sendBlockChange(solidLocation, block.getType(), block.getData().getData());
			}
		};
		run.runTaskTimer(plugin, 0L, 20L);
		for(Player P : Bukkit.getOnlinePlayers()) P.hidePlayer(P);
		
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
	
	@SuppressWarnings("deprecation")
	private void makeSolid() {
		if(isAllowedBlock()) {
			stopDisguise();
			isSolid = true;
			player.sendTitle("§aYou are now solid", "", 5, 60, 20);
			player.getInventory().setItem(4, ItemsManager.tauntButton);
			solidLocation = Utils.locationFlattenner(lastLocation);
			player.getWorld().playEffect(player.getLocation(), Effect.COLOURED_DUST, 0, 2);
			player.sendMessage(Hide.header + "§6You are now §ahidden");
			b = solidLocation.getBlock();
			plugin.getLogger().info(player.getName() + " went solid at " + solidLocation.getBlockX() + ", " + solidLocation.getBlockY() + ", " + solidLocation.getBlockZ() + " as a " + block.getType().toString());
			PacketPlayOutEntityDestroy ed = new PacketPlayOutEntityDestroy(player.getEntityId());
			for(Player P : Bukkit.getOnlinePlayers()) if(P != player) ((CraftPlayer) P).getHandle().playerConnection.sendPacket(ed);
			solidPlayerBlock = Utils.spawnFakeBlockEntity(player, solidLocation.add(0.5, 0, 0.5), block.getType(), block.getData().getData());
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
			p.sendBlockChange(solidLocation, Material.AIR, (byte) 0);
		}
		startDisguise();
		solidPlayerBlock.killEntity();
	}
	
	@SuppressWarnings("deprecation")
	private void startDisguise() {
		if(disguise == null) disguise = new FallingBlockDisguise(player, block.getType(), block.getData().getData(), plugin).restart();
		else disguise.restart();
	}
	private void stopDisguise() {
		disguise.cancel();
	}
	
	public void kill() {
		isAlive = false;
		stopDisguise();
		run.cancel();
		for(Player P : Bukkit.getOnlinePlayers()) P.showPlayer(plugin, player);
		player.setPlayerListName(player.getName());
	}
	
	public Block getBlock() {
		return b;
	}
	
	public boolean isAllowedBlock() {
		return lastLocation.getBlock().getType() == Material.AIR || lastLocation.getBlock().getType() == Material.STATIONARY_WATER || lastLocation.getBlock().getType() == Material.WATER || lastLocation.getBlock().getType() == Material.LAVA || lastLocation.getBlock().getType() == Material.STATIONARY_LAVA || lastLocation.getBlock().getType() == Material.FENCE || lastLocation.getBlock().getType() == Material.ACACIA_FENCE || lastLocation.getBlock().getType() == Material.BIRCH_FENCE || lastLocation.getBlock().getType() == Material.DARK_OAK_FENCE || lastLocation.getBlock().getType() == Material.JUNGLE_FENCE || lastLocation.getBlock().getType() == Material.NETHER_FENCE || lastLocation.getBlock().getType() == Material.SPRUCE_FENCE;
	}
	
}
