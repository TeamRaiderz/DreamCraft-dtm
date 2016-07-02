package me.juubes.DTM.commands;

import static me.juubes.DTM.Options.currentMap;
import static me.juubes.DTM.Options.timer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.juubes.DTM.DTM;
import me.juubes.DTM.GameUtil;
import me.juubes.DTM.Handler;
import me.juubes.DTM.util.Monument;
import me.juubes.DTM.util.Team;

public class DTMCommand implements CommandExecutor {
	DTM pl;

	public DTMCommand(DTM dtm) {
		pl = dtm;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!sender.hasPermission("DTM.admin"))
			return false;
		if (cmd.getName().equalsIgnoreCase("dtm")) {
			if (args.length == 0) {
				sender.sendMessage("§eCorrect usage: /dtm debug");
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("debug")) {
					try {
						sender.sendMessage("");
						sender.sendMessage("§eStarting time: §7" + timer);
						sender.sendMessage("§ePlayed time: §7" + Handler.getFormattedTime());
						sender.sendMessage("§eCurrent map: §7" + currentMap.name);
						sender.sendMessage("§eTeams:");
						for (Team t : currentMap.teams) {
							sender.sendMessage(" §eTeam §7" + t.getID().name());
						}
						sender.sendMessage("§eLobby set: §7" + (currentMap.lobby == null ? false : true));
						sender.sendMessage("§eMonuments:");
						for (Monument mon : currentMap.monuments) {
							sender.sendMessage(" §eMonument " + mon.name);
						}
					} catch (Exception e) {
						sender.sendMessage("");
					}
				} else if (args[0].equalsIgnoreCase("restart")) {
					sender.sendMessage("§eManually restarting the server...");
					GameUtil.restart();
				} else {
					sender.sendMessage("Invalid argument 1");
				}

			}
		}
		return true;
	}
}
