package be.hctel.renaissance.hideandseek.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameRanks;
import be.hctel.renaissance.hideandseek.gamespecific.enums.JoinMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class PlayerListener implements Listener {
	public void onLogin(PlayerLoginEvent a) {
		if(Hide.isServerStarting) {
			a.getPlayer().kickPlayer("Server not started yet.");
		}
		else if(Hide.preGameTimer.gameStarted) {
			if(!(a.getPlayer().hasPermission("hide.spectate"))) a.getPlayer().kickPlayer("Only staff members are allowed to spectate in Hide and Seek.");
		}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws SQLException {
		Player p = e.getPlayer();
		p.teleport(new Location(Bukkit.getWorld("HIDE_Lobby"), -79.5, 90.5, 61.5, 0.1f, 0.1f));
		p.setGameMode(GameMode.ADVENTURE);
		p.getInventory().clear();
		if(!(Hide.stats.isLoaded(p))) Hide.stats.load(p);
		Hide.rankManager.load(p);
		Hide.cosmeticManager.loadPlayer(p);
		p.setDisplayName(Hide.rankManager.getRankColor(p) + p.getName());
		String joinMessage = JoinMessages.getFromStorageCode(Hide.stats.getJoinMessageIndex(p)).getMessage();
		joinMessage = Hide.rankManager.getRankColor(p) + p.getName() + joinMessage;
		e.setJoinMessage(joinMessage);
		Hide.votesHandler.sendMapChoices(p);
		Utils.sendHeaderFooter(p, "\n§6Renaissance §eProject\n§fBringing back good memories\n", "\n§aPlaying in §bHide §aAnd §eSeek.\n");
		Hide.preGameTimer.loadPlayer(p);
	}
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) throws SQLException {
		Hide.rankManager.unLoad(e.getPlayer());
		Hide.cosmeticManager.unloadPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		switch (e.getCause()) {
		case BLOCK_EXPLOSION:
			break;
		case CONTACT:
			break;
		case CRAMMING:
			break;
		case CUSTOM:
			break;
		case DRAGON_BREATH:
			break;
		case DROWNING:
			e.setCancelled(true);
			break;
		case ENTITY_ATTACK:
			break;
		case ENTITY_EXPLOSION:
			e.setCancelled(true);
			break;
		case ENTITY_SWEEP_ATTACK:
			break;
		case FALL:
			e.setCancelled(true);
			break;
		case FALLING_BLOCK:
			e.setCancelled(true);
			break;
		case FIRE:
			e.setCancelled(true);
			break;
		case FIRE_TICK:
			e.setCancelled(true);
			break;
		case FLY_INTO_WALL:
			break;
		case HOT_FLOOR:
			break;
		case LAVA:
			e.setCancelled(true);
			break;
		case LIGHTNING:
			break;
		case MAGIC:
			e.setCancelled(true);
			break;
		case MELTING:
			break;
		case POISON:
			e.setCancelled(true);
			break;
		case PROJECTILE:
			e.setCancelled(true);
			break;
		case STARVATION:
			break;
		case SUFFOCATION:
			break;
		case SUICIDE:
			break;
		case THORNS:
			break;
		case VOID:
			break;
		case WITHER:
			break;
		default:
			break;
		
		}
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			if(Hide.preGameTimer.gameStarted) {
				if (Hide.gameEngine.areSameTeam((Player) e.getEntity(), (Player) e.getDamager())) e.setCancelled(true);
				else {
					e.setDamage(7);
					Player damaged = (Player) e.getEntity();
					damaged.setHealth(damaged.getHealth() - 7);
					damaged.getWorld().playSound(damaged.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 0.5f);
					e.setCancelled(true);
				}
			} else e.setCancelled(true);
		} else e.setCancelled(true);
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(Hide.preGameTimer.gameStarted) {
			Hide.gameEngine.addKill(e.getEntity().getKiller(), (Player) e.getEntity(), Hide.gameEngine.isSeeker(e.getEntity().getKiller()));
		}
	}
 	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(Hide.preGameTimer.gameStarted) {
 			String msg = e.getMessage();
			e.setCancelled(true);
			Player player = e.getPlayer();
		  	Bukkit.broadcastMessage(GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getChatColor() + GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
		 } else {
			String msg = e.getMessage();
			e.setCancelled(true);
			Player player = e.getPlayer();
			Bukkit.broadcastMessage("§e" + Hide.stats.getPoints(player) + " §8▍ "+ GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getChatColor() + GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
		}
	}
}
