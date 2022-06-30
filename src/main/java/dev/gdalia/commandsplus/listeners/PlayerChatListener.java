package dev.gdalia.commandsplus.listeners;

import java.time.Duration;
import java.time.Instant;

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
		if (Main.getInstance().getConfig().getBoolean("chat.locked", true)) {
			if (!Permission.PERMISSION_CHAT.hasPermission(e)) return;
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
				return;
			}
			
		    Instant one = Instant.now();
		    Instant two = punishment.getExpiry();
		    Duration res = Duration.between(one, two);
			
			Message.TEMPMUTED_MESSAGE.sendFormattedMessage(e, true, StringUtils.formatTime(res));
		});
	}
}
