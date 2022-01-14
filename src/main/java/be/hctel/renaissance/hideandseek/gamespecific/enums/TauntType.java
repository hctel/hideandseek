package be.hctel.renaissance.hideandseek.gamespecific.enums;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public enum TauntType {
	BARK(Material.RECORD_5, "Barking Dog", "bark bark", 1, 4),
	GHAST(Material.RECORD_5, "Screeching Ghast", "Wakes you up!", 1, 4),
	ENDERMAN(Material.RECORD_5, "Angry Enderman", "Endermen don't like bolognese!", 1, 4),
	CREEPER(Material.RECORD_5, "Hissing Creeper", "Run!", 1, 4),
	WATER(Material.RECORD_5, "Spashing Water", "Bomb!", 1, 4),
	PIG(Material.RECORD_5, "Squeaking Pig", "This is not Halal...", 1, 4),
	DRAGON(Material.RECORD_5, "Growling Ender Dragon", "Y u mean 2 me?", 2, 8),
	TNT(Material.TNT, "Explosion", "Kaboom!", 4, 15),
	EGGS(Material.EGG, "Raining Eggs", "Who wants an omelette?", 4, 15),
	FLAME(Material.BLAZE_POWDER, "Flames", "Flamegrilled since 2021", 4, 15),
	LOVE(Material.REDSTONE, "Love", "Luv is in the air", 4, 15),
	FIREWORKS(Material.FIREWORK, "Fireworks", "Ooooooh!", 8, 30),
	POTION(Material.POTION, "Potion Thrower", "That wasn't me!", 8, 30),
	SHEEPER(Material.SULPHUR, "Sheeper", "Bzzzz Bzzzz I'm a bee!", 8, 30),
	BAT(Material.COAL, "Batbomb", "Is that batman's bomb?", 8, 30),
	FLY_CREEPER(Material.SULPHUR, "Flying creeper", "I'm on the top of the world!", 8, 30),
	TNT_TOSS(Material.TNT, "TNT Toss", "TNT? I love TNT!", 8, 30);
	
	Material material;
	String name, description;
	int points;
	int cooldown;
	static HashMap<String, TauntType> lookup = new HashMap<String, TauntType>();
	static {
		for(TauntType t : TauntType.values()) {
			lookup.put(t.toString(), t);
		}
	}
	static HashMap<String, TauntType> nameLookup = new HashMap<String, TauntType>();
	static {
		for(TauntType t : TauntType.values()) {
			nameLookup.put(t.getName(), t);
		}
	}
	private TauntType(Material material, String name, String description, int points, int cooldown) {
		this.material = material;
		this.name = name;
		this.description = description;
		this.points = points;
		this.cooldown = cooldown;
	}
	public static TauntType getByName(String name) {
		name.toUpperCase();
		if(lookup.containsKey(name)) {
			return lookup.get(name);
		} else {
			return null;
		}
	}
	public static TauntType getByInventoryName(String name) {
		if(nameLookup.containsKey(name)) {
			return nameLookup.get(name);
		} else {
			return null;
		}
	}
	public String getName() {
		return  "§e§l" + name;
	}
	public String getDescription() {
		return description;
	}
	public Material getMaterial() {
		return material;
	}
	public int getPoints() {
		return points;
	}
	public int getCooldown() {
		return cooldown;
	}
	public ItemStack getItem() {
		ItemStack a = new ItemStack(material);
		ItemMeta b = a.getItemMeta();
		b.setDisplayName("§e§l" + getName());
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(description);
		lore.add("");
		lore.add("§d" + points + " Points/Experience");
		lore.add("");
		lore.add("§b -> Click to perfor " + name);
		b.setLore(lore);
		a.setItemMeta(b);
		return a;
	}
}
