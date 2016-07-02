package me.juubes.DTM.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.juubes.DTM.Options;
import me.juubes.DTM.util.ItemUtil;
import me.juubes.DTM.util.Team;
import me.juubes.DTM.util.TeamUtil;

public class DeathListener implements Listener {

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		Team t = TeamUtil.getTeam(p);
		if (t != null) {
			ItemUtil.respawn(p, t);
		} else {
			p.setGameMode(GameMode.SPECTATOR);
			e.setRespawnLocation(Options.currentMap.lobby);
		}
	}
}
