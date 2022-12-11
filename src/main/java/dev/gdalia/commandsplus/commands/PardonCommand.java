package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.profile.Profile;
import dev.gdalia.commandsplus.utils.profile.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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
	public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
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


		runAsync(sender, () -> {
			Optional<Profile> profile = ProfileManager.getInstance().getProfile(args[0]);

			if (profile.isEmpty()) {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				Message.PLAYER_NOT_EXIST.sendMessage(sender, true);
				return;
			}

			Punishments.getInstance()
				.getActivePunishment(profile.get().getPlayerUUID(), type, PunishmentType.valueOf("TEMP" + type))
				.ifPresentOrElse(punishment -> {
					Optional<UUID> removedBy = Stream.of(sender)
							.filter(Player.class::isInstance)
							.map(Player.class::cast)
							.map(Player::getUniqueId)
							.findAny();

					PunishmentManager.getInstance().revoke(punishment, removedBy.orElse(null));
					Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

					Message.valueOf("PLAYER_" + cmd.getName().toUpperCase()).sendFormattedMessage(sender, true, profile.get().getPlayerName());

					Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
						OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(profile.get().playerUUID());
						if (offlinePlayer.isOnline() && type.equals(PunishmentType.MUTE) || type.equals(PunishmentType.TEMPMUTE)) {
							Message.valueOf("TARGET_" + cmd.getName().toUpperCase()).sendFormattedMessage(offlinePlayer.getPlayer(), true, sender.getName());
						}
					});

				}, () -> Message.valueOf("PLAYER_NOT_" + cmd.getName().toUpperCase().replace("UN", "")).sendMessage(sender, true));
		});
	}
}
