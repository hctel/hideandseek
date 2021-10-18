package be.hctel.renaissance.hideandseek.nongame.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameMap;

public class MapLoader {
	public boolean isCopying = false;
	ArrayList<String> worldNames = new ArrayList<String>();
	ArrayList<World> worlds = new ArrayList<World>();
	World dynamicWorld;
	Plugin plugin;
	public MapLoader(Plugin plugin) {
		for(GameMap map : GameMap.values()) {
			worldNames.add(map.getSystemName());
		}
	}
	public void loadMaps() {
		for(String name : worldNames) worlds.add(Bukkit.createWorld(new WorldCreator(name)));
		for(World w: worlds) {
			Bukkit.getWorlds().add(w);
			w.setGameRuleValue("randomTickSpeed", "0");
			w.setGameRuleValue("doFireTick", "false");
			w.setGameRuleValue("doMobSpawning", "false");
			w.setGameRuleValue("randomTickSpeed", "false");
			w.setGameRuleValue("announceAdvancements", "false");
			w.setGameRuleValue("doDaylightCycle", "false");
			w.setGameRuleValue("doWeatherCycle", "false");
			w.setStorm(false);
			w.setDifficulty(Difficulty.PEACEFUL);
		}
		for(String name : worldNames) { 
			Hide.worldManager.loadWorld(name);
		}
	}
	public void loadWorldsToTempWorld(ArrayList<GameMap> map) {
		for(int i = 0; i < 6; i++) {
			Hide.worldManager.cloneWorld(map.get(i).getSystemName(), "TEMPWORLD" + i);
			Hide.worldManager.loadWorld("TEMPWORLD" + i);
		}
	}
	
	public void deleteTempWorld() {
		for(int i = 0; i < 6; i++) {
			Hide.worldManager.deleteWorld("TEMPWORLD" + i);
		}
	}	
}
