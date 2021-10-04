package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GameEngine {
	private Plugin plugin;
	
	private ArrayList<Player> hiders = new ArrayList<Player>();
	private ArrayList<Player> seekers = new ArrayList<Player>();
	
	public GameEngine(Plugin plugin) {
		this.plugin = plugin;
	}
}
