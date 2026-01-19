package be.hctel.renaissance.hideandseek;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;
import org.mvplugins.multiverse.core.MultiverseCore;
import org.mvplugins.multiverse.core.world.WorldManager;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.mojang.authlib.GameProfile;

import be.hctel.api.bungee.BungeeCordMessenger;
import be.hctel.api.fakeentities.FakePlayer;
import be.hctel.api.runnables.ArgumentRunnable;
import be.hctel.api.signs.Signer;
import be.hctel.renaissance.cosmetics.CosmeticsManager;
import be.hctel.renaissance.hideandseek.commands.DevCommands;
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
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;

public class Hide extends JavaPlugin {
	public static String version = "pre-1.0a";
	
	public volatile static HashMap<Player, String> runCommandSync = new HashMap<Player, String>();
	private BukkitRunnable commandRunner = new BukkitRunnable() {

		@Override
		public void run() {
			if(!runCommandSync.isEmpty()) {
				for(Player P : runCommandSync.keySet()) {
					P.performCommand(runCommandSync.get(P));
					runCommandSync.remove(P);
				}
			}
		}
		
	};
	
	public static boolean testServer = false;
	public static boolean isServerStarting = true;
	
	public static String header = "§8▍ §bHide§aAnd§eSeek§8 ▏ ";
	
	//Declaring every core variables
	
	public static Plugin plugin;
	public static MultiverseCore core;
	public static WorldManager worldManager;
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
	public static Connection con;
	
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
		
		//Starts SQL connection
		loadSQLCred();
		openConnection();
		
		//Creating every helpers
		stats = new Stats(con, this);
		rankManager = new RankManager(con, this);
		cosmeticManager = new CosmeticsManager(con, this);
		mapLoader = new MapLoader(this);
		mapLoader.loadMaps();
		preGameTimer = new PreGameTimer(this);
		votesHandler = new VotesHandler(this);
		bm = new BungeeCordMessenger(this);
		bm.requestServerName();
		blockShop = new BlockShop(this);
		joinMessageMenu = new JoinMessageHandler(this);
		signer = new Signer(this);
		commandRunner.runTaskTimer(plugin, 0L, 1L);
		
		//Speaks by itself
		registerListeners();
		loadCommands();
		
		shopPlayer = new FakePlayer(new Location(Bukkit.getWorld("HIDE_Lobby"), -82.5, 90.01, 65.5, -135.0f, 0.0f),plugin , new GameProfile(UUID.randomUUID(), "§e§lBlock Shop"));
		shopPlayer.setOnRightClickTask(new ArgumentRunnable() {
			@Override
			public void run(Object o) {
				if(o instanceof Player) {
					runCommandSync.put((Player) o, "blockshop");
				}
			}
		});
		
		
		
		for(Team T : plugin.getServer().getScoreboardManager().getMainScoreboard().getTeams()) {
			T.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		}
		isServerStarting = false;
		try {
			con.createStatement().execute("UPDATE servers SET status = 'UP' WHERE name = '" + bm.getServerName() + "';");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void onDisable() {
		try {
			//Disable clean-up and data save
			commandRunner.cancel();
			signer.onDisable();
			mapLoader.deleteTempWorld();
			cosmeticManager.saveAll();
			rankManager.saveAll();
			stats.saveAll();
			PacketEvents.getAPI().terminate();
			for(Team T : plugin.getServer().getScoreboardManager().getMainScoreboard().getTeams()) {
				T.unregister();
			}
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
		getCommand("blockshop").setExecutor(new UtilsCommands());
		getCommand("vote").setExecutor(new VoteCommand());
		getCommand("v").setExecutor(new VoteCommand());
		getCommand("taunt").setExecutor(new TauntCommand());
		getCommand("taunt").setTabCompleter(new TauntCommandTabCompleter());
		getCommand("s").setExecutor(new StaffComands());
		getCommand("katest").setExecutor(new StaffComands());
		getCommand("ping").setExecutor(new UtilsCommands());
		getCommand("signer").setExecutor(new SignCommands());
		getCommand("dev").setExecutor(new DevCommands());
		getCommand("showhiders").setExecutor(new StaffComands());
		getCommand("whereami").setExecutor(new UtilsCommands());
		getCommand("getskin").setExecutor(new StaffComands());
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
