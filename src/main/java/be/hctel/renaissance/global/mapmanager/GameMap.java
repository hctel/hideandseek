package be.hctel.renaissance.global.mapmanager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.json.JSONObject;

/**
 * 
 * @author <a href="https://hctel.net/">hctel</a>
 *
 */
public class GameMap implements IGameMap {
	protected World world;
	protected Location spawnLocation;
	protected JSONObject worldConfig;
		
	/**
	 * This object represents a Game Map world.
	 * @param world the Map's {@link org.bukkit.World} 
	 * @param spawnLocation the Map's {@link org.bukkit.Location}
	 * @param worldConfig the Map's JSON Config (represented by a {@link org.json.JSONObject})
	 */
	public GameMap(World world, Location spawnLocation, JSONObject worldConfig) {
		this.world = world;
		this.spawnLocation = spawnLocation;
		this.worldConfig = worldConfig;
	}
	
	/**
	 * Gets the Map's {@link org.bukkit.World} 
	 * @return the Map's {@link org.bukkit.World} 
	 */
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Gets the Map's {@link org.bukkit.Location}
	 * @return the Map's {@link org.bukkit.Location}
	 */
	public Location getSpawn() {
		return this.spawnLocation;
	}
	
	/**
	 * Gets the Map's JSON Config (represented by a {@link org.json.JSONObject})
	 * @return the Map's JSON Config (represented by a {@link org.json.JSONObject})
	 */
	public JSONObject getConfig() {
		return this.worldConfig;
	}
	public String getName() {
		return getConfig().getString("name");
	}
	
	public String getAuthor() {
		return getConfig().getString("author");
	}
	
	public static GameMap getFromJSON(JSONObject json) {
		Location l = jsonToLocation(json.getJSONObject("spawn"));
		return new GameMap(l.getWorld(), l, json);
	}
	  
	protected static Location jsonToLocation(JSONObject o) {
		try {
			if(o.has("yaw")) return new Location(Bukkit.getWorld(o.getString("world")), o.getDouble("x"), o.getDouble("y"), o.getDouble("z"), o.getFloat("yaw"), o.getFloat("pitch"));
			else return new Location(Bukkit.getWorld(o.getString("world")), o.getDouble("x"), o.getDouble("y"), o.getDouble("z"));
		} catch(Exception e) {
			throw new IllegalArgumentException("One of the needed JSON keys is missing or damaged");
		}
	}
}
