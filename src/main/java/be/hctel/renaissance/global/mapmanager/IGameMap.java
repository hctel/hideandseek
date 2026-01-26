package be.hctel.renaissance.global.mapmanager;

import org.bukkit.Location;
import org.bukkit.World;
import org.json.JSONObject;

public interface IGameMap {
	public World getWorld();
	public Location getSpawn();
	public JSONObject getConfig();
	public String getName();
}
