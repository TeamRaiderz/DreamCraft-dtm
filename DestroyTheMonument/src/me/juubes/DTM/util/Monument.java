package me.juubes.DTM.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Monument {
	public static final Material MONUMENTTYPE = Material.OBSIDIAN;

	public boolean destroyed = false;
	public Block block;
	public TeamID team;
	public MonumentID id;
	public String name;

	public Monument(Block block, TeamID team, MonumentID id) {
		this.block = block;
		this.team = team;
		this.id = id;
		this.name = TeamUtil.getTeamChatColor(team) + "§l" + team.name() + " " + id.name();
	}

	public void respawn() {
		block.setType(MONUMENTTYPE);
		destroyed = false;
	}
}
