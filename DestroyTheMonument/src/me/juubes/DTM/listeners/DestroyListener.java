package me.juubes.DTM.listeners;

import static me.juubes.DTM.Options.currentMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.juubes.DTM.ScoreboardHandler;
import me.juubes.DTM.StatisticManager;
import me.juubes.DTM.util.ItemUtil;
import me.juubes.DTM.util.Monument;
import me.juubes.DTM.util.Team;
import me.juubes.DTM.util.TeamUtil;

public class DestroyListener implements Listener {
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		for (Team t : currentMap.teams) {
			for (Monument mon : t.monuments) {
				if (!mon.destroyed && b.equals(mon.block)) {
					if (mon.team != TeamUtil.getTeam(p).getID()) {
						Bukkit.broadcastMessage(mon.name + " §ehas been destroyed by " + p.getName() + "!");
						mon.destroyed = true;
						ScoreboardHandler.updateScoreboard();
						b.setType(Material.AIR);
						StatisticManager.addStat(p.getName(), SNum.MONSBROKEN);
					} else {
						e.setCancelled(true);
						p.sendMessage("§eThis is your own monument. You can't destroy it.");
					}
				}
			}
		}
	}

	@EventHandler
	public void onBreak2(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		for (Team t : currentMap.teams) {
			if (b.getLocation().distance(t.spawn) < 1.5) {
				e.setCancelled(true);
				p.sendMessage("§eToo close to the spawn.");
			}
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Item item = e.getItemDrop();
		if (!ItemUtil.isAllowedToDrop(item.getItemStack()))
			item.remove();

	}

	@EventHandler
	public void onBreak2(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		for (Team t : currentMap.teams) {
			if (b.getLocation().distance(t.spawn) < 5) {
				e.setCancelled(true);
				p.sendMessage("§cYou can't build this close to the spawn!");
			}
		}
	}
}
