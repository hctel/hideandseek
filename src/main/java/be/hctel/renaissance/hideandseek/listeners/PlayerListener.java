package be.hctel.renaissance.hideandseek.listeners;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameRanks;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameTeam;
import be.hctel.renaissance.hideandseek.gamespecific.enums.JoinMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.PackettedUtils;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerListener implements Listener {
	
	private static TextComponent reportBug = new TextComponent (" §eIf there's any error, click ");
	static {
		TextComponent url = new TextComponent("§9§nthis link");
		url.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/hctel/hideandseek/issues"));
		TextComponent end = new TextComponent(" §e to report an issue.");
		reportBug.addExtra(url);
		reportBug.addExtra(end);
	}
	
	
	
	
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
		Hide.stats.load(p);
		Hide.rankManager.load(p);
		Hide.cosmeticManager.loadPlayer(p);
		p.setDisplayName(Hide.rankManager.getRankColor(p) + p.getName());
		e.setJoinMessage(Hide.rankManager.getRankColor(p) + p.getName() + JoinMessages.getFromStorageCode(Hide.stats.getJoinMessageIndex(p)).getMessage());
		Hide.votesHandler.sendMapChoices(p);
		Utils.sendHeaderFooter(p, "\n§6Renaissance §eProject\n§fBringing back good memories\n", "\n§aPlaying in §bHide §aAnd §eSeek.\n");
		Hide.preGameTimer.loadPlayer(p);
		Hide.shopPlayer.spawnFor(p);
		p.sendMessage("");
		p.sendMessage("");
		Utils.sendCenteredMessage(e.getPlayer(), "§6Welcome on the HnS Alpha release v1!");
		e.getPlayer().spigot().sendMessage(reportBug);
		p.getInventory().setItem(0, Utils.createQuickItemStack(Material.DIAMOND, (short) 0, "§b§lJoin messages"));
		p.setPlayerListName(Hide.rankManager.getRankColor(p) + p.getName());
	}
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) throws SQLException {
		Hide.rankManager.unLoad(e.getPlayer());
		Hide.cosmeticManager.unloadPlayer(e.getPlayer());
		Hide.stats.savePlayer((OfflinePlayer) e.getPlayer());
		if(Hide.preGameTimer.gameStarted) Hide.gameEngine.disconnect((OfflinePlayer) e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent e) {
		if(e.getCause().equals(DamageCause.FALL)) {
			e.getEntity().setFallDistance(0);
			e.setDamage(0);
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			if(Hide.preGameTimer.gameStarted) {
				if (Hide.gameEngine.areSameTeam((Player) e.getEntity(), (Player) e.getDamager())) {
					e.setCancelled(true);
					for(Player P : Bukkit.getOnlinePlayers()) PackettedUtils.sendSound(P, e.getEntity().getLocation(), "entity.player.death", 1.0f, 0.5f);
				}
				else if(Hide.gameEngine.getTeam((Player) e.getDamager()) == GameTeam.SEEKER) {
					((Player) e.getEntity()).playSound(e.getDamager().getLocation(), Sound.ENTITY_PLAYER_DEATH, 2.0f, 0.5f);
					for(Player P : Bukkit.getOnlinePlayers()) P.playSound(e.getDamager().getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 0.5f);
					//Player damaged = (Player) e.getEntity();
					e.setDamage(7);
				} 				
			} else e.setCancelled(true);
		} else e.setCancelled(true);
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if(Hide.gameEngine.getTeam(p) == GameTeam.SEEKER) {
			for(Player i : Hide.gameEngine.disguises.keySet()) {
				if(Hide.gameEngine.disguises.get(i).getBlock().equals(b)) {
					Hide.gameEngine.disguises.get(i).makeUnsolid();
					onDamageByEntity(new EntityDamageByEntityEvent(p, i, DamageCause.ENTITY_ATTACK, 0));
					i.damage(7);
					break;
				}
			}
		} else e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(e.getClickedBlock().getType() == Material.SIGN) {
				//Sign
			} //else if (e.getClickedBlock().getType() != Material.WOOD_DOOR) e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDeathMessage(null);
		//e.getEntity().spigot().respawn();
		if(Hide.preGameTimer.gameStarted) {
			Hide.gameEngine.addKill(e.getEntity().getKiller(), (Player) e.getEntity(), Hide.gameEngine.isSeeker(e.getEntity().getKiller()));
		}
	}
 	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(Utils.getUUID(e.getPlayer()).equals(Hide.stats.getTopRankPlayer())) {
			if(Hide.preGameTimer.gameStarted) {
	 			String msg = e.getMessage();
				e.setCancelled(true);
				Player player = e.getPlayer();
			  	Bukkit.broadcastMessage(GameRanks.MOD.getChatColor() + GameRanks.MOD.getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
			 } else {
				String msg = e.getMessage();
				e.setCancelled(true);
				Player player = e.getPlayer();
				Bukkit.broadcastMessage("§e" + Hide.stats.getPoints(player) + " §8▍ "+ GameRanks.MOD.getChatColor() + GameRanks.MOD.getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
			}
		} else {
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
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}
}
