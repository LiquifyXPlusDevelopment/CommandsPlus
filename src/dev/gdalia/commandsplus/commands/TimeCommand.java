package dev.gdalia.commandsplus.commands;

import java.util.List;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.structs.Message;

@CommandAutoRegistration.Command(value = "time")
public class TimeCommand implements CommandExecutor, TabCompleter{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if(!(sender instanceof Player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }
		
		Player player = (Player) sender;

        if(!player.hasPermission("commandsplus.time")) {
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
			Message.CHANGE_THE_TIME_TO_DAY.sendFormattedMessage(player, true, day);
			return true;
			}
		case "night": {
			String night = "14000";
			player.getWorld().setTime(14000);
			Message.CHANGE_THE_TIME_TO_NIGHT.sendFormattedMessage(player, true, night);
			return true;
			}
		default:
			Message.cmdUsage(cmd, sender);
			return true;
		}
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("commandsplus.time")) return null;
		if (args.length == 0) return null;
		return List.of("day", "night");
	}

}

