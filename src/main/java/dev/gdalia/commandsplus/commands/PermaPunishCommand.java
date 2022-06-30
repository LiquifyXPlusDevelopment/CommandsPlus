package dev.gdalia.commandsplus.commands;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.Punishment;
import dev.gdalia.commandsplus.structs.PunishmentType;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = {"ban", "kick", "warn", "mute"})
public class PermaPunishCommand implements CommandExecutor {
	
	/**
	 * /punishment {user} {reason}
	 * LABEL ARG0 ARG1+
	 */
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		PunishmentType type = PunishmentType.canBeType(cmd.getName().toUpperCase()) ? PunishmentType.valueOf(cmd.getName().toUpperCase()) : null;

		if (!Permission.valueOf("PERMISSION_" + type.name()).hasPermission(player)) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(player, true);
			return true;
		}
		
		if (args.length <= 1) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			Message.valueOf(type.name() + "_ARGUMENTS").sendMessage(player, true);
			return true;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		
        if (!target.hasPlayedBefore()) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.INVALID_PLAYER.sendMessage(player, true);
            return true;
        }
        
        if (type.equals(PunishmentType.MUTE) || type.equals(PunishmentType.BAN)) {
        	if (Punishments.getInstance().getActivePunishment(target.getUniqueId(), PunishmentType.valueOf(type.name().toUpperCase()),
        			PunishmentType.valueOf("TEMP" + type.name().toUpperCase())).orElse(null) != null) {
        		Message.valueOf("PLAYER_" + type.getNameAsPunishMsg().toUpperCase()).sendMessage(player, true);
    			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
    			return false;
        	}
        }
        
        if (type.equals(PunishmentType.KICK)) {
        	if (!target.isOnline()) {
    			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        		Message.UNKNOWN_PLAYER.sendMessage(player, true);
        		return false;
        	}
        }
        
            StringBuilder reasonBuilder = new StringBuilder();
            
            for (int i = 1; i < args.length; i++) 
            	reasonBuilder.append(args[i]);
                        
            String message = StringUtils.join(args, " ", 1, args.length);
			UUID executer = player.getUniqueId();

            Punishment punishment = new Punishment(
            			UUID.randomUUID(),
            			target.getUniqueId(),
            			executer,
            			type,
            			message);
            
	        	PunishmentManager.getInstance().invoke(punishment);
				Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
	            Message.valueOf("PLAYER_" + type.getNameAsPunishMsg().toUpperCase() + "_MESSAGE").sendFormattedMessage(player, true, target.getName());
        return true;
    }
}
