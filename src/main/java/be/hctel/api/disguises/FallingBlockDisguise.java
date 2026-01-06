package be.hctel.api.disguises;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R6.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R6.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftFallingBlock;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityRemoveEvent.Cause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;

import be.hctel.api.protocol.NMSBridge;
import be.hctel.api.protocol.adapters.AdapterPacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityVelocity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.Vec3D;

public class FallingBlockDisguise implements Listener {
	Player player;
	int playerEntityId;
	ProtocolManager pm;
	BlockData hideAs;
	Plugin plugin;
	boolean disguised = true;
	ArrayList<Player> bypassList = new ArrayList<>();
	Vector playerSpeed;
	private BukkitRunnable eachTickRun;
	
	/**
	 * Creates a FallingBlockDisguises and start the disguise immediately
	 * @param plugin the plugin's main class
	 * @param player the player to disguise
	 * @param material the material the Falling Block disguise must be
	 */
	public FallingBlockDisguise(Plugin plugin, Player player, Material material) {
		this.player = player;
		this.plugin = plugin;
		this.playerEntityId = player.getEntityId();
		this.pm = ProtocolLibrary.getProtocolManager();
		this.hideAs = material.createBlockData();
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
		for(Player P : plugin.getServer().getOnlinePlayers()) {
			if(!P.equals(player))
				sendTo(P);
		}
		pm.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.SPAWN_ENTITY) {
			@Override
			public void onPacketSending(PacketEvent e) {
				AdapterPacketPlayOutSpawnEntity packet = new AdapterPacketPlayOutSpawnEntity(e.getPacket());
				if(packet.getEntityType() == EntityType.PLAYER) {
					if(packet.getEntityId() == playerEntityId && isDisguised()) {
						if(bypassList.contains(e.getPlayer())) return;
						e.setCancelled(true);
						sendTo(e.getPlayer());
					}
				}
			}
		});
		pm.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.UPDATE_ATTRIBUTES, PacketType.Play.Server.ENTITY_METADATA, PacketType.Play.Server.ANIMATION, PacketType.Play.Server.COLLECT) {
			@Override
			public void onPacketSending(PacketEvent e) {
				int entityId = e.getPacket().getIntegers().getValues().get(0);
				if(playerEntityId == entityId && isDisguised() && !bypassList.contains(e.getPlayer())) {
					e.setCancelled(true);
				}
			}
		});
