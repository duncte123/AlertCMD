package nl.duncte123.dhg.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import nl.duncte123.dhg.Dhg;

public class DhgCmd implements CommandExecutor {

	private Dhg plugin;

	public DhgCmd(Dhg pl) {
		plugin = pl;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		if (!(sender instanceof Player)) {
			// sender.sendMessage(ChatColor.DARK_RED + "you must be a player to
			// do this");
			if (args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "/alert <message> or /alert reload");
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					plugin.reloadConfig();
					sender.sendMessage("[alertCMD] config reloaded");
					plugin.onDisable();
					plugin.onEnable();
					sender.sendMessage("[alertCMD] plugin reloaded");
				}else{
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + " " + args[0]));
				}
			}else {
					String all = "";
					for(int i=0; i<args.length; i++){
						all = all + args[i] + " ";
					}
					
					//Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + " " + args[0]));
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', 
							plugin.getConfig().getString("prefix") + " " + all));
				} 
			return true;
		}

		Player player = (Player) sender;
		if (player.isOp() || player.hasPermission(new Permission("alertCMD.send"))) {
			if (args.length == 0) {
				player.sendMessage(ChatColor.DARK_RED + "/alert <message> or /alert reload");
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload")) {
					plugin.reloadConfig();
					player.sendMessage("[alertCMD] config reloaded");
					plugin.onDisable();
					plugin.onEnable();
					sender.sendMessage("[alertCMD] plugin reloaded");
				}else{
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + " " + args[0]));
				}
			} else {
				String all = "";
				for(int i=0; i<args.length; i++){
					all = all + args[i] + " ";
				}
				
				//Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("prefix") + " " + args[0]));
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', 
						plugin.getConfig().getString("prefix") + " " + all));
			}
		}
		return true;
	}
}
