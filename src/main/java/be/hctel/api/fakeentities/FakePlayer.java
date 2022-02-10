package be.hctel.api.fakeentities;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;
import org.json.JSONObject;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import be.hctel.api.runnables.ArgumentRunnable;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;

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

public class FakePlayer extends EntityPlayer implements Listener {

    private final Location loc;
	private ArgumentRunnable onPunch;
	private ArgumentRunnable onRightClick;
	private UUID npcUUID;

    public FakePlayer(WorldServer ws, GameProfile gp, Location loc, Plugin plugin) {
        super(((CraftServer) Bukkit.getServer()).getServer(), ws, gp, new PlayerInteractManager(ws));
        this.loc = loc;
        this.npcUUID = this.getUniqueID();
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()); // set location
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void spawn() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            spawnFor(pl); // send all spawn packets
        }
    }

    public void spawnFor(Player p) {
        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;

        // add player in player list for player
        connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this));
        // make player spawn in world
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(this));
        // change head rotation
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(this, (byte) ((loc.getYaw() * 256f) / 360f)));
        // now remove player from tab list
        connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, this));
        // here the entity is showed, you can show item in hand like that :
        // connection.sendPacket(new PacketPlayOutEntityEquipment(getId(), 0, CraftItemStack.asNMSCopy(itemInHand)));
    }

    public void remove() {
        this.die();
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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDamage(EntityDamageByEntityEvent e) {
		System.out.println("FakePlayer damage event trigger");
		if(e.getEntity().getUniqueId() == this.npcUUID) {
			if(e.getDamager() instanceof Player && this.onPunch != null) {
				Player p = (Player) e.getDamager();
				onPunch.run(p);
			}
		} else return;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractAtEntityEvent e) {
		System.out.println("FakePlayer damage event trigger");
		if(e.getRightClicked().getUniqueId() == this.npcUUID) {
			if(this.onRightClick != null) {
				onRightClick.run(e.getPlayer());
			}
		} else return;
	}
	
	public boolean setSkin(UUID uuid) {
	    try {
	            JSONObject json = Utils.readJsonFromUrl("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString() + "?unsigned=false").getJSONArray("properties").getJSONObject(0);
	            String skin = json.getString("value");
	            String signature = json.getString("signature");
	            this.getProfile().getProperties().put("textures", new Property("textures", skin, signature));
	            return true;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
		
}
