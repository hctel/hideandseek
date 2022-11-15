package be.hctel.renaissance.cosmetics;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

/*
 * This file is a part of the Renaissance Project API
 */

public class CosmeticsManager {
	Connection con;
	Plugin plugin;
	HashMap<Player, Integer> tokens = new HashMap<Player, Integer>();
	public CosmeticsManager(Connection con, Plugin plugin) {
		this.con = con;
		this.plugin = plugin;
	}
	
	public void loadPlayer(Player player) throws SQLException {
		ResultSet rs = con.createStatement().executeQuery("SELECT * FROM cosmetics WHERE UUID = '" + Utils.getUUID(player) + "';");
		if(rs.next()) {
			tokens.put(player, rs.getInt("TOKENS"));
		} else {
			con.createStatement().execute("INSERT INTO cosmetics (UUID) VALUES ('" + Utils.getUUID(player) + "');");
		}
	}
	
	public int getTokens(Player player) {
		if(tokens.containsKey(player)) return tokens.get(player);
		else { 
			return 0;
		}
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
	
	public void unloadPlayer(Player player) throws SQLException {
		if(tokens.containsKey(player)) {
			con.createStatement().execute("UPDATE cosmetics SET TOKENS = " + tokens.get(player) + " WHERE UUID = '" + Utils.getUUID(player) + "';");
		}
	}
}
