package be.hctel.api.books;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;


public class FakeBook {
	private ItemStack book;
	private BookMeta meta;
	public FakeBook(List<String> strings) {
		book = new ItemStack(Material.WRITTEN_BOOK);
		meta = (BookMeta) book.getItemMeta();
		if(strings.size() < 51) {
			for(String S : strings) {
				if(S.length() < 257) {
					meta.setPages(strings);
					book.setItemMeta(meta);
				}
			}
		} else throw new IllegalArgumentException("Pages too long: 50 pages max and 256 chars max per page.");
	}
	
	public FakeBook(String name, String...strings) {
		book = new ItemStack(Material.WRITTEN_BOOK);
		meta = (BookMeta) book.getItemMeta();
		if(strings.length < 51) {
			for(String S : strings) {
				if(S.length() < 257) {
					meta.setPages(strings);
					meta.setDisplayName(name);
					book.setItemMeta(meta);
				}
			}
		} else throw new IllegalArgumentException("Pages too long: 50 pages max and 256 chars max per page.");
	}
	
	public ItemStack getItemStack() {
		return book;
	}
	
	public void open(Player player) {
		ItemStack itemHeld = player.getInventory().getItemInMainHand();
		player.getInventory().setItemInMainHand(book);
		openBookInHand(player);
		player.getInventory().setItemInMainHand(itemHeld);
	}
	
	private void openBookInHand(Player player)
	{
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
	    PacketContainer openBook = manager.createPacket(PacketType.Play.Server.OPEN_BOOK);
	    openBook.getIntegers().write(0, 0);
	    manager.sendServerPacket(player, openBook);
	}
}
