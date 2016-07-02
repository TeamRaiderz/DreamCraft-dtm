package me.juubes.DTM.util;

import static me.juubes.DTM.Handler.abort;
import static me.juubes.DTM.Handler.abortReason;
import static me.juubes.DTM.Options.*;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.juubes.DTM.GameUtil;
import me.juubes.DTM.Handler;
import me.juubes.DTM.Options;

public class ConfigUtil {
	public static File confFile;
	public static FileConfiguration conf;

	public static int gamesPlayed = 0;
	public static int gamesBeforeRestart = 1;

	public static void initConfig() {
		try {
			confFile = new File(pl.getDataFolder(), "config.yml");
			conf = YamlConfiguration.loadConfiguration(confFile);
			gamesBeforeRestart = conf.getInt("games-before-restart");
		} catch (Exception e) {
			System.out.println("An error occurred while loading the config.yml!");
		}
	}

	public static boolean loadNextGameworld(boolean firstRun) {
		if (firstRun) {
			mainLobby = SpawnUtil.getMainLobby();
			mapNames = conf.getStringList("GameWorlds");
			if (mapNames == null)
				createNewConfig();
			if (mapNames.isEmpty())
				return false;
			if (mainLobby == null) {
				Bukkit.broadcastMessage("§eThe Main Lobby is not set! Must not be in a gameworld.");
				abort = true;
				abortReason = "The Main Lobby is not set.";
			} else if (mapNames.contains(mainLobby.getWorld().getName())) {
				abort = true;
				abortReason = "The Main Lobby is in a gameworld.";
			}
			Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Handler(), 20, 20);
			gameNumber = 0;
		} else {
			gameNumber++;
		}

		if (gameNumber == mapNames.size())
			gameNumber = 0;

		currentMap = GameUtil.loadNextWorld(mapNames.get(gameNumber));

		Options.timer = 20;

		System.out.println("Gameworld '" + currentMap.name + "' loaded!");
		System.out.println(currentMap.teams.size() + " teams loaded!");
		System.out.println(currentMap.monuments.length + " monuments loaded!");

		if (currentMap.teams.size() < 2 && !abort) {
			abort = true;
			abortReason = "Teams for map '" + currentMap.name + "' are not set!";
		}
		if (currentMap.monuments.length < 2 && !abort) {
			abort = true;
			abortReason = "Monuments for map '" + currentMap.name + "' are not set!";
		}
		if (!ItemUtil.updateItemsForMap() && !abort) {
			abort = true;
			abortReason = "§eGameworld '" + currentMap.name + "' doesn't have a kit configured.";
		} else {
			pl.getLogger().info("Mapkit successfully updated.");
			pl.getLogger().info("Starting countdown for a new match...");
		}
		return true;
	}

	public static void createNewConfig() {
		new File(pl.getDataFolder(), "config.yml")
				.renameTo(new File(pl.getDataFolder(), "broken-config" + (int) (Math.random() * 100) + ".yml"));
		pl.saveDefaultConfig();
	}

	public static void saveConfig() {
		try {
			conf.save(confFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
