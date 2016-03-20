package nl.duncte123.dhg.events.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;

import nl.duncte123.dhg.Dhg;

public class PlayerJoin implements Listener {
	
	private Dhg plugin;
	public PlayerJoin(Dhg pl){
		plugin = pl;
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event){
		if(!(event.getPlayer() instanceof Player)){
			return;
		}
		Player player = event.getPlayer();
		if(player.isOp() || player.hasPermission(new Permission("alertCMD.*"))){
			player.sendMessage(plugin.config.getString("prefix") + ChatColor.RED + "Hey you a new version of alertCMD you can now download version " + Dhg.version + "!");
			player.sendMessage(plugin.config.getString("prefix") + ChatColor.GREEN + "You can get it from: " + ChatColor.YELLOW + Dhg.updateChecker.getLink());
		}else{
			return;
		}
	}
}
