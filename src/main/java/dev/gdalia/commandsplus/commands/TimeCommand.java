package dev.gdalia.commandsplus.commands;

import java.util.List;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "time")
public class TimeCommand implements CommandExecutor, TabCompleter{

	/**
	 * /time {day - night}
	 * LABEL ARG0
	 */
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if(!(sender instanceof Player player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }

		if(!Permission.PERMISSION_TIME.hasPermission(sender)) {
        	Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
        
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.TIME_ARGUMENTS.sendMessage(sender, true);
			return true;
		}


		switch (args[0].toLowerCase()) {
			case "day" -> {
				String day = "1000";
				player.getWorld().setTime(1000);
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Message.CHANGE_THE_TIME.sendFormattedMessage(player, true, day);
				return true;
			}
			case "night" -> {
				String night = "14000";
				player.getWorld().setTime(14000);
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Message.CHANGE_THE_TIME.sendFormattedMessage(player, true, night);
				return true;
			}
			default -> {
				player.sendMessage(Message.fixColor("&7/time [&eDay&7/&eNight&7]"));
				return true;
			}
		}
	}
	
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if(!Permission.PERMISSION_TIME.hasPermission(sender)) return null;
		if (args.length == 0) return null;
		return List.of("day", "night");
	}

}

