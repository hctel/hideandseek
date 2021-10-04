package be.hctel.renaissance.hideandseek.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameRanks;
import be.hctel.renaissance.hideandseek.gamespecific.enums.JoinMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class PlayerListener implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.teleport(new Location(Bukkit.getWorld("HIDE_Lobby"), -79.5, 90.5, 61.5, 0.1f, 0.1f));
		p.setGameMode(GameMode.ADVENTURE);
		if(!(Hide.stats.isLoaded(p))) Hide.stats.load(p);
		Hide.rankManager.load(p);
		p.setDisplayName(Hide.rankManager.getRankColor(p) + p.getName());
		String joinMessage = JoinMessages.getFromStorageCode(Hide.stats.getJoinMessageIndex(p)).getMessage();
		joinMessage = Hide.rankManager.getRankColor(p) + p.getName() + joinMessage;
		e.setJoinMessage(joinMessage);
		Hide.votesHandler.sendMapChoices(p);
		Utils.sendHeaderFooter(p, "\n§6Renaissance §eProject\n§fBringing back good memories\n", "\n§aPlaying in §bHide §aAnd §eSeek.\n");
	}
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		Hide.rankManager.unLoad(e.getPlayer());
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.FALL) e.setCancelled(true);
	}
 	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		/*if(Hide.preStartManager.isStarted) {
 			String msg = e.getMessage();
			e.setCancelled(true);
			Player player = e.getPlayer();
		  	Bukkit.broadcastMessage(GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getChatColor() + GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
		 } else {*/
			String msg = e.getMessage();
			e.setCancelled(true);
			Player player = e.getPlayer();
			Bukkit.broadcastMessage("§e" + Hide.stats.getPoints(player) + " §8▍ "+ GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getChatColor() + GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
		//}
	}
}
