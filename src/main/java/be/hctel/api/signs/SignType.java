package be.hctel.api.signs;

import java.util.HashMap;

public enum SignType {
	/**
	 * Teleports the player
	 * @deprecated Not implemented yet
	 */
	@Deprecated
	TELEPORT("TELEPORT"),
	/**
	 * Executes a command as the player
	 * <br>Operation: COMMAND,command to run
	 */
	COMMAND("COMMAND"),
	
	/**
	 * An easter egg command
	 * @deprecated Only sends a message from now.
	 * <br>Operation: EASTER_EGG,text to send
	 */
	@Deprecated
	EASTER_EGG("EASTER_EGG"),
	/**
	 * Sends the player to the specified server
	 * <br>Operation: SERVER,server where to send the player to
	 */
	SERVER("SERVER"),
	QUEUE("QUEUE");
	
	String name;
	private SignType(String s) {
		this.name = s;
	}
	
	private static HashMap<String, SignType> match = new HashMap<>();
	static {
		for(SignType T : SignType.values()) {
			match.put(T.getName(), T);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public static SignType getfromName(String s) {
		return match.get(s);
	}
}


