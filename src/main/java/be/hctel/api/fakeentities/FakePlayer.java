package be.hctel.api.fakeentities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R6.CraftServer;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.Action;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfoUpdate.PlayerInfo;
import com.mojang.authlib.GameProfile;

import be.hctel.api.protocol.adapters.AdapterPacketPlayOutSpawnEntity;
import be.hctel.api.runnables.ArgumentRunnable;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.Vec3D;


/**
 * Use to create a fake player in your plugins.
 * 
 * <p><b>Dependancies: 
 * <ul>
 * 	<li>ProtocolLib</li>
 * 	<li>PacketEvents</li>
 * </ul></b></p>
 * 
 * 
 * @author hctel
 *
 */

public class FakePlayer extends EntityPlayer implements Listener {

	private final Location loc;
	private ArgumentRunnable onRightClick;
	private CraftPlayer interfacePlayer;
	private int id;
	private ProtocolManager manager = ProtocolLibrary.getProtocolManager();
	private HashMap<HumanEntity, Long> cooldown = new HashMap<HumanEntity, Long>();
	Plugin plugin;
	ArrayList<Player> shownTo = new ArrayList<Player>();
	
	/**
	 * Creates a FakePlayer object
	 * @param loc the {@link org.bukkit.Location} where to spawn the FakePlayer.
	 * @param plugin the {@link org.bukkit.plugin.Plugin} representing the current plugin's main class.
	 * @param gameprofile the NMS GameProfile (UUID can be random, name is the FakePlayer's display name).
	 */
	public FakePlayer(Location loc, Plugin plugin, GameProfile gameprofile) {
		PositionMoveRotation dest = new PositionMoveRotation(new Vec3D(loc.getX(), loc.getY(), loc.getZ()), new Vec3D(loc.getX(), loc.getY(), loc.getZ()), loc.getPitch(), loc.getYaw());
		CraftServer srv = ((CraftServer) plugin.getServer());
		super(srv.getServer(), ((CraftWorld)loc.getWorld()).getHandle(), gameprofile, ClientInformation.a());
		this.a(dest, Set.of(Relative.values()));
		interfacePlayer = new CraftPlayer(srv, this);
		this.plugin = plugin;
		this.loc = loc;
		this.id = interfacePlayer.getEntityId();
		this.a(loc.getX(), loc.getY(), loc.getZ());
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		manager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
	      	@Override
	      	public void onPacketReceiving(PacketEvent e) {
	      		if(!cooldown.containsKey(e.getPlayer())) {
	      			cooldown.put(e.getPlayer(), System.currentTimeMillis());
	      			if(e.getPacket().getIntegers().read(0) == id) {
	          			if(onRightClick != null) {
	          				onRightClick.run(e.getPlayer());
	          			}
	          		}
	      		} else {
	      			if(e.getPacket().getIntegers().read(0) == id && (System.currentTimeMillis() - cooldown.get(e.getPlayer())) > 500) {
	          			if(onRightClick != null) {
	          				onRightClick.run(e.getPlayer());
	          			}
	          			cooldown.put(e.getPlayer(), System.currentTimeMillis());
	          		}
	      		}        		
	      	}
		});
	}
	
	/**
	 * Immediately spawns the FakePlayer for every online players
	 */
    public void spawn() {
        for (Player pl : plugin.getServer().getOnlinePlayers()) {
            spawnFor(pl);
        }
    }
    
    /**
     * Spawns the FakePlayer only for a specific player (for instance if player joined after calling spawn())
     * @param p the {@link org.bukkit.entity.Player}
     */
    public void spawnFor(Player p) {
        PlayerConnection connection = ((CraftPlayer) p).getHandle().g;
        PlayerInfo info = new PlayerInfo(new UserProfile(interfacePlayer.getUniqueId(), interfacePlayer.getName()));
        WrapperPlayServerPlayerInfoUpdate packet1 = new WrapperPlayServerPlayerInfoUpdate(Action.ADD_PLAYER, info);
        PacketEvents.getAPI().getPlayerManager().sendPacket(p, packet1);
        connection.sendPacket(new AdapterPacketPlayOutSpawnEntity(this.interfacePlayer).getHandle());
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this, (byte) ((loc.getYaw() * 256f) / 360f)));
        shownTo.add(p);
    }
    
    /**
     * Dispawns the FakePlayer for every player seeing this FakePlayer.
     */
    public void remove() {
        for(Player P : shownTo) {
        	removeForPlayer(P);
        }
    }
    
    /**
     * Dispawns the FakePlayer for only one single player
     * @param p the {@link org.bukkit.entity.Player}
     */
    public void removeForPlayer(Player p) {
    	if(shownTo.contains(p)) {
    		((CraftPlayer) p).getHandle().g.sendPacket(new PacketPlayOutEntityDestroy(this.interfacePlayer.getEntityId()));
    		shownTo.remove(p);
    	}
    }
    
	/**
	 * Sets the task to be executed when the fake player is right-clicked
	 * @param r an {@link ArgumentRunnable} where the {@link Object} is the {@link org.bukkit.entity.Player} who right-clicked the FakePlayer
	 */
	public void setOnRightClickTask(ArgumentRunnable r) {
		this.onRightClick = r;
	}
}