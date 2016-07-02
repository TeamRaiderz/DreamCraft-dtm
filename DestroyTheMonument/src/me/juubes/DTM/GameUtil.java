package me.juubes.DTM;

import static me.juubes.DTM.Handler.abort;

import static me.juubes.DTM.Handler.abortReason;
import static me.juubes.DTM.Options.*;
import static me.juubes.DTM.util.ConfigUtil.*;
import static me.juubes.DTM.util.TeamUtil.*;
import static me.juubes.DTM.util.SpawnUtil.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.juubes.DTM.listeners.SNum;
import me.juubes.DTM.util.*;
import net.md_5.bungee.api.ChatColor;

public class GameUtil {

	public static Map loadNextWorld(String mapName) {
		final String mapPath = "options.Maps." + mapName;

		List<Team> teamList = new ArrayList<>();

		// Loading the world
		World w = new WorldCreator(mapName).createWorld();
		w.save();
		w.setAutoSave(false);

		// Loading the teams
		ConfigurationSection teamSection = conf.getConfigurationSection(mapPath + ".Teams");
		if (teamSection != null) {
			if (teamSection.getKeys(false).size() < 2 && !abort) {
				Bukkit.broadcastMessage("§eYou have to add atleast two teams per map.");
				abort = true;
				abortReason = "Map contains less than two teams.";
			}

			for (String teamName : teamSection.getKeys(false)) {
				List<Monument> monumentList = new ArrayList<>();
				final String teamPath = "options.Maps." + mapName + ".Teams." + teamName;
				ConfigurationSection monumentSec = conf.getConfigurationSection(teamPath + ".Monuments");
				if (monumentSec != null) {
					if (!abort && monumentSec.getKeys(false).size() < 2) {
						Bukkit.broadcastMessage("§eYou have to have atleast one monument per team.");
						abort = true;
						abortReason = "Map contains less than two monuments";
					}

					for (String monumentName : monumentSec.getKeys(false)) {
						final String monumentPath = teamPath + ".Monuments." + monumentName;
						monumentList.add(new Monument(getMonumentLocation(monumentPath, w), TeamID.valueOf(teamName),
								MonumentID.valueOf(monumentName)));
					}
				} else {
					Bukkit.broadcastMessage("§eMonuments for team §7" + teamName + "§e are not set.");
				}

				Location spawn = getLocationFromConfig(teamPath + ".Spawn", w);
				if (spawn == null) {
					Bukkit.broadcastMessage("§eSpawn for team §7" + teamName + "§e is not set.");
				}
				teamList.add(new Team(TeamID.valueOf(teamName), monumentList.toArray(new Monument[monumentList.size()]),
						spawn));
			}
		} else {
			Bukkit.broadcastMessage("§eTeams for map '" + mapName + "' are not set.");
		}

		Location lobby = getLocationFromConfig(mapPath + ".Lobby", w);
		if (lobby == null) {
			Bukkit.broadcastMessage("§eLobby for map '" + mapName + "' is not set.");
			abort = true;
			abortReason = "Lobby for map '" + mapName + "' is not set.";
		}
		if (currentMap != null)
			currentMap.unload();
		Map map = new Map(w, mapName, teamList, lobby);
		ArrayList<Monument> mons = new ArrayList<>();
		for (Team t : map.teams) {
			for (Monument mon : t.monuments)
				mons.add(mon);
		}
		map.monuments = mons.toArray(new Monument[mons.size()]);
		return map;
	}

	public static void restart() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
			int time = 60;

			public void run() {
				if (time == 0) {
					Bukkit.broadcastMessage("§e               RESTARTING");
					for (Player p : Bukkit.getOnlinePlayers()) {
						p.kickPlayer(ChatColor.translateAlternateColorCodes('&', conf.getString("restart-message")));
					}
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
							conf.getString("restart-command").replace("/", ""));
				} else {
					if (time % 10 == 0 || time < 10)
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
								conf.getString("restart-countdown").replace("{SECONDS}", "" + time)));
					time--;
				}
			}
		}, 20, 20);
	}

	public static void testForLostTeams() {
		// Testing for lost teams
		for (Team t : currentMap.teams) {
			if (isLosing(t) && !t.lost) {
				t.lost = true;
				if (currentMap.teams.size() == 2) {
					Team winner;
					if (currentMap.teams.get(0) == t) {
						winner = currentMap.teams.get(1);
					} else {
						winner = currentMap.teams.get(0);
					}
					Bukkit.broadcastMessage("§b§m--------------------------------------");
					Bukkit.broadcastMessage(
							getTeamChatColor(winner.getID()) + winner.getName() + "§a§l team won the match!");
					Bukkit.broadcastMessage("§b§m--------------------------------------");
					for (Player p : winner.members) {
						StatisticManager.addStat(p.getName(), SNum.WINS);
					}
				} else {
					Team teamLeft = oneTeamLeft();
					if (teamLeft == null) {
						Bukkit.broadcastMessage("§b§m--------------------------------------");
						Bukkit.broadcastMessage(
								getTeamChatColor(t.getID()) + t.getName() + "§c team has lost the match!");
						Bukkit.broadcastMessage("§b§m--------------------------------------");
						return;
					} else {
						Bukkit.broadcastMessage("§b§m--------------------------------------");
						Bukkit.broadcastMessage(
								getTeamChatColor(teamLeft.getID()) + teamLeft.getName() + "§a§l team won the match!");
						Bukkit.broadcastMessage("§b§m--------------------------------------");
						for (Player p : teamLeft.members) {
							StatisticManager.addStat(p.getName(), SNum.WINS);
						}
					}
				}
				timePlayed = new Date(-7200000);
				state = WAITING;
				for (Player p : Bukkit.getOnlinePlayers())
					p.setGameMode(GameMode.SPECTATOR);
				Bukkit.getScheduler().scheduleSyncRepeatingTask(Options.pl, new Runnable() {
					int count = 30;

					public void run() {
						if (count == -1)
							return;
						if (count == 0) {
							Handler.changeMap();
							count--;
						} else {
							if (count % 10 == 0 || count <= 5)
								for (Player p : Bukkit.getOnlinePlayers()) {
									p.sendMessage("§eNext match starting in " + count);
								}
							count--;
						}
					}
				}, 20, 20);
			}
		}
	}

	private static Team oneTeamLeft() {
		List<Team> teams = new ArrayList<>();
		for (Team t : currentMap.teams) {
			if (!t.lost)
				teams.add(t);
		}
		if (teams.size() == 1) {
			return teams.get(0);
		}
		return null;
	}

	private static boolean isLosing(Team team) {
		for (Monument mon : team.monuments) {
			if (!mon.destroyed)
				return false;
		}
		return true;
	}
}
