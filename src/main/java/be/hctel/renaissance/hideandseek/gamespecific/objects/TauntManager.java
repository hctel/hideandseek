package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.TauntType;

public class TauntManager {
	Plugin plugin;
	HashMap<Player, Integer> nextTaunt = new HashMap<Player, Integer>();
	Inventory inv;
	boolean isWarmingUp = true;
	int seconds = 330;
	public TauntManager(Plugin plugin) {
		this.plugin = plugin;
		inv = buildInventory();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				seconds--;
				if(seconds == 300) isWarmingUp = false;
			}
		}.runTaskTimer(plugin, 0L, 20L);
	}
	public void openMenu(Player player) {
		player.openInventory(inv);
	}
	public void triggerMenu(InventoryClickEvent e) {
		ItemStack clicked = e.getCurrentItem();
		if(clicked.getType().equals(Material.INK_SACK)) {
			e.getView().close();
			e.setCancelled(true);
		} else if(clicked.getType().equals(Material.STAINED_GLASS_PANE)) e.setCancelled(true);
		else {
			TauntType type = TauntType.getByInventoryName(clicked.getItemMeta().getDisplayName());
			if(type != null) {
				e.getView().close();
				if(nextTaunt.get(e.getWhoClicked()) < seconds) {
					e.getWhoClicked().sendMessage(Hide.header + "§cYour taunts are on cooldown! Please wait " + (nextTaunt.get(e.getWhoClicked()) - seconds) + " seconds");
				} else if(isWarmingUp) {
					e.getWhoClicked().sendMessage(Hide.header + "§cYou can't taunt while the game is still warming up!");
				} else {
					if(nextTaunt.containsKey((Player) e.getWhoClicked())) {
						nextTaunt.replace((Player) e.getWhoClicked(), new Taunt((Player) e.getWhoClicked(), type).perform() + seconds);
					} else {
						nextTaunt.put((Player) e.getWhoClicked(), new Taunt((Player) e.getWhoClicked(), type).perform() + seconds);
					}
				}
				e.setCancelled(true);
			}
		}
	}
	
	private Inventory buildInventory() {
		Inventory a = Bukkit.createInventory(null, 45, "Select a Taunt");
		@SuppressWarnings("deprecation")
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 2);
		ItemMeta µ = glass.getItemMeta();
		µ.setDisplayName("");
		glass.setItemMeta(µ);
		@SuppressWarnings("deprecation")
		ItemStack cancel = new ItemStack(Material.INK_SACK, 1, (short) 0, (byte) 1);
		µ = cancel.getItemMeta();
		µ.setDisplayName("§c§lClose menu");
		cancel.setItemMeta(µ);
		for(int i = 0; i < 9; i++) {
			a.setItem(i, glass);
		}
		for(int i = 36; i < 45; i++) {
			if(i == 40) a.setItem(i, cancel);
			else a.setItem(i, glass);
		}
		for(int i = 1; i < 4; i++) {
			a.setItem(9*i, glass);
			a.setItem((9*i)+8, glass);
		}
		for(int i = 10; i < 17; i++) {
			a.setItem(i, TauntType.values()[i-10].getItem());
		}
		for(int i = 19; i < 26; i++) {
			a.setItem(i, TauntType.values()[i-12].getItem());
		}
		for(int i = 30; i < 33; i++) {
			a.setItem(i, TauntType.values()[i-16].getItem());
		}
		return a;
	}
}
