package dev.gdalia.commandsplus.commands;

import java.util.UUID;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.PunishmentRevoke;
import dev.gdalia.commandsplus.structs.PunishmentType;

@CommandAutoRegistration.Command(value = "unmute")
public class UnmuteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!sender.hasPermission("commandsplus.unmute")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length == 0) {
			Message.UNMUTE_ARGUMENTS.sendMessage(sender, true);
			return true;
		}
		
		Player target = Bukkit.getPlayerExact(args[0]);
        if(target == null){
            Message.INVALID_PLAYER.sendMessage(sender, true);
            return true;
        }
        
        Punishments.getInstance()
        	.getActivePunishment(target.getUniqueId(), PunishmentType.MUTE, PunishmentType.TEMPMUTE)
        	.ifPresentOrElse(punishment -> {
        		UUID executer = sender instanceof Player requester ? requester.getUniqueId() : null;
        		PunishmentManager.getInstance().revoke(new PunishmentRevoke(punishment, executer));
        		Message.PLAYER_UNMUTED.sendFormattedMessage(sender, true, target.getName());
        		Message.TARGET_UNMUTED.sendFormattedMessage(target, true, sender.getName());
        		
        		}, () -> Message.PLAYER_NOT_BANNED.sendMessage(sender, true));
		return true;
	}

}
