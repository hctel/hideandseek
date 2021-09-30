package be.hctel.renaissance.ranks;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class RankManager {
	Connection con;
	HashMap<String, Ranks> cache = new HashMap<String, Ranks>();
	HashMap<String, Integer> hideCache = new HashMap<String, Integer >(); 
	public RankManager(Connection con) {
		this.con = con;
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
	public ChatColor getRankColor(Player player) {
		if(cache.containsKey(Utils.getUUID(player))) {
			if(hideCache.get(Utils.getUUID(player)) > 0) {
				return Ranks.getRank(hideCache.get(Utils.getUUID(player))).getColor();
			} else return cache.get(Utils.getUUID(player)).getColor();
		} else return ChatColor.BLUE;
	}
	
	public boolean changeRank(Player player, Ranks rank) {
		if(rank.getIndex() > cache.get(Utils.getUUID(player)).getIndex()) {
			player.sendMessage(Hide.header + ChatMessages.NO_RANK_CHANGE.toText());
			return false;
		} else {
			hideCache.replace(Utils.getUUID(player), rank.getIndex());
			player.sendMessage(Hide.header + ChatMessages.RANKCHANGE);
			return true;
		}
	} 
	public void toggleRank(Player player) {
		if(cache.get(Utils.getUUID(player)).getIndex() == 1) {
			player.sendMessage(Hide.header + ChatMessages.NO_RANK_CHANGE);
			return;
		}
		if(hideCache.get(Utils.getUUID(player)) == 1) {
			hideCache.replace(Utils.getUUID(player), cache.get(Utils.getUUID(player)).getIndex());
		} else {
			hideCache.replace(Utils.getUUID(player), 1);
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
