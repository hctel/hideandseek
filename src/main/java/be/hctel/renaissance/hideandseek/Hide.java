package be.hctel.renaissance.hideandseek;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.mojang.authlib.GameProfile;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import be.hctel.api.bungee.BungeeCordMessenger;
import be.hctel.api.fakeentities.FakePlayer;
import be.hctel.api.runnables.ArgumentRunnable;
import be.hctel.api.signs.Signer;
import be.hctel.renaissance.cosmetics.CosmeticsManager;
import be.hctel.renaissance.hideandseek.commands.RankCommands;
import be.hctel.renaissance.hideandseek.commands.SignCommands;
import be.hctel.renaissance.hideandseek.commands.StaffComands;
import be.hctel.renaissance.hideandseek.commands.StatCommands;
import be.hctel.renaissance.hideandseek.commands.TauntCommand;
import be.hctel.renaissance.hideandseek.commands.UtilsCommands;
import be.hctel.renaissance.hideandseek.commands.VoteCommand;
import be.hctel.renaissance.hideandseek.commands.completers.StaffCommandsTabCompleter;
import be.hctel.renaissance.hideandseek.commands.completers.TauntCommandTabCompleter;
import be.hctel.renaissance.hideandseek.commonclass.VotesHandler;
import be.hctel.renaissance.hideandseek.gamespecific.objects.BlockPicker;
import be.hctel.renaissance.hideandseek.gamespecific.objects.BlockShop;
import be.hctel.renaissance.hideandseek.gamespecific.objects.GameEngine;
import be.hctel.renaissance.hideandseek.gamespecific.objects.JoinMessageHandler;
import be.hctel.renaissance.hideandseek.gamespecific.objects.PreGameTimer;
import be.hctel.renaissance.hideandseek.listeners.InventoryListener;
import be.hctel.renaissance.hideandseek.listeners.MiscListeners;
import be.hctel.renaissance.hideandseek.listeners.PlayerListener;
import be.hctel.renaissance.hideandseek.nongame.sql.Stats;
import be.hctel.renaissance.hideandseek.nongame.utils.MapLoader;
import be.hctel.renaissance.ranks.RankManager;

public class Hide extends JavaPlugin {
	public static boolean testServer = false;
	public static boolean isServerStarting = true;
	
	public static String header = "§8▍ §bHide§aAnd§eSeek§8 ▏ ";
	
	//Declaring every core variables
	
	public static Plugin plugin;
	public static MultiverseCore core;
	public static MVWorldManager worldManager;
	public static ProtocolManager protocolLibManager;
	public static BungeeCordMessenger bm;
	public static Signer signer;
	
	//Declaring every saveables helper variables
	
	public static Stats stats;
	public static RankManager rankManager;
	public static CosmeticsManager cosmeticManager;
	
	//Declaring every project variables
	
	public static MapLoader mapLoader;
	public static VotesHandler votesHandler;
	public static BlockPicker blockPicker;
	public static PreGameTimer preGameTimer;
	public static GameEngine gameEngine;
	public static BlockShop blockShop;
	public static JoinMessageHandler joinMessageMenu;
	
	public static FakePlayer shopPlayer;
	//Declaring SQL variables
	
	private String host, user, database, password;
	private int port;
	private Connection con;
	
	@Override
	public void onEnable() {
		getLogger().info("Enabling HideAndSeek...");
		plugin = this;
		
		//Enables external libraries
		core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		protocolLibManager = ProtocolLibrary.getProtocolManager();
		worldManager = core.getMVWorldManager();
		
		//Starts SQL connection
		loadSQLCred();
		openConnection();
		
		//Creating every helpers
		stats = new Stats(con);
		rankManager = new RankManager(con, this);
		cosmeticManager = new CosmeticsManager(con, this);
		mapLoader = new MapLoader(plugin);
		mapLoader.loadMaps();
		preGameTimer = new PreGameTimer(this);
		votesHandler = new VotesHandler(plugin);
		bm = new BungeeCordMessenger(this);
		blockShop = new BlockShop(this);
		joinMessageMenu = new JoinMessageHandler(this);
		signer = new Signer(this);
		
		//Speaks bby itself
		registerListeners();
		loadCommands();
		shopPlayer = new FakePlayer(((CraftWorld) Bukkit.getWorld("HIDE_Lobby")).getHandle(), new GameProfile(UUID.fromString("fef039ef-e6cd-4987-9c84-26a3e6134277"), "§eShop"), new Location(Bukkit.getWorld("HIDE_Lobby"), -82.5, 90.01, 65.5, -135.0f, 0.0f), plugin);
		//shopPlayer.setSkin(UUID.fromString("5a7e8a16-e191-4fb8-8c92-869434202089"));
		shopPlayer.setSkin("https://namemc.com/texture/212dd1d9cafbb877.png");
		shopPlayer.setOnRightClickTask(new ArgumentRunnable() {
			@Override
			public void run(Object o) {
				if(o instanceof String) {
					Player p = Bukkit.getPlayer((String) o);
					blockShop.openInventory(p);
				}
			}
		});
	}
	
	
	@Override
	public void onDisable() {
		try {
			//Disable clean-up and data save
			signer.onDisable();
			mapLoader.deleteTempWorld();
			cosmeticManager.saveAll();
			rankManager.saveAll();
			stats.saveAll();
			plugin = null;
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
		getCommand("updateprofile").setTabCompleter(new StaffCommandsTabCompleter());
		getCommand("gotoworld").setExecutor(new StaffComands());
		getCommand("gotoworld").setTabCompleter(new StaffCommandsTabCompleter());
		getCommand("vote").setExecutor(new VoteCommand());
		getCommand("v").setExecutor(new VoteCommand());
		getCommand("taunt").setExecutor(new TauntCommand());
		getCommand("taunt").setTabCompleter(new TauntCommandTabCompleter());
		getCommand("s").setExecutor(new StaffComands());
		getCommand("katest").setExecutor(new StaffComands());
		getCommand("ping").setExecutor(new UtilsCommands());
		getCommand("signer").setExecutor(new SignCommands());
	}
	
	private void openConnection() {
		getLogger().info("Enabling SQL connection to database");
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
				getLogger().info("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true ," + user + ", " + password);
				try {
					@SuppressWarnings("unused")
					Statement st = con.createStatement();
					getLogger().info("SQL is operational!");
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
		saveDefaultConfig();
		testServer = getConfig().getBoolean("testServer");
		host = getConfig().getString("SQL.host");
		user = getConfig().getString("SQL.user");
		database = getConfig().getString("SQL.database");
		password = getConfig().getString("SQL.password");
		port = 3306;
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
		getServer().getPluginManager().registerEvents(new MiscListeners(), this);
	}
}
