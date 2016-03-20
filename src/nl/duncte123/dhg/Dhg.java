package nl.duncte123.dhg;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import nl.duncte123.dhg.commands.DhgCmd;
import nl.duncte123.dhg.events.player.PlayerJoin;
import nl.duncte123.dhg.events.player.PlayerJoin1_8;
import nl.duncte123.dhg.events.player.PlayerJoin1_9;

public class Dhg extends JavaPlugin {
	public final Logger Logger = getLogger();
	public static UpdateChecker updateChecker;
	public final FileConfiguration config = getConfig();
	public static String version;
	public final PluginManager pm = getServer().getPluginManager();
	static Dhg plugin;
	public boolean updateDone = false;
	public PluginDescriptionFile pdfFile = getDescription();

	public void onDisable() {
		Logger.info(pdfFile.getName() + " Has Been Disabled!");
	}

	public void onEnable() {
		RegisterCommands();
		saveDefaultConfig();
		Logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			public void run() {
				updater();
				configFile();
			}
		}, 20);
	}

	public void RegisterCommands() {
		getCommand("alert").setExecutor(new DhgCmd(this));
	}

	public void updater() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				updatececkfunk();
			}
		}, 0, 20 * 60 * 60);
	}

	public void updatececkfunk() {
		Logger.info("checking for updates...");
		updateChecker = new UpdateChecker(this, "http://dev.bukkit.org/bukkit-plugins/alertcmd/files.rss");
		if (updateChecker.updateNeeded()) {
			if (!(updateChecker.getVersion().equals(""))) {
				version = updateChecker.getVersion();
			} else {
				version = "(version not available)";
			}
			Logger.info("");
			Logger.info("#################################################################");
			Logger.info("A new version is available: " + version + "yours " + pdfFile.getVersion());
			Logger.info("You can get it from: " + updateChecker.getLink());
			Logger.info("#################################################################");
			Logger.info("");
			pm.registerEvents(new PlayerJoin(this), this);
		} else {
			Logger.info("no update available");
		}
	}

	public void playerdone() {
		if (updateDone) {
			// DO nothing
		} else {
			if (Bukkit.getServer().getVersion().contains("1.8")) {
				pm.registerEvents(new PlayerJoin1_8(this), this);
			} else {
				pm.registerEvents(new PlayerJoin1_9(this), this);
			}

			updateDone = true;
		}
	}

	public void configFile() {
		PluginDescriptionFile pdfFile = getDescription();

		String configVersion = config.getString("version");
		// Logger.info(configVersion);
		// Logger.info("DEBUG: config ver length " +
		// configVersion.split("\\.").length);
		// Ignore last digit if it is 4 digits long
		if (configVersion.split("\\.").length == 4) {
			configVersion = configVersion.substring(0, configVersion.lastIndexOf('.'));
		}
		// Save for this version
		String version = pdfFile.getVersion();
		// Logger.info("DEBUG: version length " + version.split("\\.").length);
		if (version.split("\\.").length == 4) {
			version = version.substring(0, version.lastIndexOf('.'));
		}
		if (configVersion.isEmpty() || !(configVersion.equalsIgnoreCase(version))) {
			// Check to see if this has already been shared
			File newConfig = new File(this.getDataFolder(), "config.new.yml");
			Logger.warning("***********************************************************");
			Logger.warning("Config file is out of date. See config.new.yml for updates!");
			Logger.warning("set your settings in the config.new.yml");
			Logger.warning("delete the config.yml");
			Logger.warning("and rename config.new.yml to config.yml");
			Logger.warning("");
			Logger.warning("config.yml version is '" + configVersion + "'");
			Logger.warning("Latest config version is '" + version + "'");
			Logger.warning("***********************************************************");
			if (!(newConfig.exists())) {
				File oldConfig = new File(this.getDataFolder(), "config.yml");
				File bakConfig = new File(this.getDataFolder(), "config.bak");
				if (oldConfig.renameTo(bakConfig)) {
					this.saveResource("config.yml", false);
					oldConfig.renameTo(newConfig);
					bakConfig.renameTo(oldConfig);
				}
			}
		}
	}
}
