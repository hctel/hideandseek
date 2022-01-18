package be.hctel.renaissance.hideandseek.commands.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import be.hctel.renaissance.hideandseek.gamespecific.enums.TauntType;

public class TauntCommandTabCompleter implements TabCompleter {
	static List<String> tabs = new ArrayList<String>();
	static {
		for(TauntType T : TauntType.values()) {
			tabs.add(T+ "");
		}
	}
	List<String> empty = new ArrayList<String>();
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(args.length == 1) return tabs;
		else return empty;
	}

}
