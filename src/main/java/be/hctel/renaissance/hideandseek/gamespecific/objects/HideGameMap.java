package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import be.hctel.renaissance.global.mapmanager.GameMap;

public class HideGameMap extends GameMap {
	private Location seekerSpawn;
	private ArrayList<Material> defaultBlocks = new ArrayList<Material>();
	private ArrayList<Material> disabledBlocks = new ArrayList<Material>();
	private HideGameAchievement achievement;
	
	public HideGameMap(World world, Location spawnLocation, JSONObject worldConfig) throws Exception {
		super(world, spawnLocation, worldConfig);
		seekerSpawn = jsonToLocation(worldConfig.getJSONObject("seekerSpawn"));
		for(Object O : worldConfig.getJSONArray("blocks")) {
			if(O instanceof String) {
				Material match = Material.matchMaterial((String) O);
				if(match == null) throw new Exception(String.format("%s does not refer to a valid material.", (String) O));
				defaultBlocks.add(match);
			}
		}
		if(defaultBlocks.isEmpty()) throw new Exception("Map must have at least one default block");
		if(worldConfig.has("disabledBlocks")) {
			if(!worldConfig.getJSONArray("disabledBlocks").isEmpty()) {
				for(Object O : worldConfig.getJSONArray("disabledBlocks")) {
					if(O instanceof String) {
						Material match = Material.matchMaterial((String) O);
						if(match == null) throw new Exception(String.format("%s does not refer to a valid material.", (String) O));
						disabledBlocks.add(match);
					}
				}
			}
		}
		if(worldConfig.has("achievement")) {
			String achName = worldConfig.getString("achievement");
			achievement = new HideGameAchievement(achName, "Win as hider OR get the most kills as a seeker on " + getName(), world.getName(), 1);
			HideGameAchievement.perMapAchievement.put(this, achievement);
		}
	}
	
	public Location getSeekerSpawn() {
		return seekerSpawn;
	}
	
	public ArrayList<Material> getDefaultBlocks() {
		return defaultBlocks;
	}
	
	public ArrayList<ItemStack> getDefaultBlocksItem() {
		ArrayList<ItemStack> out = new ArrayList<ItemStack>();
		for(Material M : getDefaultBlocks()) {
			out.add(new ItemStack(M));
		}
		return out;
	}
	
	public ArrayList<Material> getDisabledBlocks() {
		return disabledBlocks;
	}
	
	public ArrayList<ItemStack> getDisabledBlocksItem() {
		ArrayList<ItemStack> out = new ArrayList<ItemStack>();
		for(Material M : getDisabledBlocks()) {
			out.add(new ItemStack(M));
		}
		return out;
	}
	
	public static HideGameMap getFromJSON(JSONObject json) throws IllegalArgumentException {
		try {
			Location l = jsonToLocation(json.getJSONObject("spawn"));
			return new HideGameMap(l.getWorld(), l, json);
		} catch (Exception e) {
			throw new IllegalArgumentException(String.format("Map %s has encountered a problem when loading. (%s)", json.getString("name"), e.getMessage()));
		}		
	}
}
