package me.juubes.DTM;

import java.util.Date;
import java.util.List;

import org.bukkit.Location;

import me.juubes.DTM.util.Map;

public class Options {
	public static final int RUNNING = 1, WAITING = 0;

	public static DTM pl;
	public static int gameNumber = 0, state = 0, timer;
	public static Date timePlayed = new Date(-7200000);
	public static Location mainLobby;

	public static List<String> mapNames;
	public static Map currentMap;

	public static boolean statsEnabled = false;

}