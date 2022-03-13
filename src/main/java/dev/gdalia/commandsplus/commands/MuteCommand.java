package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Punishment;
import dev.gdalia.commandsplus.structs.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@dev.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "mute")
public class MuteCommand implements CommandExecutor {
	
	/**
	 /mute {user} {reason}
	 */
	
	@Override
    public boolean onCommand(CommandSender sender, Command cmd,
    		String label, String[] args) {
 	
		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!sender.hasPermission("commandsplus.mute")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length <= 2) {
			Message.MUTE_ARGUMENTS.sendMessage(sender, true);
			return true;
		}
		
		Player target = Bukkit.getPlayerExact(args[1]);
		
        if(target == null) {
        	Message.INVALID_PLAYER.sendMessage(sender, true);
            return true;
        }
        
        Punishments.getInstance().getActivePunishment(target.getUniqueId(), PunishmentType.MUTE, PunishmentType.TEMPMUTE).ifPresentOrElse(punishment ->
        	Message.PLAYER_MUTED.sendMessage(sender, true), () -> {
        	
            StringBuilder reasonBuilder = new StringBuilder();
            
            for (int i = 2; i <= args.length; i++) 
            	reasonBuilder.append(args[i]);
            
            
            UUID executer = null;
            if (sender instanceof Player requester) executer = requester.getUniqueId();
            
            Punishment punishment = new Punishment(
            			UUID.randomUUID(),
            			target.getUniqueId(),
            			executer,
            			PunishmentType.MUTE,
            			reasonBuilder.toString());
            
            PunishmentManager.getInstance().invoke(punishment);
            Message.PLAYER_MUTED_MESSAGE.sendFormattedMessage(sender, true, target.getName());
            Message.TARGET_MUTED_MESSAGE.sendMessage(target, true);
        });
        return true;
    }
}
