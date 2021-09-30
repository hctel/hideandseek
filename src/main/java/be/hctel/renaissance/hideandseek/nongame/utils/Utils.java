package be.hctel.renaissance.hideandseek.nongame.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;

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
	public static Object getMaxValueKey(HashMap<Object, Integer> map) {
		int max = Integer.MIN_VALUE;
		Object maxKey;
		for(Object o : map.keySet()) {
			if(map.get(o) > max) max = map.get(o);
		}
		return max;
	}
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
	
	public static void sendActionBarMessage(Player player, String msg) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
	}
}
