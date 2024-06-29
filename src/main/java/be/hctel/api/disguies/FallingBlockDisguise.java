package be.hctel.api.disguies;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFallingBlock;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.packetwrapper.WrapperPlayServerUpdateAttributes;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityFallingBlock;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity;

public class FallingBlockDisguise {
	Player p;
	Plugin plugin;
	Material m;
	byte d;
	boolean isCancelled = false;
	CraftFallingBlock a;
	EntityFallingBlock passenger;
	BukkitTask resendTask;
	
	@SuppressWarnings("deprecation")
	public FallingBlockDisguise(Player player, Material m, byte d, Plugin plugin) {
		this.plugin = plugin;	
		this.p = player;
		this.m = m;
		a = ((CraftFallingBlock) p.getWorld().spawnFallingBlock(p.getLocation().add(255, 50, 255), m, d));
		a.setGravity(true);
		passenger = a.getHandle();
		//passenger.setNoGravity(true);
		restart();
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
											ListenerPriority.HIGH,
											PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
					@Override
					public void onPacketSending(PacketEvent e) {
						//plugin.getLogger().info("	onPacketSending!");
						if(e.getPlayer() != p && !isCancelled) {
							WrapperPlayServerNamedEntitySpawn pk = new WrapperPlayServerNamedEntitySpawn(e.getPacket());
							if(pk.getEntityID() == p.getEntityId()) {
								plugin.getLogger().info("	Sending disguise to " + e.getPlayer().getName());
								e.setCancelled(true);
								sendToPlayer(e.getPlayer());
							}
						}
					}
		});
		resendTask = new BukkitRunnable() {

			@Override
			public void run() {
				if(!isCancelled) {
					for(org.bukkit.entity.Entity E : p.getNearbyEntities(10, 10, 10)) {
						if(E instanceof Player) {
							sendToPlayer((Player) E);
						}
					}
				}
			}
			
		}.runTaskTimer(plugin, 0L, 5*20L);
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
				ListenerPriority.HIGHEST,
				PacketType.Play.Server.UPDATE_ATTRIBUTES) {
			@Override
			public void onPacketSending(PacketEvent e) {
				//plugin.getLogger().info("onPacketSending 2");
				if(e.getPlayer() != p && !isCancelled) {
					WrapperPlayServerUpdateAttributes pk = new WrapperPlayServerUpdateAttributes(e.getPacket());
					//plugin.getLogger().info("Details: " + pk.getEntityID() + " - " + p.getEntityId() + "");
						if(pk.getEntityID() == p.getEntityId()) {
							e.setCancelled(true);
							plugin.getLogger().info("	Cancelled Update Attributes packet");
						}
				}
			}
		});

	}
	 
	
	
	public void remove() {
		passenger.killEntity();
		PacketPlayOutEntityDestroy ed = new PacketPlayOutEntityDestroy(p.getEntityId());
		for(Player P : Bukkit.getOnlinePlayers()) if(P != p) {
			((CraftPlayer) P).getHandle().playerConnection.sendPacket(ed);
		}
		
	}
	
	
	public FallingBlockDisguise restart() {
		remove();
		send();
		return this;
	}
	
	
	@SuppressWarnings("deprecation")
	private void send() {
		passenger = ((CraftFallingBlock) p.getWorld().spawnFallingBlock(p.getLocation().add(255, 50, 255), m, d)).getHandle();
		passenger.locX = p.getLocation().getX();
		passenger.locY = p.getLocation().getY();
		passenger.locZ = p.getLocation().getZ();
		try {

		    Field f = Entity.class.getDeclaredField("id");
		    f.setAccessible(true);
		    f.setInt(passenger, ((CraftPlayer) p).getHandle().getId());

			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}

			for (Player o : Bukkit.getOnlinePlayers()) {
				PacketPlayOutSpawnEntity pck = new PacketPlayOutSpawnEntity(passenger, 70, getDataInt());
				if (p == o) {
					continue;
				}
				plugin.getLogger().info(String.format("		Sending %s to %s", pck.toString(), o.getName()));
				((CraftPlayer) o).getHandle().playerConnection.sendPacket(pck);
			    ((CraftPlayer) o).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) p).getHandle()));
			
		    }
	}
	
	@SuppressWarnings("deprecation")
	private void sendToPlayer(Player player) {
		if(player == p) return;
		passenger = ((CraftFallingBlock) p.getWorld().spawnFallingBlock(p.getLocation().add(255, 50, 255), m, d)).getHandle();
		passenger.locX = p.getLocation().getX();
		passenger.locY = p.getLocation().getY();
		passenger.locZ = p.getLocation().getZ();
		try {

		    Field f = Entity.class.getDeclaredField("id");
		    f.setAccessible(true);
		    f.setInt(passenger, ((CraftPlayer) p).getHandle().getId());

			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		PacketPlayOutSpawnEntity pck = new PacketPlayOutSpawnEntity(passenger, 70, getDataInt());
		plugin.getLogger().info(String.format("		Sending %s to %s as an individual send", pck.toString(), player.getName()));
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(pck);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) p).getHandle()));
	}
	
	public void cancel() {
		isCancelled = true;
		remove();
		for(Player P : Bukkit.getOnlinePlayers()) if(P != p) ((CraftPlayer) P).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) p).getHandle()));
		PacketPlayOutNamedEntitySpawn ps = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) p).getHandle());
		for(Player P : Bukkit.getOnlinePlayers()) if(P != p) ((CraftPlayer) P).getHandle().playerConnection.sendPacket(ps);
		resendTask.cancel();
	}
	
	@SuppressWarnings("deprecation")
	private int getDataInt() {
		return m.getId() | ((int) d << 0xC);
	}
		
}
