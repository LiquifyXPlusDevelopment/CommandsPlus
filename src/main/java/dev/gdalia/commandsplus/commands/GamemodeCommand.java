package dev.gdalia.commandsplus.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.structs.Gamemode;
import dev.gdalia.commandsplus.structs.Message;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "gamemode")
public class GamemodeCommand implements TabExecutor {

	/**
	 *  /gamemode {type} {player}
	 *  LABEL ARG0 ARG1
	 */
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)){
        	Message.PLAYER_CMD.sendMessage(sender, true);
            return false;
        }  
        
		if (args.length == 0) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.GAMEMODE_ARGUMENTS.sendMessage(player, true);
			return false;
		}
		
        Gamemode setGamemode;

		if (StringUtils.isNumeric(args[0])) {
			try {
				setGamemode = Gamemode.getFromInt(Integer.parseInt(args[0]));
			} catch (Exception e1) {
				Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
	        	Message.GAMEMODE_ARGUMENTS.sendMessage(player, true);
				return false;
			}
		} else {
			try {
			setGamemode = Gamemode.getFromSubCommand(args[0].toLowerCase());
			} catch (Exception e1) {
				Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
	        	Message.GAMEMODE_ARGUMENTS.sendMessage(player, true);
				return false;
			}
		}
		
		if (!player.hasPermission(setGamemode.getPermission())) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.NO_PERMISSION.sendMessage(sender, true);
			return false;
		}


		if (args.length >= 2 && Bukkit.getPlayerExact(args[1]) != null) {
			player = Bukkit.getPlayer(args[1]);
		} else if (args.length >= 2 && Bukkit.getPlayerExact(args[1]) == null) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return false;
		}
    	
    	if (player.getGameMode() == setGamemode.getAsBukkit()) {
    		boolean isSender = player.getName().equalsIgnoreCase(sender.getName());
    		Message message = isSender ? Message.GAMEMODE_ALREADY_SET : Message.GAMEMODE_ALREADY_SET_OTHER;
    		Object[] values = !isSender ? new Object[] {player.getName(), setGamemode.name()} : new Object[] {setGamemode.name()};
    		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    		message.sendFormattedMessage(sender, true, values);
    		return false;
    	}
    	
    	
    	player.setGameMode(setGamemode.getAsBukkit());
		boolean isSender = player.equals(sender);
		if (isSender) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Message.GAMEMODE_CHANGED.sendFormattedMessage(sender, true, setGamemode.name());
			return true;
		}
		
		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.GAMEMODE_CHANGED_OTHER.sendFormattedMessage(sender, true, player.getName(), setGamemode.name());
		Message.GAMEMODE_CHANGED_BY_OTHER.sendFormattedMessage(player, true, setGamemode.name(), sender.getName());
		return true;
	}

	
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length != 1) return null;
		List<String> subC = new ArrayList<>();
		subC.addAll(Stream.of(Gamemode.values())
				.map(Gamemode::getAsSubCommand)
				.toList());
		
		subC.addAll(Stream.of(Gamemode.values())
				.map(gm -> String.valueOf(gm.getAsInteger()))
				.toList());
		return subC;
	}
}
