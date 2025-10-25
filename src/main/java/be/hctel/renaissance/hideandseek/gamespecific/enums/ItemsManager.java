package be.hctel.renaissance.hideandseek.gamespecific.enums;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemsManager {
	public static ItemStack blockSelector() {
		ItemStack a = new ItemStack(Material.COMPASS);
		ItemMeta b = a.getItemMeta();
		b.setDisplayName("§b§lSelect your Block!");
		a.setItemMeta(b);
		return a;
	}
	public static ItemStack hidersSword() {
		ItemStack a = new ItemStack(Material.WOODEN_SWORD);
		ItemMeta b = a.getItemMeta();
		b.setUnbreakable(true);
		b.setDisplayName("§c§lSeeker Killer");
		b.addEnchant(Enchantment.KNOCKBACK, 1, true);
		a.setItemMeta(b);
		return a;
	}
	public static ItemStack tauntButton = new ItemStack(Material.EMERALD);
	static {
		ItemMeta b = tauntButton.getItemMeta();
		b.setDisplayName("§a§lTaunt Menu");
		tauntButton.setItemMeta(b);
	}
}
