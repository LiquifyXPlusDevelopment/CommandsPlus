package dev.gdalia.commandsplus.commands;

import java.util.UUID;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Punishment;
import dev.gdalia.commandsplus.structs.PunishmentType;

@CommandAutoRegistration.Command(value = "kick")
public class KickCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!sender.hasPermission("commandsplus.kick")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}
		
		if (args.length <= 1) {
			Message.KICK_ARGUMENTS.sendMessage(sender, true);
			return true;
		}
		
		Player player = (Player) sender;

		Player target = Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 1; i < args.length; i++)
			sb.append(args[i]);
		
		UUID executer = sender instanceof Player requester ? requester.getUniqueId() : null;
		
		Punishment punishment = new Punishment(
				UUID.randomUUID(),
				target.getUniqueId(),
				executer,
				PunishmentType.KICK,
				sb.toString());
		
		PunishmentManager.getInstance().invoke(punishment);
		Message.PLAYER_KICK.sendFormattedMessage(player, true, target.getName());
		return true;
	}

}
