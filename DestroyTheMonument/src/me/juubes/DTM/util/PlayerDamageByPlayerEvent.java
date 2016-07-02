package me.juubes.DTM.util;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageByPlayerEvent extends EntityDamageByEntityEvent {
	@Deprecated
	public PlayerDamageByPlayerEvent(Entity damager, Entity damaged, DamageCause cause, double damage) {
		super(damager, damaged, cause, damage);
	}
}
