package be.hctel.renaissance.hideandseek.nongame.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/*
 * This file is a part of the Renaissance Project API
 */

public class SpawnLocation {
	String worldName;
	double x, y, z;
	float yaw, pitch;
	public SpawnLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	public Location getLocation() {
		return new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	public float getYaw() {
		return yaw;
	}
	public float getPitch() {
		return pitch;
	}
	public String getWorldName() {
		return worldName;
	}
	public World getWorld() {
		return Bukkit.getWorld(worldName);
	}
}
