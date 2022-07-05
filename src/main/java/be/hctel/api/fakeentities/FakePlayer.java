package be.hctel.api.fakeentities;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;
import org.json.JSONObject;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
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
	
	private final String version = "a0.1";
	

    private final Location loc;
	private ArgumentRunnable onRightClick;
	private int id;
	private ProtocolManager manager = ProtocolLibrary.getProtocolManager();
	private HashMap<HumanEntity, Long> cooldown = new HashMap<HumanEntity, Long>();
	Plugin plugin;

    public FakePlayer(WorldServer ws, GameProfile gp, Location loc, Plugin plugin) {
        super(((CraftServer) Bukkit.getServer()).getServer(), ws, gp, new PlayerInteractManager(ws));
        this.plugin = plugin;
        this.loc = loc;
        this.id = this.getId();
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch()); // set location
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        manager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
        	@Override
        	public void onPacketReceiving(PacketEvent e) {
        		if(!cooldown.containsKey(e.getPlayer())) {
        			cooldown.put(e.getPlayer(), System.currentTimeMillis());
        			if(e.getPacket().getIntegers().read(0) == id) {
            			if(onRightClick != null) {
            				onRightClick.run(e.getPlayer().getName());
            			}
            		}
        		} else {
        			if(e.getPacket().getIntegers().read(0) == id && (System.currentTimeMillis() - cooldown.get(e.getPlayer())) > 500) {
            			if(onRightClick != null) {
            				onRightClick.run(e.getPlayer().getName());
            			}
            			cooldown.put(e.getPlayer(), System.currentTimeMillis());
            		}
        		}        		
        	}
		});
        checkForUpdates();
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
	 * Sets the task to be executed when the fake player is right-clicked
	 * @param r an {@link ArgumentRunnable} where the {@link Object} is the {@link Player} who right-clicked the FakePlayer
	 */
	public void setOnRightClickTask(ArgumentRunnable r) {
		this.onRightClick = r;
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
	private void checkForUpdates() {
		System.out.println(this.getClass().getSimpleName());
		String urlRequest = "http://hctel.net:8081/api/u/v/" + this.getClass().getSimpleName() + ".md5";
		String response = Utils.getHTTPResponse(urlRequest, 3000);
		boolean updateAvailable = !(response.equalsIgnoreCase(version));
		if(updateAvailable) {
			plugin.getLogger().info("");
			plugin.getLogger().warning("[hctelAPI] - An update is available for the HCTEL-API Class FakePlayer (" + response + ", you're running version" + version + "). Please contact hctel to update your API.");
			plugin.getLogger().info("");
		}
	}
		
}
