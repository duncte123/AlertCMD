package nl.duncte123.dhg.events.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import nl.duncte123.dhg.Dhg;

public class PlayerJoin1_8 implements Listener {
	public static Dhg plugin;

	public PlayerJoin1_8(Dhg pl) {
		plugin = pl;
	}

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		if (!(event.getPlayer() instanceof Player)) {
			return;
		}
		Player player = event.getPlayer();
		if (player.isOp()) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					player.sendMessage(plugin.config.getString("prefix") + ChatColor.RED + "Hey you a new version of customcraft is available, you can now download version " + Dhg.version + "! " + "yours " + plugin.pdfFile.getVersion());
					player.sendMessage(ChatColor.GREEN + "Click on the link to get it: " + ChatColor.BLUE + ChatColor.ITALIC + Dhg.updateChecker.getLink());
				}
			}, 200);

		} /*
			 * else{ return; }
			 */
	}
}
