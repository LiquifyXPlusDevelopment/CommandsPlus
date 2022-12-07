package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.utils.CentredMessage;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.StringUtils;
import dev.gdalia.commandsplus.utils.profile.Profile;
import dev.gdalia.commandsplus.utils.profile.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.Option;
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
	public Map<Integer, List<TabCompletion>> tabCompletions() {
		return null;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.DESCRIBE_PLAYER.sendMessage(sender, true);
			return;
		}

		runAsync(sender, () -> {
			Optional<Profile> profile = ProfileManager.getInstance().getProfile(args[0]);

			if (profile.isEmpty()) {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				Message.INVALID_PLAYER.sendMessage(sender, true);
				return;
			}

			Optional<Punishment> anyActivePunishment = Punishments.getInstance().getAnyActivePunishment(profile.get().getPlayerUUID());

			anyActivePunishment.ifPresentOrElse(punishment -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				sender.sendMessage(CentredMessage.generate("&7&m                    |&e " + profile.get().getPlayerName() + " punish log &7&m|                    &r"));
				sender.sendMessage(Message.fixColor("&ePunishment Id: &b" + punishment.getPunishmentUniqueId().toString()));
				Optional.ofNullable(punishment.getExecutor())
						.ifPresent(uuid -> sender.sendMessage(Message.fixColor("&eExecuted by: &b" + Bukkit.getOfflinePlayer(punishment.getExecutor()).getName())));
				sender.sendMessage(Message.fixColor("&eType: &b" + punishment.getType().getDisplayName()));

				Optional.ofNullable(punishment.getExpiry()).ifPresent(instant -> {
					Duration res = Duration.between(Instant.now(), punishment.getExpiry());
					sender.sendMessage(Message.fixColor("&eExpiry: &b" + StringUtils.formatTime(res)));
				});

				sender.sendMessage(Message.fixColor("&eReason: &b" + punishment.getReason()));
				sender.sendMessage(CentredMessage.generate("&7&m                              x x                              &r"));
			}, () -> {
				Message.PLAYER_NO_ACTIVE_PUNISHMENT.sendMessage(sender, true);
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			});
		});
	}
}
