package me.juubes.DTM.commands;

import static me.juubes.DTM.Options.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.juubes.DTM.util.SpawnUtil;
import me.juubes.DTM.util.Team;
import me.juubes.DTM.util.TeamID;

public class SetlobbyCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
		if (!(sender instanceof Player))
			return false;
		if (!sender.hasPermission("DTM.admin"))
			return false;
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("setmainlobby")) {
			if (mapNames.contains(p.getWorld().getName())) {
				p.sendMessage("§eThis is a gameworld. You can't set the mainlobby here.");

			} else {
				SpawnUtil.setMainLobby(p.getLocation());
				p.sendMessage("§eMain lobby set.");
			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("setlobby")) {
			if (mapNames.contains(p.getWorld().getName())) {
				SpawnUtil.setSpawn(null, p.getLocation());
				sender.sendMessage("§eLobby for the map set to your location.");
			} else {
				sender.sendMessage("§eThis world is not listed as a gameworld.");
			}
		} else if (cmd.getName().equalsIgnoreCase("setspawn")) {
			if (args.length == 0) {

			} else if (args.length == 1) {
				for (Team team : currentMap.teams) {
					if (args[0].equalsIgnoreCase(team.getName())) {
						SpawnUtil.setSpawn(TeamID.valueOf(args[0].toUpperCase()), p.getLocation());
						sender.sendMessage("§e" + args[0].toUpperCase() + " spawn set to your location.");
						return true;
					}
				}
				sender.sendMessage("§cInvalid team.");
			}
		}
		return true;
	}

}
