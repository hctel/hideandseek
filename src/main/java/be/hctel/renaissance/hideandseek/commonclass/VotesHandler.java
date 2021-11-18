package be.hctel.renaissance.hideandseek.commonclass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameMap;
import be.hctel.renaissance.hideandseek.nongame.utils.ChatMessages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class VotesHandler {
	Plugin plugin;
	ArrayList<GameMap> maps = new ArrayList<GameMap>();
	HashMap<GameMap, Integer> votes = new HashMap<GameMap, Integer>();
	HashMap<Player, Integer> playerVote = new HashMap<Player, Integer>();
	public ArrayList<GameMap> currentGameMaps = new ArrayList<GameMap>();
	boolean acceptingVotes = true;
	public int voted;
	public VotesHandler(Plugin plugin) {
		this.plugin = plugin;
		for(GameMap map : GameMap.values()) {
			maps.add(map);
		}
		ArrayList<GameMap> a = new ArrayList<GameMap>();
		Random r = new Random();
		for(int i = 0; i < 6; i++) {
			GameMap b = maps.get(r.nextInt(maps.size()));
			while(a.contains(b)) {
				b = maps.get(r.nextInt(maps.size()));
			}
			//To replace first map with specific map
			if(i == 0) {
				a.add(GameMap.DEPARTURE);
				votes.put(GameMap.DEPARTURE, 0);
				continue;
			}
			a.add(b);
			votes.put(b, 0);
		}
		currentGameMaps = a;
		a = null;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Hide.mapLoader.loadWorldsToTempWorld(currentGameMaps);
				
			}
		}.runTask(plugin);
	}
	
	public void sendMapChoices() {
		for(int i = 0; i < 5; i++) {
			TextComponent message = new TextComponent(Hide.header + "§7§l" + (i+1) + ". §6" + currentGameMaps.get(i).getMapName() + getVoteAmountString(currentGameMaps.get(i)));
			message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/v " + (i+1)));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to vote for §6" + currentGameMaps.get(i).getMapName()).create()));
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.spigot().sendMessage(message);
			}
			
		}
		TextComponent message = new TextComponent(Hide.header + "§7§l" + (6) + ". §cRandom Map" + getVoteAmountString(currentGameMaps.get(5)));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to vote for §cRandom Map").create()));
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/v 6"));
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.spigot().sendMessage(message);
		}
	}
	public void sendMapChoices(Player player) {
		player.sendMessage(Hide.header + "§e§lVote for a map! §7Use §f/v # §7or click.");
		for(int i = 0; i < 5; i++) {
			TextComponent message = new TextComponent(Hide.header + "§7§l" + (i+1) + " §6" + currentGameMaps.get(i).getMapName() + getVoteAmountString(currentGameMaps.get(i)));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to vote for §6" + currentGameMaps.get(i).getMapName()).create()));
			message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/v " + (i+1)));
			player.spigot().sendMessage(message);
		}
		TextComponent message = new TextComponent(Hide.header + "§7§l" + (6) + ". §cRandom Map" + getVoteAmountString(currentGameMaps.get(5)));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to vote for §cRandom Map").create()));
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/v 6"));
		player.spigot().sendMessage(message);
	}
	public void registerPlayerVote(Player player, int in, int qty) {
		if(!acceptingVotes) {
			player.sendMessage(Hide.header + ChatMessages.VOTESENDED.toText());
			return;
		}
		in--;
		if(in >= -1 && in < 6) {
			if(playerVote.containsKey(player)) {
				if(in == -1) {
					votes.replace(currentGameMaps.get(playerVote.get(player)), votes.get(currentGameMaps.get(playerVote.get(player)))-qty);
					playerVote.remove(player);
					return;
				}
				if(playerVote.get(player) == in) {
					player.sendMessage(Hide.header + ChatMessages.ALREADYVOTED.toText());
				} else {
					votes.replace(currentGameMaps.get(playerVote.get(player)), votes.get(currentGameMaps.get(playerVote.get(player)))-qty);
					playerVote.replace(player, in);
					votes.replace(currentGameMaps.get(playerVote.get(player)), votes.get(currentGameMaps.get(playerVote.get(player))) + qty);
					if(in == 5) {
						player.sendMessage(Hide.header + ChatMessages.VOTEREGISTERED.toText() + "§cRandom Map" + getVoteAmountString(currentGameMaps.get(in)) + ".");
					} else {
						player.sendMessage(Hide.header + ChatMessages.VOTEREGISTERED.toText() + currentGameMaps.get(in).getMapName() + getVoteAmountString(currentGameMaps.get(in)) + ".");
					}
				}
			} else {
				playerVote.put(player, in);
				votes.replace(currentGameMaps.get(in), votes.get(currentGameMaps.get(in)) + qty);
				if(in == 5) {
					player.sendMessage(Hide.header + ChatMessages.VOTEREGISTERED.toText() + "§cRandom Map" + getVoteAmountString(currentGameMaps.get(in)) + ".");
				} else {
					player.sendMessage(Hide.header + ChatMessages.VOTEREGISTERED.toText() + currentGameMaps.get(in).getMapName() + getVoteAmountString(currentGameMaps.get(in)) + ".");
				}
			}
		} else {
			player.sendMessage(Hide.header + ChatMessages.NAN.toText() + " (1-6).");
		}
	}
	public int endVotes() {
		acceptingVotes = false;
		GameMap voted = Collections.max(votes.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
		this.voted = currentGameMaps.indexOf(voted);
		return currentGameMaps.indexOf(voted);
	}
	private String getVoteAmountString(GameMap map) {
		int n = votes.get(map);
		String color = "§7";
		GameMap max = Collections.max(votes.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
		if(map == max) color = "§a";
		if(n == 1) {
			return color + " [§f" + n + color + " Vote]";
		} else {
			return  color + " [§f" + n + color + " Votes]";
		}
	}
}
