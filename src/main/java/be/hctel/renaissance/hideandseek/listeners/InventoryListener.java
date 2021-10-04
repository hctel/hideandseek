package be.hctel.renaissance.hideandseek.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import be.hctel.renaissance.hideandseek.Hide;

public class InventoryListener implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getInventory().getName().equalsIgnoreCase("Choose your block!")) {
			if(Hide.preGameTimer.choosingBlock && e.getCurrentItem() != null) {
				Hide.blockPicker.listener(e);
			}
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getItem() != null) {
			if(e.getItem().getType().equals(Material.COMPASS) && e.getItem().getItemMeta().getDisplayName().equals("§b§lSelect your Block!")) {
				if(Hide.preGameTimer.choosingBlock) {
					Hide.blockPicker.reopen(e.getPlayer());
				}
			}
		}
			
	}
}
