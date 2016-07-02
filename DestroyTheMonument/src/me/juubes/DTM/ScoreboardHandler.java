package me.juubes.DTM;

import static me.juubes.DTM.util.TeamUtil.*;
import static me.juubes.DTM.Options.currentMap;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import me.juubes.DTM.util.Monument;

public class ScoreboardHandler {
	private static Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

	public static void updateScoreboard() {
		Objective obj = sb.getObjective("monuments");
		if (obj != null)
			obj.unregister();
		obj = sb.registerNewObjective("monuments", "dummy");
		String good = "§a" + (char) 10004;
		String bad = "§c" + (char) 10008;
		int r = (int) (Math.random() * 100 + 1);

		List<Score> scores = new ArrayList<>();
		Monument[] monuments = currentMap.monuments;
		scores.add(obj.getScore("      " + getTeamChatColor(monuments[0].team) + "§l" + monuments[0].team));
		int spacer = 0;
		for (int i = 0; i < monuments.length; i++) {
			Monument mon = monuments[i];
			if (mon.destroyed) {
				scores.add(obj.getScore(bad + "    " + getTeamChatColor(mon.team) + mon.id));
			} else {
				scores.add(obj.getScore(good + "    " + getTeamChatColor(mon.team) + mon.id));
			}

			if (i + 1 < monuments.length) {
				if (mon.team != monuments[i + 1].team) {
					if (currentMap.teams.size() < 4)
						scores.add(obj.getScore(getSpacer(spacer)));
					spacer++;
					if (getTeam(monuments[i + 1].team).lost) {
						scores.add(obj.getScore("      §4" + monuments[i + 1].team));
					} else {
						scores.add(obj.getScore(
								"      " + getTeamChatColor(monuments[i + 1].team) + "§l" + monuments[i + 1].team));
					}
				}
			}
		}

		// Setting the scores in order
		for (int i = 0; i < scores.size(); i++) {
			scores.get(i).setScore(-i + r);
		}

		obj.setDisplayName("§eMonuments");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		// Making sure players see the scoreboard
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.setScoreboard(sb);
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
		}

		GameUtil.testForLostTeams();
	}

	private static String getSpacer(int number) {
		String ready = "";
		while (number > 0) {
			ready += " ";
			number--;
		}
		return ready;
	}

	public static Scoreboard getScoreboard() {
		return sb;
	}
}
