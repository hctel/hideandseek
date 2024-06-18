package be.hctel.renaissance.hideandseek.gamespecific.enums;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import be.hctel.renaissance.hideandseek.gamespecific.objects.BlockShop;
import be.hctel.renaissance.hideandseek.nongame.utils.SpawnLocation;

public enum GameMap {
	
	OFFICE("CS_Office", "HIDE_Office", new SpawnLocation("HIDE_Office", -3.5, 4.5, 0.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Office", 22.5, 13.5, -9.5, 0.1f, 0.1f)),
	TALAVERA("Talavera", "HIDE_Talavera", new SpawnLocation("HIDE_Talavera", -3.5, 6.5, -11.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Talavera", 19.5, 18.5, -31.5, 90.1f, 0.1f)),
	GOLDRUSH("Goldrush", "HIDE_Goldrush", new SpawnLocation("HIDE_Goldrush", 2.5, 47.5, -12.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Goldrush", 21.5, 75.5, -11.5, 90.1f, 0.1f)),
	VENICE("Venice Bridge", "HIDE_Venice", new SpawnLocation("HIDE_Venice", -8.0, 65, 6.0, 0.1f, 0.1f), new SpawnLocation("HIDE_Venice", 43.5, 74, -46.5, 0.1f, 0.1f)),
	INDUSTRIA("Industria", "HIDE_Industria", new SpawnLocation("HIDE_Industria", -33.5, 4,27.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Industria", -8.5, 16, -23.5, 0.1f, 0.1f)),
	SUNSET_TERRACE("Sunset Terrace", "HIDE_Sunset", new SpawnLocation("HIDE_Sunset", 68.5, 25, 9.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Sunset", 105.5, 44, 16.5, 90.1f, 0.1f)),
	TEITAKU("Teitaku", "HIDE_Teitaku", new SpawnLocation("HIDE_Teitaku", -79.5, 24, 48.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Teitaku", -160.5, 12, 447.5, 90.1f, 0.1f)),
	SEQUOIA("Sequoia", "HIDE_Sequoia", new SpawnLocation("HIDE_Sequoia", -79.5, 24, 48.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Sequoia", -91.5, 38, -2.5, -90.1f, 0.1f)),
	HOTEL_CALIFORNIA("Hotel California", "HIDE_HotelCalifornia",new SpawnLocation("HIDE_HotelCalifornia", 18.5, 25, 0.5, 0.1f, 0.1f), new SpawnLocation("HIDE_HotelCalifornia", 18.5, 25, 6.5, 180.1f, 0.1f)),
	LOTUS("Lotus", "HIDE_Lotus", new SpawnLocation("HIDE_Lotus", -9.5, 126, 0.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Lotus", 12.5, 113, 0.5, 90.1f, 0.1f)),
	HUMBUG("Humbug St.", "HIDE_Humbug", new SpawnLocation("HIDE_Humbug", 29.5, 6.5, 52.5, 135.1f, 0.1f), new SpawnLocation("HIDE_Humbug", 29.5, 20, 98.5, 180.1f, 0.0f)),
	HEARTHSTONE_VILLAGE("Hearthstone Village", "HIDE_Hearthstone", new SpawnLocation("HIDE_Hearthstone", -9.5, 16.0, 6.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Hearthstone", -40.5, 32.0, 11.5, 0.1f, 0.1f)),
	NEXUS_CITY("Nexus City", "HIDE_Nexus", new SpawnLocation("HIDE_Nexus", -15.5, 27.5, -12.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Nexus", -1.5, 34.5, 29.5, 180.1f, 0.1f)),
	DEPARTURE("Departure", "HIDE_Departure", new SpawnLocation("HIDE_Departure", 33.5, 36.2, 26.5, 90.1f, 0.1f), new SpawnLocation("HIDE_Departure", 14.5, 31.2, 49.5, -90.1f, 0.1f)),
	PINEAPPLE("Pineapple Port", "HIDE_Pineapple", new SpawnLocation("HIDE_Pineapple", 7.5, 12.0, 2.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Pineapple", 55.5, 13, -7.5, 0.1f, 0.1f)),
	KINGSTON("Kingston", "HIDE_Kingston", new SpawnLocation("HIDE_Kingston", 45.5, 4, 53.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Kingston", 66.5, 13, 26.5, 0.1f, 0.1f)),
	PRIP("Pripyat", "HIDE_Pripyat", new SpawnLocation("HIDE_Pripyat", 30.0, 65, -39, 0.1f, 0.1f), new SpawnLocation("HIDE_Pripyat", 30.5, 96, -27.5, 180.1f, 0.1f)),
	LIGHTHOUSE("Lighthouse", "HIDE_Lighthouse", new SpawnLocation("HIDE_Lighthouse", 385.5, 59, 514.5, -90.1f, 0.1f), new SpawnLocation("HIDE_Lighthouse", 372.5, 115, 496.5, 0.1f, 0.1f)),
	ARCH("Archaeology", "HIDE_Arch", new SpawnLocation("HIDE_Arch", -2.5, 4, 12.5, 90.1f, 0.1f), new SpawnLocation("HIDE_Arch", -32.5, 46, 8.5, 180.1f, 0.1f)),
	FROZEN("Frozen", "HIDE_Frozen", new SpawnLocation("HIDE_Frozen", 50.5, 66, 6.5, -90.1f, 0.1f), new SpawnLocation("HIDE_Frozen", 52.5, 92, 57.5, 180.1f, 0.1f)),
	SPACE("Space", "HIDE_Space", new SpawnLocation("HIDE_Space", -21.5, 60, -1.5, 0.1f, 0.1f),new SpawnLocation("HIDE_Space", -27.5, 85, 29.5, 0.1f, 0.1f)),
	SHIPYARD("Shipyard", "HIDE_Shipyard", new SpawnLocation("HIDE_Shipyard", 104.5, 72, 77.5, 0.1f, 0.1f), new SpawnLocation("HIDE_Shipyard", 89.5, 98, 36.5, 0.1f, 0.1f)),
	CRUISE("Cruise", "HIDE_Cruise", new SpawnLocation("HIDE_Cruise", 50.5, 81, -60.5, 180.1f, 0.1f), new SpawnLocation("HIDE_Cruise", 49.5, 69, -96.5, 180.1f, 0.1f)),
	BEFORESPACE("Before Space", "HIDE_BeforeSpace", new SpawnLocation("HIDE_BeforeSpace", -167.5, 70, -205.5, 0.1f, 0.1f), new SpawnLocation("HIDE_BeforeSpace", -167.5, 60, -209.5, 180.1f, 0.1f)),
	ANIMAL("Animal Village", "HIDE_Animal", new SpawnLocation("HIDE_Animal", -75.5, 87, -67.5, 180.1f, 0.1f), new SpawnLocation("HIDE_Animal", -65.5, 87, -123.5, 0.1f, 0.1f)),	
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
		if(this == GameMap.PINEAPPLE) {
			ItemStack a = new ItemStack(Material.CAULDRON);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.ANVIL);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.BOOKSHELF);
			toAdd.add(c);
			ItemStack d = new ItemStack(Material.BRICK);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.WORKBENCH);
			toAdd.add(e);
		}
		if(this == GameMap.KINGSTON) {
			ItemStack a = new ItemStack(Material.CAKE);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.ANVIL);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.LEAVES);
			toAdd.add(c);
			ItemStack d = new ItemStack(Material.BOOKSHELF);
			toAdd.add(d);
			ItemStack e = new ItemStack(Material.BEACON);
			toAdd.add(e);
			@SuppressWarnings("deprecation")
			ItemStack f = new ItemStack(Material.PRISMARINE, 1, (short) 0, (byte) 2);
			toAdd.add(f);
			@SuppressWarnings("deprecation")
			ItemStack g = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 1);
			toAdd.add(g);
		}
		if(this == GameMap.PRIP) {
			ItemStack a = new ItemStack(Material.BOOKSHELF);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.LEAVES);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.BOOKSHELF);
			toAdd.add(c);
			ItemStack d = new ItemStack(Material.DARK_OAK_STAIRS);
			toAdd.add(d);
		}
		if(this == GameMap.LIGHTHOUSE) {
			ItemStack a = new ItemStack(Material.HAY_BLOCK);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.COAL_BLOCK);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.LEAVES);
			toAdd.add(c);
			@SuppressWarnings("deprecation")
			ItemStack d = new ItemStack(Material.WOOD, 1, (short) 0, (byte) 5);
			toAdd.add(d);
		}
		if(this == GameMap.ARCH) {
			ItemStack a = new ItemStack(Material.WORKBENCH);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.QUARTZ_ORE);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.JUNGLE_WOOD_STAIRS);
			toAdd.add(c);
			ItemStack d = new ItemStack(Material.REDSTONE_BLOCK);
			toAdd.add(d);
			@SuppressWarnings("deprecation")
			ItemStack f = new ItemStack(Material.STONE, 1, (short) 0, (byte) 6);
			toAdd.add(f);
			@SuppressWarnings("deprecation")
			ItemStack g = new ItemStack(Material.WOOL, 1, (short) 0, (byte) 15);
			toAdd.add(g);
		}
		if(this == GameMap.FROZEN) {
			ItemStack a = new ItemStack(Material.COAL_BLOCK);
			toAdd.add(a);
			ItemStack b = new ItemStack(Material.BOOKSHELF);
			toAdd.add(b);
			ItemStack c = new ItemStack(Material.FLOWER_POT);
			toAdd.add(c);
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
		if(this == GameMap.FROZEN) {
			toAdd.add(new ItemStack(Material.ICE));
		}
		return toAdd;
	}
	public ArrayList<Material> getAllBlocksAvailable() {
		ArrayList<Material> toAdd = new ArrayList<Material>();
		for(ItemStack I : this.getDefaultBlocks()) {
			toAdd.add(I.getType());
		}
		for(Material M : BlockShop.availableBlocks) {
			for(ItemStack I : this.getDisabledBlocks()) {
				if (!(I.getType() == M)) toAdd.add(M);
			}
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


