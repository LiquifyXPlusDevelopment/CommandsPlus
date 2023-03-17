package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.models.drivers.FlatFilePunishmentDao;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.StringUtils;
import dev.gdalia.commandsplus.utils.chat.CentredMessage;
import dev.gdalia.commandsplus.utils.profile.Profile;
import dev.gdalia.commandsplus.utils.profile.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CommandAutoRegistration.Command(value = "check")
public class CheckCommand extends BasePlusCommand {


	public CheckCommand() {
		super(false, "check");
	}

	@Override
	public String getDescription() {
		return "Checks if the target player currently is muted/banned.";
	}

	@Override
	public String getSyntax() {
		return "/check [player]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_CHECK;
	}

	@Override
	public boolean isPlayerCommand() {
		return false;
	}

	@Override
	public Map<Integer, List<TabCompletion>> getTabCompletions() {
		return null;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.DESCRIBE_PLAYER.sendMessage(sender, true);
			return;
		}

		Player target = Bukkit.getPlayerExact(args[0]);
		if (target != null) {
			Profile tempProfile = new Profile(target.getUniqueId(), target.getName(), Instant.now(), null, null);
			checkAction(sender, tempProfile, false);
			return;
		}

		ProfileManager.getInstance().getProfileAsync(args[0]).whenComplete((profile, throwable) -> {
			if (throwable != null) {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				Message.PLAYER_NOT_EXIST.sendMessage(sender, true);
				return;
			}

			checkAction(sender, profile, true);
		});
	}

	private void checkAction(CommandSender requester, Profile profile, boolean async) {
		Optional<Punishment> anyActivePunishment = FlatFilePunishmentDao.getInstance().getAnyActivePunishment(profile.playerUUID());

		anyActivePunishment.ifPresentOrElse(punishment -> {
			Message.playSound(requester, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			requester.sendMessage(CentredMessage.generate("&7&m                    |&e " + profile.playerName() + " punish log &7&m|                    &r"));
			requester.sendMessage(Message.fixColor("&ePunishment Id: &b" + punishment.getPunishmentUniqueId().toString()));
			if (!async)
				Optional.ofNullable(punishment.getExecutor())
					.ifPresent(uuid -> requester.sendMessage(Message.fixColor("&eExecuted by: &b" + Bukkit.getOfflinePlayer(punishment.getExecutor()).getName())));
			else ProfileManager.getInstance().getProfile(punishment.getExecutor())
					.ifPresent(punishExecutor -> Message.fixColor("&eExecuted by: &b" + punishExecutor.playerName()));
			requester.sendMessage(Message.fixColor("&eType: &b" + punishment.getType().getDisplayName()));

			Optional.ofNullable(punishment.getExpiry()).ifPresent(instant -> {
				Duration res = Duration.between(Instant.now(), punishment.getExpiry());
				requester.sendMessage(Message.fixColor("&eExpiry: &b" + StringUtils.formatTime(res)));
			});

			requester.sendMessage(Message.fixColor("&eReason: &b" + punishment.getReason()));
			requester.sendMessage(CentredMessage.generate("&7&m                              x x                              &r"));
		}, () -> {
			Message.PUNISH_CHECK_NO_ACTIVE.sendMessage(requester, true);
			Message.playSound(requester, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
		});
	}
}
