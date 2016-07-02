package me.juubes.DTM.util;

import static org.bukkit.Material.DIAMOND_PICKAXE;
import static org.bukkit.Material.DIAMOND_SWORD;

import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.juubes.DTM.Options;

public class ItemUtil {
	private static ItemStack[] itemsForMap;
	public static FileConfiguration mapkitConf;

	public static void equip(Player p, Team team) {
		PlayerInventory inv = p.getInventory();
		inv.clear();
		inv.setArmorContents(getArmour(team));
		for (int i = 0; i < itemsForMap.length; i++) {
			ItemStack item = itemsForMap[i];
			if (item != null)
				p.getInventory().setItem(i, item);
		}
	}

	public static void dropInventory(Player p) {
		for (ItemStack item : p.getInventory().getContents())
			if (isAllowedToDrop(item))
				Options.currentMap.world.dropItemNaturally(p.getLocation(), item);
		for (ItemStack item : p.getInventory().getArmorContents())
			if (isAllowedToDrop(item))
				Options.currentMap.world.dropItemNaturally(p.getLocation(), item);
	}

	public static boolean isAllowedToDrop(ItemStack item) {
		if (item != null)
			if (item.getType() != Material.AIR)
				if (item.getType() != DIAMOND_PICKAXE)
					if (item.getType() != DIAMOND_SWORD)
						if (item.getType() != Material.BOW)
							if (!item.getType().name().startsWith("LEATHER"))
								return true;
		return false;
	}

	public static void respawn(Player p, Team team) {
		if (team == null)
			team = TeamUtil.getTeam(p);
		p.setGameMode(GameMode.SURVIVAL);
		p.setExp(0);
		p.setFallDistance(0);
		p.setHealth(20);
		p.setFoodLevel(20);
		ItemUtil.equip(p, team);
		p.teleport(team.spawn);
	}

	private static ItemStack[] getArmour(Team team) {
		ItemStack[] items = new ItemStack[] { new ItemStack(Material.LEATHER_BOOTS),
				new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_CHESTPLATE),
				new ItemStack(Material.LEATHER_HELMET) };
		for (ItemStack item : items) {
			LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
			meta.setDisplayName("§eTeam Armor");
			meta.setColor(TeamUtil.getTeamColor(team.getID()));
			meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
			item.setItemMeta(meta);

		}
		return items;
	}

	@SuppressWarnings("unchecked")
	public static boolean updateItemsForMap() {
		List<ItemStack> rawList = (List<ItemStack>) mapkitConf.getList(Options.currentMap.name + ".default");
		if (rawList == null)
			return false;
		itemsForMap = (ItemStack[]) rawList.toArray(new ItemStack[rawList.size()]);
		return true;
	}
}
