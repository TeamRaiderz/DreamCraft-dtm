package me.juubes.DTM.util;

import static me.juubes.DTM.Options.currentMap;
import static me.juubes.DTM.util.ConfigUtil.conf;
import static me.juubes.DTM.util.ConfigUtil.saveConfig;
import static org.bukkit.Color.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeamUtil {

	public static Team getTeam(Player p) {
		for (Team team : currentMap.teams) {
			if (team.members.contains(p))
				return team;
		}
		return null;
	}

	public static void joinTeam(Player p, TeamID team) {
		if (team == null) {
			List<Team> tempTeams = new ArrayList<>();
			tempTeams.addAll(currentMap.teams);
			tempTeams.sort(new Comparator<Team>() {
				public int compare(Team team1, Team team2) {
					if (team1.members.size() < team2.members.size())
						return -1;
					else if (team1.members.size() > team2.members.size())
						return 1;
					else
						return 0;
				};
			});
			currentMap.teams.get(currentMap.teams.indexOf(tempTeams.get(0))).members.add(p);
		} else {
			if (getTeam(p) != null) {
				try {
					throw new Exception("Player is already in a team");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				getTeam(team).members.add(p);
			}
		}

		p.setCustomName(getTeamChatColor(getTeam(p).getID()) + p.getName());
		p.setDisplayName(p.getCustomName());
		p.setPlayerListName(p.getCustomName());
	}

	public static Color getTeamColor(TeamID team) {
		if (team == TeamID.AQUA)
			return AQUA;
		if (team == TeamID.PURPLE)
			return PURPLE;
		if (team == TeamID.BLACK)
			return BLACK;
		if (team == TeamID.BLUE)
			return BLUE;
		if (team == TeamID.GREEN)
			return GREEN;
		if (team == TeamID.RED)
			return RED;
		if (team == TeamID.WHITE)
			return WHITE;
		if (team == TeamID.YELLOW)
			return YELLOW;
		return null;
	}

	public static String getTeamChatColor(TeamID team) {
		String color;
		if (team == TeamID.PURPLE) {
			color = "§5";
		} else if (team == TeamID.CYAN) {
			color = "§b";
		} else if (team == TeamID.PINK) {
			color = "§d";
		} else {
			color = ChatColor.valueOf(team.name()) + "";
		}
		return color;
	}

	public static Team getTeam(TeamID team) {
		for (Team t : currentMap.teams) {
			if (t.getID() == team)
				return t;
		}
		return null;
	}

	public static void addTeam(TeamID team, String name) {
		currentMap.teams.add(new Team(team, null, null));
		conf.createSection("options.Maps." + name + ".Teams." + team.toString());
		saveConfig();
	}

	public static void addTeam(TeamID team, String name, Location spawn) {
		Team teamToAdd = new Team(team, null, spawn);
		teamToAdd.spawn = spawn;
		currentMap.teams.add(teamToAdd);
		conf.createSection("options.Maps." + name + ".Teams." + team.toString());
		SpawnUtil.setSpawn(team, spawn);
	}

	public static void removeTeam(TeamID team, String name) {
		currentMap.teams.remove(getTeam(team));
		conf.set("options.Maps." + name + ".Teams." + team, null);
	}
}
