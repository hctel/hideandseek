package be.hctel.renaissance.hideandseek;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.mvplugins.multiverse.core.MultiverseCore;
import org.mvplugins.multiverse.core.world.WorldManager;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.retrooper.packetevents.PacketEvents;

import be.hctel.renaissance.framework.RenaissancePlugin;
import be.hctel.renaissance.global.mapmanager.MapManager;
import be.hctel.renaissance.hideandseek.commands.StaffComands;
import be.hctel.renaissance.hideandseek.commands.TauntCommand;
import be.hctel.renaissance.hideandseek.commands.UtilsCommands;
import be.hctel.renaissance.hideandseek.commands.VoteCommand;
import be.hctel.renaissance.hideandseek.commands.completers.StaffCommandsTabCompleter;
import be.hctel.renaissance.hideandseek.commands.completers.TauntCommandTabCompleter;
import be.hctel.renaissance.hideandseek.commonclass.VotesHandler;
import be.hctel.renaissance.hideandseek.gamespecific.objects.BlockPicker;
import be.hctel.renaissance.hideandseek.gamespecific.objects.GameEngine;
import be.hctel.renaissance.hideandseek.gamespecific.objects.HideGameMap;
import be.hctel.renaissance.hideandseek.gamespecific.objects.PreGameTimer;
import be.hctel.renaissance.hideandseek.listeners.InventoryListener;
import be.hctel.renaissance.hideandseek.listeners.MiscListeners;
import be.hctel.renaissance.hideandseek.listeners.PlayerListener;
import be.hctel.renaissance.hideandseek.nongame.utils.MapLoader;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public class Hide extends JavaPlugin implements RenaissancePlugin {
	public static String version = "pre-1.0a";
		
	public static boolean testServer = false;
	public static boolean isServerStarting = true;
	
	public static String header = "§8▍ §bHide§aAnd§eSeek§8 ▏ ";
	public static Location spawnLocation;
	
	//Declaring every core variables
	
	public static Plugin plugin;
	public static MultiverseCore core;
	public static WorldManager worldManager;
	public static ProtocolManager protocolLibManager;
	
	//Declaring every project variables
	
	public static MapManager<HideGameMap> mapManager;
	public static MapLoader mapLoader;
	public static VotesHandler votesHandler;
	public static BlockPicker blockPicker;
	public static PreGameTimer preGameTimer;
	public static GameEngine gameEngine;
	
	@Override
	public void onLoad() {
		getLogger().info("Waking up");
		PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
		PacketEvents.getAPI().load();
	}
	
	@Override
	public void onEnable() {
		getLogger().info("Enabling HideAndSeek...");
		plugin = this;
		PacketEvents.getAPI().init();
		//Enables external libraries
		core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		protocolLibManager = ProtocolLibrary.getProtocolManager();
		worldManager = core.getApi().getWorldManager();
		
		//Creating every helpers
		mapLoader = new MapLoader(this);
		mapLoader.loadMaps();
		preGameTimer = new PreGameTimer(this);
		votesHandler = new VotesHandler(this);
		
		//Speaks by itself
		registerListeners();
		loadCommands();
	
		spawnLocation = new Location(getServer().getWorld(getConfig().getString("spawn.world")), getConfig().getDouble("spawn.x"), getConfig().getDouble("spawn.y"), getConfig().getDouble("spawn.z"), (float) getConfig().getDouble("spawn.yaw"), (float) getConfig().getDouble("spawn.pitch"));
		
		for(Team T : plugin.getServer().getScoreboardManager().getMainScoreboard().getTeams()) {
			T.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		}
		isServerStarting = false;
	}
	
	
	@Override
	public void onDisable() {
		//Disable clean-up and data save
		mapLoader.deleteTempWorld();
		mapManager.onDisable();
		PacketEvents.getAPI().terminate();
		for(Team T : plugin.getServer().getScoreboardManager().getMainScoreboard().getTeams()) {
			T.unregister();
		}
		plugin = null;
	}
	
	private void loadCommands() {
		getCommand("gotoworld").setExecutor(new StaffComands());
		getCommand("gotoworld").setTabCompleter(new StaffCommandsTabCompleter());
		getCommand("vote").setExecutor(new VoteCommand());
		getCommand("v").setExecutor(new VoteCommand());
		getCommand("taunt").setExecutor(new TauntCommand());
		getCommand("taunt").setTabCompleter(new TauntCommandTabCompleter());
		getCommand("ping").setExecutor(new UtilsCommands());
		getCommand("showhiders").setExecutor(new StaffComands());
	}
	
	
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
		getServer().getPluginManager().registerEvents(new MiscListeners(), this);
	}

	@Override
	public String getHeader() {
		return header;
	}
	
	@Override
	public Plugin getPlugin() {
		return this;
	}
}
