package be.hctel.renaissance.hideandseek.nongame.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.RecursiveTask;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.comphenix.packetwrapper.AbstractPacket;
import com.comphenix.packetwrapper.WrapperPlayServerBlockChange;
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntity;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.viaversion.viaversion.api.Via;

import be.hctel.renaissance.hideandseek.Hide;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockChange;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity;

/*
 * This file is a part of the Renaissance Project API
 */

public class Utils {
	/**
	 * Converts a string to an int
	 * @param input the string to convert
	 * @return the int
	 * @throws NumberFormatException if the provided String cannot be converted to an int
	 */
	public static int convertToInt(String input) throws NumberFormatException {
		return Integer.parseInt(input);
	}
	/**
	 * Gets an ItemStack from a numerical ID.
	 * @param id The numerica ID of the item
	 * @return the ItemStack
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getItemStackFromNumericalID(int id) {
		return new ItemStack(Material.getMaterial(id));
	}
	/**
	 * Gets an ItemStack from a numerical ID and a data value
	 * @param id the numerical ID of the item
	 * @param data the data value of the item
	 * @return the ItemStack
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getItemStackFromNumericalID(int id, int data) {
		return new ItemStack(Material.getMaterial(id), 1, (short) 0, (byte) data);
	}
	/**
	 * Gets an ItemStack matching a formatted name.
	 * Used mainly in the JSON storages
	 * @param formattedName the formatted name
	 * @return the ItemStack
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getItemStackFromFormattedName(String formattedName) {
		String[] parts = formattedName.split(":");
		String a = parts[0];
		String b = parts[1];
		int c = Utils.convertToInt(b);
		Material d = Material.matchMaterial(a);
		return new ItemStack(d, 1, (short) 0, (byte) c);
	}
	/**
	 * Gets the formatted name of an ItemStack
	 * @param item the ItemStack
	 * @return the formatted name
	 */
	@SuppressWarnings("deprecation")
	public static String getFormattedName(ItemStack item) {
		if (item.getData().getData() != 0) return item.getType().toString();
		else return item.getType().toString() + ":" + item.getData().getData();
	}
	/**
	 * Gets the UUID of an online player
	 * @param player the player to get the UUID of
	 * @return the UUID of the player
	 */
	public static String getUUID(Player player) {
		String u = player.getUniqueId().toString();
		String uu = u.replace("-", "");
		return uu;
	}
	/**
	 * Gets the UUID of an offline player
	 * @param player the player to get the UUID of
	 * @return the UUID of the player
	 */
	public static String getUUID(OfflinePlayer player) {
		String u = player.getUniqueId().toString();
		String uu = u.replace("-", "");
		return uu;
	}
	/**
	 * Get the user-friendly name of an ItemStack
	 * @param it the ItemStack to get the name of
	 * @return the name of te ItemStack
	 */
	public static String getUserItemName(ItemStack it) {
		Material a = it.getType();
		String aN = a.toString().toLowerCase();
		@SuppressWarnings("deprecation")
		String aN1 = StringUtils.capitalise(aN);
		@SuppressWarnings("deprecation")
		int b = it.getData().getData();
		if(a == Material.STONE) {
			 switch (b) {
			 case 0:
				 return "Stone";
			 case 1:
				 return "Granite";
			 case 2:
				 return "Polished granite";
			 case 3:
				 return "Diorite";
			 case 4:
				 return "Polished diorite";
			 case 5:
				 return "Andesite";
			 case 6:
				 return "Polished andesite";
			 }
		} else if(a == Material.STAINED_CLAY) {
			 switch (b) {
			 case 0:
				 return "White clay";
			 case 1:
				 return "Orange clay";
			 case 2:
				 return "Magenta clay";
			 case 3:
				 return "Light blue clay";
			 case 4:
				 return "Yellow clay";
			 case 5:
				 return "Lime clay";
			 case 6:
				 return "Pink clay";
			 case 7:
				 return "Gray clay";
			 case 8:
				 return "Light gay clay";
			 case 9:
				 return "Cyan clay";
			 case 10:
				 return "Purple clay";
			 case 11:
				 return "Blue clay";
			 case 12:
				 return "Brown clay";
			 case 13:
				 return "Green clay";
			 case 14:
				 return "Red clay";
			 case 15:
				 return "Black clay";	 
			 }
		} else if(a == Material.WOOD) {
			switch (b) {
			 case 0:
				 return "Oak planks";
			 case 1:
				 return "Spruce planks";
			 case 2:
				 return "Birch planks";
			 case 3:
				 return "Jungle planks";
			 case 4:
				 return "Acacia planks";
			 case 5:
				 return "Dark oak planks";
			}
		} else if(a == Material.LOG) {
			switch (b) {
			 case 0:
				 return "Oak log";
			 case 1:
				 return "Spruce log";
			 case 2:
				 return "Birch log";
			 case 3:
				 return "Jungle log";
			 case 4:
				 return "Acacia log";
			 case 5:
				 return "Dark oak log";
			}
		}
		else {
			return aN1;
		}
		return aN1;
	}
	/**
	 * Spawns a firework at the given location
	 * @param location the location where the firewxork should be spawned
	 */
	public static void spawnFireworks(Location location){
	        Location loc = location;
	        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
	        FireworkMeta fwm = fw.getFireworkMeta();
	       
	        fwm.setPower(1);
	        fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).flicker(false).build());
	        fwm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(false).build());
	       
	        fw.setFireworkMeta(fwm);
	        fw.detonate();
	}
	/**
	 * Maps a value into a new range
	 * @param x The value to map
	 * @param in_min The old minimum value
	 * @param in_max The old max value
	 * @param out_min The new minimum value
	 * @param out_max The new max value
	 * @return The mapped value
	 */
	public static long map(long x, long in_min, long in_max, long out_min, long out_max) {
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	/**
	 * Maps a value into a new range
	 * @param x The value to map
	 * @param in_min The old minimum value
	 * @param in_max The old max value
	 * @param out_min The new minimum value
	 * @param out_max The new max value
	 * @return The mapped value
	 */
	public static long map(int x, int in_min, int in_max, int out_min, int out_max) {
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	/**
	 * Maps a value into a new range
	 * @param x The value to map
	 * @param in_min The old minimum value
	 * @param in_max The old max value
	 * @param out_min The new minimum value
	 * @param out_max The new max value
	 * @return The mapped value
	 */
	public static double map(float x, float in_min, float in_max, float out_min, float out_max) {
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	private final static int CENTER_PX = 154;
	/**
	 * Sends a centered text to a player
	 * @param player The player to send the message to
	 * @param message The message to send
	 */
	public static void sendCenteredMessage(Player player, String message){
	        if(message == null || message.equals("")) player.sendMessage("");
	                message = ChatColor.translateAlternateColorCodes('&', message);
	 
	                int messagePxSize = 0;
	                boolean previousCode = false;
	                boolean isBold = false;
	 
	                for(char c : message.toCharArray()){
	                        if(c == '§'){
	                                previousCode = true;
	                                continue;
	                        }else if(previousCode == true){
	                                previousCode = false;
	                                if(c == 'l' || c == 'L'){
	                                        isBold = true;
	                                        continue;
	                                }else isBold = false;
	                        }else{
	                                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
	                                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
	                                messagePxSize++;
	                        }
	                }
	 
	                int halvedMessageSize = messagePxSize / 2;
	                int toCompensate = CENTER_PX - halvedMessageSize;
	                int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
	                int compensated = 0;
	                StringBuilder sb = new StringBuilder();
	                while(compensated < toCompensate){
	                        sb.append(" ");
	                        compensated += spaceLength;
	                }
	                player.sendMessage(sb.toString() + message);
	}
	/**
	 * Capitalizes the first letter of a string
	 * @param toCapitalize The string to capitalize
	 * @return The capitalized string.
	 */
	public static String capitalize(String toCapitalize) {
		return toCapitalize.substring(0, 1).toUpperCase() + toCapitalize.substring(1).toLowerCase();
	}
	/**
	 * Get the index of the maximum value in an {@link ArrayList}
	 * @param array The ArrayList ({@link Integer}) to get the max value key from
	 * @return The key of the max value
	 */
	public static int getMaxValuePosition(ArrayList<Integer> array) {
		int votes = 0;
		int key = 0;
		for(int i = 0; i < 6; i++) {
			if(array.get(i) > votes) {
				key = i;
				votes = array.get(i);
			}
		}
		return key;
	}
	/**
	 * Get the max assigned integer to an object in an {@link HashMap}
	 * @param map The HashMap to get the max assigned value from
	 * @return The object matching the max value
	 */
	public static Object getMaxValueKey(HashMap<Object, Integer> map) {
		int max = Integer.MIN_VALUE;
		for(Object o : map.keySet()) {
			if(map.get(o) > max) max = map.get(o);
		}
		return max;
	}
	/**
	 * Sends the TabList header and footer to a player
	 * @param player The player to send the header/footer to
	 * @param header The header
	 * @param footer The footer
	 */
	public static void sendHeaderFooter(Player player, String header, String footer) {
        IChatBaseComponent tabHeader = ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tabFooter = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try
        {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, tabHeader);
            headerField.setAccessible(false);
            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, tabFooter);
            footerField.setAccessible(false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
	/**
	 * Sends an action bar message to a player (used to clean other classes)
	 * @param player The player to send the message to
	 * @param msg The message to send
	 */
	public static void sendActionBarMessage(Player player, String msg) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
	}
	public static ItemStack createQuickItemStack(Material material, short damage, String name) {
		ItemStack toReturn = new ItemStack(material, 1, damage);
		ItemMeta meta = toReturn.getItemMeta();
		meta.setDisplayName(name);
		toReturn.setItemMeta(meta);
		return toReturn;
		
	}
	/**
	 * @deprecated This method uses an anmbiguous data type object
	 * @param player
	 * @param material
	 * @param damage
	 * @param data
	 * @param name
	 * @return
	 */
	public static ItemStack createQuickItemStack(Material material, short damage, byte data, String name) {
		ItemStack toReturn = new ItemStack(material, 1, damage, data);
		ItemMeta meta = toReturn.getItemMeta();
		meta.setDisplayName(name);
		toReturn.setItemMeta(meta);
		return toReturn;
	}
	/**
	 * Creates a delayed recursive loop
	 * @param plugin The main plugin instance
	 * @param initialParamValue The initial value (for(int i = <b>60</b>; i >...))
	 * @param maxParamValue The maximal value (for(int i = 60; i > <b>42</b>, i = -2))
	 * @param increment The increment to add every time the loop finishes(for(int i = 0; i > 42; i = -<b>2</b>))
	 * @param delay The delay to wait before repeating the task, expressed in server ticks
	 * @param task the {@link RecursiveTask} to run every time the loop is runned.
	 */
	public static void recursiveDelayed(Plugin plugin, int initialParamValue, int maxParamValue, int increment, long delay, RecursiveExecutable task) {
		
		new BukkitRunnable() {
			private int param = initialParamValue;
			private boolean isIncreasing = maxParamValue >= initialParamValue;
			@Override
			public void run() {
				if(isIncreasing) {
					if(param < maxParamValue) {
						task.run(param);
					} else cancel();
				} else {
					if(param > maxParamValue) {
						task.run(param);
					} else cancel();
				}
				param = +increment;
			}
		}.runTaskTimer(plugin, 0L, delay);
	}
	public static String getFullUUID(String formattedUUID) {
		String uuid = formattedUUID.replaceAll(                                            
			    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",                            
			    "$1-$2-$3-$4-$5");
		return uuid;
	}
	public static String randomString(int lenght) {
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = lenght;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}
	public static boolean locationComparator(Location a, Location b) {
		return (a.getBlockX() == b.getBlockX() && a.getBlockZ() == b.getBlockZ() && (a.getBlockY() - b.getBlockY()) < 4);
	}
	public static void spawnBlock(Player player, Location loc, int blockID, int data){
		WrapperPlayServerSpawnEntity fbs = new WrapperPlayServerSpawnEntity();
	 
	        fbs.setEntityID(new Random().nextInt());
	        fbs.setObjectData(blockID | (data << 0x10));
	        Location l = player.getLocation();
	        fbs.setX(l.getX());
	        fbs.setY(l.getY() + 5);
	        fbs.setZ(l.getZ());
	        fbs.setOptionalSpeedX(0.1);
	        fbs.setOptionalSpeedY(0);
	        fbs.setOptionalSpeedY(0);
	        PacketPlayOutSpawnEntity packet = (PacketPlayOutSpawnEntity) fbs.getHandle().getHandle();
	        ByteBuf b = Unpooled.buffer(1000);
			PacketDataSerializer s = new PacketDataSerializer(b);
			try {
				packet.b(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Via.getAPI().sendRawPacket(player.getUniqueId(), b);
	        
	    }
	@SuppressWarnings("deprecation")
	public static void sendBlockChange(Player player, Material block, Location location) {
		PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange();
		try {
			Field t = packet.getClass().getField("Location");
			Field l = packet.getClass().getField("Block ID");
			t.setAccessible(true);
			t.set(packet, block.getId());
			t.setAccessible(false);
			l.setAccessible(true);
			l.set(packet, locationEncoder(location));
			l.setAccessible(false);	
			ByteBuf b = Unpooled.buffer(1000);
			PacketDataSerializer s = new PacketDataSerializer(b);
			packet.b(s);
			Via.getAPI().sendRawPacket(player.getUniqueId(), b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static int locationEncoder(Location l) {
		int x = l.getBlockX();
		int y = l.getBlockY();
		int z = l.getBlockZ();
		int out = ((x & 0x3FFFFFF) << 38) | ((y & 0xFFF) << 26) | (z & 0x3FFFFFF);
		return out;		
	}
		
 
}