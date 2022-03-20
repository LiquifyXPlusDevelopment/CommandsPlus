package dev.gdalia.commandsplus.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.PunishmentType;

public class PlayerChatListener implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player e = event.getPlayer();
		if(Permission.PERMISSION_CHAT.hasPermission(e)) return;
		if (Main.getInstance().getConfig().getBoolean("chat.locked")) {
			event.setCancelled(true);
			Message.CHAT_LOCKED.sendMessage(e, true);
			return;
		}
		
		Punishments.getInstance().getActivePunishment(e.getUniqueId(), PunishmentType.MUTE, PunishmentType.TEMPMUTE).ifPresent(punishment -> {
			event.setCancelled(true);
			Message.MUTED_MESSAGE.sendMessage(e, true);
		});
	}
}
