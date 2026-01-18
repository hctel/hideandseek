package be.hctel.renaissance.hideandseek.listeners;

import java.sql.SQLException;
import java.util.UUID;

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
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.CollisionRule;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.NameTagVisibility;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.OptionData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.ScoreBoardTeamInfo;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTeams.TeamMode;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameRanks;
import be.hctel.renaissance.hideandseek.gamespecific.enums.GameTeam;
import be.hctel.renaissance.hideandseek.gamespecific.enums.JoinMessages;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerListener implements Listener {
	
	private static TextComponent reportBug = new TextComponent (" §eIf there's any error, click ");
	static {
		TextComponent url = new TextComponent("§9§nthis link");
		url.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://forms.gle/rcj97SFdc4pBQbeT7"));
		TextComponent end = new TextComponent(" §e to report an issue.");
		reportBug.addExtra(url);
		reportBug.addExtra(end);
	}
	
	// private static ItemStack rulesBook = new FakeBook("§b§lRules §7§l& §e§lInfo", "§d§lHide and Seek\n§7-=-=-=-=-=-=-=-=-\n\n§c§lRules\n\n§81: Do not cheat\n§82: Do not glitch\n§83: Clean chat\n§84: Respect others\n\n§8Full rules are\n§8available at hctel.net").getItemStack();	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent a) {
		if(Hide.preGameTimer.choosingBlock && !Hide.preGameTimer.gameStarted) {
			a.disallow(Result.KICK_OTHER, "§ePlease do not rejoin games when block picking is active.");
			return;
		}
		else if(Hide.isServerStarting) {
			a.disallow(Result.KICK_OTHER, "Server not started yet.");
			return;
		}
		if(!Hide.preGameTimer.gameStarted) {
			if(Bukkit.getOnlinePlayers().size() > 23) {
				a.disallow(Result.KICK_FULL, "§This server is full.");
			}
		}
		/*else if(Hide.preGameTimer.gameStarted) {
			if(!(a.getPlayer().hasPermission("hide.spectate"))) a.getPlayer().kickPlayer("Only staff members are allowed to spectate in Hide and Seek.");
		}*/
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) throws SQLException {
		if(Hide.preGameTimer.choosingBlock && !Hide.preGameTimer.gameStarted) {
			e.getPlayer().kickPlayer("§ePlease do not rejoin games when block picking is active.");
			return;
		}
		if(Hide.preGameTimer.gameStarted) {
			Hide.gameEngine.addSpectator(e.getPlayer());
			e.getPlayer().sendMessage(Hide.header + "§eYou are a spectator.");
			e.getPlayer().setGameMode(GameMode.SPECTATOR);
		}
		Player p = e.getPlayer();
		//p.sendTitle("§c§lDEV MODE", "§eIf you're not involved in testing, please disconnect!", 10, 120, 20);
		if(!Hide.preGameTimer.gameStarted) {
			p.teleport(new Location(Bukkit.getWorld("HIDE_Lobby"), -79.5, 90.5, 61.5, 0.1f, 0.1f));
			p.setGameMode(GameMode.ADVENTURE);
		}
		p.getInventory().clear();
		Hide.bm.sendForward("PlayerJoin", Hide.plugin.getServer().getOnlinePlayers().size()+"");
		Hide.stats.load(p);
		Hide.rankManager.load(p);
		Hide.cosmeticManager.loadPlayer(p);
		Hide.shopPlayer.spawnFor(p);
		p.setDisplayName(Hide.rankManager.getRankColor(p) + p.getName());
		e.setJoinMessage(Hide.rankManager.getRankColor(p) + p.getName() + JoinMessages.getFromStorageCode(Hide.stats.getJoinMessageIndex(p)).getMessage());
		if(!Hide.preGameTimer.gameStarted) Hide.votesHandler.sendMapChoices(p);
		Utils.sendHeaderFooter(p, "\n§6Renaissance §eProject\n§fBringing back good memories\n", "\n§aPlaying in §bHide §aAnd §eSeek.\n");
		Hide.preGameTimer.loadPlayer(p);
		p.sendMessage("");
		p.sendMessage("");
		Utils.sendCenteredMessage(e.getPlayer(), "§6Welcome on the HnS Alpha release v1!");
		e.getPlayer().spigot().sendMessage(reportBug);
		if(!Hide.preGameTimer.gameStarted) {
			new BukkitRunnable() {
				@Override
				public void run() {
					//p.getInventory().setItem(0, rulesBook);
					p.getInventory().setItem(1, Utils.createQuickItemStack(Material.DIAMOND, (short) 0, "§6§lView Vote Menu"));
					p.getInventory().setItem(2, Utils.createQuickItemStack(Material.BOOK, (short) 0, "§r§lSeeker kill records"));
					p.getInventory().setItem(7, Utils.createQuickItemStack(Material.COMPARATOR, (short) 0, "§b§lJoin messages"));
					p.getInventory().setItem(8, Utils.createQuickItemStack(Material.SLIME_BALL, (short) 0, "§c§lReturn to Hub"));
				}
			}.runTaskLater(Hide.plugin, 1L);
		}
		p.setPlayerListName(Hide.rankManager.getRankColor(p) + p.getName());
		p.setCollidable(false);
		p.getCollidableExemptions().clear();
		ScoreBoardTeamInfo ti = new ScoreBoardTeamInfo(Component.empty(), Component.empty(), Component.empty(), NameTagVisibility.ALWAYS, CollisionRule.NEVER, NamedTextColor.WHITE, OptionData.NONE);
		WrapperPlayServerTeams t = new WrapperPlayServerTeams(UUID.randomUUID().toString(), TeamMode.CREATE, ti, p.getName());
		PacketEvents.getAPI().getPlayerManager().sendPacket(p, t);
	}
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) throws SQLException {
		e.setQuitMessage(null);
		Hide.votesHandler.registerPlayerVote(e.getPlayer(), 0, Hide.rankManager.getRank(e.getPlayer()).getVotes());
		Hide.rankManager.unLoad(e.getPlayer());
		Hide.cosmeticManager.unloadPlayer(e.getPlayer());
		Hide.stats.saveOfflinePlayer((OfflinePlayer) e.getPlayer());
		if(Hide.preGameTimer.gameStarted && Bukkit.getOnlinePlayers().size()-1 == 0) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "s");
		if(Hide.preGameTimer.gameStarted) Hide.gameEngine.disconnect((OfflinePlayer) e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onDamage(EntityDamageEvent e) {
		if(e.getCause() == DamageCause.CUSTOM) {
			return;
		}
		if(e.getCause() == DamageCause.ENTITY_ATTACK) {
			if(e.getEntity() instanceof Player && e.getDamageSource() instanceof Player) {
				Player player = (Player) e.getEntity();
				Player damager = (Player) e.getDamageSource();
				if(Hide.preGameTimer.gameStarted) {
					if(e.getDamage() > player.getHealth()) {
						e.setDamage(0);
						player.setHealth(20);
						player.getInventory().clear();
						Hide.gameEngine.addKill(damager, player, Hide.gameEngine.isHider(player));
					}
				}
				return;
			}
			e.setCancelled(true);
			return;
		}
		if(e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if(Hide.preGameTimer.gameStarted) {
				if(e.getDamage() > player.getHealth()) {
					e.setDamage(0);
					player.setHealth(20);
					player.getInventory().clear();
					Hide.gameEngine.addKill(null, player, Hide.gameEngine.isHider(player));
				} else if(e.getCause().equals(DamageCause.FALL)) {
					player.setFallDistance(0);
					e.setDamage(0);
				} else {
					return;
				}
			}
		} e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player damaged = (Player) e.getEntity();
			Player damager = (Player) e.getDamager();
			if(Hide.preGameTimer.gameStarted) {
				if (Hide.gameEngine.areSameTeam((Player) e.getEntity(), (Player) e.getDamager()) || Hide.gameEngine.isGameFinished) {
					e.setCancelled(true);
				}
				else {
					
					if(Hide.gameEngine.getTeam((Player) e.getDamager()) == GameTeam.SEEKER && Hide.gameEngine.getTeam(damaged) == GameTeam.HIDER) {
						damaged.playSound(e.getDamager().getLocation(), Sound.ENTITY_PLAYER_DEATH, 2.0f, 0.5f);
						for(Player P : Bukkit.getOnlinePlayers()) P.playSound(e.getDamager().getLocation(), Sound.ENTITY_PLAYER_DEATH, 1.0f, 0.5f);
						if(damaged.getHealth() < 7) {
							damaged.setHealth(20.0);
							damaged.getInventory().clear();
							Hide.gameEngine.addKill(damager, damaged, true);
							return;
						}
						damaged.damage(7);
						return;
					} else if(Hide.gameEngine.getTeam(damager) == GameTeam.HIDER && Hide.gameEngine.getTeam(damaged) == GameTeam.SEEKER) {
						if(e.getDamage() > damaged.getHealth()) {
							e.setDamage(0);
							damaged.setHealth(20);
							damaged.getInventory().clear();
							Hide.gameEngine.addKill(damager, damaged, false);
						}
						return;
					}
				}
			}
		}
		e.setCancelled(true);
	}
	
	@SuppressWarnings("removal")
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if(Hide.gameEngine.getTeam(p) == GameTeam.SEEKER) {
			if(b == null) return;
			for(Player i : Hide.gameEngine.disguises.keySet()) {
				if(Hide.gameEngine.disguises.get(i) == null) return;
				if(Hide.gameEngine.disguises.get(i).getBlock() == null) return;
				if(Hide.gameEngine.disguises.get(i).getBlock().equals(b)) {
					Hide.gameEngine.disguises.get(i).makeUnsolid();
					onDamageByEntity(new EntityDamageByEntityEvent(p, i, DamageCause.ENTITY_ATTACK, 0));
					if(i.getHealth() < 7) {
						if(Hide.preGameTimer.gameStarted) {
							i.getInventory().clear();
							i.setHealth(20.0);
							Hide.gameEngine.addKill(p, (Player) i, Hide.gameEngine.isSeeker(p));
						}
						return;
					}
					i.damage(7);
					return;
				}
			}
		} else e.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.LEFT_CLICK_BLOCK && Hide.preGameTimer.gameStarted) {
			onBlockDamage(new BlockDamageEvent(e.getPlayer(), e.getClickedBlock(), e.getItem(), false));
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDeathMessage(null);
		e.getEntity().spigot().respawn();
		
	}
 	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(Utils.getUUID(e.getPlayer()).equals(Hide.stats.getTopRankOfflinePlayer())) {
			if(Hide.preGameTimer.gameStarted) {
				if(Hide.gameEngine.getTeam(e.getPlayer()).equals(GameTeam.SPECTATOR)) {
					String msg = e.getMessage();
					e.setCancelled(true);
					Player player = e.getPlayer();
				  	Bukkit.broadcastMessage("§4SPEC §8▍ " + GameRanks.MOD.getChatColor() + GameRanks.MOD.getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
				} else {
					String msg = e.getMessage();
					e.setCancelled(true);
					Player player = e.getPlayer();
				  	Bukkit.broadcastMessage(GameRanks.MOD.getChatColor() + GameRanks.MOD.getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
				}	 			
			 } else {
				String msg = e.getMessage();
				e.setCancelled(true);
				Player player = e.getPlayer();
				Bukkit.broadcastMessage("§e" + Hide.stats.getPoints(player) + " §8▍ "+ GameRanks.MOD.getChatColor() + GameRanks.MOD.getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
			}
		} else {
		if(Hide.preGameTimer.gameStarted) {
			if(Hide.gameEngine.getTeam(e.getPlayer()).equals(GameTeam.SPECTATOR)) {
				String msg = e.getMessage();
				e.setCancelled(true);
				Player player = e.getPlayer();
			  	Bukkit.broadcastMessage("§4SPEC §8▍ " + GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getChatColor() + GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
			} else {
				String msg = e.getMessage();
				e.setCancelled(true);
				Player player = e.getPlayer();
			  	Bukkit.broadcastMessage(GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getChatColor() + GameRanks.getMatchingRank(Hide.stats.getPoints(player)).getName() +" " + Hide.rankManager.getRankColor(player) + player.getName() + " §8» §f" + msg);
			} 			
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
