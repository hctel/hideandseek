package be.hctel.renaissance.cosmetics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

/*
 * This file is a part of the Renaissance Project API
 */

public class CosmeticsManager {
	Connection con;
	Plugin plugin;
	HashMap<OfflinePlayer, Integer> tokens = new HashMap<OfflinePlayer, Integer>();
	HashMap<OfflinePlayer, Integer> goldMedals = new HashMap<>();
	public CosmeticsManager(Connection con, Plugin plugin) {
		this.con = con;
		this.plugin = plugin;
	}
	
	public void loadPlayer(OfflinePlayer player) throws SQLException {
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM cosmetics WHERE UUID = '" + Utils.getUUID(player) + "';");
		if(rs.next()) {
			tokens.put(player, rs.getInt("TOKENS"));
			goldMedals.put(player, rs.getInt("MEDALS"));
		} else {
			con.createStatement().execute("INSERT INTO cosmetics (UUID) VALUES ('" + Utils.getUUID(player) + "');");
			tokens.put(player, 0);
			goldMedals.put(player, 0);
		}
	}
	
	public int getTokens(OfflinePlayer player) {
		if(tokens.containsKey(player)) return tokens.get(player);
		else { 
			return 0;
		}
	}
	public int getGoldMedals(OfflinePlayer player) {
		return goldMedals.get(player);
	}
	
	public boolean setTokens(Player player, int amount) {
		if(tokens.containsKey(player)) {
			tokens.replace(player, amount);
			return true;
		} else return false;
	}	
	public boolean addTokens(Player player, int amount) {
		return setTokens(player, getTokens(player) + amount);
	}
	public boolean setGoldMedals(Player player, int amount) {
		if(tokens.containsKey(player)) {
			tokens.replace(player, amount);
			return true;
		} else return false;
	}
	public boolean addGoldMedals(Player player, int amount) {
		return setGoldMedals(player, amount + getTokens(player));
	}
	public boolean addGoldMedal(Player player) {
		return addGoldMedals(player, 1);
	}
	
	public void unloadPlayer(Player player) throws SQLException {
		if(tokens.containsKey(player)) {
			con.createStatement().execute("UPDATE cosmetics SET TOKENS = " + tokens.get(player) + ", MEDALS = "+ goldMedals.get(player) + " WHERE UUID = '" + Utils.getUUID(player) + "';");
			tokens.remove(player);
			goldMedals.remove(player);
		}
	}
	public boolean isLoaded(OfflinePlayer player) {
		return tokens.containsKey(player);
	}
	
	public void saveAll() {
		for(OfflinePlayer p : tokens.keySet()) {
			try {
				con.createStatement().execute("UPDATE cosmetics SET TOKENS = " + tokens.get(p) + " WHERE UUID = '" + Utils.getUUID(p) + "';");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
