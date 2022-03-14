package be.hctel.api.bungee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeCordMessenger implements PluginMessageListener {
	Plugin plugin;
	String serverName;
	
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
		switch (subchannel) {
		case "GetServer":
			serverName = in.readUTF();
			break;
		default:
			
			break;
		}
	}
	
	public void requestServerName() {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("GetServer");
		new BukkitRunnable() {
			@Override
			public void run() {
				if(Bukkit.getOnlinePlayers().size() > 0) {
					Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0L, 20L);
	}
	
	public void sendToServer(Player player, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	public void addToQueue(Player player, ServerType queue) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("AddQueue");
		out.writeUTF(player.getName() + ":" + queue);
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	public void send(String subchannel, String content) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(subchannel);
		out.writeUTF(serverName + ":" + content);
		Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
		
	public void unload() {
		plugin.getServer().getMessenger().unregisterOutgoingPluginChannel(plugin);
		plugin.getServer().getMessenger().unregisterIncomingPluginChannel(plugin);
	}
}
