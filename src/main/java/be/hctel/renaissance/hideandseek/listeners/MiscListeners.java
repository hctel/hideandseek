package be.hctel.renaissance.hideandseek.listeners;

import org.bukkit.entity.EntityType;
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
		if(e.getEntityType() != EntityType.DROPPED_ITEM && e.getEntityType() != EntityType.FALLING_BLOCK && e.getEntityType() != EntityType.SHEEP && e.getEntityType() != EntityType.CREEPER && e.getEntityType() != EntityType.BAT) e.setCancelled(true);
	}
}
