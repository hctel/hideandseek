package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import be.hctel.renaissance.global.IGameAchievement;

public class HideGameAchievement implements IGameAchievement {
	
	public static HideGameAchievement PEEKABOO = new HideGameAchievement("Peek a Boo", "Be the first hider to be found", "PEEKABOO",1);
	public static HideGameAchievement THECHOSENONE = new HideGameAchievement("The Chosen One!", "Be the first seeker", "THECHOSENONE",1);
	public static HideGameAchievement SETINPLACE = new HideGameAchievement("Set in place", "As a hider, find a spot and never move to win the game", "SETINPLACE",1);
	public static HideGameAchievement SECONDCHANCE = new HideGameAchievement("Second Chance", "Get hurt by a seeker and still manage to win whilist as a hider", "SECONDCHANCE",1);
	public static HideGameAchievement FURNACE = new HideGameAchievement("Things Are Heating Up!", "Kill a furnace", "FURNACE",1);
	public static HideGameAchievement ICE = new HideGameAchievement("ICEY What You Did There", "Kill an ice block", "ICE",1);
	public static HideGameAchievement PLANT = new HideGameAchievement("The Root Of The Issue", "Kill a plant pot", "PLANT",1);
	public static HideGameAchievement LEAF = new HideGameAchievement("LEAF Me Alone", "Kill a leaf block", "LEAF",1);
	public static HideGameAchievement ANVIL = new HideGameAchievement("A Heavy Issue", "Kill an anvil", "ANVIL",1);
	public static HideGameAchievement BEACON = new HideGameAchievement("Powered by Beacons", "Kill a beacon block", "BEACON",1);
	public static HideGameAchievement SNOW = new HideGameAchievement("SNOW Way!", "Kill a snow block", "SNOW", 1);
	public static HideGameAchievement DAREALMVP = new HideGameAchievement("Da Real MVP", "Kill 10 hiders in the same game", "DAREALMVP",1);
	public static HideGameAchievement SOCLOSEYETSOFAR = new HideGameAchievement("So Close Yet So Far", "Lose a gamse as a seeker with only 1 hider left", "SOCLOSEYETSOFAR",1);
	public static HideGameAchievement HIDER1 = new HideGameAchievement("Found You!", "Kill your first hider", "HIDER1", 1);
	public static HideGameAchievement HIDER50 = new HideGameAchievement("Bloodshed", "Kill 50 hiders", "HIDER50", 50);
	public static HideGameAchievement HIDER250 = new HideGameAchievement("The Assasinator", "Kill 250 hiders", "HIDER250", 250);
	public static HideGameAchievement HIDER500 = new HideGameAchievement("Slaughter Fest", "Kill 500 hiders", "HIDER500", 500);
	public static HideGameAchievement HIDER1000 = new HideGameAchievement("Extermination", "Kill 1000 hiders", "HIDER1000", 1000);
	public static HideGameAchievement LEVELHALF = new HideGameAchievement("Ohhh... I'm half way there!", "Level a block half way to Max Level (25)", "LEVELHALF", 1);
	public static HideGameAchievement LEVELTOP = new HideGameAchievement("I'm on the of the world", "Level a block to Max Level (50)", "LEVELTOP", 1);
	public static HideGameAchievement TAUNTER = new HideGameAchievement("Catch me if you can!", "Use a taunt", "TAUNTER", 1);
	public static HideGameAchievement SEEKER1 = new HideGameAchievement("Turning the Tables", "Kill your first seeker", "SEEKER1", 1);
	public static HideGameAchievement SEEKER25 = new HideGameAchievement("Chaos Calamity", "Kill 25 Seekers", "SEEKER25", 25);
	public static HideGameAchievement MASTER = new HideGameAchievement("The last place You look", "Unlock all achievements in Hide and Seek", "MASTER", 1);
	
	public static HashMap<HideGameMap, HideGameAchievement> perMapAchievement = new HashMap<HideGameMap, HideGameAchievement>();
	private static HashMap<String, HideGameAchievement> storageCodeToAch;
	
	private String name;
	private String description;
	private String storageCode;
	private int unlockProgress;
	public HideGameAchievement(String name, String description, String storageCode, int unlockProgress) {
		this.name = name;
		this.description = description;
		this.storageCode = storageCode;
		this.unlockProgress	= unlockProgress;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getStorageCode() {
		return storageCode;
	}

	@Override
	public int getUnlockLevel() {
		return unlockProgress;
	}
	
	public static HideGameAchievement getAchievementFromStorageName(String storageName) {
		if(storageCodeToAch == null) {
			storageCodeToAch = new HashMap<String, HideGameAchievement>();
			for(HideGameAchievement A : Arrays.asList(PEEKABOO, THECHOSENONE, SETINPLACE, SECONDCHANCE, FURNACE, ICE, PLANT, LEAF, ANVIL, BEACON, SNOW, DAREALMVP, SOCLOSEYETSOFAR, HIDER1, HIDER50, HIDER250, HIDER500, HIDER1000, LEVELHALF, LEVELTOP, TAUNTER, SEEKER1, SEEKER25, MASTER)) {
				storageCodeToAch.put(A.getStorageCode(), A);
			}
			for(HideGameAchievement A : perMapAchievement.values()) {
				storageCodeToAch.put(A.getStorageCode(), A);
			}			
		}
		return storageCodeToAch.get(storageName);
	}
	
	public static Collection<HideGameAchievement> values() {
		return storageCodeToAch.values();
	}
	
}
