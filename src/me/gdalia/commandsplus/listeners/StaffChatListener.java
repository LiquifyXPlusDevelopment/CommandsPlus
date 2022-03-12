package me.gdalia.commandsplus.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.gdalia.commandsplus.Main.PlayerCollection;
import me.gdalia.commandsplus.structs.Message;

public class StaffChatListener implements Listener{

	@EventHandler
	public void onSendMessage(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		String msg = event.getMessage();
		
		if(!player.hasPermission("commandsplus.staffchat")) return;
		if (!PlayerCollection.getStaffchatPlayers().contains(uuid)) return;
			event.setCancelled(true);
			Bukkit.getOnlinePlayers().stream()
			.filter(staff -> staff.hasPermission("commandplus.staffchat.see"))
			.forEach(staff -> 
					staff.sendMessage(Message.staffChatFormat().replace("{player}", player.getName()).replace("{message}", msg)));
			
	}
}
