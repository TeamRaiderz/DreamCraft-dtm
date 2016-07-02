package me.juubes.DTM;

import static me.juubes.DTM.Options.pl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class StatisticManager {
	public static List<Statistic> stats = new ArrayList<>();
	public static FileConfiguration statConf;
	public static File folder;

	public StatisticManager() {
		folder = new File(pl.getDataFolder(), "stats");
	}

	public static void unloadStats(String name) {
		if (!Options.statsEnabled)
			return;
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(new File(folder, name + ".yml"));
		Stat stat = getStats(name);
		stats.remove(stat.getStats());
		conf.set("stats", stat.getStats());

		try {
			conf.save(new File(folder, name + ".yml"));
		} catch (IOException e) {
		}
	}

	public static void addStat(String name, int statNumber) {
		int i = (int) StatisticManager.getStats(name).getStats()[statNumber];
		i++;
		getStats(name).getStats()[statNumber] = i;
	}

	public static Statistic getStats(String name) {
		for (Statistic stat : stats) {
			if (stat.getName().equalsIgnoreCase(name)) {
				return stat;
			}
		}
		return new Statistic(name, null);
	}

	public static void loadStats(String name) {
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(new File(folder, name + ".yml"));
		List<Integer> objList = conf.getIntegerList("stats");
		stats.add(new Statistic(name, objList.toArray(new Integer[5])));
	}
}
