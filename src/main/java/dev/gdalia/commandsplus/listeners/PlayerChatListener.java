package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.inventory.InventoryUtils;
import dev.gdalia.commandsplus.models.punishmentdrivers.FlatFilePunishments;
import dev.gdalia.commandsplus.models.ReportManager;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.structs.reports.ReportComment;
import dev.gdalia.commandsplus.ui.CommentsUI;
import dev.gdalia.commandsplus.utils.StringUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class PlayerChatListener implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player e = event.getPlayer();
		if (Main.getInstance().getConfig().getBoolean("chat.locked", false)) {
			if (Permission.PERMISSION_CHAT.hasPermission(e)) return;
			Message.playSound(e, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			event.setCancelled(true);
			Message.CHAT_LOCKED.sendMessage(e, true);
			return;
		}

		Optional.of(InventoryUtils.getInstance().commentText.containsKey(e.getUniqueId()))
				.map(report -> InventoryUtils.getInstance().commentText.get(e.getUniqueId()))
				.ifPresent(x -> {
					new BukkitRunnable() {
						@Override
						public void run() {
							ReportManager.getInstance().addComment(x, new ReportComment(e, Instant.now(), event.getMessage()));
							new CommentsUI(e.getPlayer()).openCommentsGUI(InventoryUtils.getInstance().commentText.get(e.getUniqueId()));
							InventoryUtils.getInstance().commentText.remove(e.getUniqueId());
							Message.playSound(e, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

							e.resetTitle();
						}
					}.runTask(Main.getInstance());

					event.setCancelled(true);
				});

		FlatFilePunishments.getInstance().getActivePunishment(e.getUniqueId(), PunishmentType.MUTE, PunishmentType.TEMPMUTE).ifPresent(punishment -> {
			Message.playSound(e, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			event.setCancelled(true);
			
			if (punishment.getType().equals(PunishmentType.MUTE)) {
				Message.PUNISH_MUTE_CHAT_DENIED.sendMessage(e, true);
				return;
			}
			
		    Instant one = Instant.now();
		    Instant two = punishment.getExpiry();
		    Duration res = Duration.between(one, two);
			
			Message.PUNISH_TEMPMUTE_CHAT_DENIED.sendFormattedMessage(e, true, StringUtils.formatTime(res));
		});
	}
}
