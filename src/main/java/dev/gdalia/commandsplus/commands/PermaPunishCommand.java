package dev.gdalia.commandsplus.commands;

import java.util.UUID;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
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

@CommandAutoRegistration.Command(value = {"ban", "kick", "warn", "mute"})
public class PermaPunishCommand implements CommandExecutor {
	
	/**
	 * /punishment {user} {reason}
	 * LABEL ARG0 ARG1+
	 */
	
    @SuppressWarnings({ "deprecation"})
	@Override
    public boolean onCommand(CommandSender sender, Command cmd,
    		String label, String[] args) {
 	
		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		PunishmentType type = PunishmentType.canBeType(cmd.getName().toUpperCase()) ? PunishmentType.valueOf(cmd.getName().toUpperCase()) : null;

		if (!Permission.valueOf("PERMISSION_" + type.name()).hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length <= 1) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			Message.valueOf(type.name() + "_ARGUMENTS").sendMessage(sender, true);
			return true;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		
        if(!target.hasPlayedBefore()) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.INVALID_PLAYER.sendMessage(sender, true);
            return true;
        }
        
            StringBuilder reasonBuilder = new StringBuilder();
            
            for (int i = 1; i < args.length; i++) 
            	reasonBuilder.append(args[i]);
            
            
            UUID executer = sender instanceof Player requester ? requester.getUniqueId() : null;

            Punishment punishment = new Punishment(
            			UUID.randomUUID(),
            			target.getUniqueId(),
            			executer,
            			type,
            			reasonBuilder.toString());

        if (type == PunishmentType.WARN || type == PunishmentType.KICK) {
            PunishmentManager.getInstance().invoke(punishment);
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            Message.valueOf("PLAYER_" + type.getNameAsPunishMsg().toUpperCase() + "_MESSAGE").sendFormattedMessage(sender, true, target.getName());
            return true;
        }else {
            Punishments.getInstance().getActivePunishment(target.getUniqueId(), PunishmentType.valueOf(type.name().toUpperCase()),
            		PunishmentType.valueOf("TEMP" + type.name().toUpperCase())).ifPresentOrElse(punishments ->
        	Message.valueOf("PLAYER_" + type.getNameAsPunishMsg().toUpperCase()).sendMessage(sender, true), () -> {
        		
                PunishmentManager.getInstance().invoke(punishment);
    			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                Message.valueOf("PLAYER_" + type.getNameAsPunishMsg().toUpperCase() + "_MESSAGE").sendFormattedMessage(sender, true, target.getName());
        		});
        }
        
        return true;
    }
}
