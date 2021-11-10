package be.hctel.renaissance.hideandseek;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import be.hctel.renaissance.cosmetics.CosmeticsManager;
import be.hctel.renaissance.hideandseek.commands.RankCommands;
import be.hctel.renaissance.hideandseek.commands.StaffComands;
import be.hctel.renaissance.hideandseek.commands.StatCommands;
import be.hctel.renaissance.hideandseek.commands.VoteCommand;
import be.hctel.renaissance.hideandseek.commonclass.VotesHandler;
import be.hctel.renaissance.hideandseek.gamespecific.objects.BlockPicker;
import be.hctel.renaissance.hideandseek.gamespecific.objects.GameEngine;
import be.hctel.renaissance.hideandseek.gamespecific.objects.PreGameTimer;
import be.hctel.renaissance.hideandseek.listeners.InventoryListener;
import be.hctel.renaissance.hideandseek.listeners.PlayerListener;
import be.hctel.renaissance.hideandseek.nongame.sql.Stats;
import be.hctel.renaissance.hideandseek.nongame.utils.MapLoader;
import be.hctel.renaissance.ranks.RankManager;

public class Hide extends JavaPlugin {
	public static boolean testServer = true;
	public static boolean isServerStarting = true;
	
	public static String header = "§8▍ §bHide§aAnd§eSeek§8 ▏ ";
	
	public static Plugin plugin;
	public static MultiverseCore core;
	public static MVWorldManager worldManager;
	public static ProtocolManager protocolLibManager;
	
	public static Stats stats;
	public static RankManager rankManager;
	public static CosmeticsManager cosmeticManager;
	
	public static MapLoader mapLoader;
	public static VotesHandler votesHandler;
	public static BlockPicker blockPicker;
	public static PreGameTimer preGameTimer;
	public static GameEngine gameEngine;
	
	
	private String host, user, database, password;
	private int port;
	private Connection con;
	
	@Override
	public void onEnable() {
		plugin = this;
		core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		worldManager = core.getMVWorldManager();
		loadSQLCred();
		openConnection();
		stats = new Stats(con);
		rankManager = new RankManager(con);
		cosmeticManager = new CosmeticsManager(con, this);
		mapLoader = new MapLoader(plugin);
		mapLoader.loadMaps();
		preGameTimer = new PreGameTimer(this);
		registerListeners();
		loadCommands();
		votesHandler = new VotesHandler(plugin);
		protocolLibManager = ProtocolLibrary.getProtocolManager();
	}
	
	
	@Override
	public void onDisable() {
		try {
			stats.saveAll();
			rankManager.saveAll();
			mapLoader.deleteTempWorld();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadCommands() {
		getCommand("stats").setExecutor(new StatCommands());
		getCommand("togglerank").setExecutor(new RankCommands());
		getCommand("setrank").setExecutor(new RankCommands());
		getCommand("updateprofile").setExecutor(new StaffComands());
		getCommand("gotoworld").setExecutor(new StaffComands());
		getCommand("vote").setExecutor(new VoteCommand());
		getCommand("v").setExecutor(new VoteCommand());
	}
	private void openConnection() {
		getLogger().info("[ " + getName() + "] Enabling SQL connection to database");
		try {
			if (con != null && !con.isClosed()) {
			    return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    synchronized (this) {
	        try {
				if (con != null && !con.isClosed()) {
				    return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        try {
				con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true", user, password);
				System.out.println("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true ," + user + ", " + password);
				try {
					@SuppressWarnings("unused")
					Statement st = con.createStatement();
					System.out.println("[ " + getName() + "] SQL is operational!");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
	private void loadSQLCred() {
		if(testServer) {
			host = "localhost";
			user = "root";
			database = "testdb";
			password = "1234";
			port = 3306;			
		}
	}
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryListener(), this);
	}
}
