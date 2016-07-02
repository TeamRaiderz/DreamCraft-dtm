package me.juubes.DTM.commands;

import static me.juubes.DTM.Options.pl;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.juubes.DTM.Options;
import me.juubes.DTM.util.ItemUtil;

public class MapkitCommand implements CommandExecutor {

	public MapkitCommand() {

	}

	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!(sender instanceof Player))
			return false;
		if (!sender.hasPermission("DTM.admin"))
			return false;
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("mapkit") && sender.hasPermission("DTM.mapkit")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("save")) {
					if (mapExists(p.getWorld().getName())) {
						saveMapkit(p.getInventory().getContents(), p.getWorld().getName());
						sender.sendMessage("§eMapkit for map '" + p.getWorld().getName() + "' saved!");
					} else {
						sender.sendMessage("§eThis world is not registered or loaded as a gameworld.");
						sender.sendMessage("§eList it in the config or enter another gameworld.");
						sender.sendMessage("§e/mapkit create <gameworld>");
					}
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("save")) {
					if (mapExists(args[1])) {
						saveMapkit(p.getInventory().getContents(), args[1]);
						sender.sendMessage("§eMapkit for map '" + args[1] + "' saved!");
					} else {
						sender.sendMessage("§eThis map doesn't exist!");
					}
				}
			} else {
				sender.sendMessage("§eToo. Much. Args.");
			}
		}
		return true;
	}

	private boolean mapExists(String name) {
		for (String s : Options.mapNames) {
			if (s.equals(name))
				return true;
		}
		return false;
	}

	private void saveMapkit(ItemStack[] items, String name) {
		ItemUtil.mapkitConf.set(name + ".default", items);
		try {
			ItemUtil.mapkitConf.save(new File(pl.getDataFolder(), "mapkits.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
