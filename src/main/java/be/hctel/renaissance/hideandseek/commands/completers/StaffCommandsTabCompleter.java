package be.hctel.renaissance.hideandseek.commands.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class StaffCommandsTabCompleter implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if(command.getName().equalsIgnoreCase("updateprofile")) {
			ArrayList<String> out = new ArrayList<String>();
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
		return null;
	}

}
