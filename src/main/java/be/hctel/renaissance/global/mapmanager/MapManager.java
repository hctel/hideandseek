package be.hctel.renaissance.global.mapmanager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import be.hctel.api.config.Config;
import be.hctel.renaissance.framework.RenaissancePlugin;

public class MapManager<T extends GameMap> {
	
	private Config mapConfig;
	private RenaissancePlugin rPlugin;
	private Plugin plugin;
	
	private Method getMapMethod;
	
	private ArrayList<T> gameMaps = new ArrayList<>();
	private HashMap<World, T> gameMapsPerWorld = new HashMap<>();
	
	public MapManager(@NotNull RenaissancePlugin plugin, @NotNull Class<T> mapTypeClass) throws Exception {
		this(plugin, mapTypeClass, "maps.json");
	}
	
	public MapManager(@NotNull RenaissancePlugin plugin, @NotNull Class<T> mapTypeClass, @NotNull String fileName) throws Exception {
		this.rPlugin = plugin;
		this.plugin = this.rPlugin.getPlugin();
		try {
			this.getMapMethod = mapTypeClass.getMethod("getFromJSON", JSONObject.class);
			this.mapConfig = new Config(this.plugin, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			plugin.getPlugin().getLogger().severe("");
			plugin.getPlugin().getLogger().severe("Couldn't load maps.json!!");
			plugin.getPlugin().getLogger().severe("");
			throw new Exception("Couldn't load maps.json!!");
		}
		if(mapConfig.getConfig().length() == 0) {
			plugin.getPlugin().getLogger().severe("");
			plugin.getPlugin().getLogger().severe("maps.json MUST at least contain one map!!");
			plugin.getPlugin().getLogger().severe("");
			throw new Exception("maps.json MUST at least contain one map!!");
		}
		for(World W : Bukkit.getWorlds()) {
			W.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
			W.setGameRule(GameRule.DO_FIRE_TICK, false);
			W.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
			W.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
			W.setGameRule(GameRule.DO_MOB_SPAWNING, false);
			W.setDifficulty(Difficulty.NORMAL);
			this.plugin.getLogger().info("Loading " + W.getName());
			JSONObject mapJson;
			if(!mapConfig.getConfig().has(W.getName())) {
//				mapJson = new JSONObject();
//				mapJson.put("spawn", locationToJson(W.getSpawnLocation()));
//				mapJson.put("name", W.getName());
//				mapJson.put("seekerSpawn", locationToJson(W.getSpawnLocation()));
//				mapJson.put("blocks", new JSONArray("[\"STONE\"]"));
			} else {
				mapJson = mapConfig.getConfig().getJSONObject(W.getName());
				T map = getMapFromJsonObject(mapJson);
				gameMaps.add(map);
				gameMapsPerWorld.put(W, map);
				mapConfig.getConfig().put(W.getName(), mapJson);
			}
//			T map = getMapFromJsonObject(mapJson);
//			gameMaps.add(map);
//			gameMapsPerWorld.put(W, map);
//			mapConfig.getConfig().put(W.getName(), mapJson);
		}
	}
		
	public T getMap(World w) {
		return gameMapsPerWorld.get(w);
	}
	
	public Config getMapConfig() {
		return this.mapConfig;
	}
	
	public List<T> getMaps() {
		return gameMaps;
	}
	
	public JSONObject getMapConfig(World w) {
		return getMap(w).getConfig();
	}
	
	public void onDisable() {
		try {
			this.mapConfig.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private T getMapFromJsonObject(JSONObject o) {
		try {
			return (T) getMapMethod.invoke(null, o);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	private static JSONObject locationToJson(Location loc) {
//		JSONObject o = new JSONObject();
//		o.put("world", loc.getWorld().getName());
//		o.put("x", loc.getX());
//		o.put("y", loc.getY());
//		o.put("z", loc.getZ());
//		o.put("yaw", loc.getYaw());
//		o.put("pitch", loc.getPitch());
//		return o;
//	}
}
