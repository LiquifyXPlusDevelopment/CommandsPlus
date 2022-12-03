package dev.gdalia.commandsplus.listeners;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import dev.gdalia.commandsplus.models.ReportManager;
import dev.gdalia.commandsplus.structs.punishments.PunishmentGUI;
import dev.gdalia.commandsplus.structs.reports.ReportComment;
import dev.gdalia.commandsplus.inventory.InventoryUtils;
import dev.gdalia.commandsplus.ui.CommentsUI;
import dev.gdalia.commandsplus.ui.PunishUI;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.utils.StringUtils;
import org.bukkit.scheduler.BukkitRunnable;

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
							cancel();
						}
					}.runTaskTimer(Main.getInstance(), 1, 1);
					event.setCancelled(true);
				});

		Optional.of(InventoryUtils.getInstance().timeText.containsKey(e.getUniqueId()))
				.map(punishment -> InventoryUtils.getInstance().timeText.get(e.getUniqueId()))
				.ifPresent(x -> {
					new BukkitRunnable() {
						@Override
						public void run() {
							Duration duration;
							duration = StringUtils.phraseToDuration(event.getMessage(),
									ChronoUnit.SECONDS, ChronoUnit.MINUTES,
									ChronoUnit.HOURS, ChronoUnit.DAYS,
									ChronoUnit.WEEKS, ChronoUnit.MONTHS,
									ChronoUnit.YEARS);

							Instant expiry = Instant.now().plus(duration);

							x.setExpiry(expiry);
							new PunishUI(e.getPlayer()).openReasonGUI(x.getPunished(), x);
							InventoryUtils.getInstance().timeText.remove(e.getUniqueId());
							Message.playSound(e, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

							e.resetTitle();
							cancel();
						}
					}.runTaskTimer(Main.getInstance(), 1, 1);
					event.setCancelled(true);
				});

		Optional.of(InventoryUtils.getInstance().reasonText.containsKey(e.getUniqueId()))
				.map(punishment -> InventoryUtils.getInstance().reasonText.get(e.getUniqueId()))
				.ifPresent(x -> {
					new BukkitRunnable() {
						@Override
						public void run() {
							x.setReason(event.getMessage());
							new PunishUI(e.getPlayer()).invokeInitializePunishGUI(x.getPunished(), x);
							InventoryUtils.getInstance().reasonText.remove(e.getUniqueId());
							Message.playSound(e, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

							e.resetTitle();
							cancel();
						}
					}.runTaskTimer(Main.getInstance(), 1, 1);
					event.setCancelled(true);
				});

		Punishments.getInstance().getActivePunishment(e.getUniqueId(), PunishmentType.MUTE, PunishmentType.TEMPMUTE).ifPresent(punishment -> {
			Message.playSound(e, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			event.setCancelled(true);
			
			if (punishment.getType().equals(PunishmentType.MUTE)) {
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
