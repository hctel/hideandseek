package be.hctel.api.protocol;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R6.block.CraftBlock;
import org.bukkit.craftbukkit.v1_21_R6.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.core.BlockPosition;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.level.block.state.IBlockData;

public class NMSBridge {
	public static PlayerConnection getPlayerConnection(Player player) {
		return ((CraftPlayer) player).getHandle().g;
	}
	
	public static WorldServer getWorld(World w) {
		return ((CraftWorld) w).getHandle();
	}
	
	public static BlockPosition getBlockPosition(Location l) {
		return ((CraftBlock) l.getBlock()).getPosition();
	}
	
	public static IBlockData getIBlockData(Material m) {
		return getIBlockData(m.createBlockData());
	}
	
	public static IBlockData getIBlockData(BlockData m) {
		CraftBlockData craftBlockData = ((CraftBlockData) m);
		return craftBlockData.getState();
	}

	public static int getDataInt(Material m) {
		IBlockData nmsBlockData = getIBlockData(m);
		return net.minecraft.world.level.block.Block.j(nmsBlockData);
	}
	
	public static int getDataInt(BlockData m) {
		IBlockData nmsBlockData = getIBlockData(m);
		return net.minecraft.world.level.block.Block.j(nmsBlockData);
	}
}
