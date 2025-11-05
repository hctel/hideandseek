package be.hctel.api.disguies;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_21_R6.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity.ObjectTypes;
import com.comphenix.packetwrapper.WrapperPlayServerUpdateAttributes;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import net.minecraft.world.level.block.state.IBlockData;


public class FallingBlockDisguise {
	Player p;
	Plugin plugin;
	BlockData m;
	boolean isCancelled = false;
	FallingBlock passenger;
	
	private ArrayList<Player> excludeList = new ArrayList<>();
	
	@SuppressWarnings("deprecation")
	public FallingBlockDisguise(Player player, Material m, Plugin plugin) {
		this.plugin = plugin;	
		this.p = player;
		this.m = m.createBlockData();
		FallingBlock b = player.getWorld().spawnFallingBlock(player.getLocation().clone().add(0,255,0), this.m);
		b.setGravity(false);
		b.setInvulnerable(true);
		b.setDropItem(false);
		restart();
		
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
											ListenerPriority.HIGH,
											PacketType.Play.Server.NAMED_ENTITY_SPAWN) {
					@Override
					public void onPacketSending(PacketEvent e) {
						if(e.getPlayer() != p) {
							restart(e.getPlayer());
						}
					}
		});
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin,
				ListenerPriority.HIGHEST,
				PacketType.Play.Server.UPDATE_ATTRIBUTES) {
			@Override
			public void onPacketSending(PacketEvent e) {
				//plugin.getLogger().info("onPacketSending 2");
				if((e.getPlayer() != p | !excludeList.contains(e.getPlayer())) && !isCancelled) {
					WrapperPlayServerUpdateAttributes pk = new WrapperPlayServerUpdateAttributes(e.getPacket());
					//plugin.getLogger().info("Details: " + pk.getEntityID() + " - " + p.getEntityId() + "");
						if(pk.getEntityID() == p.getEntityId()) {
							e.setCancelled(true);
							//plugin.getLogger().info("	Cancelled Update Attributes packet");
						}
				}
			}
		});

	}
	 
	
	
	public void remove() {
		passenger.remove();
		ProtocolManager mgr = ProtocolLibrary.getProtocolManager();
		PacketContainer packet = mgr.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
		packet.getIntegers().write(0, p.getEntityId());
		for(Player P : Bukkit.getOnlinePlayers()) {
			mgr.sendServerPacket(P, packet);
		}
		
	}
	
	
	public FallingBlockDisguise restart() {
		remove();
		send();
		return this;
	}
	
	private void restart(Player player) {
		CraftPlayer p = (CraftPlayer) player;
		ProtocolManager mgr = ProtocolLibrary.getProtocolManager();
		PacketContainer packet1 = mgr.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
		packet1.getIntegers().write(0, p.getEntityId());
		mgr.sendServerPacket(player, packet1);
		WrapperPlayServerPlayerInfo packet2 = new WrapperPlayServerPlayerInfo();
		packet2.setAction(PlayerInfoAction.ADD_PLAYER);
		WrappedGameProfile profile = new WrappedGameProfile(this.p.getUniqueId(), this.p.getName());
		WrappedChatComponent displayName = WrappedChatComponent.fromText(this.p.getDisplayName());
		PlayerInfoData data = new PlayerInfoData(profile,this.p.getPing(), NativeGameMode.fromBukkit(this.p.getGameMode()), displayName);
		packet2.setData(Arrays.asList(data));
		mgr.sendServerPacket(p, packet2.getHandle());
		//p.getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer) this.p).getHandle()));
		WrapperPlayServerSpawnEntity packet3 = new WrapperPlayServerSpawnEntity(passenger, ObjectTypes.FALLING_BLOCK, getDataInt());
		packet3.setEntityID(this.p.getEntityId());
		mgr.sendServerPacket(p, packet3.getHandle());
	}
	
	
	private void send() {
		ProtocolManager mgr = ProtocolLibrary.getProtocolManager();
		passenger = p.getWorld().spawnFallingBlock(p.getLocation().add(0,255,0), m);
		passenger.setGravity(false);
		passenger.setInvulnerable(true);
		passenger.setDropItem(false);
		WrapperPlayServerSpawnEntity packet1 = new WrapperPlayServerSpawnEntity(passenger, ObjectTypes.FALLING_BLOCK, getDataInt());
		packet1.setEntityID(this.p.getEntityId());
//		WrapperPlayServerPlayerInfoRemove packet2 = new WrapperPlayServerPlayerInfoRemove();
//		packet2.setProfileIds(Arrays.asList(new UUID[] {this.p.getUniqueId()}));
		for(Player P : Bukkit.getOnlinePlayers()) {
			if(P.equals(this.p) | excludeList.contains(P)) continue;
			mgr.sendServerPacket(P, packet1.getHandle());
		}
	}
	
	public void sendToPlayer(Player player) {
		ProtocolManager mgr = ProtocolLibrary.getProtocolManager();
		passenger = p.getWorld().spawnFallingBlock(p.getLocation().add(0,255,0), m);
		passenger.setGravity(false);
		passenger.setInvulnerable(true);
		passenger.setDropItem(false);
		WrapperPlayServerSpawnEntity packet1 = new WrapperPlayServerSpawnEntity(passenger, ObjectTypes.FALLING_BLOCK, getDataInt());
		packet1.setEntityID(this.p.getEntityId());
//		WrapperPlayServerPlayerInfoRemove packet2 = new WrapperPlayServerPlayerInfoRemove();
//		packet2.setProfileIds(Arrays.asList(new UUID[] {this.p.getUniqueId()}));
		mgr.sendServerPacket(player, packet1.getHandle());		
	}
	
	public void cancel() {
		isCancelled = true;
		remove();
		ProtocolManager mgr = ProtocolLibrary.getProtocolManager();
		WrapperPlayServerNamedEntitySpawn packet1 = new WrapperPlayServerNamedEntitySpawn(p);
		for(Player P : Bukkit.getOnlinePlayers()) if(P != p) mgr.sendServerPacket(P, packet1.getHandle());;
	}
	
	public void excludeFrom(Player player) {
		excludeList.add(player);
		player.showPlayer(plugin, p);
		ProtocolManager mgr = ProtocolLibrary.getProtocolManager();
		WrapperPlayServerNamedEntitySpawn packet1 = new WrapperPlayServerNamedEntitySpawn(p);
		mgr.sendServerPacket(player, packet1.getHandle());
	}
	
	private int getDataInt() {
		CraftBlockData cd = ((CraftBlockData) m);
		IBlockData nd = cd.getState();
		return net.minecraft.world.level.block.Block.j(nd);
	}
		
}
