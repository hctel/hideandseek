package be.hctel.renaissance.hideandseek.gamespecific.enums;

import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public enum JoinMessages {
	HIDE(0, " §7wants to hide!"),
	BLEND_IN(1, " §ewants to blend in!"),
	DISAPPEAR(2, " §6is ready to disappear."),
	HUNT(3, " §6is ready to hunt!"),
	MASTER(4, " §dis a hiding master."),
	NINJA(5, " §8is going to be a ninja."),
	SNEAKY(6, " §cWants to be sneaky!"),
	DIFFERENCE(7, " §fis going to spot the difference!"),
	MAGIC(8, " §bwants to do a magic trick");
	
	int stor;
	String message;	
	private JoinMessages(int stor, String message) {
		this.stor = stor;
		this.message = message;
	}
	public int getStorageCode() {
		return stor;
	}
	public String getMessage() {
		return message;
	}
	public String getMenuName() {
		String base = this.toString();
		base.replace("_", " ");
		return Utils.capitalize(base);
	}
	public static JoinMessages getFromStorageCode(int storageCode) {
		switch(storageCode) {
		case 0: return HIDE;
		case 1: return BLEND_IN;
		case 2: return DISAPPEAR;
		case 3: return HUNT;
		case 4: return MASTER;
		case 5: return NINJA;
		case 6: return SNEAKY;
		case 7: return DIFFERENCE;
		case 8: return MAGIC;
		}
		return HIDE;
	}
}
