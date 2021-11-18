package be.hctel.renaissance.hideandseek.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.viaversion.viaversion.api.Via;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameRanks;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameTeam;
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
		p.sendMessage(Hide.header + "§aWelcome on HnS indev v1 release!");
	}
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) throws SQLException {
		Hide.rankManager.unLoad(e.getPlayer());
		Hide.cosmeticManager.unloadPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		System.out.println("Triggerred damage event");
		if(Hide.preGameTimer.gameStarted) {
			if(e.getEntity() instanceof Player) {
				System.out.println("    damage event is player");
				if(e.getCause() == DamageCause.FALL) {
					System.out.println("         and is fall");
						e.setCancelled(true);
				}
				if(e.getCause() == DamageCause.DROWNING) {
					e.setCancelled(true);
				}
			}
		} else e.setCancelled(true);
	}
	
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			if(Hide.preGameTimer.gameStarted) {
				if (Hide.gameEngine.areSameTeam((Player) e.getEntity(), (Player) e.getDamager())) e.setCancelled(true);
				else if(Hide.gameEngine.getTeam((Player) e.getDamager()) == GameTeam.SEEKER) {
					Player damaged = (Player) e.getEntity();
					int d = (int) (damaged.getHealth() >= 6 ? 6 : damaged.getHealth());
					damaged.setHealth(damaged.getHealth() - d);
					damaged.getWorld().playSound(damaged.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 0.5f);
					e.setCancelled(true);
				} 				
			} else e.setCancelled(true);
		} else e.setCancelled(true);
	}
	@EventHandler
	public void onBlockDamage(BlockDamageEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if(Hide.gameEngine.getTeam(p) == GameTeam.SEEKER) {
			for(Player i : Hide.gameEngine.disguises.keySet()) {
				if(Hide.gameEngine.disguises.get(i).getBlock().equals(b)) {
					Player damaged = i;
					int d = (int) (damaged.getHealth() >= 6 ? 6 : damaged.getHealth());
					damaged.setHealth(damaged.getHealth() - d);
					damaged.getWorld().playSound(damaged.getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 0.5f);
				}
			}
		} else e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getPlayer().getLocation().getWorld().getName().contains("TEMPWORLD") || e.getPlayer().getLocation().getWorld().getName().equals("HIDE_Lobby")) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					e.setCancelled(true);
					
				}
			}.runTaskLater(Hide.plugin, 1L);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getClickedBlock().getType() != null) {
			if(e.getClickedBlock().getType() == Material.SIGN) {
				//Sign
			} else if (e.getClickedBlock().getType() != Material.WOOD_DOOR) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		if(Hide.preGameTimer.gameStarted) {
			Hide.gameEngine.addKill(e.getEntity().getKiller(), (Player) e.getEntity(), Hide.gameEngine.isSeeker(e.getEntity().getKiller()));
			Player p = (Player) e.getEntity();
			p.spigot().respawn();
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
