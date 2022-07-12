package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.punishments.PunishmentRevoke;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
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

	@SuppressWarnings("deprecation")
	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		PunishmentType type = PunishmentType.valueOf(cmd.getName().toUpperCase().replace("UN", ""));

		if (!Permission.valueOf("PERMISSION_" + cmd.getName().toUpperCase()).hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(sender, true);
			return;
		}
		
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			Message.valueOf(cmd.getName().toUpperCase() +"_ARGUMENTS").sendMessage(sender, true);
			return;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore()) {
        	Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.INVALID_PLAYER.sendMessage(sender, true);
            return;
        }
        
        Punishments.getInstance()
        	.getActivePunishment(target.getUniqueId(), type, PunishmentType.valueOf("TEMP" + type))
        	.ifPresentOrElse(punishment -> {
        		PunishmentManager.getInstance().revoke(new PunishmentRevoke(
								punishment,
								Optional.of(((Player) sender).getUniqueId()).orElse(null)));
        		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

        		Message.valueOf("PLAYER_" + cmd.getName().toUpperCase()).sendFormattedMessage(sender, true, target.getName());
				if (target.isOnline() && type.equals(PunishmentType.MUTE) || type.equals(PunishmentType.TEMPMUTE)) {
					Message.valueOf("TARGET_" + cmd.getName().toUpperCase()).sendFormattedMessage(target.getPlayer(), true, sender.getName());
				}

        		}, () -> Message.valueOf("PLAYER_NOT_" + cmd.getName().toUpperCase()).sendMessage(sender, true));
	}
}
