package be.hctel.renaissance.hideandseek.nongame.utils;

/*
 * This file is a part of the Renaissance Project API
 */

public enum ChatMessages {
	NO_RANK_CHANGE("§cSorry, but your current rank doesn't allow you to disguise your current rank as that rank."),
	RANKCHANGE("§aYour rank was successfully changed."),
	RANKTOGGLE("§aToggled your rank!"),
	RANKINDEXINVALID("§cPlease enter a value from 1 to 4."),
	NOPERM("§cYou don't have the permission to run this command!"),
	ALREADYVOTED("§cYou have already voted for that map."),
	VOTESENDED("§cVoting is not active right now."),
	VOTEREGISTERED("§aYou voted for §6"),
	NAN("§cPlease provide a valid number"),
	STARTCANCELLED("§cStart cancelled! We don't have enough players anymore."),
	PLAYER_NOTFOUND("§cSorry, but that player wasn't found.");
	
	
	String message;
	private ChatMessages(String message) {
		this.message = message;
	}
	
	public String toText() {
		return message;
	}
}
