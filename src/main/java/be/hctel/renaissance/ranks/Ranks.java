package be.hctel.renaissance.ranks;

import java.util.HashMap;

import net.md_5.bungee.api.ChatColor;

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
	HELPER("Helper", ChatColor.DARK_GREEN, 6, 3),
	MOD("Moderator", ChatColor.RED, 7, 3), 
	SR_MOD("Sr. Moderator", ChatColor.DARK_RED, 8, 3),
	TEAM("Renaissance Team", ChatColor.YELLOW, 9, 4);
	
	
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
	
	public int getIndex() {
		return index;
	}
	public ChatColor getColor() {
		return color;
	}
	public String getName() {
		return name;
	}
	public int getVotes() {
		return votes;
	}
	public static Ranks getRank(int index) {
		return lookup.get(index);
	}
}
