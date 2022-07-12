package dev.gdalia.commandsplus.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;

public class StaffChatListener implements Listener{

	@EventHandler
	public void onSendMessage(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String msg = event.getMessage();

		if(!Permission.PERMISSION_STAFFCHAT.hasPermission(player)) return;
		if (!PlayerCollection.getStaffchatPlayers().contains(uuid)) return;
			event.setCancelled(true);
			Bukkit.getOnlinePlayers().stream()
			.filter(Permission.PERMISSION_STAFFCHAT_SEE::hasPermission)
			.forEach(staff -> 
					staff.sendMessage(Message.staffChatFormat().replace("{player}", player.getName()).replace("{message}", msg)));
			
	}
}
