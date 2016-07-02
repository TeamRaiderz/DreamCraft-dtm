package me.juubes.DTM.util;

import static me.juubes.DTM.Options.currentMap;
import static me.juubes.DTM.util.ConfigUtil.conf;
import static me.juubes.DTM.util.ConfigUtil.saveConfig;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import me.juubes.DTM.Options;

public class SpawnUtil {

	public static Location getLocationFromConfig(String path, World w) {
		double X = 0, Y, Z;
		float yaw, pitch;
		try {
			X = (double) conf.get(path + ".X");
		} catch (Exception e) {
			return null;
		}
		Y = (double) conf.get(path + ".Y");
		Z = (double) conf.get(path + ".Z");
		yaw = (float) conf.getDouble(path + ".Yaw");
		pitch = (float) conf.getDouble(path + ".Pitch");
		return new Location(w, X, Y, Z, yaw, pitch);
	}

	public static Block getMonumentLocation(String path, World w) {
		int X = 0, Y, Z;
		try {
			X = (int) conf.get(path + ".X");
		} catch (Exception e) {
			return null;
		}
		Y = (int) conf.get(path + ".Y");
		Z = (int) conf.get(path + ".Z");
		return w.getBlockAt(X, Y, Z);

	}

	public static void setSpawn(TeamID team, Location loc) {
		String wName = loc.getWorld().getName();
		if (team == null) {
			currentMap.lobby = loc;
			conf.set("options.Maps." + wName + ".Lobby.X", loc.getX());
			conf.set("options.Maps." + wName + ".Lobby.Y", loc.getY());
			conf.set("options.Maps." + wName + ".Lobby.Z", loc.getZ());
			conf.set("options.Maps." + wName + ".Lobby.Yaw", loc.getYaw());
			conf.set("options.Maps." + wName + ".Lobby.Pitch", loc.getPitch());
			saveConfig();
			return;
		}
		String name = team.name();
		try {
			// Updating the location of the Spawn
			for (Team PlayingTeams : currentMap.teams) {
				if (PlayingTeams.getID() == team) {
					PlayingTeams.spawn = loc;
				}
			}
		} catch (Exception e) {
			Bukkit.getLogger().severe("Error occurred updating " + name + " spawn for map " + wName + ".");
		}

		conf.set("options.Maps." + wName + ".Teams." + name + ".Spawn.X", loc.getX());
		conf.set("options.Maps." + wName + ".Teams." + name + ".Spawn.Y", loc.getY());
		conf.set("options.Maps." + wName + ".Teams." + name + ".Spawn.Z", loc.getZ());
		conf.set("options.Maps." + wName + ".Teams." + name + ".Spawn.Yaw", loc.getYaw());
		conf.set("options.Maps." + wName + ".Teams." + name + ".Spawn.Pitch", loc.getPitch());
		saveConfig();
	}

	public static void setMonument(TeamID teamm, MonumentID monID, Block block) {
		String wName = block.getWorld().getName();
		String team = teamm.name();
		String type = monID.name();
		conf.set("options.Maps." + wName + ".Teams." + team + ".Monuments." + type + ".X", block.getX());
		conf.set("options.Maps." + wName + ".Teams." + team + ".Monuments." + type + ".Y", block.getY());
		conf.set("options.Maps." + wName + ".Teams." + team + ".Monuments." + type + ".Z", block.getZ());
		saveConfig();
	}

	public static void setMainLobby(Location loc) {
		Options.mainLobby = loc;
		conf.set("options.MainLobby.World", loc.getWorld().getName());
		conf.set("options.MainLobby.X", loc.getX());
		conf.set("options.MainLobby.Y", loc.getY());
		conf.set("options.MainLobby.Z", loc.getZ());
		conf.set("options.MainLobby.Yaw", loc.getYaw());
		conf.set("options.MainLobby.Pitch", loc.getPitch());
		saveConfig();
		return;
	}

	public static Location getMainLobby() {
		double X = 0, Y, Z;
		float yaw, pitch;
		try {
			X = (double) conf.get("options.MainLobby.X");
		} catch (Exception e) {
			return null;
		}
		World world = Bukkit.getWorld(conf.getString("options.MainLobby.World"));
		if (world == null)
			return null;
		Y = (double) conf.get("options.MainLobby.Y");
		Z = (double) conf.get("options.MainLobby.Z");
		yaw = (float) conf.getDouble("options.MainLobby.Yaw");
		pitch = (float) conf.getDouble("options.MainLobby.Pitch");
		return new Location(world, X, Y, Z, yaw, pitch);
	}
}
