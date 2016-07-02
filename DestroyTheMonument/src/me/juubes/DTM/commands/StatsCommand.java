package me.juubes.DTM.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.juubes.DTM.StatisticManager;
import me.juubes.DTM.listeners.SNum;

public class StatsCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!sender.hasPermission("DTM.stats"))
			return false;
		if (args.length == 0) {
			showStats(sender.getName(), sender);
		} else {
			showStats(args[0], sender);
		}
		return true;
	}

	private static void showStats(String name, CommandSender sender) {
		sender.sendMessage("§e  " + name + "'s Stats");
		Integer[] stats = StatisticManager.getStats(name).getStats();
		sender.sendMessage("§eKills: " + stats[SNum.KILLS]);
		sender.sendMessage("§eDeaths: " + stats[SNum.DEATHS]);
		sender.sendMessage("§eK/D Ratio: " + (stats[SNum.DEATHS] == 0 ? 0 : (stats[SNum.KILLS] / stats[SNum.DEATHS])));
		sender.sendMessage("§eMonuments broken: " + stats[SNum.MONSBROKEN]);
		sender.sendMessage("§eMatch wins: " + stats[SNum.WINS]);
		sender.sendMessage(" ");
	}
}
