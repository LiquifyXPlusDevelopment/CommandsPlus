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
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.Punishment;
import dev.gdalia.commandsplus.structs.PunishmentType;

@CommandAutoRegistration.Command(value = "warn")
public class WarnCommand implements CommandExecutor{

	/**
	 * /warn {user} {reason}
	 * LABEL ARG0 ARG1+
	 */
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!Permission.PERMISSION_WARN.hasPermission(sender)) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length <= 1) {
			Message.WARN_ARGUMENTS.sendMessage(sender, true);
			return true;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (!target.hasPlayedBefore()) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}
		
        StringBuilder reasonBuilder = new StringBuilder();
		
        for (int i = 1; i < args.length; i++) 
        	reasonBuilder.append(args[i]);
		
		UUID executer = sender instanceof Player requester ? requester.getUniqueId() : null;
		Punishment punishment = new Punishment(UUID.randomUUID(), target.getUniqueId(), executer, PunishmentType.WARN, reasonBuilder.toString());
		
		PunishmentManager.getInstance().invoke(punishment);
		Message.PLAYER_WARN_MESSAGE.sendFormattedMessage(sender, true, target.getName());
		return true;
	}
}
