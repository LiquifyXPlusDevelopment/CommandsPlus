package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.PunishmentType;
import dev.gdalia.commandsplus.utils.CentredMessage;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@CommandAutoRegistration.Command(value = "check")
public class CheckCommand implements CommandExecutor {

	/**
	 * /check {user}
	 * LABEL ARG0
	 */
	

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!Permission.PERMISSION_CHECK.hasPermission(player)) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(player, true);
			return true;
		}

		if (args.length == 0) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.DESCRIBE_PLAYER.sendMessage(player, true);
			return true;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (!target.hasPlayedBefore()) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.INVALID_PLAYER.sendMessage(player, true);
			return true;
		}
		
		if (Punishments.getInstance().getActivePunishment(target.getUniqueId(), PunishmentType.BAN, PunishmentType.TEMPBAN, PunishmentType.MUTE, PunishmentType.TEMPMUTE).orElse(null) == null) {
			Message.PLAYER_NO_ACTIVE_PUNISHMENT.sendMessage(player, true);
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			return false;
		}
		
		Punishments.getInstance().getActivePunishment(target.getUniqueId(), PunishmentType.BAN, PunishmentType.TEMPBAN, PunishmentType.MUTE, PunishmentType.TEMPMUTE).ifPresent(punishment -> {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			player.sendMessage(CentredMessage.generate("&7&m                    |&e " + target.getName() + " punish log &7&m|                    &r"));
			player.sendMessage(Message.fixColor("&ePunishment Id: &b" + punishment.getPunishmentUniqueId().toString()));
			Optional.ofNullable(punishment.getExecuter()).ifPresent(uuid -> player.sendMessage(Message.fixColor("&eExecuted by: &b" + Bukkit.getOfflinePlayer(punishment.getExecuter()).getName())));
			player.sendMessage(Message.fixColor("&eType: &b" + punishment.getType().getDisplayName()));
			
			Optional.ofNullable(punishment.getExpiry()).ifPresent(instant -> {
				
			    Instant one = Instant.now();
			    Instant two = punishment.getExpiry();
			    Duration res = Duration.between(one, two);
			player.sendMessage(Message.fixColor("&eExpiry: &b" + StringUtils.formatTime(res)));
			});

			player.sendMessage(Message.fixColor("&eReason: &b" + punishment.getReason()));
			player.sendMessage(CentredMessage.generate("&7&m                              x x                              &r"));
		});
		return true;
	}

}
