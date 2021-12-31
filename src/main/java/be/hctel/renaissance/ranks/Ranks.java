package be.hctel.renaissance.ranks;

import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;

/*
 * This file is a part of the Renaissance Project API
 */

public enum Ranks {
	/**
	 * Regular rank.
	 * Gets 1 vote, color is blue
	 */
	REGULAR("Regular Rank", ChatColor.BLUE, 1, 1),
	/**
	 * <p style="color:#ffaa00">Gold rank</p>
	 * <p>
	 * <p>First premium rank, 30 days time limit
	 * <p>
	 * <p>2 votes
	 */
	GOLD("Gold Premium", ChatColor.GOLD, 2, 2),
	/**
	 * <p style="color:#55FFFF">Diamond rank</p>
	 * <p>
	 * <p>60 days time limit
	 * <p>
	 * <p>2 votes
	 */
	DIAMOND("Diamond Premium", ChatColor.AQUA, 3, 2),
	/**
	 * <p style="color:#55FF55">Emerald Rank</p>
	 * <p>
	 * <p>90 days time limit
	 * <p>
	 * <p>3 votes
	 */
	EMERALD("Emerald Premium", ChatColor.GREEN, 4, 3),
	/**
	 * <p style="color:#FF55FF">Ultimate Rank</p>
	 * <p>
	 * <p>No time limit
	 * <p>
	 * <p>4 votes
	 */
	ULTIMATE("Ultimate", ChatColor.LIGHT_PURPLE , 5, 4),
	/**
	 * <p style="color:#00AA00">Helper</p>
	 * <p>
	 * <p>No time limit
	 * <p>
	 * <p><b>Staff rank</b>
	 * <p>
	 * <p>4 votes
	 */
	HELPER("Helper", ChatColor.DARK_GREEN, 6, 4),
	/**
	 * <p style="color:#FF5555">Moderator</p>
	 * <p>
	 * <p>No time limit
	 * <p>
	 * <p><b>Staff rank</b>
	 * <p>
	 * <p>4 votes
	 */
	MOD("Moderator", ChatColor.RED, 7, 4),
	/**
	 * <p style="color:#AA0000">Sr. Moderator</p>
	 * <p>
	 * <p>No time limit
	 * <p>
	 * <p><b>Staff rank</b>
	 * <p>
	 * <p>4 votes
	 */
	SR_MOD("Sr. Moderator", ChatColor.DARK_RED, 8, 4),
	/**
	 * <p style="color:#FFFF55">Renaissance Team</p>
	 * <p>
	 * <p>No time limit
	 * <p>
	 * <p><b>Staff rank</b>
	 * <p>
	 * <p>5 votes
	 */
	TEAM("Renaissance Team", ChatColor.YELLOW, 9, 5);
	
	
	private static HashMap<Integer, Ranks> lookup = new HashMap<Integer, Ranks>();
	static {
		for(Ranks a : Ranks.values()) {
			lookup.put(a.getIndex(), a);
		}
	}
	
	
	String name;
	ChatColor color;
	int index;
	int votes;
	private Ranks(String name, ChatColor color, int index, int votes) {
		this.index = index;
		this.color = color;
		this.name = name;
		this.votes = votes;
	}
	
	/**
	 * Gets the internal number the rank.
	 * @return the internal rank code
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Gets the chatcolor matching the rank (use in chat and tab)
	 * @return the chatcolor of the rank
	 */
	public ChatColor getColor() {
		return color;
	}
	
	/**
	 * Gets the user-friendly rank name
	 * @return the user-friendly rank name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the amount of votes a player with this rank has.
	 * @return the number of votes
	 */
	public int getVotes() {
		return votes;
	}
	
	/**
	 * Gets a rank from its internal code
	 * @param index the internal rank code
	 * @return the rank matching the rank code
	 */
	public static Ranks getRank(int index) {
		return lookup.get(index);
	}
}
