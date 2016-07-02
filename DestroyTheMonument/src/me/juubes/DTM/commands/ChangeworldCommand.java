package me.juubes.DTM.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeworldCommand implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!sender.hasPermission("DTM.admin"))
			return false;
		if (cmd.getName().equalsIgnoreCase("cworld")) {
			if (args.length == 1) {
				if (sender instanceof Player) {
					if (Bukkit.getWorlds().contains(Bukkit.getWorld(args[0]))) {
						((Player) sender).teleport(Bukkit.getWorld(args[0]).getSpawnLocation());
					} else {
						sender.sendMessage("§cWorld doesn't exist.");
					}
				}
			}
		}
		return true;
	}
}
