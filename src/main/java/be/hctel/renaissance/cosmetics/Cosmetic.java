package be.hctel.renaissance.cosmetics;

public enum Cosmetic {
	//HIDE Cosmetics
	DEFAULTARMOR(GameType.HIDE, "Plain old Iron Armor");
	
	GameType game;
	String name;
	private Cosmetic(GameType game, String name) {
		this.game = game;
		this.name = name;
	}
}
