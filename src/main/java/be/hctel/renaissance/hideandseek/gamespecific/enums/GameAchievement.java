package be.hctel.renaissance.hideandseek.gamespecific.enums;

import java.util.HashMap;
import java.util.Map;

public enum GameAchievement {
	PEEKABOO("Peek a Boo", "Be the first hider to be found", "PEEKABOO",1),
	THECHOSENONE("The Chosen One!", "Be the first seeker", "THECHOSENONE",1),
	SETINPLACE("Set in place", "As a hider, find a spot and never move to win the game", "SETINPLACE",1),
	SECONDCHANCE("Second Chance", "Get hurt by a seeker and still manage to win whilist as a hider", "SECONDCHANCE",1),
	FURNACE("Things Are Heating Up!", "Kill a furnace", "FURNACE",1),
	ICE("ICEY What You Did There", "Kill an ice block", "ICE",1),
	PLANT("The Root Of The Issue", "Kill a plant pot", "PLANT",1),
	LEAF("LEAF Me Alone", "Kill a leaf block", "LEAF",1),
	ANVIL("A Heavy Issue", "Kill an anvil", "ANVIL",1),
	BEACON("Powered by Beacons", "Kill a beacon block", "BEACON",1),
	SNOW("SNOW Way!", "Kill a snow block", "SNOW", 1),
	DAREALMVP("Da Real MVP", "Kill 10 hiders in the same game", "DAREALMVP",1),
	SOCLOSEYETSOFAR("So Close Yet So Far", "Lose a gamse as a seeker with only 1 hider left", "SOCLOSEYETSOFAR",1),
	HIDER1("Found You!", "Kill your first hider", "HIDER1", 1),
	HIDER50("Bloodshed", "Kill 50 hiders", "HIDER50", 50),
	HIDER250("The Assasinator", "Kill 250 hiders", "HIDER250", 250),
	HIDER500("Slaughter Fest", "Kill 500 hiders", "HIDER500", 500),
	HIDER1000("Extermination", "Kill 1000 hiders", "HIDER1000", 1000),
	LEVELHALF("Ohhh... I'm half way there!", "Level a block half way to Max Level(50)", "LEVELHALF", 1),
	LEVELTOP("I'm on the of the world", "Level a block to Max Level(50)", "LEVELTOP", 1),
	TAUNTER("Catch me if you can!", "Use a taunt", "TAUNTER", 1),
	SEEKER1("Turning the Tables", "Kill your first seeker", "SEEKER1", 1),
	SEEKER25("Chaos Calamity", "Kill 25 Seekers", "SEEKER25", 25),
	SUNSET_TERRACE("The Sun goes Down", "Win as a hider OR get the most kills as a seeker on Sunset Terrace.", "HIDE_SunsetTerrace", 1),
	OASIS("A relaxing victory", "Win as a hider OR get the most kills as a seeker on Oasis.", "HIDE_Oasis", 1),
	VILLA("Living the Life", "Win as a hider OR get the most kills as a seeker on Villa.", "HIDE_Villa", 1),
	SPACE("I Can't Breathe", "Win as a hider OR get the most kills as a seeker on Space.", "SPACE", 1),
	VENICE("VEEE-NICE!", "Win as a hider OR get the most kills as a seeker on Venice Bridge.", "VENICE", 1),
	HOTEL_CALIFORNIA("Welcome to Hotel California!", "Win as a hider OR get the most kills as a seeker on Hotel California.", "HIDE_HotelCalifornia",1),
	HOTEL("Room Service", "Win as a hider OR get the most kills as a seeker on Hote.", "HOTEL", 1),
	BORA("BORAHHHHH!", "Win as a hider OR get the most kills as a seeker on Bora Bora.", "BORABORA", 1),
	CSOFFICE("It's Official!", "Win as a hider OR get the most kills as a seeker on CS_Office.", "CS_OFFICE", 1),
	NEXUS("Hack The Planet!", "Win as a hider OR get the most kills as a seeker on Nexus City.", "HIDE_NexusCity", 1),
	TOWNSQUARE("It's hip to be square!", "Win as a hider OR get the most kills as a seeker on Town Square.", "HIDE_TownSquareRevamp_REDO", 1),
	INDUSTRIA("Indusrty Pro", "Win as a hider OR get the most kills as a seeker on Industria.", "INDUSTRIA", 1),
	PRIPYAT("Radiating Results", "Win as a hider OR get the most kills as a seeker on Prypiat.", "PRIPYAT",1),
	BEFORESPACE("Launch Codes", "Win as a hider OR get the most kills as a seeker on Before Space.", "BEFORESPACE", 1),
	PARIS("Eifel AWESOME", "Win as a hider OR get the most kills as a seeker on Paris.", "PARIS", 1),
	CRUISE("Setting Sail", "Win as a hider OR get the most kills as a seeker on Cruise.", "CRUISE", 1),
	HEARTHSTONE("Village People", "Win as a hider OR get the most kills as a seeker on Hearthstone Village.", "HEARTHSTONE", 1),
	SHIPYARD("Salty Sailor", "Win as a hider OR get the most kills as a seeker on Shipyard.", "SHIPYARD", 1),
	ANIMAL("Nohas Ark", "Win as a hider OR get the most kills as a seeker on Animal Village.", "ANIMALVILLAGE", 1),
	LOTUS("Photosynthesis", "Win as a hider OR get the most kills as a seeker on Lutos.", "LOTUS", 1),
	KINGSTON("Driving home for Christmmas", "Win as a hider OR get the most kills as a seeker on Kingston.", "HIDE_Kingston", 1),
	HUMBUG("Humbug St.", "Win as a hider OR get the most kills as a seeker on Humbug St.", "HIDE_HumbugSt", 1),
	CHINATOWN("Chinatown", "Win as a hider OR get the most kills as a seeker on Chinatown.", "HIDE_ChinaTown",1),
	TALAVERA("Talavera", "Win as a hider OR get the most kills as a seeker on Talavera.", "HIDE_Talavera",1),
	GOLDRUSH("Gold, Gold! Gold everywhere!", "Win as a hider OR get the most kills as a seeker on Goldrush.", "HIDE_Goldrush", 1),
	PINEAPPLE("Pineapple Port", "Win as a hider OR get the most kills as a seeker on Pineapple Port.", "HIDE_PineapplePort", 1),
	HOSPITAL("It's an emmergency!", "Win as a hider OR get the most kills as a seeker on Hospital.", "HIDE_Hospital", 1),
	SEQUOIA("May the FOREST be with you", "Win as a hider OR get the most kills as a seeker on Sequoia.", "HIDE_Sequoia", 1),
	KEEP("It's MINE", "Win as hider OR het the most kills as a seeer on Keep", "KEEP",1),
	MASTER("The last place You look", "Unlock all achievements in Hide and Seek", "MASTER", 1);
	
	String name;
	String description;
	String json;
	int unlockLevel;
		
	private static final Map<String, GameAchievement> lookup = new HashMap<String, GameAchievement>();
	static {
		for(GameAchievement d : GameAchievement.values()) {
			lookup.put(d.getJsonCode(), d);
		}
	}
	
	private GameAchievement(String name, String description, String json, int unlockLevel) {
		this.name = name;
		this.description = description;
		this.json = json;
		this.unlockLevel = unlockLevel;
		
	}
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String getJsonCode() {
		return json;
	}
	public int getUnlockProgress() {
		return unlockLevel;
	}
	
	
	public static GameAchievement getFromJSON(String jsoncode) {
		if(lookup.containsKey(jsoncode))return lookup.get(jsoncode);
		else return null;
	}
	
}
