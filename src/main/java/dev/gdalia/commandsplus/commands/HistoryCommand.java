package dev.gdalia.commandsplus.commands;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.gdalia.commandsplus.structs.Punishment;
import dev.gdalia.commandsplus.utils.CentredMessage;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.PunishmentType;
import dev.gdalia.commandsplus.utils.StringUtils;

@CommandAutoRegistration.Command(value = "history")
public class HistoryCommand implements CommandExecutor {
	
	/**
	 * /history {user}
	 * LABEL ARG0
	 */
	
	private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
				.ofPattern("d MMM uuuu")
				.withZone(ZoneId.systemDefault());
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!sender.hasPermission("commandsplus.history")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length == 0) {
			Message.HISTORY_ARGUMENTS.sendMessage(sender, true);
			return true;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (!target.hasPlayedBefore()) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}

		List<Punishment> history = Punishments.getInstance().getHistory(target.getUniqueId());

		if (history.isEmpty()) {
			Message.HISTORY_DOES_NOT_EXIST.sendMessage(sender, true);
			return true;
		}

		sender.sendMessage(CentredMessage.generate("&7&m&l               |&e " + target.getName() + " punish log &7&m&l|               &r"));
		history.forEach(punishment -> {
			sender.sendMessage(Message.fixColor("&ePunishment Id: &b" + punishment.getPunishmentUniqueId().toString()));
			Optional.ofNullable(punishment.getExecuter()).ifPresent(uuid -> sender.sendMessage(Message.fixColor("&eExecuted by: &b" + Bukkit.getOfflinePlayer(uuid).getName())));
			sender.sendMessage(Message.fixColor("&eType: &b" + punishment.getType().getDisplayName()));

			if (!List.of(PunishmentType.WARN, PunishmentType.KICK).contains(punishment.getType())) {
				Optional.ofNullable(punishment.getExpiry()).ifPresentOrElse(instant -> {
					sender.sendMessage(Message.fixColor("&eIs permanent?: &b" + BooleanUtils.toStringYesNo(false)));
					sender.sendMessage(Message.fixColor("&eExpiry: &b" + StringUtils.createTimeFormatter(instant, "HH:mm, dd/MM/uu")));
				}, () -> sender.sendMessage(Message.fixColor("&Is permanent?: &b" + BooleanUtils.toStringYesNo(true))));
			}

			sender.sendMessage(Message.fixColor("&eReason: &b" + punishment.getReason()));
			sender.sendMessage(CentredMessage.generate("&7&m&l                    x x                    &r"));
		});
		return true;
	}
}
