package be.hctel.renaissance.hideandseek.listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import be.hctel.api.books.FakeBook;
import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameMap;

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
		else if(e.getInventory().getName().equalsIgnoreCase("Block Shop!")) {
			if(!Hide.preGameTimer.choosingBlock && !Hide.preGameTimer.gameStarted && e.getCurrentItem() != null) {
				if(e.getCurrentItem().getType() != Material.AIR) Hide.blockShop.eventHandler(e);
			}
		}
		else if(e.getInventory().getName().equalsIgnoreCase("Join messages menu")) {
			if(!Hide.preGameTimer.choosingBlock && !Hide.preGameTimer.gameStarted && e.getCurrentItem() != null) {
				if(e.getCurrentItem().getType() != Material.AIR) Hide.joinMessageMenu.eventHandler(e);
			}
		}
		else if(e.getInventory().getName().equalsIgnoreCase("Vote for an Option")) {
			e.setCancelled(true);
			Hide.votesHandler.registerPlayerVote((Player) e.getWhoClicked(), (int) (e.getSlot()/9)+1, Hide.rankManager.getRank((Player) e.getWhoClicked()).getVotes());
			Hide.votesHandler.refreshVotesInventory((Player) e.getWhoClicked());
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if(e.getInventory().getName().equalsIgnoreCase("Vote for an Option")) {
			Hide.votesHandler.closeVotesInventory((Player) e.getPlayer());
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
			if(e.getItem().getType().equals(Material.DIAMOND) && e.getItem().getItemMeta().getDisplayName().equals("§6§lView Vote Menu")) {
				Hide.votesHandler.openVotesInventory(e.getPlayer());
			}
			if(e.getItem().getType().equals(Material.REDSTONE_COMPARATOR) && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§b§lJoin messages") && !Hide.preGameTimer.gameStarted) {
				Hide.joinMessageMenu.openInventory(e.getPlayer());
			}
			if(e.getItem().getType().equals(Material.SLIME_BALL) && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§c§lReturn to Hub")) {
				Hide.bm.sendToServer(e.getPlayer(), "HUB01");
			}
			if(e.getItem().getType().equals(Material.BOOK) && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§r§lSeeker kill records")) {
				Player p = e.getPlayer();
				int counter = 1;
				String currentPage = "§3Your map kills\n";
				ArrayList<String> pages = new ArrayList<>();
				for(GameMap M : GameMap.values()) {
					if(counter == 14) {
						counter = 0;
						pages.add(currentPage);
						currentPage = "";
					}
					currentPage += String.format("§1%s: §2%d\n", M.getMapName(), Hide.stats.getKilledOnMap(p, M));
					counter++;
				}
				pages.add(currentPage);
				new FakeBook(pages).open(p);
			}
		}			
	}
}
