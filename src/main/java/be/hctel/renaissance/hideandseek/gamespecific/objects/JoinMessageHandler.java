package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.JoinMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class JoinMessageHandler {
	Plugin plugin;
	HashMap<OfflinePlayer, ArrayList<JoinMessages>> owned = new HashMap<OfflinePlayer, ArrayList<JoinMessages>>();
	public JoinMessageHandler(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void openInventory(Player player) {
		owned.put(player, Hide.stats.getUnlockedJoinMessages(player));
		Inventory inv = Bukkit.createInventory(null, 9, "Join messages menu");
		ArrayList<String> lore = new ArrayList<String>();
		for(JoinMessages M : JoinMessages.values()) {
			lore.clear();
			lore.add("");
			lore.add(Hide.rankManager.getRankColor(player) + player.getName() + M.getMessage());
			lore.add("");
			if(owned.get(player).contains(M)) {
				lore.add("§a§lYou already own this item.");
				lore.add("§eClick to select");
				if(Hide.stats.getJoinMessageIndex(player) == M.getStorageCode()) {
					inv.addItem(getSimpflifiedItem(Material.EMERALD, true, "§b" + M.getMenuName() + " §a§l(SELECTED)", lore));
				} else {
					inv.addItem(getSimpflifiedItem(Material.EMERALD, false, "§b" + M.getMenuName(), lore));
				}
			} else {
				lore.add("§7Click to buy this join message!");
				lore.add("");
				lore.add("§bPrice: §a" + "§b" + M.getPrice() + " Tokens");
				inv.addItem(getSimpflifiedItem(Material.COAL, false, M.getMenuName(), lore));
			}
		}
		player.openInventory(inv);
	}
	
	public void eventHandler(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		if(item.getEnchantments().isEmpty()) {
			JoinMessages m = JoinMessages.getFromStorageCode(e.getRawSlot());
			if(item.getType() == Material.COAL) {
				if(m.getPrice() > Hide.cosmeticManager.getTokens(p)) {
					p.sendMessage(Hide.header + "§cYou don't have enough tokens to buy this join message.");
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1.0f, 0.5f);
					e.setCancelled(true);
					return;
				} else {
					owned.get(p).add(m);
					Hide.stats.unlockJoinMessage(p, m);
					Hide.cosmeticManager.addTokens(p, -m.getPrice());
					p.sendMessage(Hide.header + "§aYou bought the " + m.getMenuName() + " join message!");
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.975f);
					e.setCancelled(true);
					p.closeInventory();
					return;
				}
			} else if(item.getType() == Material.EMERALD) {
				Hide.stats.setJoinMessage(p, m);
				p.sendMessage(Hide.header + "§aSet your join message to " + Hide.rankManager.getRankColor(p) + p.getName() + m.getMessage() + "§a!");
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.975f);
				e.setCancelled(true);
				p.closeInventory();
				return;
			}
		} else {
			p.sendMessage(Hide.header + "§cYou can't select this join message as it's already selected.");
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1.0f, 0.5f);
			e.setCancelled(true);
			return;
		}
	}
	
	private ItemStack getSimpflifiedItem(Material material, boolean isEnchanted, String name, ArrayList<String> lore) {
		ItemStack r = new ItemStack(material);
		ItemMeta m = r.getItemMeta();
		if(isEnchanted) m.addEnchant(Enchantment.KNOCKBACK, 1, false);
		m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		m.setDisplayName(name);
		m.setLore(lore);
		r.setItemMeta(m);
		return r;
	}
}
