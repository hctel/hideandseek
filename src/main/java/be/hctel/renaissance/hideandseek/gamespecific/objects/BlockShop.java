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
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class BlockShop {
	Plugin plugin;
	public final static Material[] availableBlocks = {Material.OBSIDIAN, Material.QUARTZ_ORE, Material.REDSTONE_BLOCK, Material.REDSTONE_ORE, Material.SOUL_SAND, Material.JACK_O_LANTERN, Material.JUKEBOX, Material.LAPIS_BLOCK, Material.ICE, Material.BOOKSHELF, Material.MELON_BLOCK, Material.PUMPKIN, Material.REDSTONE_LAMP_OFF, Material.LEAVES, Material.ENDER_PORTAL_FRAME, Material.ENCHANTMENT_TABLE, Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.EMERALD_BLOCK, Material.TNT};
	HashMap<OfflinePlayer, ArrayList<Material>> blocks = new HashMap<OfflinePlayer, ArrayList<Material>>();
	public BlockShop(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void openInventory(Player player) {
		if(!blocks.containsKey(player)) blocks.put(player, Hide.stats.getUnlockedBlocks(player));
		Inventory inv = Bukkit.createInventory(null, 54, "Block shop!");
		ArrayList<String> lore = new ArrayList<String>();
		for(Material M : availableBlocks) {
			lore.clear();
			lore.add("");
			lore.add("§7Right-click to buy this " + Utils.getUserItemName(M) + ".");
			lore.add("");
			lore.add("§aPrice§r: " + getItemPrice(M));
			lore.add("");
			if(blocks.get(player).contains(M)) {
				lore.add("§a§lYou already own this item!");
				inv.setItem(getItemPosition(M), getSimpflifiedItem(M, true, lore));
			} else {
				inv.setItem(getItemPosition(M), getSimpflifiedItem(M, false, lore));
			}
		}
		player.openInventory(inv);
	}
	
	public void eventHandler(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
			if(blocks.get(p).contains(e.getCurrentItem().getType())) {
				p.sendMessage(Hide.header + "§cYou can't buy this block as you already own it.");
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1.0f, 0.5f);
				e.setCancelled(true);
				return;
			} else {
				if(Hide.cosmeticManager.getTokens(p) >= getItemPrice(e.getCurrentItem().getType())) {
					p.sendMessage(Hide.header + "§aYou bought the " + Utils.getUserItemName(e.getCurrentItem()) + " block. Enjoy it in your next game!");
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.975f);
					Hide.cosmeticManager.addTokens(p, -getItemPrice(e.getCurrentItem().getType()));
					Hide.stats.unlockBlock(p, e.getCurrentItem());
					e.setCancelled(true);
					blocks.get(p).add(e.getCurrentItem().getType());
					Material M = e.getCurrentItem().getType();
					ArrayList<String> lore = new ArrayList<String>();
					lore.clear();
					lore.add("");
					lore.add("§7Right-click to buy this " + Utils.getUserItemName(M) + ".");
					lore.add("");
					lore.add("§aPrice§r: " + getItemPrice(M));
					lore.add("");
					if(blocks.get(p).contains(M)) {
						lore.add("§a§lYou already own this item!");
						e.getInventory().setItem(getItemPosition(M), getSimpflifiedItem(M, true, lore));
					} else {
						e.getInventory().setItem(getItemPosition(M), getSimpflifiedItem(M, false, lore));
					}
					
				} else {
					p.sendMessage(Hide.header + "§cYou can't buy this block as you don't have enough tokens. Play more games to earn some!");
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BASS, 1.0f, 0.5f);
					e.setCancelled(true);
					return;
				}
			}
	}
	
	private ItemStack getSimpflifiedItem(Material material, boolean isEnchanted, ArrayList<String> lore) {
		ItemStack r = new ItemStack(material);
		ItemMeta m = r.getItemMeta();
		if(isEnchanted) m.addEnchant(Enchantment.KNOCKBACK, 1, false);
		m.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		m.setLore(lore);
		r.setItemMeta(m);
		return r;
	}
	
	private int getItemPrice(Material material) {
		switch(material) {
		case ENDER_PORTAL:
			return 4000;
		case ENCHANTMENT_TABLE:
			return 4000;
		case IRON_BLOCK:
			return 4000;
		case EMERALD_BLOCK:
			return 6000;
		case DIAMOND_BLOCK:
			return 6000;
		case TNT:
			return 8000;
		default: 
			return 2000;		
		}
	}
	
	private int getItemPosition(Material material) {
		switch(material) {
		case OBSIDIAN:
			return 11;
		case QUARTZ_ORE:
			return 12;
		case REDSTONE_BLOCK:
			return 13;
		case REDSTONE_ORE:
			return 14;
		case SOUL_SAND:
			return 15;
		case JACK_O_LANTERN:
			return 19;
		case JUKEBOX:
			return 20;
		case LAPIS_BLOCK:
			return 21;
		case ICE:
			return 22;
		case BOOKSHELF:
			return 23;
		case MELON_BLOCK:
			return 24;
		case PUMPKIN:
			return 25;
		case REDSTONE_LAMP_OFF:
			return 28;
		case LEAVES:
			return 29;
		case ENDER_PORTAL_FRAME:
			return 30;
		case ENCHANTMENT_TABLE:
			return 31;
		case IRON_BLOCK:
			return 32;
		case DIAMOND_BLOCK:
			return 33;
		case EMERALD_BLOCK:
			return 34;
		case TNT:
			return 40;
		default:
			return 0;
		}
	}
}
