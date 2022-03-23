package dev.gdalia.commandsplus.commands;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.Punishment;
import dev.gdalia.commandsplus.structs.PunishmentType;
import dev.gdalia.commandsplus.utils.StringUtils;

@CommandAutoRegistration.Command(value = {"tempmute", "tempban"})
public class TempPunishCommand implements CommandExecutor {
	
	/**
	 * /tempmute {user} {time} {reason}
	 * LABEL ARG0 ARG1 ARG2+
	 */
	
	@SuppressWarnings("deprecation")
	@Override
    public boolean onCommand(CommandSender sender, Command cmd,
    		String label, String[] args) {
 	
		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		String typeString = cmd.getName().replace("TEMP", "").toUpperCase();
		PunishmentType type = PunishmentType.canBeType(typeString) ? PunishmentType.valueOf(typeString) : null;

		if (!Permission.valueOf("PERMISSION_" + type.name()).hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length <= 2) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			Message.valueOf(type.name() + "_ARGUMENTS").sendMessage(sender, true);
			return true;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		
        if (target == null) {
        	Message.INVALID_PLAYER.sendMessage(sender, true);
        	Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            return true;
        }

        Punishments.getInstance().getActivePunishment(target.getUniqueId(), PunishmentType.valueOf(type.name().toUpperCase()), PunishmentType.valueOf(type.name().replace("TEMP", "").toUpperCase())).ifPresentOrElse(punishment ->
    	Message.valueOf("PLAYER_" + type.getNameAsPunishMsg().toUpperCase()).sendMessage(sender, true), () -> {
			Duration duration;

			try {
				duration = StringUtils.phraseToDuration(args[1],
						ChronoUnit.SECONDS, ChronoUnit.MINUTES,
						ChronoUnit.HOURS, ChronoUnit.DAYS,
						ChronoUnit.WEEKS, ChronoUnit.MONTHS,
						ChronoUnit.YEARS);
			} catch (IllegalStateException ex1) {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
				Message.TEMPMUTE_ARGUMENTS.sendMessage(sender, true);
				return;
			}

			StringBuilder reasonBuilder = new StringBuilder();

			for (int i = 2; i < args.length; i++)
				reasonBuilder.append(args[i]);

			Instant expiry = Instant.now().plus(duration);

			UUID executer = sender instanceof Player requester ? requester.getUniqueId() : null;

			Punishment punishment = new Punishment(
					UUID.randomUUID(),
					target.getUniqueId(),
					executer,
					type,
					reasonBuilder.toString());

			punishment.setExpiry(expiry);

			PunishmentManager.getInstance().invoke(punishment);
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			String expiryAsString = StringUtils.createTimeFormatter(expiry, "HH:mm, dd/MM/uu");
			Message.valueOf("PLAYER_" + type.getNameAsPunishMsg().toUpperCase() + "_MESSAGE").sendFormattedMessage(sender, true, target.getName(), expiryAsString);
		});
        return true;
    }
}
