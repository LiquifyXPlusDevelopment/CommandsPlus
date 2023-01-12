package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.punishmentdrivers.FlatFilePunishments;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.profile.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@CommandAutoRegistration.Command(value = {"unban", "unmute"})
public class PardonCommand extends BasePlusCommand {

	public PardonCommand() {
		super(false, "unban", "unmute");
	}

	@Override
	public String getDescription() {
		return "Pardon command to players who were punished.";
	}

	@Override
	public String getSyntax() {
		return "/(unban || unmute) [player]";
	}

	@Override
	public Permission getRequiredPermission() {
		return null;
	}

	@Override
	public boolean isPlayerCommand() {
		return false;
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> getTabCompletions() {
		return null;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		PunishmentType type = PunishmentType.valueOf(cmd.getName().toUpperCase().replace("UN", ""));

		if (!Permission.valueOf("PERMISSION_" + cmd.getName().toUpperCase()).hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.COMMAND_NO_PERMISSION.sendMessage(sender, true);
			return;
		}
		
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			Message.valueOf(cmd.getName().toUpperCase() +"_ARGUMENTS").sendMessage(sender, true);
			return;
		}


		ProfileManager.getInstance().getProfileAsync(args[0]).whenComplete((profile, throwable) -> {
			if (throwable != null) {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				Message.PLAYER_NOT_EXIST.sendMessage(sender, true);
				return;
			}

			FlatFilePunishments.getInstance()
				.getActivePunishment(
				profile.playerUUID(),
				type, PunishmentType.valueOf("TEMP" + type.name()))
			.ifPresentOrElse(punishment -> {
				Optional<UUID> removedBy = Stream.of(sender)
					.filter(Player.class::isInstance)
					.map(Player.class::cast)
					.map(Player::getUniqueId)
					.findAny();

				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				type.getRevokeMessage().sendFormattedMessage(sender, true, profile.playerName());

				Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
					PunishmentManager.getInstance().revoke(punishment, removedBy.orElse(null));

					Player target = Bukkit.getPlayer(profile.playerUUID());
					if (!type.isKickable() && target != null)
						Message.PUNISH_REVOKE_UNMUTE_MESSAGE.sendFormattedMessage(target, true, sender.getName());
				});
			}, () -> type.getNotPunishedMessage().sendMessage(sender, true));
		});
	}
}
