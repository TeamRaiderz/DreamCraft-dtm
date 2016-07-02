package me.juubes.DTM;

import static me.juubes.DTM.Options.*;
import static me.juubes.DTM.util.ConfigUtil.*;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.juubes.DTM.util.*;

public class Handler implements Runnable {
	public static boolean abort = false;
	public static String abortReason = null;

	@SuppressWarnings("deprecation")
	public static String getFormattedTime() {
		String time;
		if (timePlayed.getTime() > new Date(-7200000 + 59 * 60 * 1000).getTime())
			time = timePlayed.getHours() + " hours " + timePlayed.getMinutes() + " minutes and "
					+ timePlayed.getSeconds() + " seconds";
		else if (timePlayed.getTime() > new Date(-7200000 + 59 * 1000).getTime())
			time = timePlayed.getMinutes() + " minutes and " + timePlayed.getSeconds() + " seconds";
		else
			time = timePlayed.getSeconds() + " seconds";
		return time;
	}

	@SuppressWarnings("deprecation")
	public void run() {
		if (state == RUNNING) {
			timePlayed = new Date(timePlayed.getTime() + 1000);
			if (timePlayed.getTime() % (1000 * 60 * 5) == 0) {
				Bukkit.broadcastMessage("§eTime played: " + timePlayed.getMinutes() + " minutes");
			}
		}
		if (timer < 0)
			return;
		if (timer == 0) {
			try {
				startGame();
			} catch (Exception e) {
				e.printStackTrace();
				Bukkit.broadcastMessage("§eStarting the game failed! Check the console. " + abortReason);
			}
		} else {
			if (timer % 10 == 0 || timer < 10)
				Bukkit.broadcastMessage("§eMatch starting in " + timer);
			if (currentMap.lobby != null) { // If null aborting already true
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getWorld().getName() != currentMap.world.getName()) {
						p.teleport(currentMap.lobby);
					}
				}
			}
		}
		timer--;
	}

	public static void changeMap() {
		gamesPlayed++;
		if (gamesPlayed == gamesBeforeRestart) {
			GameUtil.restart();
			return;
		}

		if (mainLobby != null)
			for (Player p : currentMap.world.getPlayers())
				p.teleport(mainLobby);
		ConfigUtil.loadNextGameworld(false);
	}

	public static void startGame() {
		if (abort) {
			Bukkit.broadcastMessage("§eGame aborting! Reason: \n  " + abortReason);
			return;
		}
		for (Player p : Bukkit.getOnlinePlayers()) {
			TeamUtil.joinTeam(p, null);
		}
		for (Team team : currentMap.teams) {
			team.respawn();
			for (Monument mon : team.monuments) {
				mon.respawn();
			}
		}

		ScoreboardHandler.updateScoreboard();
		state = RUNNING;
	}
}
