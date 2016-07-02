package me.juubes.DTM.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import me.juubes.DTM.Options;

public class Map {
	public String name;
	public World world;
	public Location lobby;
	public Monument[] monuments;
	public List<Team> teams = new ArrayList<>();

	public Map(World w, String name, List<Team> teams, Location lobby) {
		this.name = name;
		this.world = w;
		this.lobby = lobby;

		if (teams != null)
			this.teams = teams;

	}

	public Map(String s) {
		this(null, s, null, null);
	}

	public void load() {
		System.out.println("Loading gameworld " + name + "...");
		new WorldCreator(name).createWorld();
		world = Bukkit.getWorld(name);
	}

	public void unload() {
		if (name == null)
			System.out.println(this);
		world = Bukkit.getWorld(name);
		if (world != null) {
			for (Player p : world.getPlayers()) {
				p.teleport(Options.mainLobby);
			}
			Bukkit.unloadWorld(name, false);
		}
	}
}
