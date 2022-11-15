package be.hctel.renaissance.ranks;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

/*
 * This file is a part of the Renaissance Project API
 */

public class RankManager {
	Connection con;
	HashMap<String, Ranks> cache = new HashMap<String, Ranks>();
	HashMap<String, Integer> hideCache = new HashMap<String, Integer >(); 
	HashMap<Ranks, Team> teams = new HashMap<Ranks, Team>();
	String[] n = {"i", "h", "g", "f", "e", "d", "c", "b", "a"};
	public RankManager(Connection con, Plugin plugin) {
		this.con = con;
		for(Ranks R : Ranks.values()) {
			Team team = Bukkit.getScoreboardManager().getNewScoreboard().registerNewTeam(n[R.getIndex()-1]);
			team.setColor(R.getColor());
			teams.put(R, team);
		}
	}

	/**
	 * Loads the rank of a player
	 * @param player
	 */
	public void load(Player player) { 
		if(!(cache.containsKey(Utils.getUUID(player)))) {
			Statement st;
			try {
				st = con.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM RANKS WHERE UUID ='" + Utils.getUUID(player) + "';");
				if(rs.next()) {
					cache.put(Utils.getUUID(player), Ranks.getRank(rs.getInt("rankID")));
					hideCache.put(Utils.getUUID(player), rs.getInt("hiddenRank"));
					teams.get(Ranks.getFromChatColor(getRankColor(player))).addEntry(player.getName());
				} else {
					st.execute("INSERT INTO RANKS (UUID) VALUES ('" + Utils.getUUID(player) + "');");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	public Ranks getRank(Player player) {
		return cache.get(Utils.getUUID(player));
	}
	public ChatColor getRankColor(OfflinePlayer offlinePlayer) {
		if(cache.containsKey(Utils.getUUID(offlinePlayer))) {
			if(hideCache.get(Utils.getUUID(offlinePlayer)) > 0) {
				return Ranks.getRank(hideCache.get(Utils.getUUID(offlinePlayer))).getColor();
			} else return cache.get(Utils.getUUID(offlinePlayer)).getColor();
		} else return ChatColor.BLUE;
	}
	
	public boolean changeRank(Player player, Ranks rank) {
		if(rank.getIndex() > cache.get(Utils.getUUID(player)).getIndex()) {
			player.sendMessage(Hide.header + ChatMessages.NO_RANK_CHANGE.toText());
			return false;
		} else {
			hideCache.replace(Utils.getUUID(player), rank.getIndex());
			player.sendMessage(Hide.header + ChatMessages.RANKCHANGE.toText());
			return true;
		}
	} 
	public void toggleRank(Player player) {
		if(cache.get(Utils.getUUID(player)).getIndex() == 1) {
			player.sendMessage(Hide.header + ChatMessages.NO_RANK_CHANGE.toText());
			return;
		}
		if(hideCache.get(Utils.getUUID(player)) == 1) {
			hideCache.replace(Utils.getUUID(player), cache.get(Utils.getUUID(player)).getIndex());
			player.sendMessage(Hide.header + ChatMessages.RANKTOGGLE.toText());
		} else {
			hideCache.replace(Utils.getUUID(player), 1);
			player.sendMessage(Hide.header + ChatMessages.RANKTOGGLE.toText());
		}
	}
	public void unLoad(Player player) {
		if(cache.containsKey(Utils.getUUID(player))) {
			cache.remove(Utils.getUUID(player));
			hideCache.remove(Utils.getUUID(player));
		}
	}
	public void reLoad(Player player) {
		cache.remove(Utils.getUUID(player));
		hideCache.remove(Utils.getUUID(player));
		load(player);
	}
	public void saveAll() throws SQLException {
		Statement st = con.createStatement();
		for(String s : hideCache.keySet()) {
			st.execute("UPDATE RANKS SET hiddenRank = " + hideCache.get(s) + " WHERE UUID = '" + s + "';");
		}
	}
}
