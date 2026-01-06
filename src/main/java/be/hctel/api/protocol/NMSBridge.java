package be.hctel.api.protocol;

import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.network.PlayerConnection;

public class NMSBridge {
	public static PlayerConnection getPlayerConnection(Player player) {
		return ((CraftPlayer) player).getHandle().g;
	}
}
