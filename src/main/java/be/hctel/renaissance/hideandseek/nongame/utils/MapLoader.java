package be.hctel.renaissance.hideandseek.nongame.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.Plugin;

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
		worldNames.add("TEMPWORLD");
	}
	public void loadMaps() {
		for(String name : worldNames) worlds.add(Bukkit.createWorld(new WorldCreator(name)));
		for(World w: worlds) Bukkit.getWorlds().add(w);
	}
	public void loadWorldToTempWorld(GameMap map) {
		World sourceWorld = Bukkit.getServer().getWorld(map.getSystemName());
		File source = sourceWorld.getWorldFolder();
		World targetWorld = Bukkit.getServer().getWorld("TEMPWORLD");
		File target = targetWorld.getWorldFolder();
		try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	 
	    }
		targetWorld = Bukkit.getServer().getWorld("TEMPWORLD");
		targetWorld.setDifficulty(Difficulty.PEACEFUL);
		
	}
	
	private void copyWorld(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	 
	    }
	}
}
