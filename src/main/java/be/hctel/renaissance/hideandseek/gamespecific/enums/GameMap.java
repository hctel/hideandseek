package be.hctel.renaissance.hideandseek.gamespecific.enums;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import be.hctel.renaissance.hideandseek.nongame.utils.SpawnLocation;

public enum GameMap {
	
	OFFICE("CS_Office", "HIDE_Office", new SpawnLocation("HIDE_Office", -3.5, 4.5, 0.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Office", 22.5, 13.5, -9.5, 0.1f, 0.1f)),
	TALAVERA("Talavera", "HIDE_Talavera", new SpawnLocation("HIDE_Talavera", -3.5, 6.5, -11.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Talavera", 19.5, 18.5, -31.5, 90.1f, 0.1f)),
	GOLDRUSH("Goldrush", "HIDE_Goldrush", new SpawnLocation("HIDE_Goldrush", 2.5, 47.5, -12.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Goldrush", 21.5, 75.5, -11.5, 90.1f, 0.1f)),
	VENICE("Venice Bridge", "HIDE_Venice", new SpawnLocation("HIDE_Venice", -8.0, 65, 6.0, 0.1f, 0.1f), new SpawnLocation("HIDE_Venice", 43.5, 74, -46.5, 0.1f, 0.1f)),
	INDUSTRIA("Industria", "HIDE_Industria", new SpawnLocation("HIDE_Industria", -33.5, 4,27.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Industria", -8.5, 16, -23.5, 0.1f, 0.1f)),
	SUNSET_TERRACE("Sunset Terrace", "HIDE_Sunset", new SpawnLocation("HIDE_Sunset", 68.5, 25, 9.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Sunset", 105.5, 44, 16.5, 90.1f, 0.1f)),
	TEITAKU("Teitaku", "HIDE_Teitaku", new SpawnLocation("HIDE_Teitaku", -79.5, 24, 48.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Teitaku", -160.5, 12, 447.5, 90.1f, 0.1f)),
	SEQUOIA("Sequoia", "HIDE_Sequoia", new SpawnLocation("HIDE_Sequoia", -91.5, 38, -2.5, -90.1f, 0.1f), new SpawnLocation("HIDE_Sequoia", -79.5, 24, 48.5, 0.1f, 0.1f)),
	HOTEL_CALIFORNIA("Hotel California", "HIDE_HotelCalifornia",new SpawnLocation("HIDE_HotelCalifornia", 18.5, 25, 0.5, 0.1f, 0.1f), new SpawnLocation("HIDE_HotelCalifornia", 18.5, 25, 6.5, 180.1f, 0.1f)),
	LOTUS("Lotus", "HIDE_Lotus", new SpawnLocation("HIDE_Lotus", -9.5, 126, 0.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Lotus", 12.5, 113, 0.5, 90.1f, 0.1f)),
	HUMBUG("Humbug St.", "HIDE_Humbug", new SpawnLocation("HIDE_Humbug", 29.5, 6.5, 52.5, 135.1f, 0.1f), new SpawnLocation("HIDE_Humbug", -3.0, 64.5, -35.0, 0.1f, 0.0f)),
	HEARTHSTONE_VILLAGE("Hearthstone Village", "HIDE_Hearthstone", new SpawnLocation("HIDE_Hearthstone", -9.5, 16.0, 6.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Hearthstone", -40.5, 32.0, 11.5, 0.1f, 0.1f)),
	NEXUS_CITY("Nexus City", "HIDE_Nexus", new SpawnLocation("HIDE_Nexus", -15.5, 27.5, -12.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Nexus", -1.5, 34.5, 29.5, 180.1f, 0.1f)),
	DEPARTURE("Departure", "HIDE_Departure", new SpawnLocation("HIDE_Departure", 33.5, 36.2, 26.5, 90.1f, 0.1f), new SpawnLocation("HIDE_Departure", 14.5, 31.2, 49.5, -90.1f, 0.1f)),
	HOTEL("Hotel", "HIDE_Hotel", new SpawnLocation("HIDE_Hotel", -2, 64, -35, 0.1f, 0.1f), new SpawnLocation("HIDE_Hotel", -3, 61, -21, 0.1f, 0.1f));
	
	String mapName;
	String systemName;
	SpawnLocation hiderlocation;
	SpawnLocation seekerlocation;
	private static HashMap<String, GameMap> lookup = new HashMap<String, GameMap>();
	static {
		for(GameMap m : GameMap.values()) {
			lookup.put(m.getSystemName(), m);
		}
		
	}
	
	private GameMap(String mapName, String systemName, SpawnLocation hiderlocation, SpawnLocation seekerlocation) {
		this.mapName = mapName;
		this.systemName = systemName;
		this.seekerlocation = seekerlocation;
		this.hiderlocation = hiderlocation;
	}
	public String getMapName() {
		return mapName;
	}
	public String getSystemName() {
		return systemName;
	}
	public ArrayList<ItemStack> getDefaultBlocks() {
		ArrayList<ItemStack> toAdd = new ArrayList<ItemStack>();
		if(this == GameMap.HOTEL) {
			ItemStack a = new ItemStack(Material.WOOD);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.SNOW_BLOCK);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.QUARTZ_BLOCK);
			toAdd.add(c);
			ItemStack d = new ItemStack(Material.BEACON);
			toAdd.add(d);
		}
		if(this == GameMap.HUMBUG) {
			@SuppressWarnings("deprecation")
			ItemStack a = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 5);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.JUKEBOX);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.ANVIL);
			toAdd.add(c);
			ItemStack d = new ItemStack(Material.WORKBENCH);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.BOOKSHELF);
			toAdd.add(e);
		}
		if(this == GameMap.HEARTHSTONE_VILLAGE) {
			ItemStack a = new ItemStack(Material.HAY_BLOCK);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.WORKBENCH);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.FURNACE);
			toAdd.add(c);
			ItemStack d = new ItemStack(Material.COAL_BLOCK);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.BOOKSHELF);
			toAdd.add(e);
			}
		if(this == GameMap.LOTUS) {
			ItemStack a = new ItemStack(Material.WORKBENCH);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.BEACON);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.BOOKSHELF);
			toAdd.add(c);
			ItemStack d = new ItemStack(Material.ANVIL);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.FURNACE);
			toAdd.add(e);
		}
		if(this == GameMap.NEXUS_CITY) {
			ItemStack a = new ItemStack(Material.QUARTZ_STAIRS);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.BEACON);
			toAdd.add(b);
			@SuppressWarnings("deprecation")
			ItemStack c = new ItemStack(Material.PRISMARINE, 1, (short) 0, (byte) 2);
			toAdd.add(c);
			@SuppressWarnings("deprecation")
			ItemStack d = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 1);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.ANVIL);
			toAdd.add(e);
		}
		if(this == GameMap.OFFICE) {
			ItemStack a = new ItemStack(Material.LEAVES);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.FLOWER_POT);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.PISTON_BASE);
			toAdd.add(c);
			ItemStack d = new ItemStack(Material.WORKBENCH);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.BOOKSHELF);
			toAdd.add(e);
			ItemStack f = new ItemStack(Material.FURNACE);
			toAdd.add(f);
			ItemStack g = new ItemStack(Material.ANVIL);
			toAdd.add(g);
		}
		if(this == GameMap.TALAVERA) {
			ItemStack a = new ItemStack(Material.BEACON);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.LEAVES);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.NOTE_BLOCK);
			toAdd.add(c);
			@SuppressWarnings("deprecation")
			ItemStack d = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 3);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.BOOKSHELF);
			toAdd.add(e);
		}
		if(this == GameMap.GOLDRUSH) {
			ItemStack a = new ItemStack(Material.ANVIL);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.HAY_BLOCK);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.WORKBENCH);
			toAdd.add(c);
			@SuppressWarnings("deprecation")
			ItemStack d = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 2);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.GOLD_ORE);
			toAdd.add(e);
		}
		if(this == GameMap.VENICE) {
			ItemStack a = new ItemStack(Material.BEACON);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.FLOWER_POT);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.JUKEBOX);
			toAdd.add(c);
		}
		if(this == GameMap.INDUSTRIA) {
			ItemStack a = new ItemStack(Material.WORKBENCH);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.BOOKSHELF);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.ANVIL);
			toAdd.add(c);
			ItemStack e = new ItemStack(Material.JUKEBOX);
			toAdd.add(e);
			ItemStack f = new ItemStack(Material.BEACON);
			toAdd.add(f);
			ItemStack g = new ItemStack(Material.FLOWER_POT);
			toAdd.add(g);
		}
		if(this == GameMap.SUNSET_TERRACE) {
			@SuppressWarnings("deprecation")
			ItemStack a = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 3);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.LEAVES);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.BOOKSHELF);
			toAdd.add(c);
			ItemStack e = new ItemStack(Material.MELON);
			toAdd.add(e);
			ItemStack f = new ItemStack(Material.HARD_CLAY);
			toAdd.add(f);
		}
		if(this == GameMap.TEITAKU) {
			ItemStack a = new ItemStack(Material.WORKBENCH);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.BOOKSHELF);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.ANVIL);
			toAdd.add(c);
			ItemStack e = new ItemStack(Material.JUKEBOX);
			toAdd.add(e);
			ItemStack f = new ItemStack(Material.BEACON);
			toAdd.add(f);
			ItemStack g = new ItemStack(Material.FLOWER_POT);
			toAdd.add(g);
		}
		if(this == GameMap.SEQUOIA) {
			@SuppressWarnings("deprecation")
			ItemStack a = new ItemStack(Material.STAINED_CLAY, 1, (short) 0, (byte) 5);
			toAdd.add(a);
			@SuppressWarnings("deprecation")
			ItemStack b = new ItemStack(Material.LOG, 1, (short) 0, (byte) 3);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.LEAVES);
			toAdd.add(c);
			ItemStack e = new ItemStack(Material.BOOKSHELF);
			toAdd.add(e);
			@SuppressWarnings("deprecation")
			ItemStack f = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 2);
			toAdd.add(f);
			ItemStack g = new ItemStack(Material.SPRUCE_WOOD_STAIRS);
			toAdd.add(g);
		}
		if(this == GameMap.HOTEL_CALIFORNIA) {
			ItemStack a = new ItemStack(Material.RED_SANDSTONE);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.FLOWER_POT);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.LEAVES);
			toAdd.add(c);
			@SuppressWarnings("deprecation")
			ItemStack d = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 1);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.BEACON);
			toAdd.add(e);
			ItemStack f = new ItemStack(Material.BOOKSHELF);
			toAdd.add(f);
		}
		if(this == GameMap.LOTUS) {
			ItemStack a = new ItemStack(Material.WORKBENCH);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.BEACON);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.BOOKSHELF);
			toAdd.add(c);
			ItemStack d = new ItemStack(Material.ANVIL);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.FURNACE);
			toAdd.add(e);
		}
		if(this == GameMap.DEPARTURE) {
			ItemStack a = new ItemStack(Material.WOOD_STAIRS);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.GLOWSTONE);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.BEACON);
			toAdd.add(c);
			@SuppressWarnings("deprecation")
			ItemStack d = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 7);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.QUARTZ_BLOCK);
			toAdd.add(e);
		}
		return toAdd;
	}
	public ArrayList<ItemStack> getDisabledBlocks() {
		ArrayList<ItemStack> toAdd = new ArrayList<ItemStack>();
		if(this == GameMap.OFFICE) {
			toAdd.add(new ItemStack(Material.ICE));
		}if(this == GameMap.HEARTHSTONE_VILLAGE) {
			toAdd.add(new ItemStack(Material.ICE));
		}
		return toAdd;
	}
	public Location getSeekerStart() {
		return this.seekerlocation.getLocation();
	}
	public Location getHiderStart() {
		return this.hiderlocation.getLocation();
	}
	public static GameMap getFromSystemName(String name) {
		return lookup.get(name);
	}
}


