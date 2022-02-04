package be.hctel.api.fakeentities;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.Plugin;

import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;

import be.hctel.api.runnables.ArgumentRunnable;

/**
 * Use to create a fake player in your plugins.
 * 
 * <p><b>Dependancies: 
 * <ul>
 * 	<li>ProtocolLib</li>
 * 	<li>PacketWrapper</li>
 * </ul></b></p>
 * 
 * 
 * @author hctel
 *
 */

public class FakePlayer implements Listener {
	Plugin plugin;
	ProtocolManager manager;
	WrapperPlayServerNamedEntitySpawn npc;
	WrappedDataWatcher watcher;
	UUID npcUUID;
	int entityID;
	Location location;
	ArgumentRunnable onPunch;
	ArgumentRunnable onRightClick;
	
	/**
	 * Instantiates the FakePlayer
	 * @param plugin your plugin's main class (the one that extends org.bukit.plugin.java.JavaPlugin)
	 * @param manager the ProtocolLib's ProtocolManager
	 * @param location the location where the FakePlayer should be spawned;
	 */
	public FakePlayer(Plugin plugin, ProtocolManager manager, Location location) {
		this.plugin = plugin;
		this.manager = manager;
		this.watcher = new WrappedDataWatcher();
		this.npc = new WrapperPlayServerNamedEntitySpawn();
		this.npcUUID = UUID.randomUUID();
		this.entityID = new Random().nextInt();
		this.location = location;
		npc.setEntityID(entityID);
		npc.setPlayerUUID(npcUUID);
		npc.setX(location.getX());
		npc.setY(location.getY());
		npc.setZ(location.getZ());
		npc.setYaw(location.getYaw());
		npc.setPitch(location.getPitch());
		watcher.setObject(0, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0);
		npc.getHandle().getDataWatcherModifier().write(0, watcher);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	/**
	 * Sends the FakePlayer to everyone connected to the server
	 * @return the FakePlayer object
	 */
	public FakePlayer sendToAll() {
		for(Player P : Bukkit.getOnlinePlayers()) {
			sendToPlayer(P);
		}
		return this;
	}
	
	/**
	 * Sends the FakePlayer to a specific player
	 * @param player the player to send the FakePlayer to
	 */
	public void sendToPlayer(Player player) {
		try {
				manager.sendServerPacket(player, npc.getHandle());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets the task to be executed when the fake player is punched
	 * @param r an {@link ArgumentRunnable} where the {@link Object} is the {@link Player} who punched the FakePlayer
	 */
	public void setOnPunchTask(ArgumentRunnable r) {
		this.onPunch = r;
	}
	
	/**
	 * Sets the task to be executed when the fake player is right-clicked
	 * @param r an {@link ArgumentRunnable} where the {@link Object} is the {@link Player} who right-clicked the FakePlayer
	 */
	public void setOnRightClickTask(ArgumentRunnable r) {
		this.onRightClick = r;
	}	

	@EventHandler
	private void onDamage(EntityDamageEvent e) {
		if(e.getEntity().getUniqueId() == this.npcUUID) {
			e.setCancelled(true);
		} else return;
	}
	
	@EventHandler
	private void onDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity().getUniqueId() == this.npcUUID) {
			if(e.getDamager() instanceof Player && this.onPunch != null) {
				Player p = (Player) e.getDamager();
				onPunch.run(p);
			}
			e.setCancelled(true);
		} else return;
	}
	
	@EventHandler
	private void onPlayerInteract(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked().getUniqueId() == this.npcUUID) {
			if(this.onRightClick != null) {
				onRightClick.run(e.getPlayer());
			}
		} else return;
	}
		
}
