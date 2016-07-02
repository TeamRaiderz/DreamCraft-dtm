package me.juubes.DTM.commands;

import static me.juubes.DTM.Options.currentMap;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.juubes.DTM.util.MonumentID;
import me.juubes.DTM.util.SpawnUtil;
import me.juubes.DTM.util.Team;

public class SetmonumentCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!(sender instanceof Player))
			return false;
		if (!sender.hasPermission("DTM.admin"))
			return false;
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("setmonument")) {
			if (args.length == 0) {
				p.sendMessage("§eCorrect usage: /setmonument <team> <position>");
			} else if (args.length == 1) {
				p.sendMessage("§eCorrect usage: /setmonument <team> <position>");
			} else if (args.length == 2) {
				for (Team team : currentMap.teams) {
					if (args[0].equalsIgnoreCase(team.getID().name())) {
						for (MonumentID id : MonumentID.values()) {
							if (args[1].equalsIgnoreCase(id.name())) {
								SpawnUtil.setMonument(team.getID(), id, p.getTargetBlock((Set<Material>) null, 20));
								sender.sendMessage("§eMonument §7" + team.getID().name() + " " + id.name() + " §eset.");
								return true;
							}
						}
						sender.sendMessage("§eThis map doesn't have a monument called §7" + args[1].toUpperCase() + " "
								+ args[1].toUpperCase());
						return true;
					}
				}
				sender.sendMessage("§eThis map doesn't have a team called §7" + args[1].toUpperCase());
			}
		}
		return true;
	}
}
