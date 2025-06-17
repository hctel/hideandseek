package be.hctel.api.signs;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;
import org.json.JSONObject;

import be.hctel.api.config.Config;
import be.hctel.renaissance.hideandseek.Hide;

/**
 * A Sign-Interact helper for Java spigot plugins
 * @author hctel
 *
 */
public class Signer implements Listener {
	Plugin plugin;
	Config config;
	JSONObject jsonConfig;
	HashMap<Block, SignData> signs = new HashMap<>();
	HashMap<Player, String> editors = new HashMap<>();
	boolean allSignsLoaded = false;
	
	/**
	 * Creates the object, automatically registering the listeners
	 * @param plugin the main class of the plugin extending JavaPlugin
	 */
	public Signer(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		try {
			this.config = new Config(plugin, "signs.json");
		} catch (JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.jsonConfig = config.getConfig();
	}
	
	/**
	 * Adds an editor to the system. Editors get automatically removed when carrying an operation on a sign
	 * @param player the editor to add
	 * @param operation the operation to do, matching the syntax {@link SignType},matching operation
	 */
	public void addEditor(Player player, String operation) {
		loadSigns();
		editors.put(player, operation);
		player.sendMessage(Hide.header + "§aEntered Signer editor mode");
	}
	
	/**
	 * Removes an editor from the system
	 * @param player the editor to remove
	 */
	public void removeEditor(Player player) {
		editors.remove(player);
	}
	
	/**
	 * Listener listening for a {@link PlayerInteractEvent}
	 * @param e The PlayerInteractEvent
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void listenersEvent(PlayerInteractEvent e) {
		loadSigns();
		if(e.getClickedBlock() == null) return;
		if(e.getClickedBlock().getType() == Material.SIGN || e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN) {
			Player p = e.getPlayer();
			if(signs.containsKey(e.getClickedBlock())) {
				SignData clickedData = signs.get(e.getClickedBlock());
				switch(clickedData.getType()) {
				case COMMAND:
					Bukkit.dispatchCommand(p, clickedData.getData());
					break;
				case EASTER_EGG:
					p.sendMessage(clickedData.getData());
					break;
				case QUEUE:
					break;
				case SERVER:
					Hide.bm.sendToServer(p, clickedData.getData().replace(" ", ""));
					break;
				case TELEPORT:
					break;
				default:
					break;
				
				}
			}
		}
	}
	
	/**
	 * Listener listening for a {@link BlockBreakEvent}
	 * @param e The PlayerInteractEvent
	 */
	@EventHandler
	@SuppressWarnings("deprecation")
	public void listenersEvent(BlockBreakEvent e) {
		if(e.getBlock().getType() == Material.SIGN || e.getBlock().getType() == Material.SIGN_POST || e.getBlock().getType() == Material.WALL_SIGN) {
		if(editors.containsKey(e.getPlayer()) && e.getPlayer().isOp()) {
			if(editors.get(e.getPlayer()).equalsIgnoreCase("remove ")) {
				signs.remove(e.getBlock());
				jsonConfig.remove(e.getBlock().getLocation().getWorld().getName() + "," + e.getBlock().getLocation().getBlockX() + "," + e.getBlock().getLocation().getBlockY() + "," + e.getBlock().getLocation().getBlockZ());
				e.getPlayer().sendMessage(Hide.header + "§aRemoved this sign from Signer.");
				editors.remove(e.getPlayer());
			} else {
				if(!(editors.get(e.getPlayer()).equalsIgnoreCase("remove "))) {
					String[] editData = editors.get(e.getPlayer()).split(",");
					SignType type = SignType.getfromName(editData[0]);
					if(type == null) {
						e.getPlayer().sendMessage(Hide.header + "§cPlease enter a correct sign type!");
						e.setCancelled(true);
						return;
					}
					SignData data = new SignData(type, editData[1], e.getBlock().getLocation());
					signs.put(e.getBlock(), data);
					JSONObject signJson = new JSONObject();
					signJson.put("type", type.getName());
					signJson.put("data", data.getData());
					jsonConfig.put(data.getLocation().getWorld().getName() + "," + data.getLocation().getBlockX() + "," + data.getLocation().getBlockY() + "," + data.getLocation().getBlockZ(), signJson);
					e.getPlayer().sendMessage(Hide.header + "§aAdded the sign to Signer.");
					editors.remove(e.getPlayer());
				}
				}
			e.setCancelled(true);
		}
		} else {
			if(e.getBlock().getType() == Material.SIGN || e.getBlock().getType() == Material.SIGN_POST || e.getBlock().getType() == Material.WALL_SIGN) {
				Player p = e.getPlayer();
				if(signs.containsKey(e.getBlock())) {
					SignData clickedData = signs.get(e.getBlock());
					switch(clickedData.getType()) {
					case COMMAND:
						Bukkit.dispatchCommand(p, clickedData.getData());
						break;
					case EASTER_EGG:
						p.sendMessage(clickedData.getData());
						break;
					case QUEUE:
						break;
					case SERVER:
						Hide.bm.sendToServer(p, clickedData.getData().replace(" ", ""));
						break;
					case TELEPORT:
						break;
					default:
						break;
					
					}
					e.setCancelled(true);
				}
			}
		}
	}
	
	/**
	 * Saves the config
	 */
	public void onDisable(){
		plugin.getLogger().info("Saving signs...");
		try {
			config.save(jsonConfig);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadSigns() {
		if(!allSignsLoaded) {
			plugin.getLogger().info("Loading signs...");
			for(String key : jsonConfig.keySet()) {
				String[] signPos = key.split(",");
				SignData data = new SignData(SignType.getfromName(jsonConfig.getJSONObject(key).getString("type")), jsonConfig.getJSONObject(key).getString("data"), signPos);
				if(data.getLocation().getBlock().getType() != Material.SIGN && data.getLocation().getBlock().getType() != Material.SIGN_POST && data.getLocation().getBlock().getType() != Material.WALL_SIGN) {
					plugin.getLogger().warning("Wrong block type detected for sign at " + signPos[1] + "," + signPos[2] + "," + signPos[3] + " in world " + signPos[0]);
					plugin.getLogger().warning("Detected type: " + data.getLocation().getBlock().getType());
					continue;
				}
				signs.put(data.getLocation().getBlock(), data);
			}
			plugin.getLogger().info("All signs loaded!");
			allSignsLoaded = true;
		}
	}
}
