package nl.duncte123.dhg.events.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import nl.duncte123.dhg.Dhg;

public class PlayerJoin1_9 implements Listener {
	public static Dhg plugin;

	public PlayerJoin1_9(Dhg pl) {
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
					player.sendMessage(ChatColor.RED
							+ "Hey you, a new version of customcraft is available, you can now download version "
							+ Dhg.version + "! " + "yours " + plugin.pdfFile.getVersion());
					// player.sendMessage(ChatColor.GREEN + "Click on the link
					// to get it: " + ChatColor.BLUE + ChatColor.ITALIC +
					// Customcraft.updateChecker.getLink());
					IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"" + "Click on the link to get it: "
							+ "\",\"color\": \"green\",\"extra\":[{\"text\":\"" + Dhg.updateChecker.getLink()
							+ "\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\""
							+ Dhg.updateChecker.getLink() + "\"},\"color\": \"blue\", \"italic\": \"true\"}]}");

					PacketPlayOutChat packet = new PacketPlayOutChat(comp);
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
				}
			}, 200);

		} /*
			 * else{ return; }
			 */
	}
}
