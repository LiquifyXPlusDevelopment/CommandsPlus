package me.gdalia.commandsplus.commands;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.gdalia.commandsplus.Main;
import me.gdalia.commandsplus.models.Punishments;
import me.gdalia.commandsplus.structs.Message;
import me.gdalia.commandsplus.structs.PunishmentType;
import me.gdalia.commandsplus.utils.StringUtils;
import me.gdalia.commandsplus.utils.Utils;

@me.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "check")
public class CheckCommand implements CommandExecutor {

	private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
			.ofPattern("d MMM uuuu")
			.withZone(ZoneId.systemDefault());

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!sender.hasPermission("commandsplus.check")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}

		if (args.length == 0) {
			Message.DESCRIBE_PLAYER.sendMessage(sender, true);
			return true;
		}

		@SuppressWarnings("deprecation")
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (!target.hasPlayedBefore()) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}
		
		Punishments.getInstance().getActivePunishment(target.getUniqueId()).ifPresentOrElse(activePunish -> {
			Message.PLAYER_ACTIVE_PUNISHMENT.sendMessage(sender, true);
			ConfigurationSection cs = Main.getPunishmentsConfig().getConfigurationSection(activePunish.getPunishmentUniqueId().toString());
			cs.getValues(false).entrySet().forEach(entry -> {
				if (entry.getValue() instanceof String stringValue) {
					if (StringUtils.isUniqueId(stringValue)) {
						String name = Bukkit.getOfflinePlayer(UUID.fromString(stringValue)).getName();
						sender.sendMessage(Utils.color("&e" + entry.getKey() + "&7: &b" + name));
						return;
					}
					
					if (PunishmentType.canBeType(stringValue)) {
						PunishmentType type = PunishmentType.valueOf(stringValue);
						sender.sendMessage(Utils.color("&e" + entry.getKey() + "&7: &b" + type.getDisplayName()));
						return;
					}
					
					sender.sendMessage(Utils.color("&e" + entry.getKey() + "&7: &b" + stringValue));
					return;
					
				} else if (entry.getValue() instanceof Long longValue) {
					Instant expiry = Instant.ofEpochMilli(longValue);
					String endDate = DATE_TIME_FORMATTER.format(expiry);
					sender.sendMessage(Utils.color("&e" + entry.getKey() + "&7: &b" + endDate));
					return;
					
				}
			});
		}, () -> Message.PLAYER_NO_ACTIVE_PUNISHMENT.sendMessage(sender, true));
		return true;
	}

}
