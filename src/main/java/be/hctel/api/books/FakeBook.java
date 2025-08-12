package be.hctel.api.books;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayInUseItem;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;

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
		PacketPlayInUseItem packet = new PacketPlayInUseItem();
		openBookInHand(player);
		player.getInventory().setItemInMainHand(itemHeld);
	}
	
	private void openBookInHand(Player player)
	{
	    ByteBuf buf = Unpooled.buffer(256);
	    buf.setByte(0, (byte)0);
	    buf.writerIndex(1);
	    PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
	    CraftPlayer craftP = (CraftPlayer)player;
	    craftP.getHandle().playerConnection.sendPacket(packet);
	}
}
