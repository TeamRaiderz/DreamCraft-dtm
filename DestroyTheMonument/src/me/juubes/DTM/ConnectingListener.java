package me.juubes.DTM;

import static me.juubes.DTM.Options.currentMap;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.juubes.DTM.util.ItemUtil;
import me.juubes.DTM.util.Team;
import me.juubes.DTM.util.TeamUtil;

public class ConnectingListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		Player p = e.getPlayer();
		p.setScoreboard(ScoreboardHandler.getScoreboard());
		p.getInventory().clear();
		if (currentMap.lobby != null)
			p.teleport(currentMap.lobby);
		if (currentMap.teams.size() == 0)
			return;
		if (Options.state == Options.RUNNING) {
			TeamUtil.joinTeam(p, null);
			ItemUtil.respawn(p, null);
			p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
		} else {
			p.setGameMode(GameMode.SPECTATOR);
			p.teleport(currentMap.lobby);
			p.setCustomName("§7" + p.getName());
			p.setDisplayName(p.getCustomName());
			p.setPlayerListName(p.getCustomName());
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		for (Team t : currentMap.teams) {
			if (t.members.contains(p)) {
				t.members.remove(p);
			}
		}
		e.setQuitMessage(null);
	}
}