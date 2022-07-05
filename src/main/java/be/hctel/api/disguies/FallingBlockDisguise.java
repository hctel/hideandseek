package be.hctel.api.disguies;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftFallingBlock;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
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
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class FallingBlockDisguise {
	Player p;
	Plugin plugin;
	Material m;
	byte d;
	boolean isCancelled = false;		
	EntityFallingBlock passenger;
	
	@SuppressWarnings("deprecation")
	public FallingBlockDisguise(Player player, Material m, byte d, Plugin plugin) {
		this.plugin = plugin;	
		this.p = player;
		this.m = m;
		passenger = ((CraftFallingBlock) p.getWorld().spawnFallingBlock(p.getLocation().add(255, 50, 255), m, d)).getHandle();
		restart();
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
											ListenerPriority.NORMAL,
											PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
					@Override
					public void onPacketSending(PacketEvent e) {
						if(e.getPlayer() != p && !isCancelled) {
							WrapperPlayServerNamedEntitySpawn pk = new WrapperPlayServerNamedEntitySpawn(e.getPacket());
							if(pk.getEntityID() == p.getEntityId()) {
								e.setCancelled(true);
								sendToPlayer(e.getPlayer());
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
				
		passenger.setNoGravity(true);
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
			
				if (p == o) {
		    	continue;
		    }

		    ((CraftPlayer) o).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntity(passenger, 70, getDataInt()));
		    ((CraftPlayer) o).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) p).getHandle()));
		}
	}
	
	private void sendToPlayer(Player player) {
		PacketPlayOutEntityDestroy ed = new PacketPlayOutEntityDestroy(p.getEntityId());
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(ed);
		passenger.setNoGravity(true);
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

		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntity(passenger, 70, getDataInt()));
	}
	
	public void cancel() {
		isCancelled = true;
		remove();
		for(Player P : Bukkit.getOnlinePlayers()) if(P != p) ((CraftPlayer) P).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) p).getHandle()));
		PacketPlayOutNamedEntitySpawn ps = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) p).getHandle());
		for(Player P : Bukkit.getOnlinePlayers()) if(P != p) ((CraftPlayer) P).getHandle().playerConnection.sendPacket(ps);
		
	}
	
	@SuppressWarnings("deprecation")
	private int getDataInt() {
		return m.getId() | ((int) d << 0xC);
	}
		
}
