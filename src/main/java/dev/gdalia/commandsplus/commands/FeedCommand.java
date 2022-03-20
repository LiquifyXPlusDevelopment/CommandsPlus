package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;

@CommandAutoRegistration.Command(value = "feed")
public class FeedCommand implements CommandExecutor {

	/**
	 * /feed {user}
	 * LABEL ARG0
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {

		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!Permission.PERMISSION_FEED.hasPermission(sender)) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}

		Player player = (Player)sender;
		
		if (args.length >= 1 && Bukkit.getPlayerExact(args[0]) != null) {
			player = Bukkit.getPlayer(args[0]);
		} else if (args.length >= 1 && Bukkit.getPlayerExact(args[0]) == null) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return false;
		}
		
		if(player == sender) {
			player.setFoodLevel(20);
			Message.FEED_PLAYER.sendMessage(sender, true);
			return true;
		}
		
		player.setFoodLevel(20);
		Message.FEED_TARGET.sendFormattedMessage(sender, true, player.getName());
		return true;
	}

}
