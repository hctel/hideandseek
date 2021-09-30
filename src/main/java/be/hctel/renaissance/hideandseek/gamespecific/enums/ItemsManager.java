package be.hctel.renaissance.hideandseek.gamespecific.enums;

import org.bukkit.Material;
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
}
