package me.juubes.DTM.listeners;

import static me.juubes.DTM.util.TeamUtil.getTeam;
import static me.juubes.DTM.util.TeamUtil.getTeamChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.juubes.DTM.Options;
import me.juubes.DTM.StatisticManager;
import me.juubes.DTM.util.ItemUtil;
import me.juubes.DTM.util.PlayerDamageByPlayerEvent;

public class AttackListener implements Listener {

	@Deprecated
	@EventHandler
	public void translateEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Bukkit.getPluginManager().callEvent(
					new PlayerDamageByPlayerEvent(e.getDamager(), e.getEntity(), e.getCause(), e.getDamage()));
		}
	}

	@EventHandler(ignoreCancelled = false)
	public void onAttack(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;
		Player target = (Player) e.getEntity();
		if (target.getHealth() - e.getFinalDamage() <= 0) {
			if (e instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) e;
				Entity damager = event.getDamager();
			} else {

			}
		}
	}

	@EventHandler
	public void onAttack(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player target = (Player) e.getEntity();
			if (target.getHealth() - e.getFinalDamage() <= 0) {
				if (e.getDamager() instanceof Player) {
					Player p = (Player) e.getDamager();
					if (getTeam(p) == getTeam(target)) {
						e.setCancelled(true);
						return;
					}
					e.setCancelled(true);
					ItemUtil.dropInventory(target);
					ItemUtil.respawn(target, getTeam(target));
					if (target.getName() == p.getName())
						return;
					StatisticManager.addStat(target.getName(), SNum.DEATHS);
					StatisticManager.addStat(p.getName(), SNum.KILLS);
					Bukkit.broadcastMessage(getTeamChatColor(getTeam(target).getID()) + target.getName()
							+ "§e has been killed by " + getTeamChatColor(getTeam(p).getID()) + p.getName());
				} else if (e.getDamager() instanceof Arrow) {
					Arrow arrow = (Arrow) e.getDamager();
					if (arrow.getShooter() == null)
						return;

					if (arrow.getShooter() instanceof Player) {
						Player shooter = (Player) arrow.getShooter();
						if (getTeam(target) == getTeam((Player) arrow.getShooter())) {
							e.setCancelled(true);
						} else {
							e.setCancelled(true);
							ItemUtil.dropInventory(target);
							ItemUtil.respawn(target, getTeam(target));
							StatisticManager.addStat(target.getName(), SNum.DEATHS);
							StatisticManager.addStat(shooter.getName(), SNum.KILLS);
							Bukkit.broadcastMessage(getTeamChatColor(getTeam(target).getID()) + target.getName()
									+ "§e was shot to death by " + getTeamChatColor(getTeam(shooter).getID())
									+ shooter.getName());
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (Options.state == Options.WAITING) {
			e.setCancelled(true);
			return;
		}
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (p.getLocation().getY() < 0 || p.getHealth() - e.getFinalDamage() <= 0) {
				e.setCancelled(true);
				ItemUtil.dropInventory(p);
				ItemUtil.respawn(p, getTeam(p));
				StatisticManager.addStat(p.getName(), SNum.DEATHS);
				try {
					Player damager = (Player) p.getLastDamageCause().getEntity();

					if (e.getCause() == DamageCause.VOID) {
						if (damager.getName() != p.getName()) {
							Bukkit.broadcastMessage(getTeamChatColor(getTeam(p).getID()) + p.getName()
									+ "§e was knocked out of the world by " + getTeamChatColor(getTeam(damager).getID())
									+ damager.getName());
						} else {
							Bukkit.broadcastMessage(
									getTeamChatColor(getTeam(p).getID()) + p.getName() + "§e fell into the void");
						}
					}
				} catch (Exception ex) {
					System.out.println(
							"Psst: Jos näät tän tekstin consoles kerro juubesile että joku kusi classis AttackListener, rivi 74");
				}
			}
		}
	}
}
