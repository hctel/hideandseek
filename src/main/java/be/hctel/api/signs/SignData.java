package be.hctel.api.signs;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class SignData {
	private SignType type;
	private String data;
	private Location location;
	
	/**
	 * Creates a SignData from a block
	 * @param type The {@link SignType} of the sign
	 * @param data The operation that this sign carries
	 * @param location The {@link Location} of the sign
	 */
	public SignData(SignType type, String data, Location location) {
		this.type = type;
		this.data = data;
		this.location = location;
	}
	
	/**
	 * Creates a SignData from the config gile
	 * @param type The {@link SignType} of the sign
	 * @param data The operation that this sign carries
	 * @param signPos The {@link String} containing the location of the sign
	 */
	public SignData(SignType type, String data, String[] signPos) {
		this.type = type;
		this.data = data;
		this.location = new Location(Bukkit.getWorld(signPos[0]), Integer.parseInt(signPos[1]), Integer.parseInt(signPos[2]), Integer.parseInt(signPos[3]));
	}
	
	public SignType getType() {
		return this.type;
	}
	
	public String getData() {
		return this.data;
	}
	
	public Location getLocation() {
		return this.location;
	}
}
