package be.hctel.renaissance.hideandseek.gamespecific.enums;

import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public enum JoinMessages {
	RONAN(-1, " §6wants to be a §aR§cu§bb§ei§fk§b'§es §cC§au§bb§ee", 999999999),
	HIDE(0, " §7wants to hide!", 0),
	BLEND_IN(1, " §ewants to blend in!", 2000),
	DISAPPEAR(2, " §6is ready to disappear.", 2000),
	HUNT(3, " §6is ready to hunt!", 2000),
	MASTER(4, " §dis a hiding master.", 5000),
	NINJA(5, " §8is going to be a ninja.", 5000),
	SNEAKY(6, " §cWants to be sneaky!", 2000),
	DIFFERENCE(7, " §fis going to spot the difference!", 3000),
	MAGIC(8, " §bwants to do a magic trick", 7500);
	
	int stor;
	String message;	
	int price;
	private JoinMessages(int stor, String message, int price) {
		this.stor = stor;
		this.message = message;
		this.price = price;
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
	public int getPrice() {
		return price;
	}
	
	public static JoinMessages getFromStorageCode(int storageCode) {
		switch(storageCode) {
		case -1: return RONAN;
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
	public static JoinMessages getFromString(String name) {
		for(JoinMessages M : JoinMessages.values()) {
			if(M.toString().equalsIgnoreCase(name)) {
				return M;
			}
		}
		return HIDE;
	}
}
