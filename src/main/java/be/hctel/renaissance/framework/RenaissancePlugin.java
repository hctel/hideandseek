package be.hctel.renaissance.framework;

import org.bukkit.plugin.Plugin;

public interface RenaissancePlugin {
	public void onEnable();
	public void onDisable();
	public String getHeader();
	public Plugin getPlugin();
}
