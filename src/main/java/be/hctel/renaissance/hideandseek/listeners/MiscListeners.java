package be.hctel.renaissance.hideandseek.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class MiscListeners implements Listener {
	
	@EventHandler
	public void onEggThrow(PlayerEggThrowEvent e) {
		e.setHatching(false);
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e) {
		switch(e.getEntityType()) {
		case FALLING_BLOCK:
		case SHEEP:
		case CREEPER:
		case BAT:
		case TNT:
		case EGG:
		case FIREWORK_ROCKET:
		case SPLASH_POTION:
			break;
		default:
			e.setCancelled(true);
			break;
		}
	}
}
