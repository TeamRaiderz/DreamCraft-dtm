package me.juubes.DTM;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.juubes.DTM.commands.*;
import me.juubes.DTM.listeners.*;
import me.juubes.DTM.util.ConfigUtil;
import me.juubes.DTM.util.ItemUtil;

public class DTM extends JavaPlugin {

	public void onEnable() {
		// Generating the config
		if (!(new File(getDataFolder(), "config.yml").exists()))
			saveDefaultConfig();

		// Initializing classes
		SetlobbyCommand lobCom = new SetlobbyCommand();
		DTMCommand com = new DTMCommand(this);
		AddteamCommand teamcom = new AddteamCommand();
		MapkitCommand mapkit = new MapkitCommand();
		Options.pl = this;
		ConfigUtil.initConfig();
		Options.statsEnabled = false;
		// ConfigUtil.conf.getBoolean("enable-stats");

		ItemUtil.mapkitConf = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "mapkits.yml"));

		if (!ConfigUtil.loadNextGameworld(true)) {
			System.out.println("--------------------------------------------------------------");
			System.out.println(" DTM disabling! You have to configure the gaming worlds first.");
			System.out.println(" If you want the default config, delete the old one.");
			System.out.println("--------------------------------------------------------------");
		} else {
			// Listening to events
			PluginManager pm = Bukkit.getPluginManager();
			pm.registerEvents(new ConnectingListener(), this);
			pm.registerEvents(new DestroyListener(), this);
			pm.registerEvents(new AttackListener(), this);
			pm.registerEvents(new DeathListener(), this);
			if (ConfigUtil.conf.getBoolean("format-chat"))
				pm.registerEvents(new ChatFormatting(), this);

		}
		// Getting the commands
		getCommand("setspawn").setExecutor(lobCom);
		getCommand("DTM").setExecutor(com);
		getCommand("cworld").setExecutor(new ChangeworldCommand());
		getCommand("mapkit").setExecutor(mapkit);
		getCommand("setmainlobby").setExecutor(lobCom);
		getCommand("setlobby").setExecutor(lobCom);
		getCommand("addteam").setExecutor(teamcom);
		getCommand("removeteam").setExecutor(teamcom);
		getCommand("setmonument").setExecutor(new SetmonumentCommand());
		if (Options.statsEnabled) {
			getCommand("stats").setExecutor(new StatsCommand());
			for (Player p : Bukkit.getOnlinePlayers()) {
				StatisticManager.loadStats(p.getName());
			}
		}
	}

	public void onDisable() {
		if (Options.currentMap != null)
			Options.currentMap.unload();
		for (Player p : Bukkit.getOnlinePlayers()) {
			StatisticManager.unloadStats(p.getName());
		}

	}
}