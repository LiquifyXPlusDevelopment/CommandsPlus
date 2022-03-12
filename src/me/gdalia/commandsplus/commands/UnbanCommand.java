package me.gdalia.commandsplus.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.gdalia.commandsplus.models.PunishmentManager;
import me.gdalia.commandsplus.models.Punishments;
import me.gdalia.commandsplus.structs.Message;
import me.gdalia.commandsplus.structs.PunishmentRevoke;
import me.gdalia.commandsplus.structs.PunishmentType;

@me.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "unban")
public class UnbanCommand implements CommandExecutor{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!sender.hasPermission("commandsplus.unban")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length == 0) {
			Message.UNBAN_ARGUMENTS.sendMessage(sender, true);
			return true;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(!target.hasPlayedBefore()) {
        	Message.INVALID_PLAYER.sendMessage(sender, true);
            return true;
        }
        
        Punishments.getInstance()
        	.getActivePunishment(target.getUniqueId(), PunishmentType.BAN, PunishmentType.TEMPBAN)
        	.ifPresentOrElse(punishment -> {
        		UUID executer = sender instanceof Player requester ? requester.getUniqueId() : null;
        		PunishmentManager.getInstance().revoke(new PunishmentRevoke(punishment, executer));
        		Message.PLAYER_UNBANNED.sendFormattedMessage(sender, true, target.getName());
        		}, () -> Message.PLAYER_NOT_BANNED.sendFormattedMessage(sender, true));
        return true;
	}

}
