package dev.gdalia.commandsplus.commands;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import dev.gdalia.commandsplus.structs.*;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandAutoRegistration.Command(value = {"ban", "kick", "warn", "mute"})
public class PermaPunishCommand extends BasePlusCommand {


	public PermaPunishCommand() {
		super(false, "ban", "kick", "warn", "mute");
	}

	@Override
	public String getDescription() {
		return "Permanently punish players from/in the server.";
	}

	@Override
	public String getSyntax() {
		return "/(ban || kick || warn || mute) [player] [reason]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_PUNISH;
	}

	@Override
	public boolean isPlayerCommand() {
		return false;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		PunishmentType type = PunishmentType.canBeType(cmd.getName().toUpperCase()) ? PunishmentType.valueOf(cmd.getName().toUpperCase()) : null;

		if (!Permission.valueOf("PERMISSION_" + type.name()).hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(sender, true);
			return;
		}
		
		if (args.length <= 1) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			Message.valueOf(type.name() + "_ARGUMENTS").sendMessage(sender, true);
			return;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		
        if (!target.hasPlayedBefore()) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.INVALID_PLAYER.sendMessage(sender, true);
            return;
        }
        
        if (type.equals(PunishmentType.MUTE) || type.equals(PunishmentType.BAN)) {
        	if (Punishments.getInstance().getActivePunishment(target.getUniqueId(), PunishmentType.valueOf(type.name().toUpperCase()),
        			PunishmentType.valueOf("TEMP" + type.name().toUpperCase())).orElse(null) != null) {
        		Message.valueOf("PLAYER_" + type.getNameAsPunishMsg().toUpperCase()).sendMessage(sender, true);
    			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
    			return;
        	}
        }
        
        if (type.equals(PunishmentType.KICK)) {
        	if (!target.isOnline()) {
    			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        		Message.UNKNOWN_PLAYER.sendMessage(sender, true);
        		return;
        	}
        }
        
            StringBuilder reasonBuilder = new StringBuilder();
            
            for (int i = 1; i < args.length; i++) 
            	reasonBuilder.append(args[i]);
                        
            String message = StringUtils.join(args, " ", 1, args.length);
            Punishment punishment = new Punishment(
					UUID.randomUUID(),
					target.getUniqueId(),
					Optional.of(((Player) sender).getUniqueId()).orElse(null),
					type,
					message,
					false);
            
	        	PunishmentManager.getInstance().invoke(punishment);
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
	            Message.valueOf("PLAYER_" + type.getNameAsPunishMsg().toUpperCase() + "_MESSAGE").sendFormattedMessage(sender, true, target.getName());
    }

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
		return null;
	}
}
