/*******************************************************************************
 * This file is part of AlertCMD.
 *
 *     AlertCMD is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     AlertCMD is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with AlertCMD.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package nl.duncte123.dhg;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.gravitydevelopment.updater.Updater;
import net.gravitydevelopment.updater.Updater.UpdateType;
import nl.duncte123.dhg.commands.DhgCmd;
import nl.duncte123.dhg.events.player.PlayerJoin;

public class Dhg extends JavaPlugin {
	public final Logger Logger = getLogger();
	public static Updater updater;
	public final FileConfiguration config = getConfig();
	public static String version;
	public static String url;
	public final PluginManager pm = getServer().getPluginManager();
	static Dhg plugin;
	public boolean updateDone = false;
	public PluginDescriptionFile pdfFile = getDescription();

	public void onDisable() {
		Logger.info(pdfFile.getName() + " Has Been Disabled!");
		Bukkit.getServer().getScheduler().cancelTasks(this);
		Bukkit.getServer().getScheduler().cancelAllTasks();
		HandlerList.unregisterAll(this);
	}

	public void onEnable() {
		saveDefaultConfig();
		RegisterCommands();
		
		Logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
		getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

			public void run() {
				updater();
				
				saveDefaultConfig();
				configFile();
				
				RegisterCommands();
			}
		}, 20);
	}

	public void RegisterCommands() {
		getCommand("broadcast").setExecutor(new DhgCmd(this));
		if(this.config.getBoolean("useAlertCmd")){
			getCommand("alert").setExecutor(new DhgCmd(this));
		}
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
		updater = new Updater(this, 98580, getFile(), UpdateType.NO_DOWNLOAD, true);
		if (updater.shouldUpdate(pdfFile.getVersion(), updater.getLatestName().replaceAll("[a-zA-Z ]", ""))) {
			version = updater.getLatestName().replaceAll("[a-zA-Z ]", "");
			url = updater.getLatestFileLink();
			Logger.info("");
			Logger.info("#################################################################");
			Logger.info("A new version is available: " + version + " yours " + pdfFile.getVersion());
			//Logger.info("You can get it from: " + updater.getLatestFileLink());
			Logger.info("You can get it from:  http://dev.bukkit.org/bukkit-plugins/alertcmd");
			Logger.info("#################################################################");
			Logger.info("");
			playerdone();
		} else {
			Logger.info("no update available");
		}
	}

	public void playerdone() {
		if (!updateDone) {
			pm.registerEvents(new PlayerJoin(this), this);
		} 
			updateDone = true;
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
