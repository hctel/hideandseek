package be.hctel.renaissance.hideandseek.nongame.utils;


import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;
import org.mvplugins.multiverse.core.utils.result.Attempt;
import org.mvplugins.multiverse.core.world.LoadedMultiverseWorld;
import org.mvplugins.multiverse.core.world.MultiverseWorld;
import org.mvplugins.multiverse.core.world.options.CloneWorldOptions;
import org.mvplugins.multiverse.core.world.options.DeleteWorldOptions;
import org.mvplugins.multiverse.core.world.reasons.DeleteFailureReason;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.objects.HideGameMap;

/*
 * This file is a part of the Renaissance Project API
 */


public class MapLoader {
	public boolean isCopying = false;
	ArrayList<String> worldNames = new ArrayList<String>();
	ArrayList<World> worlds = new ArrayList<World>();
	World dynamicWorld;
	Plugin plugin;
	public MapLoader(Plugin plugin) {
		this.plugin = plugin;
		for(HideGameMap map : Hide.mapManager.getMaps()) {
			worldNames.add(map.getWorld().getName());
		}
	}
	public void loadMaps() {
		for(String name : worldNames) worlds.add(Bukkit.createWorld(new WorldCreator(name)));
		for(World w: worlds) {
			Bukkit.getWorlds().add(w);
			w.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
			w.setGameRule(GameRule.DO_FIRE_TICK, false);
			w.setGameRule(GameRule.DO_MOB_SPAWNING, false);
			w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
			w.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
			w.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
			w.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
			w.setStorm(false);
			w.setDifficulty(Difficulty.PEACEFUL);
		}
		for(String name : worldNames) { 
			Hide.worldManager.loadWorld(name);
		}
	}
	public void loadWorldsToTempWorld(ArrayList<HideGameMap> map) {
		for(int i = 0; i < 6; i++) {
			plugin.getLogger().info(String.format("Cloning %s", map.get(i).getWorld().getName()));
			MultiverseWorld mvworld = Hide.worldManager.getWorld(map.get(i).getWorld()).get();
			LoadedMultiverseWorld world = (Hide.worldManager.isLoadedWorld(mvworld) ? Hide.worldManager.getLoadedWorld(mvworld).get() : Hide.worldManager.loadWorld(mvworld).get());
			CloneWorldOptions option = CloneWorldOptions.fromTo(world, "TEMPWORLD" + i);
			Hide.worldManager.cloneWorld(option);
			Hide.worldManager.loadWorld("TEMPWORLD" + i);
			Bukkit.getWorld("TEMPWORLD" + i).setGameRule(GameRule.KEEP_INVENTORY, true); 
			Bukkit.getWorld("TEMPWORLD" + i).setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
			Bukkit.getWorld("TEMPWORLD" + i).setGameRule(GameRule.DO_FIRE_TICK, false);
			Bukkit.getWorld("TEMPWORLD" + i).setGameRule(GameRule.DO_MOB_SPAWNING, false);
			Bukkit.getWorld("TEMPWORLD" + i).setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
			Bukkit.getWorld("TEMPWORLD" + i).setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
			Bukkit.getWorld("TEMPWORLD" + i).setGameRule(GameRule.DO_WEATHER_CYCLE, false);
			Bukkit.getWorld("TEMPWORLD" + i).setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
			Bukkit.getWorld("TEMPWORLD" + i).setGameRule(GameRule.LOCATOR_BAR, false);
			Bukkit.getWorld("TEMPWORLD" + i).setDifficulty(Difficulty.NORMAL);
		}
		plugin.getLogger().info("ALL WORLDS LOADED");
	}
	
	public void deleteTempWorld() {
		for(int i = 0; i < 6; i++) {
			plugin.getLogger().info(String.format("Deleting TEMPWORLD%d", i));
			Attempt<String, DeleteFailureReason> result = Hide.worldManager.deleteWorld(DeleteWorldOptions.world(Hide.worldManager.getWorld("TEMPWORLD" + i).get()));
			while(!result.isSuccess()) {
				plugin.getLogger().log(Level.WARNING, String.format("Could not delete world: %s (%s)", result.getFailureReason(), result.getFailureMessage().formatted()));
				result = Hide.worldManager.deleteWorld(DeleteWorldOptions.world(Hide.worldManager.getWorld("TEMPWORLD" + i).get()));
			}			
		}
	}	
}
