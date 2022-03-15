package dev.gdalia.commandsplus.commands;

import java.util.UUID;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Punishment;
import dev.gdalia.commandsplus.structs.PunishmentType;

@CommandAutoRegistration.Command(value = "ban")
public class BanCommand implements CommandExecutor {
	
	/**
	 * /ban {user} {reason}
	 * LABEL ARG0   ARG1+
	 */
	
    @SuppressWarnings({ "deprecation"})
	@Override
    public boolean onCommand(CommandSender sender, Command cmd,
    		String label, String[] args) {
 	
		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!sender.hasPermission("commandsplus.ban")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length <= 1) {
			Message.BAN_ARGUMENTS.sendMessage(sender, true);
			return true;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
		
        if(!target.hasPlayedBefore()) {
        	Message.INVALID_PLAYER.sendMessage(sender, true);
            return true;
        }
        
        Punishments.getInstance().getActivePunishment(target.getUniqueId(), PunishmentType.BAN, PunishmentType.TEMPBAN).ifPresentOrElse(punishment ->
    	Message.PLAYER_BANNED.sendMessage(sender, true), () -> {
            
            StringBuilder reasonBuilder = new StringBuilder();
            
            for (int i = 1; i < args.length; i++) 
            	reasonBuilder.append(args[i]);
            
            
            UUID executer = null;
            if (sender instanceof Player requester) executer = requester.getUniqueId();
            
            Punishment punishment = new Punishment(
            			UUID.randomUUID(),
            			target.getUniqueId(),
            			executer,
            			PunishmentType.BAN,
            			reasonBuilder.toString());
                    
            PunishmentManager.getInstance().invoke(punishment);
            Message.PLAYER_BAN_MESSAGE.sendFormattedMessage(sender, true, target.getName());
    	});
        return true;
    }
}
