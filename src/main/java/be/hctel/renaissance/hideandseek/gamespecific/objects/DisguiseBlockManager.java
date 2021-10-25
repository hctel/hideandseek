package be.hctel.renaissance.hideandseek.gamespecific.objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class DisguiseBlockManager {
	Player player;
	ItemStack block;
	Plugin plugin;
	public DisguiseBlockManager(Player player, ItemStack block, Plugin plugin) {
		this.player = player;
		this.block = block;
		this.plugin = plugin;
	}
}
