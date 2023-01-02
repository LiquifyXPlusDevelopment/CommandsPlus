package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.StringUtils;
import dev.gdalia.commandsplus.utils.chat.CentredMessage;
import dev.gdalia.commandsplus.utils.profile.Profile;
import dev.gdalia.commandsplus.utils.profile.ProfileManager;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CommandAutoRegistration.Command(value = "history")
public class HistoryCommand extends BasePlusCommand {

	public HistoryCommand() {
		super(false, "history");
	}

	@Override
	public String getDescription() {
		return "View player's punishment history.";
	}

	@Override
	public String getSyntax() {
		return "/history [player]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_HISTORY;
	}

	@Override
	public boolean isPlayerCommand() {
		return false;
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
		return null;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			sender.sendMessage(ChatColor.GRAY + getSyntax());
			return;
		}

		Player target = Bukkit.getPlayerExact(args[0]);
		if (target != null) {
			Profile tempProfile = new Profile(target.getUniqueId(), target.getName(), Instant.now(), null, null);
			historyAction(sender, tempProfile, false);
			return;
		}


		ProfileManager.getInstance().getProfileAsync(args[0]).whenComplete((profile, throwable) -> {
			if (throwable != null) {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				Message.PLAYER_NOT_EXIST.sendMessage(sender, true);
				return;
			}

			historyAction(sender, profile, true);
		});
	}

	private void historyAction(CommandSender sender, Profile profile, boolean async) {
		List<Punishment> history = Punishments.getInstance().getHistory(profile.playerUUID());
		if (history.isEmpty()) {
			Message.PUNISH_HISTORY_EMPTY.sendMessage(sender, true);
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			return;
		}

		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		sender.sendMessage(CentredMessage.generate("&7&m                    |&e " + profile.playerName() + " punish log &7&m|                    &r"));
		history.forEach(punishment -> {
			sender.sendMessage(Message.fixColor("&ePunishment Id: &b" + punishment.getPunishmentUniqueId().toString()));
			if (!async)
				Optional.ofNullable(punishment.getExecutor())
					.ifPresent(uuid -> sender.sendMessage(Message.fixColor("&eExecuted by: &b" + Bukkit.getOfflinePlayer(punishment.getExecutor()).getName())));
			else ProfileManager.getInstance().getProfile(punishment.getExecutor())
					.ifPresent(profileExecutor -> Message.fixColor("&eExecuted by: &b" + profileExecutor.playerName()));
			sender.sendMessage(Message.fixColor("&eType: &b" + punishment.getType().getDisplayName()));

			if (!List.of(PunishmentType.KICK, PunishmentType.WARN).contains(punishment.getType())) {
				Optional.ofNullable(punishment.getExpiry()).ifPresentOrElse(instant -> {
					sender.sendMessage(Message.fixColor("&eIs permanent?: &b" + BooleanUtils.toStringYesNo(false)));
					sender.sendMessage(Message.fixColor("&eExpiry: &b" + StringUtils.createTimeFormatter(instant, "HH:mm, dd/MM/uu")));
				}, () -> sender.sendMessage(Message.fixColor("&eIs permanent?: &b" + BooleanUtils.toStringYesNo(true))));
			}

			sender.sendMessage(Message.fixColor("&eReason: &b" + punishment.getReason()));
			sender.sendMessage(CentredMessage.generate("&7&m                              x x                              &r"));
		});
	}
}