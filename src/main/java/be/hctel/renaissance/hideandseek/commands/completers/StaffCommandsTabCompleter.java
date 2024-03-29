package be.hctel.renaissance.hideandseek.commands.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import be.hctel.renaissance.hideandseek.gamespecific.enums.GameMap;

public class StaffCommandsTabCompleter implements TabCompleter {
	ArrayList<String> out = new ArrayList<String>();
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		out.clear();
		if(command.getName().equalsIgnoreCase("updateprofile")) {
			if(args.length == 1) for(Player P : Bukkit.getOnlinePlayers()) out.add(P.getName());
			if(args.length == 2) {
				out.add("points");
				out.add("victories");
				out.add("gamesplayed");
				out.add("killsasseeker");
				out.add("killsashider");
				out.add("deaths");
				out.add("blockunlock");
				out.add("blockexperience.");
				out.add("blocklevel.");
				out.add("achievements.");
			}
			if(args.length == 3) out.add("<value>");
			return out;
		}
		else if(command.getName().equalsIgnoreCase("gotoworld")) {
			if(args.length == 1) {
				for(GameMap M : GameMap.values()) {
					out.add(M.getSystemName());
				}
			}
			return out;
		}
		return null;
	}

}
