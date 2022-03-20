package dev.gdalia.commandsplus.commands;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
		
		Punishments.getInstance().getHistory(target.getUniqueId()).forEach(punishment -> {
			ConfigurationSection cs = Main.getPunishmentsConfig().getConfigurationSection(punishment.getPunishmentUniqueId().toString());
			cs.getValues(false).entrySet().forEach(entry -> {
				if (entry.getValue() instanceof String stringValue) {
					if (StringUtils.isUniqueId(stringValue)) {
						String name = Bukkit.getOfflinePlayer(UUID.fromString(stringValue)).getName();
						sender.sendMessage(Message.fixColor("&e" + entry.getKey() + "&7: &b" + name));
						return;
					}
					
					if (PunishmentType.canBeType(stringValue)) {
						PunishmentType type = PunishmentType.valueOf(stringValue);
						sender.sendMessage(Message.fixColor("&e" + entry.getKey() + "&7: &b" + type.getDisplayName()));
						return;
					}
					
					sender.sendMessage(Message.fixColor("&e" + entry.getKey() + "&7: &b" + stringValue));
					return;
					
				} else if (entry.getValue() instanceof Long longValue) {
					Instant expiry = Instant.ofEpochMilli(longValue);
					String endDate = DATE_TIME_FORMATTER.format(expiry);
					sender.sendMessage(Message.fixColor("&e" + entry.getKey() + "&7: &b" + endDate));
					return;
					
				} else if (entry.getValue() instanceof Boolean boolValue) {
					sender.sendMessage(Message.fixColor("&e" + entry.getKey() + "&7: &b" + BooleanUtils.toStringYesNo(boolValue)));
					return;
				}
			});
		});
		return true;
	}
}
