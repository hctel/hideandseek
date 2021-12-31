package be.hctel.api.bungee;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeCordMessenger implements PluginMessageListener {
	Plugin plugin;
	
	/**
	 * 
	 * @param plugin
	 */
	public BungeeCordMessenger(Plugin plugin) {
		this.plugin = plugin;
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if(!channel.equals("BungeeCord")) return;
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
	}
	/**
	 * 
	 * @param player
	 * @param server
	 */
	public void sendToServer(Player player, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	/**
	 * 
	 */
	public void unload() {
		plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin);
		plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin);
	}
}
