package be.hctel.renaissance.hideandseek.nongame.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.RecursiveTask;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftFallingBlock;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRemoveEvent.Cause;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import be.hctel.api.protocol.NMSBridge;
import be.hctel.api.protocol.adapters.AdapterPacketPlayOutSpawnEntity;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.world.entity.item.EntityFallingBlock;

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
	 * Gets the formatted name of an ItemStack
	 * @param item the ItemStack
	 * @return the formatted name
	 */
	public static String getFormattedName(ItemStack item) {
		return item.getType().toString();
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
		return getUserItemName(it.getType());
	}
	/**
	 * Get the user-friendly name of a material
	 * @param a the Material to get the name of
	 * @return the name of te ItemStack
	 */
	public static String getUserItemName(Material a) {
		return StringUtils.capitalize(a.toString().replace('_', ' ').toLowerCase());
	}
	/**
	 * Spawns a firework at the given location
	 * @param location the location where the firewxork should be spawned
	 */
	public static void spawnFireworks(Location location){
        Firework fw = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(1);
        fwm.addEffects(FireworkEffect.builder().withColor(Color.RED).withFade(Color.ORANGE).flicker(true).build());
        fw.setFireworkMeta(fwm);
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
	 * Sends the TabList header and footer to a player
	 * @param player The player to send the header/footer to
	 * @param header The header
	 * @param footer The footer
	 */
	public static void sendHeaderFooter(Player player, String header, String footer) {
		IChatBaseComponent tabHeader = (IChatBaseComponent) WrappedChatComponent.fromLegacyText(header).getHandle();
        IChatBaseComponent tabFooter = (IChatBaseComponent) WrappedChatComponent.fromLegacyText(footer).getHandle();
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabHeader, tabFooter);
        ((CraftPlayer) player).getHandle().g.sendPacket(packet);
    }
	/**
	 * Sends an action bar message to a player (used to clean other classes)
	 * @param player The player to send the message to
	 * @param msg The message to send
	 */
	public static void sendActionBarMessage(Player player, String msg) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacy(msg));
	}
	/**
	 * A quick way to create an ItemStack and make the code cleaner
	 * @param material the material of the ItemStack
	 * @param damage the durability value of the ItemStack
	 * @param name the name of the ItemStack
	 * @return the generated ItemStack
	 */
	public static ItemStack createQuickItemStack(Material material, String name) {
		ItemStack toReturn = new ItemStack(material);
		ItemMeta meta = toReturn.getItemMeta();
		meta.setDisplayName(name);
		toReturn.setItemMeta(meta);
		return toReturn;		
	}
	public static ItemStack createQuickItemStack(Material material, short damage, String name, String...lore) {
		return createQuickItemStack(material, damage, false, name, lore);
		
	}
	public static ItemStack createQuickItemStack(Material material, short damage, boolean enchanted, String name, String...lore) {
		ItemStack toReturn = new ItemStack(material);
		ItemMeta meta = toReturn.getItemMeta();
		meta.setDisplayName(name);
		if(enchanted) {
			meta.addEnchant(Enchantment.BANE_OF_ARTHROPODS, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		List<String> loreList = new ArrayList<>();
		for(String S : lore) loreList.add(S);
		meta.setLore(loreList);
		toReturn.setItemMeta(meta);
		return toReturn;
	}
	
	public static ItemStack createQuickItemStack(Material material, boolean enchanted, String name, String...lore) {
		ItemStack toReturn = new ItemStack(material);
		ItemMeta meta = toReturn.getItemMeta();
		meta.setDisplayName(name);
		if(enchanted) {
			meta.addEnchant(Enchantment.BANE_OF_ARTHROPODS, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		List<String> loreList = new ArrayList<>();
		for(String S : lore) loreList.add(S);
		meta.setLore(loreList);
		toReturn.setItemMeta(meta);
		return toReturn;
	  }
	/**
	 * Creates a delayed recursive loop
	 * @param plugin The main plugin instance
	 * @param initialParamValue The initial value (for(int i = <b><u>60</u></b>; i >...))
	 * @param maxParamValue The maximal value (for(int i = 60; i > <b><u>42</u></b>, i = -2))
	 * @param increment The increment to add every time the loop finishes(for(int i = 0; i > 42; i = -<b><u>2</u></b>))
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
	/**
	 * Gets the full UUID (untrimmed UUID) from a trimmed UUID
	 * @param formattedUUID the trimmed UUID
	 * @return the full UUID
	 */
	public static String getFullUUID(String formattedUUID) {
		String uuid = formattedUUID.replaceAll(                                            
			    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",                            
			    "$1-$2-$3-$4-$5");
		return uuid;
	}
	/**
	 * Generates a random string
	 * @param lenght the length of the string
	 * @return a random string
	 */
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
	/**
	 * Checks if two locations have a deltaY less than 4
	 * @param a the first location
	 * @param b the second location
	 * @return whether the two locations have a deltaY less than 4
	 */
	public static boolean locationComparator(Location a, Location b) {
		return (a.getBlockX() == b.getBlockX() && a.getBlockZ() == b.getBlockZ() && (a.getBlockY() - b.getBlockY()) < 4);
	}
	
	public static Location locationFlattener(Location sourceLoc) {
		return new Location(sourceLoc.getWorld(), sourceLoc.getBlockX()+0.5, sourceLoc.getY(), sourceLoc.getBlockZ()+0.5);
	}
	
	public static int sendFakeFallingBlock(Player player, Location location, Material material) {
		Location blockLoc = locationFlattener(location);
		EntityFallingBlock eFallingBlock = EntityFallingBlock.a(NMSBridge.getWorld(location.getWorld()), NMSBridge.getBlockPosition(locationFlattener(blockLoc)), NMSBridge.getIBlockData(material));
		CraftFallingBlock cFallingBlock = (CraftFallingBlock) eFallingBlock.getBukkitEntity();
		cFallingBlock.setCancelDrop(true);
		cFallingBlock.setGravity(true);
		AdapterPacketPlayOutSpawnEntity pck = new AdapterPacketPlayOutSpawnEntity(ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SPAWN_ENTITY));
		pck.setEntityId(cFallingBlock.getEntityId());
		pck.setUniqueId(eFallingBlock.cT());
		pck.setEntityType(EntityType.FALLING_BLOCK);
		pck.setEntityData(NMSBridge.getDataInt(material));
		pck.setX(blockLoc.getX());
		pck.setY(blockLoc.getY()+0.1);
		pck.setZ(blockLoc.getZ());
		eFallingBlock.discard(Cause.DESPAWN);
		NMSBridge.getPlayerConnection(player).sendPacket(pck.getHandle());
		return cFallingBlock.getEntityId();
	}
	
	public static void sendEntityDestroyPacket(Player player, int entityId) {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(entityId);
		NMSBridge.getPlayerConnection(player).sendPacket(packet);
	}
	
	public static Location locationFlattenner(Location loc) {
		return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch());
	}
	
	public static String formatSeconds(int timeInSeconds)
	{
	    int secondsLeft = timeInSeconds % 3600 % 60;
	    int minutes = (int) Math.floor(timeInSeconds % 3600 / 60);
	    //int hours = (int) Math.floor(timeInSeconds / 3600);
	    String MM = ((minutes     < 10) ? "0" : "") + minutes;
	    String SS = ((secondsLeft < 10) ? "0" : "") + secondsLeft;

	    return MM + ":" + SS;
	} 

	public static boolean doubleContains(List<Entity> list, ArrayList<Player> seekers) {
		for(Player P : seekers) {
			if(list.contains(P)) return true;
			else continue;
		}
		return false;
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	    	sb.append((char) cp);
	    }
	    return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException, URISyntaxException {
	    InputStream is = new URI(url).toURL().openStream();
	    try {
	    	BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	    	String jsonText = readAll(rd);
	    	JSONObject json = new JSONObject(jsonText);
	    	return json;
	    } finally {
	    	is.close();
	    }
	}
		  
	public static String getHTTPResponse(String url, int timeout) throws URISyntaxException {
	    HttpURLConnection c = null;
	    try {
	        URL u = new URI(url).toURL();
	        c = (HttpURLConnection) u.openConnection();
	        c.setRequestMethod("GET");
	        c.setRequestProperty("Content-length", "0");
	        c.setRequestProperty("content-type", "text/plain; charset=utf-8");
	        c.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36");
	        c.setUseCaches(false);
	        c.setAllowUserInteraction(false);
	        c.setConnectTimeout(timeout);
	        c.setReadTimeout(timeout);
	        c.connect();
	        int status = c.getResponseCode();

	        switch (status) {
	            case 200:
	            case 201:
	                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
	                StringBuilder sb = new StringBuilder();
	                String line;
	                while ((line = br.readLine()) != null) {
	                    sb.append(line+"\n");
	                }
	                br.close();
	                return sb.toString();
	        }

	    } catch (MalformedURLException ex) {
	       ex.printStackTrace();
	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    } finally {
	       if (c != null) {
	          try {
	              c.disconnect();
	          } catch (Exception ex) {
	        	  ex.printStackTrace();
	          }
	       }
	    }
	    return null;
	}

	public static <K,V extends Comparable<? super V>> List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

	  List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
	
	  Collections.sort(sortedEntries, 
      new Comparator<Entry<K,V>>() {
          @Override
          public int compare(Entry<K,V> e1, Entry<K,V> e2) {
              return e2.getValue().compareTo(e1.getValue());
          }
      }
	  );
	  return sortedEntries;
	} 
}