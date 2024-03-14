package be.hctel.renaissance.hideandseek.gamespecific.enums;

import java.util.ArrayList;
import java.util.HashMap;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;


public enum GameRanks {
	BLIND("§7", "Blind", 0, 100),
	SHORT_SIGHTED("§3", "Short Sighted", 100, 1000),
	SNEAKER("§b", "Sneaker", 1000, 2500),
	SNEAKY("§d", "Sneaky", 2500, 5000),
	DECEPTIVE("§6", "Deceptive", 5000, 10000),
	MYSTERIOUS("§e", "Mysterious", 10000, 15000),
	DISGUISED("§a", "Disguised", 15000, 20000),
	CAMOUFLAGED("§2", "Camouflaged", 20000, 30000),
	CHAMELEON("§c", "Chameleon", 30000, 40000),
	STEALTHY("§b", "Stealthy", 40000, 50000),
	MASKED("§6","Masked", 50000, 75000),
	HUNTER("§e", "Hunter", 75000, 100000),
	MAGICIAN("§d", "Magician", 100000, 150000),
	ESCAPIST("§3", "Escapist", 150000, 300000),
	INVISIBLE("§9", "Invisible", 300000, 500000),
	SHADOW("§5", "Shadow", 500000, 750000),
	LOBBY("§2", "Lobby", 750000, 1000000),
	HOUDINI("§b§l", "Houdini", 1000000, 1750000),
	NINJA("§8§l", "Ninja", 1750000, 2500000),
	WALLY("§c§l", "Wally", 2500000, 4000000),
	GHOST("§f§l", "Ghost", 4000000, 6000000),
	SILOUHETTE("§3§l", "Siloihette", 6000000, 8000000),
	PHANTOM("§5§l", "Phantom", 8000000, 10000000),
	VANISHED("§1§l", "Vanished", 10000000, 1000000000),
	MOD("§a§l", "Master §e§lof §b§lDisguise", Integer.MAX_VALUE-1, Integer.MAX_VALUE),
	CODEBREAKER("§0§l", "Code Breaker", 1000000000, Integer.MAX_VALUE-2);
	
	
	private static HashMap<Integer, GameRanks> lookup1 = new HashMap<Integer, GameRanks>();
	private static HashMap<Integer, GameRanks> lookup2 = new HashMap<Integer, GameRanks>();
	static {
		for(GameRanks a : GameRanks.values()) {
			lookup1.put(a.getMaxPoints(), a);
			lookup2.put(a.getMinPoints(), a);
		}
	}
	
	String colorCode;
	String name;
	int minPoints;
	int maxPoints;
	private GameRanks(String colorCode, String name, int minPoints, int maxPoints) {
		this.colorCode = colorCode;
		this.name = name;
		this.minPoints = minPoints;
		this.maxPoints = maxPoints;
	}
		
	public int getMaxPoints() {
		return maxPoints;
	}
	public int getMinPoints() {
		return minPoints;
	}
	public String getChatColor() {
		return colorCode;
	}
	public String getName() {
		return name;
	}
	public TextComponent getTextComponent() {
		TextComponent t = new TextComponent(getChatColor() + getName());
		t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(getChatColor() + getName() + "§f[" + getMinPoints() + "-" + getMaxPoints() + "]").create()));
		return t;
	}	
	
	public static GameRanks getMatchingRank(int points) {
		ArrayList<GameRanks> all = new ArrayList<GameRanks>();
		GameRanks out = BLIND;
		for(GameRanks a : GameRanks.values()) {
			all.add(a);
		}
		for(GameRanks a : all) {
			if(a.getMaxPoints() > points && a.getMinPoints() <= points) out = a;
		}
		return out;
	}
}
