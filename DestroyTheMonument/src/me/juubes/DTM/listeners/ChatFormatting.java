package me.juubes.DTM.listeners;

import static me.juubes.DTM.util.TeamUtil.getTeam;
import static me.juubes.DTM.util.TeamUtil.getTeamChatColor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormatting implements Listener {
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		e.setFormat("<" + getTeamChatColor(getTeam(p).getID()) + p.getCustomName() + "§r> " + e.getMessage());
	}
}
