package be.hctel.renaissance.hideandseek.nongame.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import be.hctel.renaissance.hideandseek.gamespecific.enums.GameAchievement;
import be.hctel.renaissance.hideandseek.gamespecific.enums.JoinMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.AdvancedMath;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;


/**
 * A class manipulating JSONObjects for eassy stats saving in an SQL Database.
 * 
 * Here the class was created for HideAndSeek (but can easily be adapted). 
 * 
 * @author hugod
 *
 */
public class Stats {
	private Connection con;
	private String baseJson = "{\"UUID\":\"%UUID\",\"total_points\":0,\"victories\":0,\"hiderkills\":0,\"seekerkills\":0,\"deaths\":0,\"gamesplayed\":0,\"blocks\":\"\",\"bookupgrade\":null,\"timealive\":0,\"rawBlockExperience\":{},\"blockExperience\":{},\"achievements\":{\"SEEKER25\":{\"progress\":0,\"unlockedAt\":0},\"SEEKER1\":{\"progress\":0,\"unlockedAt\":0},\"SETINPLACE\":{\"progress\":0,\"unlockedAt\":0},\"HIDER500\":{\"progress\":0,\"unlockedAt\":0},\"HIDER250\":{\"progress\":0,\"unlockedAt\":0},\"HIDER50\":{\"progress\":0,\"unlockedAt\":0},\"HIDER1000\":{\"progress\":0,\"unlockedAt\":0},\"HIDER1\":{\"progress\":0,\"unlockedAt\":0},},\"lastlogin\":%TIME%,\"firstlogin\":%TIME%,\"title\":\"Blind\"}";
	private HashMap<String, JSONObject> jsonList = new HashMap<String , JSONObject>();
	private HashMap<OfflinePlayer, JSONArray> unlockedJMS = new HashMap<OfflinePlayer, JSONArray>();
	private HashMap<OfflinePlayer, Integer> jms = new HashMap<OfflinePlayer, Integer>();
	String topPlayerUUID = "";
	
	
	public Stats(Connection con) {
		this.con = con;
		try {
			ResultSet rs = con.createStatement().executeQuery("SELECT * FROM HIDE ORDER BY points DESC LIMIT 1;");
			if(rs.next()) {
				topPlayerUUID = rs.getString("UUID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads a player into the cache
	 * @param player the {@link Player} to load
	 */
	public void load(Player player) {
		String json = baseJson;
		int joinMessage = 0;
		JSONArray unlockedjms = new JSONArray("[\"HIDE\"]");
		String uuid = Utils.getUUID(player);
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM HIDE WHERE UUID = '" + uuid + "';");
			if(rs.next()) {
				json = rs.getString("JSON");
				joinMessage  = rs.getInt("usedJoinMessage");
				unlockedjms = new JSONArray(rs.getString("unlockedJoinMessage"));
			} else {
				json.replace("%UUID", Utils.getUUID(player));
				json.replace("%TIME%", System.currentTimeMillis()+ "");
				st.execute("INSERT INTO HIDE (UUID, JSON, unlockedJoinMessage) VALUES ('" + Utils.getUUID(player) + "', '" + json.toString() + "', '[\"HIDE\"]');");
			}
			JSONObject j = new JSONObject(json);
			jsonList.put(Utils.getUUID(player), j);
			unlockedJMS.put(player, unlockedjms);
			jms.put(player, joinMessage);
			Bukkit.getServer().getLogger().log(Level.INFO, "Loaded stats! UUID : " + uuid + ", points " + j.getInt("total_points") + ", joinMessages " + joinMessage +  "," + unlockedjms);
			if(j.getInt("total_points") != rs.getInt("points")) {
				st.execute(String.format("UPDATE HIDE SET points = %d WHERE UUID = '%s'", j.getInt("total_points"), uuid));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * Loads an OfflinePlayer to the cache. Same as {@link loadPlayer(Player)} but with an OfflinePlayer
	 * <br>Useful for offline stats checking
	 * @param player the {@link OfflinePlayer} to load
	 */
	public void load(OfflinePlayer player) {
		String json = baseJson;
		int joinMessage = 0;
		JSONArray unlockedjms = new JSONArray("[\"HIDE\"]");
		String uuid = Utils.getUUID(player);
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM HIDE WHERE UUID = '" + uuid + "';");
			if(rs.next()) {
				json = rs.getString("JSON");
				joinMessage  = rs.getInt("usedJoinMessage");
				unlockedjms = new JSONArray(rs.getInt("unlockedJoinMessage"));
			} else {
				json.replace("%UUID", Utils.getUUID(player));
				json.replace("%TIME%", System.currentTimeMillis()+ "");
				st.execute("INSERT INTO HIDE (UUID, JSON, unlockedJoinMessage) VALUES ('" + Utils.getUUID(player) + "', '" + json.toString() + "', '[\"HIDE\"]');");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject j = new JSONObject(json);
		jsonList.put(Utils.getUUID(player), j);
		unlockedJMS.put(player, unlockedjms);
		jms.put(player, joinMessage);
		Bukkit.getServer().getLogger().log(Level.INFO, "Loaded stats! UUID : " + uuid + ", points " + j.getInt("total_points") + ", joinMessages " + joinMessage +  "," + unlockedjms);
	}
	
	/**
	 * Checks if a player is loaded
	 * @param player
	 * @return true if the player is loaded; false if it isn't
	 */
	public boolean isLoaded(OfflinePlayer player) {
		return jsonList.containsKey(Utils.getUUID(player));
	}	
	
	/**
	 * Gets the chosen {@link JoinMessages} index of the player
	 * @param player
	 * @return
	 */
	
	public String getTopRankPlayer() {
		return topPlayerUUID;
	}
	
	public int getJoinMessageIndex(Player player) {
		return jms.get(player);
	}
	/**
	 * Gets the player's unlocked join messages
	 * @param player
	 * @return
	 */
	public ArrayList<JoinMessages> getUnlockedJoinMessages(Player player) {
		JSONArray a = unlockedJMS.get(player);
		ArrayList<JoinMessages> r = new ArrayList<JoinMessages>();
		for(int i = 0; i < a.length(); i++) {
			r.add(JoinMessages.getFromString(a.getString(i)));
		}
		return r;
	}
	public int getPoints(OfflinePlayer player) {
		return jsonList.get(Utils.getUUID(player)).getInt("total_points");
	}
	public int getPoints(Player player) {
		return jsonList.get(Utils.getUUID(player)).getInt("total_points");
	}
	public int getVictories(Player player) {
		return jsonList.get(Utils.getUUID(player)).getInt("victories");
	}
	public int getVictories(OfflinePlayer player) {
		return jsonList.get(Utils.getUUID(player)).getInt("victories");
	}
	public int getGamesPlayed(Player player) {
		return jsonList.get(Utils.getUUID(player)).getInt("gamesplayed");
	}
	public int getGamesPlayed(OfflinePlayer player) {
		return jsonList.get(Utils.getUUID(player)).getInt("gamesplayed");
	}
	public int getDeaths(Player player) {
		return jsonList.get(Utils.getUUID(player)).getInt("deaths");
	}
	public int getDeaths(OfflinePlayer player) {
		System.out.println(player);
		return jsonList.get(Utils.getUUID(player)).getInt("deaths");
	}
	public ArrayList<GameAchievement> getAchievements(Player player) {
		if(jsonList.get(Utils.getUUID(player)).get("achievements") == null) {
			return new ArrayList<GameAchievement>();
		}
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		ArrayList<GameAchievement> list = new ArrayList<GameAchievement>();
		for(String k : ach.keySet()) {
			if(GameAchievement.getFromJSON(k) != null) list.add(GameAchievement.getFromJSON(k));
		}
		return list;
	}
	public ArrayList<GameAchievement> getAchievements(OfflinePlayer player) {
		if(jsonList.get(Utils.getUUID(player)).get("achievements") == null) {
			return new ArrayList<GameAchievement>();
		}
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		ArrayList<GameAchievement> list = new ArrayList<GameAchievement>();
		for(String k : ach.keySet()) {
			if(GameAchievement.getFromJSON(k) != null) list.add(GameAchievement.getFromJSON(k));
		}
		return list;
	}
	public int getAchievementProgress(Player player, GameAchievement achievement) {
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		if(ach.has(achievement.getJsonCode())) {
			return ach.getJSONObject(achievement.getJsonCode()).getInt("progress");
		} else return 0;
	}
	public int getAchievementProgress(OfflinePlayer player, GameAchievement achievement) {
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		if(ach.has(achievement.getJsonCode())) {
			return ach.getJSONObject(achievement.getJsonCode()).getInt("progress");
		} else return 0;
	}
	/**
	 * @deprecated Returns -1 if not complete. Be careful when using
	 * @param player
	 * @param achievement
	 * @return
	 */
	public long getAchievementUnlockDate(Player player, GameAchievement achievement) {
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		if(ach.has(achievement.getJsonCode())) {
			if(getAchievementProgress(player, achievement) < achievement.getUnlockProgress()) return -1;
			else {
				return ach.getJSONObject(achievement.getJsonCode()).getLong("unlockedAt");
			}
		} else return -1;
		
	} 
	/**
	 * @deprecated Returns -1 if not complete. Be careful when using
	 * @param player
	 * @param achievement
	 * @return
	 */
	public long getAchievementUnlockDate(OfflinePlayer player, GameAchievement achievement) {
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		if(ach.has(achievement.getJsonCode())) {
			if(getAchievementProgress(player, achievement) < achievement.getUnlockProgress()) return -1;
			else {
				return ach.getJSONObject(achievement.getJsonCode()).getLong("unlockedAt");
			}
		} else return -1;
		
	} 
	public ArrayList<GameAchievement> getCompletedAchievements(Player player) {
		if(jsonList.get(Utils.getUUID(player)).get("achievements") == null) {
				return new ArrayList<GameAchievement>();
			}
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		ArrayList<GameAchievement> list = new ArrayList<GameAchievement>();
		for(String k : ach.keySet()) {
			if(GameAchievement.getFromJSON(k) != null) {
				if(getAchievementProgress(player, GameAchievement.getFromJSON(k)) >= GameAchievement.getFromJSON(k).getUnlockProgress()) {
					list.add(GameAchievement.getFromJSON(k));
				}
			}
		}
		return list;
	}
	public ArrayList<GameAchievement> getCompletedAchievements(OfflinePlayer player) {
		if(jsonList.get(Utils.getUUID(player)).get("achievements") == null) {
			//ArrayList<GameAchievement> out = new ArrayList<GameAchievement>();
			if(jsonList.get(Utils.getUUID(player)).get("achievements") == null) {
				return new ArrayList<GameAchievement>();
			}
		}
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		ArrayList<GameAchievement> list = new ArrayList<GameAchievement>();
		for(String k : ach.keySet()) {
			if(GameAchievement.getFromJSON(k) != null) {
				if(getAchievementProgress(player, GameAchievement.getFromJSON(k)) >= GameAchievement.getFromJSON(k).getUnlockProgress()) {
					list.add(GameAchievement.getFromJSON(k));
				}
			}
		}
		return list;
	}
	public ArrayList<GameAchievement> getUncompletedAchievements(Player player) {
		if(jsonList.get(Utils.getUUID(player)).get("achievements") == null) {
			ArrayList<GameAchievement> out = new ArrayList<GameAchievement>();
			for (GameAchievement a : GameAchievement.values()) {
				out.add(a);
			}
			return out;
		}
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		ArrayList<GameAchievement> list = new ArrayList<GameAchievement>();
		for(String k : ach.keySet()) {
			if(GameAchievement.getFromJSON(k) != null) {
				if(getAchievementProgress(player, GameAchievement.getFromJSON(k)) < GameAchievement.getFromJSON(k).getUnlockProgress()) {
					list.add(GameAchievement.getFromJSON(k));
				}
			}
		}
		return list;
	}
	public ArrayList<GameAchievement> getUncompletedAchievements(OfflinePlayer player) {
		if(jsonList.get(Utils.getUUID(player)).get("achievements") == null) {
			ArrayList<GameAchievement> out = new ArrayList<GameAchievement>();
			for (GameAchievement a : GameAchievement.values()) {
				out.add(a);
			}
			return out;
		}
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		ArrayList<GameAchievement> list = new ArrayList<GameAchievement>();
		for(String k : ach.keySet()) {
			if(GameAchievement.getFromJSON(k) != null) {
				if(getAchievementProgress(player, GameAchievement.getFromJSON(k)) < GameAchievement.getFromJSON(k).getUnlockProgress()) {
					list.add(GameAchievement.getFromJSON(k));
				}
			}
		}
		return list;
	}
	//Game specific
	public int getKilledSeekers(Player player) {
		return jsonList.get(Utils.getUUID(player)).getInt("hiderkills");
	}
	public int getKilledSeekers(OfflinePlayer player) {
		return jsonList.get(Utils.getUUID(player)).getInt("hiderkills");
	}
	public int getKilledHiders(Player player) {
		return jsonList.get(Utils.getUUID(player)).getInt("seekerkills");
	}
	public int getKilledHiders(OfflinePlayer player) {
		return jsonList.get(Utils.getUUID(player)).getInt("seekerkills");
	}
	public ArrayList<Material> getUnlockedBlocks(Player player) {
		JSONObject json = jsonList.get(Utils.getUUID(player));
		if(json.get("blocks") == null) return new ArrayList<Material>();
		else {
			ArrayList<Material> out = new ArrayList<Material>();
			String[] l = json.getString("blocks").split(",");
			for(String f : l) {
				if(f != "") {
					try {
					out.add(Utils.getItemStackFromNumericalID(Utils.convertToInt(f)).getType());
					} catch (NumberFormatException e) {
						System.out.println("bad number detected. Ignored.");
					}
				}
			}
			return out;
		}
	}
	public ArrayList<Material> getUnlockedBlocks(OfflinePlayer player) {
		if(!jsonList.containsKey(Utils.getUUID(player))) load(player);
		JSONObject json = jsonList.get(Utils.getUUID(player));
		if(json.get("blocks") == null) return new ArrayList<Material>();
		else {
			ArrayList<Material> out = new ArrayList<Material>();
			String[] l = json.getString("blocks").split(",");
			for(String f : l) {
				if(f != "") {
					try {
					out.add(Utils.getItemStackFromNumericalID(Utils.convertToInt(f)).getType());
					} catch (NumberFormatException e) {
						System.out.println("bad number detected. Ignored.");
					}
				}
			}
			return out;
		}
	}
	public int getRawBlockExperience(Player player, ItemStack block) {
		JSONObject json = jsonList.get(Utils.getUUID(player)).getJSONObject("rawBlockExperience");
		if(json.has(Utils.getFormattedName(block))) return json.getInt(Utils.getFormattedName(block));
		else return 0;
	}
	public int getRawBlockExperience(OfflinePlayer player, ItemStack block) {
		JSONObject json = jsonList.get(Utils.getUUID(player)).getJSONObject("rawBlockExperience");
		if(json.has(Utils.getFormattedName(block))) return json.getInt(Utils.getFormattedName(block));
		else return 0;
	}
	public int getBlockLevel(Player player, ItemStack block) {
		JSONObject json = jsonList.get(Utils.getUUID(player)).getJSONObject("blockExperience");
		if(json.has(Utils.getFormattedName(block))) return json.getInt(Utils.getFormattedName(block));
		else return 0;
	}
	public int getBlockLevel(OfflinePlayer player, ItemStack block) {
		JSONObject json = jsonList.get(Utils.getUUID(player)).getJSONObject("blockExperience");
		if(json.has(Utils.getFormattedName(block))) return json.getInt(Utils.getFormattedName(block));
		else return 0;
	}
	public String getBlockLevelString(Player player, ItemStack block) {
		int l = getBlockLevel(player, block);
		if(l == 0) {
			return "§8Unplayed block";
		}
		if(l < 5) {
			return "§7Lvl " + l + " §r";
		}
		else if(l >= 5 && l < 10) {
			return "§bLvl " + l + " §r";
		}
		else if(l >= 10 && l < 15) {
			return "§aLvl " + l + " §r";
		}
		else if(l >=15 && l < 20) {
			return "§eLvl " + l + " §r";
		}
		else if(l >= 20 && l < 25) {
			return "§6Lvl " + l + " §r";
		}
		else if(l >= 25 && l < 30) {
			return "§cLvl " + l + " §r";
		}
		else if(l >= 30 && l < 35) {
			return "§dLvl " + l + " §r";
		}
		else if(l >=35 && l < 40) {
			return "§3Lvl " + l + " §r";
		}
		else if(l >= 40 && l < 45) {
			return "§2Lvl " + l + " §r";
		}
		else if(l >= 45 && l < 50) {
			return "§5Lvl " + l + " §r";
		}
		else if( l >= 50) {
			return "§9Lvl " + l + " §r";
		}
		else {
			return "§4§lNaN §r";
		}
	}
	public String getSmallLevelProgressBar(Player player, ItemStack block) {
		int barsToFill = 0;
		int endLevelRequirements = AdvancedMath.arithmeticSum(50, 0, getBlockLevel(player, block))+50;
		int currentLevelXP = getRawBlockExperience(player, block)-(AdvancedMath.arithmeticSum(50, 0, getBlockLevel(player, block)-1)+50);
		if(currentLevelXP == 0) barsToFill = (int) Utils.map(currentLevelXP, 0, endLevelRequirements, 0, 20);
		int blankBar = barsToFill-20;
		String out = "";
		for(int i = 0; i < barsToFill; i++) {
			out = out + "§a&l│";
		}
		for(int i = 0; i < blankBar; i++) {
			out = out+ "§8§l│";
		}
		out = out + "   §f" + currentLevelXP + "/" + endLevelRequirements;
		return out;
	}
	public String getBigLevelProgressBar(Player player, ItemStack block) {
		int endLevelRequirements = AdvancedMath.arithmeticSum(50, 0, getBlockLevel(player, block))+50;
		int currentLevelXP = getRawBlockExperience(player, block)-(AdvancedMath.arithmeticSum(50, 0, getBlockLevel(player, block)-1)+50);
		int barsToFill = (int) Utils.map(currentLevelXP, 0, endLevelRequirements, 0, 40);
		int blankBar = barsToFill-40;
		String out = "";
		for(int i = 0; i < barsToFill; i++) {
			out = out + "§a&l│";
		}
		for(int i = 0; i < blankBar; i++) {
			out = out+ "§8§l│";
		}
		out = out + "   §f" + currentLevelXP + "/" + endLevelRequirements;
		return out;
	}
	public int getCurrentLevelExperience(Player player, ItemStack block) {
		return getRawBlockExperience(player, block)-(AdvancedMath.arithmeticSum(50, 0, getBlockLevel(player, block)-1)+50);
	}
	public int getCurrentLevelGoal(Player player, ItemStack block) {
		return AdvancedMath.arithmeticSum(50, 0, getBlockLevel(player, block))+50;
	}

	
	//Saving
	public void addPoints(Player player, int toAdd) {
		int oldValue = getPoints(player);
		jsonList.get(Utils.getUUID(player)).put("total_points", oldValue + toAdd);		
	}
	public void addVictory(Player player) {
		int oldValue = getVictories(player);
		jsonList.get(Utils.getUUID(player)).put("victories", oldValue + 1);
	}
	public void addGame(Player player) {
		int oldValue = getGamesPlayed(player);
		jsonList.get(Utils.getUUID(player)).put("gamesplayed", oldValue + 1);
	}
	public void addDeath(Player player) {
		int oldValue = getDeaths(player);
		jsonList.get(Utils.getUUID(player)).put("deaths", oldValue + 1);
	}
	public void completeAchievement(Player player, GameAchievement achievement) {
		JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		if(ach == null) ach = new JSONObject();
		JSONObject e = new JSONObject();
		e.put("progress", achievement.getUnlockProgress());
		e.put("unlockedAt", System.currentTimeMillis());
		ach.put(achievement.getJsonCode(), e);
		jsonList.get(Utils.getUUID(player)).put("achievements", ach);
	}
	public void addAchievementProgress(Player player, GameAchievement achievement, int toAdd) {
		int oldValue = getAchievementProgress(player, achievement);
		//JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		JSONObject e = new JSONObject();
		e.put("progress", oldValue + toAdd);
		e.put("unlockedAt", -1);
	}
	public void addPoints(OfflinePlayer player, int toAdd) {
		int oldValue = getPoints(player);
		jsonList.get(Utils.getUUID(player)).put("total_points", oldValue + toAdd);		
	}
	public void addVictory(OfflinePlayer player) {
		int oldValue = getVictories(player);
		jsonList.get(Utils.getUUID(player)).put("victories", oldValue + 1);
	}
	public void addGame(OfflinePlayer player) {
		int oldValue = getGamesPlayed(player);
		jsonList.get(Utils.getUUID(player)).put("gamesplayed", oldValue + 1);
	}
	public void addDeath(OfflinePlayer player) {
		int oldValue = getDeaths(player);
		jsonList.get(Utils.getUUID(player)).put("deaths", oldValue + 1);
	}
	public void completeAchievement(OfflinePlayer player, GameAchievement achievement) {
		//JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		JSONObject e = new JSONObject();
		e.put("progress", achievement.getUnlockProgress());
		e.put("unlockedAt", System.currentTimeMillis());
	}
	public void addAchievementProgress(OfflinePlayer player, GameAchievement achievement, int toAdd) {
		int oldValue = getAchievementProgress(player, achievement);
		//JSONObject ach = jsonList.get(Utils.getUUID(player)).getJSONObject("achievements");
		JSONObject e = new JSONObject();
		e.put("progress", oldValue + toAdd);
		e.put("unlockedAt", -1);
	}
	public void unlockJoinMessage(OfflinePlayer player, JoinMessages message) {
		unlockedJMS.get(player).put(message.toString());
	}
	public void setJoinMessage(OfflinePlayer player, JoinMessages message) {
		jms.put(player, message.getStorageCode());
	}
	
	public void saveAll() throws SQLException {
		Statement st = con.createStatement();
		for(String u : jsonList.keySet()) {
			st.execute("UPDATE HIDE SET JSON = '" + jsonList.get(u).toString() + "' WHERE UUID = '" + u + "';");
			st.execute("UPDATE HIDE SET unlockedJoinMessage = '" + unlockedJMS.get(Bukkit.getPlayer(UUID.fromString(Utils.getFullUUID(u)))).toString() + "' WHERE UUID = '" + u + "';");
			st.execute("UPDATE HIDE SET usedJoinMessage = " +jms.get(Bukkit.getPlayer(UUID.fromString(Utils.getFullUUID(u)))) + " WHERE UUID = '" + u + "';");
		}
	}
	
	public void savePlayer(OfflinePlayer offlinePlayer) throws SQLException {
		if(jsonList.containsKey(Utils.getUUID(offlinePlayer))) {
			Statement st = con.createStatement();
			String u = Utils.getUUID(offlinePlayer);
			st.execute("UPDATE HIDE SET JSON = '" + jsonList.get(u).toString() + "' WHERE UUID = '" + u + "';");
			st.execute("UPDATE HIDE SET unlockedJoinMessage = '" + unlockedJMS.get(Bukkit.getPlayer(UUID.fromString(Utils.getFullUUID(u)))).toString() + "' WHERE UUID = '" + u + "';");
			st.execute("UPDATE HIDE SET usedJoinMessage = " +jms.get(Bukkit.getPlayer(UUID.fromString(Utils.getFullUUID(u)))) + " WHERE UUID = '" + u + "';");
			st.execute(String.format("UPDATE HIDE SET POINTS = %d WHERE UUID = '%s';", jsonList.get(u).getInt("total_points"), u));
			jsonList.remove(u);
			unlockedJMS.remove(offlinePlayer);
			jms.remove(offlinePlayer);
		}
	}
	
	//Game specific
	public void addKilledSeeker(Player player) {
		int oldValue = getKilledSeekers(player);
		jsonList.get(Utils.getUUID(player)).put("hiderkills", oldValue + 1);
	}
	public void addKilledHider(Player player) {
		int oldValue = getKilledHiders(player);
		jsonList.get(Utils.getUUID(player)).put("seekerkills", oldValue + 1);
	}
	@SuppressWarnings("deprecation")
	public void unlockBlock(Player player, ItemStack block) {
		JSONObject json = jsonList.get(Utils.getUUID(player));
		if(json.get("blocks") == null) json.put("blocks", block.getTypeId());
		else {
			String oldValue = json.getString("blocks");
			String newValue = oldValue + "," + block.getTypeId();
			json.put("blocks", newValue);
		}
	}
	public void addBlockExperience(Player player, int toAdd, ItemStack block) {
		setBlockExperience(player, getRawBlockExperience(player, block)+toAdd, block);
	}
	public void setBlockExperience(Player player, int toSet, ItemStack block) {
		JSONObject rawBlockExperience = jsonList.get(Utils.getUUID(player)).getJSONObject("rawBlockExperience");
		JSONObject blockExperience = jsonList.get(Utils.getUUID(player)).getJSONObject("blockExperience");
		if(rawBlockExperience == null || !rawBlockExperience.has(Utils.getFormattedName(block))) { 
			rawBlockExperience.put(Utils.getFormattedName(block), toSet);
			blockExperience.put(Utils.getFormattedName(block), 1);
		} else {
			int level = 1;
			while(AdvancedMath.arithmeticSum(50, 50, level) >= toSet) {
				level++;
			}
			rawBlockExperience.put(Utils.getFormattedName(block), toSet);
			if(level > blockExperience.getInt(Utils.getFormattedName(block))) {
				blockExperience.put(Utils.getFormattedName(block), level);
			}
		}
	}
	public void addKilledSeeker(OfflinePlayer player) {
		int oldValue = getKilledSeekers(player);
		jsonList.get(Utils.getUUID(player)).put("hiderkills", oldValue + 1);
	}
	public void addKilledHider(OfflinePlayer player) {
		int oldValue = getKilledHiders(player);
		jsonList.get(Utils.getUUID(player)).put("seekerkills", oldValue + 1);
	}
	@SuppressWarnings("deprecation")
	public void unlockBlock(OfflinePlayer player, ItemStack block) {
		JSONObject json = jsonList.get(Utils.getUUID(player));
		if(json.get("blocks") == null) json.put("blocks", block.getTypeId());
		else {
			String oldValue = json.getString("blocks");
			String newValue = oldValue + "," + block.getTypeId();
			json.put("blocks", newValue);
		}
	}
	public void addBlockExperience(OfflinePlayer player, int toAdd, ItemStack block) {
		setBlockExperience(player, getRawBlockExperience(player, block)+toAdd, block);
	}
	public void setBlockExperience(OfflinePlayer player, int toSet, ItemStack block) {
		JSONObject rawBlockExperience = jsonList.get(Utils.getUUID(player)).getJSONObject("rawBlockExperience");
		JSONObject blockExperience = jsonList.get(Utils.getUUID(player)).getJSONObject("blockExperience");
		if(rawBlockExperience == null || !rawBlockExperience.has(Utils.getFormattedName(block))) { 
			rawBlockExperience.put(Utils.getFormattedName(block), toSet);
			blockExperience.put(Utils.getFormattedName(block), 1);
		} else {
			int level = 1;
			while(AdvancedMath.arithmeticSum(50, 50, level)+50 >= toSet) {
				level++;
			}
			rawBlockExperience.put(Utils.getFormattedName(block), toSet);
			if(level > blockExperience.getInt(Utils.getFormattedName(block))) {
				blockExperience.put(Utils.getFormattedName(block), level);
			}
		}
	}

	public int getKills(Player player) {
		return getKilledHiders(player) + getKilledSeekers(player);
	}
	
}