//		pm.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.ENTITY_POSITION_SYNC) {
//			@Override
//			public void onPacketSending(PacketEvent e) {
//				if(!test) {
//					int entityId = e.getPacket().getIntegers().getValues().get(0);
//					if(playerEntityId == entityId && isDisguised() && !bypassList.contains(e.getPlayer())) {
//						ClientboundEntityPositionSyncPacket pck = (ClientboundEntityPositionSyncPacket) e.getPacket().getHandle();
//						PositionMoveRotation nmsPos = pck.e();
////						new ClientboundEntityPositionSyncPacket(entityId, new PositionMoveRotation(nmsPos.a(), new Vec3D(player.getVelocity().getX(), player.getVelocity().getY(), player.getVelocity().getZ()), nmsPos.c(), nmsPos.d()), pck.f());
////						e.setPacket(new PacketContainer(PacketType.Play.Server.ENTITY_POSITION_SYNC, pck));
//						Vec3D toSendInstead = new Vec3D((player.isSprinting() ? 0 : player.getVelocity().getX()), 0, (player.isSprinting() ? 0 : player.getVelocity().getZ()));
//						PositionMoveRotation newNmsPos = new PositionMoveRotation(nmsPos.a(), toSendInstead, nmsPos.c(), nmsPos.d());
//						ClientboundEntityPositionSyncPacket newPck = new ClientboundEntityPositionSyncPacket(pck.b(), newNmsPos, true);
//						e.setPacket(PacketContainer.fromPacket(newPck));
////						System.out.printf("Packet: (%f, %f, %f), ", newPck.e().b().g, newPck.e().b().h, newPck.e().b().i);
////						System.out.printf("Player: (%f, %f, %f)\n", player.getVelocity().getX(), player.getVelocity().getY(), player.getVelocity().getZ());
//					}
//				}
//			}
//		});
//		pm.addPacketListener(new PacketAdapter(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.ENTITY_VELOCITY) {
//			@Override
//			public void onPacketSending(PacketEvent e) {
//				if(!test) {
//					int entityId = e.getPacket().getIntegers().getValues().get(0);
//					if(playerEntityId == entityId && isDisguised() && !bypassList.contains(e.getPlayer())) {
//						PacketPlayOutEntityVelocity pck = (PacketPlayOutEntityVelocity) e.getPacket().getHandle();
////						Vec3D vel = pck.e();
//						Vec3D toSendInstead = new Vec3D((player.isSprinting() ? 0 : player.getVelocity().getX()), 0, (player.isSprinting() ? 0 : player.getVelocity().getZ()));
//						PacketPlayOutEntityVelocity newPck = new PacketPlayOutEntityVelocity(pck.b(), toSendInstead);
//						e.setPacket(PacketContainer.fromPacket(newPck));
////						System.out.printf("Velocity Packet: (%f, %f, %f), ", vel.g, vel.h, vel.i);
////						System.out.printf("Velocity Player: (%f, %f, %f)\n", player.getVelocity().getX(), player.getVelocity().getY(), player.getVelocity().getZ());
//					}
//				}
//			}
//		});
		eachTickRun = new BukkitRunnable() {
			
			@Override
			public void run() {
				if(disguised) {
					if(!player.getVelocity().equals(playerSpeed)) {
						playerSpeed = player.getVelocity();
						Vec3D vec = new Vec3D(playerSpeed.getX(), playerSpeed.getY(), playerSpeed.getZ());
						PacketPlayOutEntityVelocity pck = new PacketPlayOutEntityVelocity(playerEntityId, vec);
						for(Player P : plugin.getServer().getOnlinePlayers()) {
							if(!P.equals(player)) {
								NMSBridge.getPlayerConnection(P).sendPacket(pck);
							}
						}
					}
				} else {
					cancel();
				}
			}
		};
		eachTickRun.runTaskTimerAsynchronously(plugin, 0L, 1L);
	}
	
	/**
	 * Sends the disguise to a player
	 * @param sendTo who to send the disguise to
	 * @throws IllegalArgumentException if the player specified is the player being disguised
	 */
	public void sendTo(Player sendTo) {
		if(sendTo.equals(player)) {
			throw new IllegalArgumentException(String.format("Cannot send a disguise to the disguised player (%s -> %s)", player.getName(), sendTo.getName()));
		}
		EntityFallingBlock eFallingBlock = EntityFallingBlock.a(((CraftWorld) player.getWorld()).getHandle(), ((CraftBlock) player.getLocation().getBlock()).getPosition(), getIBlockData(hideAs));
		CraftFallingBlock cFallingBlock = (CraftFallingBlock) eFallingBlock.getBukkitEntity();
		cFallingBlock.setCancelDrop(true);
		cFallingBlock.setGravity(false);
		cFallingBlock.getEntityId();
		DataWatcher dw = eFallingBlock.aC();
		AdapterPacketPlayOutSpawnEntity pck = new AdapterPacketPlayOutSpawnEntity(ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY));
		pck.setEntityId(player.getEntityId());
		pck.setUniqueId(eFallingBlock.cT());
		pck.setEntityType(EntityType.FALLING_BLOCK);
		pck.setEntityData(getDataInt(hideAs));
		pck.setX(player.getLocation().getX());
		pck.setY(player.getLocation().getY()+0.1);
		pck.setZ(player.getLocation().getZ());
		eFallingBlock.discard(Cause.DESPAWN);
		PacketPlayOutEntityMetadata pck2 = new PacketPlayOutEntityMetadata(player.getEntityId(), dw.c());
		PlayerConnection con = NMSBridge.getPlayerConnection(sendTo);
		sendTo.showPlayer(plugin, player);
		con.sendPacket(pck.getHandle());
		con.sendPacket(pck2);
	}
	
	/**
	 * Returns whether the disguise is currently active
	 * @return true if the disguise is active - false if it isn't
	 */
	public boolean isDisguised() {
		return disguised;
	}
	
	/**
	 * Reveals the disguised player to another player
	 * @param forWho who the disguised player must be revealed to
	 */
	public void cancelFor(Player forWho) {
		if(forWho.equals(player)) {
			throw new IllegalArgumentException(String.format("Cannot reveal the disguised player to itself (%s -> %s)", player.getName(), forWho.getName()));
		}
		bypassList.add(forWho);
		AdapterPacketPlayOutSpawnEntity respawnPacket = new AdapterPacketPlayOutSpawnEntity(player);
		PacketPlayOutEntityDestroy ed = new PacketPlayOutEntityDestroy(player.getEntityId());
		PlayerConnection con = NMSBridge.getPlayerConnection(forWho);
		con.sendPacket(ed);
		con.sendPacket(respawnPacket.getHandle());
		forWho.showPlayer(plugin, player);
		sendAvatarMetadata(forWho);
	}
	
	/**
	 * Stops the disguise
	 * @param stayVanished if the player should stay invisible after cancelling the disguise
	 */
	public void cancel(boolean stayVanished) {
		if(!disguised) plugin.getLogger().log(Level.WARNING, "Tried to cancel a disguise while it has already been cancelled.");
		disguised = false;
		PacketPlayOutEntityDestroy ed = new PacketPlayOutEntityDestroy(player.getEntityId());
		AdapterPacketPlayOutSpawnEntity respawnPacket = new AdapterPacketPlayOutSpawnEntity(player);
		for(Player P : plugin.getServer().getOnlinePlayers()) {
			PlayerConnection con = NMSBridge.getPlayerConnection(P);
			if(!P.equals(player)) {
				con.sendPacket(ed);
				if(stayVanished) {
					P.hidePlayer(plugin, player);
				} else {
					P.showPlayer(plugin, player);
					con.sendPacket(respawnPacket.getHandle());
					sendAvatarMetadata(P);
				}
			}			
		}
	}
	
	/**
	 * Restarts the disguise
	 * @return A new FallingBlockDisguise object
	 */
	public FallingBlockDisguise restart() {
		return new FallingBlockDisguise(plugin, player, hideAs.getMaterial());
	}	
	
	/**
	 * Gets the NMS data int from a BlockData. Internal use
	 * @param m ({@link org.bukkit.block.data.BlockData})
	 * @return
	 */
	private int getDataInt(BlockData m) {
		IBlockData nmsBlockData = getIBlockData(m);
		return net.minecraft.world.level.block.Block.j(nmsBlockData);
	}
	
	/**
	 * Gets the NMS IBlockData from a Spigot BlockData. Internal use.
	 * @param m ({@link org.bukkit.block.data.BlockData})
	 * @return
	 */
	private IBlockData getIBlockData(BlockData m) {
		CraftBlockData craftBlockData = ((CraftBlockData) m);
		return craftBlockData.getState();
	}
	
	private void sendAvatarMetadata(Player sendTo) {
		EntityData<Byte> data = new EntityData<Byte>(16, EntityDataTypes.BYTE, (byte) 0x7F);
		List<EntityData<?>> li = new ArrayList<EntityData<?>>(); 
		li.add(data);
		WrapperPlayServerEntityMetadata pck = new WrapperPlayServerEntityMetadata(playerEntityId, li);
		PacketEvents.getAPI().getPlayerManager().sendPacket(sendTo, pck);
	}
	
//	private void setGlow(Player player, Player receiver) {
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
//        packet.getIntegers().write(0, player.getEntityId()); //Set packet's entity id
//        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
//		//WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
//        watcher.setEntity(player); //Set the new data watcher's target
//        byte currentData = watcher.getByte(0);
//        watcher.setObject(0, currentData | 0x40); //Set status to glowing, found on protocol page
//        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
//    }
//	
//	private void setUnglow(Player player, Player receiver) {
//        PacketContainer packet = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);
//        packet.getIntegers().write(0, player.getEntityId()); //Set packet's entity id
//        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
//        //WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
//        watcher.setEntity(player); //Set the new data watcher's target
//        byte currentData = watcher.getByte(0);
//        watcher.setObject(0, currentData ^ 0x40); //Set status to glowing, found on protocol page
//        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
//    }
	
	@EventHandler
	public void onItemPickup(EntityPickupItemEvent e) {
		if(e.getEntity() instanceof Player && isDisguised()) {
			Player p = (Player) e.getEntity();
			if(p.equals(player)) {
				System.out.println("Triggered entity pickup");
				e.setCancelled(true);
				ItemStack item = e.getItem().getItemStack();
				e.getItem().remove();
				p.getInventory().addItem(item);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
			}
		}
	}
}
	