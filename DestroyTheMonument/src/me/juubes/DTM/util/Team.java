package me.juubes.DTM.util;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Team {
	private TeamID id;
	private String name;
	public boolean lost = false;
	public Location spawn;
	public Monument[] monuments = new Monument[0];
	public ArrayList<Player> members = new ArrayList<Player>();

	public Team(TeamID id, Monument[] monuments, Location spawn) {
		this.id = id;
		if (monuments != null)
			this.monuments = monuments;
		name = id.name();
		this.spawn = spawn;
	}

	public void purge() {
		for (Player p : members) {
			members.remove(p);
		}
	}

	public void respawn() {
		for (Player p : members) {
			ItemUtil.respawn(p, this);
		}
	}

	public TeamID getID() {
		return id;
	}

	public String getName() {
		return name;
	}
}
