package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameMap;
import be.hctel.renaissance.hideandseek.nongame.sql.Stats;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class BlockPicker {
	GameMap map;
	Stats stats;
	HashMap<Player, ItemStack> playerBlock = new HashMap<Player, ItemStack>();
	HashMap<Player, Inventory> playerBlockSelector = new HashMap<Player, Inventory>();
	HashMap<Player, Material> playerItem = new HashMap<Player, Material>();
	private Plugin plugin;
	
	public BlockPicker(GameMap map, Stats stats, Plugin plugin) {
		this.map = map;
		this.stats = stats;
		this.plugin = plugin;
	}
	
	public void buildBlockSelector(Player player) {
		Random r = new Random();
		ArrayList<ItemStack> def = map.getDefaultBlocks();
		ArrayList<ItemStack> cus = new ArrayList<ItemStack>();
		for(Material M : stats.getUnlockedBlocks(player)) cus.add(new ItemStack(M));
		ArrayList<ItemStack> dis = map.getDisabledBlocks();
		for(int o = 0; o < cus.size(); o++) {
			ItemStack i = cus.get(o);
			if(dis.contains(i)) cus.remove(i);
		}
		int is = def.size()+cus.size()+9;
		int iv = 54;
		if(is <= 54 && is > 45) {
			iv = 54;
		} else if(is <= 45 && is > 36) {
			iv = 54;
		} else if(is <= 36 && is > 27) {
			iv = 45;
		} else if(is <= 27 && is > 18) {
			iv = 36;
		} else if(is <= 18 && is > 9) {
			iv = 27;
		} else if(is <= 9) {
			iv = 18;
		}
		Inventory inv = Bukkit.createInventory(null, iv, "Choose your block!");
		for(int i = 0; i < def.size(); i++) {
			ItemStack a = def.get(i);
			ItemStack aA = def.get(i);
			if(a.getType().equals(Material.FLOWER_POT)) {
				aA = new ItemStack(Material.FLOWER_POT);
				a = new ItemStack(Material.FLOWER_POT_ITEM);
			}
			if (a.getType().equals(Material.CAKE)) {
				aA = new ItemStack(Material.CAKE_BLOCK);
				a = new ItemStack(Material.CAKE);
			}
			if(a.getType().equals(Material.CAULDRON)) {
				aA = new ItemStack(Material.CAULDRON);
				a = new ItemStack(Material.CAULDRON_ITEM);
			}
			ItemMeta b = a.getItemMeta();
			b.setDisplayName("�e�l" + Utils.getUserItemName(aA));
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("�7Will this " + Utils.getUserItemName(aA) + " block");
			lore.add("�7be a good choice?");
			lore.add("");
			lore.add(Hide.stats.getSmallLevelProgressBar(player, a));
			lore.add("");
			b.setLore(lore);
			a.setItemMeta(b);
			inv.setItem(i, a);
		}
		for(int i = 9; i < 18; i++) {
			@SuppressWarnings("deprecation")
			ItemStack a = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 0, (byte) 7);
			ItemMeta b = a.getItemMeta();
			b.setDisplayName("");
			a.setItemMeta(b);
			inv.setItem(i, a);
		}
		for(int i = 0; i < cus.size(); i++) {
			ItemStack a = cus.get(i);
			ItemMeta b = a.getItemMeta();
			b.setDisplayName("�e�l" + Utils.getUserItemName(a));
			b.addEnchant(Enchantment.KNOCKBACK, 1, false);
			b.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("");
			lore.add("�7Will this " + Utils.getUserItemName(a) + " block");
			lore.add("�7be a good choice?");
			lore.add("");
			lore.add(Hide.stats.getSmallLevelProgressBar(player, a));
			lore.add("");
			b.setLore(lore);
			a.setItemMeta(b);
			inv.setItem(i+18, a);
		}
		playerBlockSelector.put(player, inv);
		ArrayList<ItemStack> all = def;
		for(ItemStack it : cus) {
			all.add(it);
		}
		int picked = r.nextInt(all.size());
		if(all.get(picked).getType().equals(Material.FLOWER_POT_ITEM)) {
			playerBlock.put(player, new ItemStack(Material.FLOWER_POT));
			playerItem.put(player, Material.FLOWER_POT_ITEM);
		} 
		else if(all.get(picked).getType().equals(Material.CAULDRON_ITEM)) {
			playerBlock.put(player, new ItemStack(Material.CAULDRON));
			playerItem.put(player, Material.CAULDRON_ITEM);
		} else if(all.get(picked).getType() == Material.CAKE) {
			playerBlock.put(player, new ItemStack(Material.CAKE_BLOCK));
			playerItem.put(player, Material.CAKE);
		}
		else {
			playerBlock.put(player, all.get(picked));
			playerItem.put(player, all.get(picked).getType());
		}
		player.openInventory(inv);
	}
	
	public void reopen(Player player) {
		if(playerBlockSelector.containsKey(player)) {
			player.openInventory(playerBlockSelector.get(player));
		}
	}
	
	public void listener(InventoryClickEvent e) {
		if(e.getInventory().getName().equalsIgnoreCase("Choose your block!")) {
			Player p = (Player) e.getWhoClicked();
			ItemStack picked = e.getCurrentItem();
			if(picked.getType() == Material.STAINED_GLASS_PANE) {
				e.setCancelled(true);
				return;
			}
			playerItem.put(p,picked.getType());
			if(picked.getType() == Material.FLOWER_POT_ITEM) picked = new ItemStack(Material.FLOWER_POT);
			if(picked.getType() == Material.CAKE) picked = new ItemStack(Material.CAKE_BLOCK);
			if(picked.getType() == Material.CAULDRON_ITEM) picked = new ItemStack(Material.CAULDRON);
			p.closeInventory();
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.99f);
			p.sendMessage(Hide.header + "�aGood choice! �eSet your block to " + Utils.getUserItemName(picked));
			playerBlock.put(p, picked);
			plugin.getLogger().info(String.format("%s has picked a block! (%s)", p.getName(), picked.getType().toString()));
			e.setCancelled(true);
		}
	}
}
