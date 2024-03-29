package be.hctel.renaissance.ranks;

import java.util.HashMap;

import org.bukkit.ChatColor;


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
	 * Gold rank
	 * 
	 * First premium rank, 30 days time limit
	 * 
	 * 2 votes, gold color
	 */
	GOLD("Gold Premium", ChatColor.GOLD, 2, 2),
	/**
	 * Diamond rank
	 * 
	 * 
	 */
	DIAMOND("Diamond Premium", ChatColor.AQUA, 3, 3),
	
	EMERALD("Emerald Premium", ChatColor.GREEN, 4, 3),
	ULTIMATE("Ultimate Premium", ChatColor.LIGHT_PURPLE , 5, 3),
	VIP("VIP", ChatColor.DARK_PURPLE , 6, 3),
	TEAM_NECTAR("Team Nectar", ChatColor.DARK_AQUA, 7, 3),
	HELPER("Helper", ChatColor.DARK_GREEN, 8, 3),
	MOD("Moderator", ChatColor.RED, 9, 3), 
	SR_MOD("Sr. Moderator", ChatColor.DARK_RED, 10, 3),
	ASSISTANT("Staff Assistant", ChatColor.DARK_RED, 11, 3),
	MANAGER("Staff Manager", ChatColor.DARK_RED, 12, 3),
	COMMUNITY_MANAGER("Community Manager", ChatColor.DARK_RED, 13, 3),
	DEV("Developer", ChatColor.GRAY, 14, 4),
	TEAM("Renaissance Team", ChatColor.YELLOW, 15, 4);
	
	
	private static HashMap<Integer, Ranks> lookup = new HashMap<Integer, Ranks>();
	static {
		for(Ranks a : Ranks.values()) {
			lookup.put(a.getIndex(), a);
		}
	}
	private static HashMap<ChatColor, Ranks> lookupColor = new HashMap<ChatColor, Ranks>();
	static {
		for(Ranks a : Ranks.values()) {
			lookupColor.put(a.getColor(), a);
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
	 * 
	 * @return The storage index of the rank
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * 
	 * @return The default {@link ChatColor} of this rank
	 */
	public ChatColor getColor() {
		return color;
	}
	/**
	 * 
	 * @return The full name of the rank
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @return The number of votes players possessing this rank have
	 */
	public int getVotes() {
		return votes;
	}
	/**
	 * 
	 * @param index : The storage index to get the rank from
	 * @return The matching rank
	 * @throws IllegalArgumentException If the index is outside the indexes range
	 */
	public static Ranks getRank(int index) throws IllegalArgumentException {
		if(!lookup.containsKey(index)) throw new IllegalArgumentException("The rank system does not contain the specified rank index.");
		return lookup.get(index);
	}
	/**
	 * 
	 * @param color
	 * @return
	 */
	public static Ranks getFromChatColor(ChatColor color) {
		return lookupColor.get(color);
	}
}
