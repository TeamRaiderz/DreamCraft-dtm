package me.juubes.DTM.commands;

import static me.juubes.DTM.Options.currentMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.juubes.DTM.util.TeamID;
import me.juubes.DTM.util.TeamUtil;

public class AddteamCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
		if (!(sender instanceof Player))
			return false;
		Player p = (Player) sender;
		if (!p.hasPermission("DTM.admin"))
			return false;
		if (cmd.getName().equalsIgnoreCase("addteam")) {
			if (args.length == 0) {
				p.sendMessage("§eCorrect usage: /addteam <team>");
			} else if (args.length == 1) {
				for (TeamID team : TeamID.values()) {
					if (team.name().equalsIgnoreCase(args[0])) {
						if (!currentMap.teams.contains(TeamUtil.getTeam(team))) {
							TeamUtil.addTeam(team, p.getWorld().getName(), p.getLocation());
							sender.sendMessage(
									"§eTeam §7" + team + " §eadded for world " + p.getWorld().getName() + "!");
							sender.sendMessage("§eTeam's spawn set to your location.");
						} else {
							sender.sendMessage("§eThe team §7" + team + " is already selected for the map.");
						}
						return true;
					}
				}
			} else {
				for (TeamID team : TeamID.values()) {
					if (team.name().equalsIgnoreCase(args[1])) {
						if (!currentMap.teams.contains(TeamUtil.getTeam(team))) {
							TeamUtil.addTeam(team, args[2]);
							sender.sendMessage("§eTeam §7" + team + " §eadded!");
						} else {
							sender.sendMessage("§eThe team §7" + team + " is already selected for the map.");
						}
						return true;
					}
				}
			}
			sender.sendMessage("§cInvalid team.");
		} else if (cmd.getName().equalsIgnoreCase("removeteam")) {
			if (args.length == 0) {
				p.sendMessage("§eCorrect usage: /removeteam <team>");
			} else if (args.length == 1) {
				for (TeamID team : TeamID.values()) {
					if (team.name().equalsIgnoreCase(args[0])) {
						if (currentMap.teams.contains(TeamUtil.getTeam(team))) {
							TeamUtil.removeTeam(team, p.getWorld().getName());
							sender.sendMessage("§eTeam §7" + team.name() + "§e removed.");
						} else {
							sender.sendMessage("§eThe team '" + team + "' is already selected for the map.");
						}
						return true;
					}
				}
				p.sendMessage("§cInvalid team.");
			} else {
				for (TeamID team : TeamID.values()) {
					if (team.name().equalsIgnoreCase(args[0])) {
						if (currentMap.teams.contains(TeamUtil.getTeam(team))) {
							TeamUtil.removeTeam(team, args[1]);
							sender.sendMessage("§eTeam §7" + team.name() + "§e removed.");
						} else {
							sender.sendMessage("§eThe team '" + team + "' is already selected for the map.");
						}
						return true;
					}
				}
				p.sendMessage("§cInvalid team.");
			}
		}
		return true;
	}
}
