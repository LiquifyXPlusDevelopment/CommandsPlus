package dev.gdalia.commandsplus.commands;


import java.util.List;
import java.util.Optional;

import dev.gdalia.commandsplus.structs.Punishment;
import dev.gdalia.commandsplus.utils.CentredMessage;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.PunishmentType;
import dev.gdalia.commandsplus.utils.StringUtils;

@CommandAutoRegistration.Command(value = "history")
public class HistoryCommand implements CommandExecutor {
	
	/**
	 * /history {user}
	 * LABEL ARG0
	 */
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!Permission.PERMISSION_HISTORY.hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			Message.HISTORY_ARGUMENTS.sendMessage(sender, true);
			return true;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (!target.hasPlayedBefore()) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}

		List<Punishment> history = Punishments.getInstance().getHistory(target.getUniqueId());

		if (history.isEmpty()) {
			Message.HISTORY_DOES_NOT_EXIST.sendMessage(sender, true);
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			return true;
		}
		
		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		sender.sendMessage(CentredMessage.generate("&7&m&l               |&e " + target.getName() + " punish log &7&m&l|               &r"));
		history.forEach(punishment -> {
			sender.sendMessage(Message.fixColor("&ePunishment Id: &b" + punishment.getPunishmentUniqueId().toString()));
			Optional.ofNullable(punishment.getExecuter()).ifPresent(uuid -> sender.sendMessage(Message.fixColor("&eExecuted by: &b" + Bukkit.getOfflinePlayer(punishment.getExecuter()).getName())));
			sender.sendMessage(Message.fixColor("&eType: &b" + punishment.getType().getDisplayName()));

			if (!List.of(PunishmentType.KICK, PunishmentType.WARN).contains(punishment.getType())) {
				Optional.ofNullable(punishment.getExpiry()).ifPresentOrElse(instant -> {
					sender.sendMessage(Message.fixColor("&eIs permanent?: &b" + BooleanUtils.toStringYesNo(false)));
					sender.sendMessage(Message.fixColor("&eExpiry: &b" + StringUtils.createTimeFormatter(instant, "HH:mm, dd/MM/uu")));
				}, () -> sender.sendMessage(Message.fixColor("&eIs permanent?: &b" + BooleanUtils.toStringYesNo(true))));
			}

			sender.sendMessage(Message.fixColor("&eReason: &b" + punishment.getReason()));
			sender.sendMessage(CentredMessage.generate("&7&m&l                    x x                    &r"));
		});
		return true;
	}
}