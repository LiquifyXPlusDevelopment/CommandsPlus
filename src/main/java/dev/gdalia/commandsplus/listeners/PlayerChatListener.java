package dev.gdalia.commandsplus.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.PunishmentType;
import dev.gdalia.commandsplus.utils.StringUtils;

public class PlayerChatListener implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player e = event.getPlayer();
		if(Permission.PERMISSION_CHAT.hasPermission(e)) return;
		if (Main.getInstance().getConfig().getBoolean("chat.locked")) {
			Message.playSound(e, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			event.setCancelled(true);
			Message.CHAT_LOCKED.sendMessage(e, true);
			return;
		}
		
		Punishments.getInstance().getActivePunishment(e.getUniqueId(), PunishmentType.MUTE, PunishmentType.TEMPMUTE).ifPresent(punishment -> {
			Message.playSound(e, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			event.setCancelled(true);
			if (punishment.getType() == PunishmentType.MUTE) {
				Message.MUTED_MESSAGE.sendMessage(e, true);
			}
			Message.TEMPMUTED_MESSAGE.sendFormattedMessage(e, true, StringUtils.createTimeFormatter(punishment.getExpiry(), "HH:mm, dd/MM/uu"));
		});
	}
}
