package be.hctel.api.scoreboard;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/*
 * Copyright § hctel
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the §Software§), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * The Software is provided §as is§, without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the software or the use or other dealings in the Software. 
 */

/**
 * DynamicScoreboard class.
 * Use to create custom scoreboards in your Spigot Plugins.
 * @author hctel
 *
 */


public class DynamicScoreboard {
	String identifier;
	String name;
	ArrayList<Player> recipients = new ArrayList<Player>();
	HashMap<Integer, String> line = new HashMap<Integer, String>();
	Scoreboard board;
	Objective obj;
	ScoreboardManager manager;
	
	/**
	 * Creates a DynamicScoreboard.
	 * 
	 * @param identifier The internal identifier of the scoreboard. <b><u>CANNOT BE USED TWICE!</u></b>
	 * @param name The display name of the scoreboard
	 * @param manager The plugin's scoreboard manager
	 * 
	 */
	public DynamicScoreboard(String identifier, String name, ScoreboardManager manager) {
		this.identifier = identifier;
		this.name = name;
		this.manager = manager;
		this.board = manager.getNewScoreboard();
		this.obj = board.registerNewObjective(identifier, Criteria.DUMMY, name);
		this.obj.setDisplayName(name);
		this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	/**
	 * Adds a line to the scoreboard
	 * @param lineIndex The number of the line where you want to put the text
	 * @param lineContent The text of the line
	 */
	public void setLine(int lineIndex, String lineContent) {
		setLine(lineIndex, lineContent, true);
	}
	
	public void setLine(int lineIndex, String lineContent, boolean replace) {
		if(replace && line.containsKey(lineIndex)) removeLine(lineIndex);
		Score a = obj.getScore(lineContent);
		line.put(lineIndex, lineContent);
		a.setScore(lineIndex);
	}
	
	/**
	 * Removes a line from the scoreboard
	 * @param lineIndex The number assigned to that line
	 * @return whether the line was removed or not
	 */
	public boolean removeLine(int lineIndex) {
		boolean a = this.line.containsKey(lineIndex);
		if(a) {
			obj.getScoreboard().resetScores(line.get(lineIndex));
			line.remove(lineIndex);
		}
		return a;
	}
	
	/**
	 * Adds a receiver to the scoreboard receiver list
	 * @param player The player to add to the list
	 * @return whether the player was added to the list
	 */
	public boolean addReceiver(Player player) {
		boolean a = !recipients.contains(player);
		if(a) {
			recipients.add(player);
			player.setScoreboard(board);
		}
		return a;
	}
	
	/**
	 * Removes a receiver from the scoreboard receiver list
	 * @param player The player to remove from the list
	 * @return whether the player was removed from the list
	 */
	public boolean removeReceiver(Player player) {
		boolean a = recipients.contains(player);
		if(a) {
			recipients.remove(player);
			player.setScoreboard(manager.getNewScoreboard());
		}
		return a;
	}
}
