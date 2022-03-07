package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class Preferences {
	HashMap<Player, Boolean> viewsSelfDisguise = new HashMap<Player, Boolean>();

	Plugin plugin;
	Connection con;
	
	public Preferences(Plugin plugin, Connection con) {
		this.plugin = plugin;
		this.con = con;
	}
	
	public void loadPlayer(Player player) throws SQLException {
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM HIDE WHERE UUID = '" + Utils.getUUID(player) + "';");
	}
}
