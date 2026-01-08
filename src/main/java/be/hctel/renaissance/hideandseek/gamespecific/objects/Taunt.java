package be.hctel.renaissance.hideandseek.gamespecific.objects;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import be.hctel.renaissance.hideandseek.Hide;
import be.hctel.renaissance.hideandseek.gamespecific.enums.TauntType;
import be.hctel.renaissance.hideandseek.nongame.utils.Utils;

public class Taunt {
	Player player;
	TauntType type;
	public Taunt(Player player, TauntType type) {
		this.player = player;
		this.type = type;
	}
	public int perform() {
		Hide.stats.addPoints(player, type.getPoints());
		Hide.gameEngine.addPoints(player, type.getPoints());
		Hide.stats.addBlockExperience(player, type.getPoints(), Hide.blockPicker.playerBlock.get(player));
		switch(type) {
		case BARK:
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WOLF_AMBIENT, 1.0f, 1.0f);
			break;
		case CREEPER:
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CREEPER_HURT, 1.0f, 1.0f);
			break;
		case GHAST:
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
			break;
		case ENDERMAN:
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0f, 1.0f);
			break;
		case WATER:
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_SPLASH, 1.0f, 0.5f);
			break;
		case PIG: 
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIG_AMBIENT, 1.0f, 1.0f);
			break;
		case DRAGON:
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
			break;
		case TNT:
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
			break;
		case EGGS:
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
			Random r = new Random();
			for(int i = 0; i < 4; i++) {
				Location loc = new Location(player.getWorld(), player.getLocation().getBlockX()+(r.nextDouble()-0.5), player.getLocation().getBlockY(),  player.getLocation().getBlockZ()+(r.nextDouble()-0.5));
				player.getWorld().spawnEntity(loc, EntityType.EGG);
			}
			break;
		case FLAME:
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 1.0f, 1.0f);
			player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10, 1.5, 1.5, 1.5);
			break;
		case LOVE:
			player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
			player.getWorld().spawnParticle(Particle.HEART, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 10, 1.5, 1.5, 1.5);
			break;
		case FIREWORKS:
			Utils.spawnFireworks(player.getLocation());
			break;
		case POTION:
			Random ra = new Random();
			for(int i = 0; i < 4; i++) {
				Location loc = new Location(player.getWorld(), player.getLocation().getBlockX()+(ra.nextDouble()-0.5), player.getLocation().getBlockY(),  player.getLocation().getBlockZ()+(ra.nextDouble()-0.5));
				player.getWorld().spawnEntity(loc, EntityType.SPLASH_POTION);
			}
			break;
		case BAT:
			for(int i = 0; i < 5; i++) {
				final Entity e = player.getWorld().spawnEntity(player.getLocation(), EntityType.BAT);
				player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BAT_AMBIENT, 1.0f, 1.0f);
				e.setInvulnerable(true);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Hide.plugin, new Runnable() {
					public void run() {
						e.getWorld().spawnParticle(Particle.CRIT, e.getLocation(), 5);
						e.getWorld().playSound(e.getLocation(), Sound.ENTITY_BAT_DEATH, 1.0f, 1.0f);
						e.remove();
					}
				}, 60L);
			}
			break;
		case FLY_CREEPER:
			final Creeper e1 = (Creeper) player.getWorld().spawnEntity(player.getLocation(), EntityType.CREEPER);
			e1.setPowered(true);
			e1.setGravity(false);
			e1.setVelocity(new Vector(0, 2, 0));
			e1.setCustomName("Buzz Buzz I'm a beeee");
			e1.setInvulnerable(true);
			e1.setSilent(true);
			e1.setAI(false);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Hide.plugin, new Runnable() {
				public void run() {
					e1.getWorld().playSound(e1.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
					e1.getWorld().spawnParticle(Particle.EXPLOSION, e1.getLocation(), 1);
					e1.remove();
				}
			}, 60L);
			break;
		case SHEEPER:
			final Sheep e2 = (Sheep) player.getWorld().spawnEntity(player.getLocation(), EntityType.SHEEP);
			e2.setColor(DyeColor.YELLOW);
			e2.setGravity(false);
			e2.setVelocity(new Vector(0, 2, 0));
			e2.setCustomName("Buzz Buzz I'm a beeee");
			e2.setInvulnerable(true);
			e2.setAI(false);
			e2.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 60, 1, false, false));
			e2.getLocation().getWorld().playSound(e2.getLocation(), Sound.ENTITY_SHEEP_AMBIENT, 1.0f, 1.0f);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Hide.plugin, new Runnable() {
				public void run() {
					e2.getWorld().playSound(e2.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
					e2.getWorld().spawnParticle(Particle.EXPLOSION, e2.getLocation(), 1, 1.5, 1.5, 1.5);
					e2.remove();
				}
			}, 60L);
			break;
		case TNT_TOSS:
			final Entity te1 = player.getWorld().spawnEntity(player.getLocation(),EntityType.TNT);
			te1.teleport(te1.getLocation().add(0, 1, 0));
			te1.setVelocity(getLaunchVector(player.getLocation(), 5));
			Bukkit.getScheduler().scheduleSyncDelayedTask(Hide.plugin, new Runnable() {
				public void run() {
					player.getWorld().spawnParticle(Particle.EXPLOSION, te1.getLocation(), 1);
					te1.remove();
				}
			}, 5L);
			break;
		default:
			break;
		}
		return type.getCooldown();
	}
	
	private Vector getLaunchVector(Location loc, int multiplier) {
		Vector initial = loc.getDirection();
		return new Vector(initial.getX()*multiplier, initial.getY()*multiplier, initial.getZ()*multiplier);
	}
}
