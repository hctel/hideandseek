package be.hctel.renaissance.cosmetics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

/*
 * This file is a part of the Renaissance Project API
 */

public class CosmeticsManager {
	Connection con;
	Plugin plugin;
	HashMap<OfflinePlayer, Integer> tokens = new HashMap<OfflinePlayer, Integer>();
	public CosmeticsManager(Connection con, Plugin plugin) {
		this.con = con;
		this.plugin = plugin;
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					con.createStatement().execute("a;");
				} catch (SQLException e) {
					System.out.println("Pinged DB");
				}
			}
			
		}.runTaskTimer(plugin, 0L, 15*60*20L);
	}
	
	public void loadPlayer(OfflinePlayer player) throws SQLException {
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM cosmetics WHERE UUID = '" + Utils.getUUID(player) + "';");
		if(rs.next()) {
			tokens.put(player, rs.getInt("TOKENS"));
		} else {
			con.createStatement().execute("INSERT INTO cosmetics (UUID) VALUES ('" + Utils.getUUID(player) + "');");
		}
	}
	
	public int getTokens(OfflinePlayer player) {
		if(tokens.containsKey(player)) return tokens.get(player);
		else { 
			return 0;
		}
	}
	
	public boolean setTokens(OfflinePlayer player, int amount) {
		if(tokens.containsKey(player)) {
			tokens.replace(player, amount);
			return true;
		} else return false;
	}
	
	public void unloadPlayer(OfflinePlayer player) throws SQLException {
		if(tokens.containsKey(player)) {
			con.createStatement().execute("UPDATE cosmetics SET TOKENS = " + tokens.get(player) + " WHERE UUID = '" + Utils.getUUID(player) + "';");
		}
	}
	
	public boolean isLoaded(OfflinePlayer t) {
		return tokens.containsKey(t);
	}
}
