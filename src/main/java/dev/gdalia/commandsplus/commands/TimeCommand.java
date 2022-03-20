package dev.gdalia.commandsplus.commands;

import java.util.List;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;

@CommandAutoRegistration.Command(value = "time")
public class TimeCommand implements CommandExecutor, TabCompleter{

	/**
	 * /time {day - night}
	 * LABEL ARG0
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if(!(sender instanceof Player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }
		
		Player player = (Player) sender;

        if(!Permission.PERMISSION_TIME.hasPermission(sender)) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
        
		if (args.length == 0) {
			Message.CASE.sendMessage(sender, true);
			return true;
		}

        
		switch (args[0].toLowerCase()) {
		case "day": {
			String day = "1000";
			player.getWorld().setTime(1000);
			Message.CHANGE_THE_TIME.sendFormattedMessage(player, true, day);
			return true;
			}
		case "night": {
			String night = "14000";
			player.getWorld().setTime(14000);
			Message.CHANGE_THE_TIME.sendFormattedMessage(player, true, night);
			return true;
			}
		default:
			player.sendMessage(Message.fixColor("&7/time [&eday&7/&enight&7]"));
			return true;
		}
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!Permission.PERMISSION_TIME.hasPermission(sender)) return null;
		if (args.length == 0) return null;
		return List.of("day", "night");
	}

}

