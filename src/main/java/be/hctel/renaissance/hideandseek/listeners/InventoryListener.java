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
				if(e.getCurrentItem().getType() != Material.AIR) Hide.blockPicker.listener(e);
			}
		}
		else if(e.getInventory().getName().equalsIgnoreCase("Select a Taunt")) {
			if(Hide.preGameTimer.gameStarted && e.getCurrentItem() != null) {
				if(e.getCurrentItem().getType() != Material.AIR) Hide.gameEngine.getTauntManager().triggerMenu(e);
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
			if(e.getItem().getType().equals(Material.EMERALD) && e.getItem().getItemMeta().getDisplayName().equals("§a§lTaunt Menu")) {
				if(Hide.preGameTimer.gameStarted) {
					Hide.gameEngine.getTauntManager().openMenu(e.getPlayer());
				}
			}
		}
			
	}
}
