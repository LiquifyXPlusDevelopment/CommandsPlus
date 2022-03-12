package me.gdalia.commandsplus.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.gdalia.commandsplus.structs.Gamemode;
import me.gdalia.commandsplus.structs.Message;

@me.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "gamemode")
public class GamemodeCommand implements TabExecutor {

	/**
	 *  /gamemode (type) (player)
	 *  LABEL	   ARG0    ARG1
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
        if(!(sender instanceof Player)){
        	Message.PLAYER_CMD.sendMessage(sender, true);
            return false;
        }
        
		if (args.length == 0) {
			Message.GAMEMODE_ARGUMENTS.sendMessage(sender, true);
			return false;
		}
		
        Gamemode setGamemode = null;

		
		if (StringUtils.isNumeric(args[0])) {
			try {
				setGamemode = Gamemode.getFromInt(Integer.parseInt(args[0]));
			} catch (Exception e1) {
	        	Message.GAMEMODE_ARGUMENTS.sendMessage(sender, true);
				return false;
			}
		} else {
			try {
			setGamemode = Gamemode.getFromSubCommand(args[0].toLowerCase());
			} catch (Exception e1) {
	        	Message.GAMEMODE_ARGUMENTS.sendMessage(sender, true);
				return false;
			}
		}
		
		
		if(!sender.hasPermission(setGamemode.getPermission())) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
			return false;
		}
        
		Player player = (Player)sender;
		
		if (args.length >= 2 && Bukkit.getPlayerExact(args[1]) != null) {
			player = Bukkit.getPlayer(args[1]);
		} else if (args.length >= 2 && Bukkit.getPlayerExact(args[1]) == null) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return false;
		}
    	
    	if (player.getGameMode() == setGamemode.getAsBukkit()) {
    		boolean isSender = player.getName().equalsIgnoreCase(sender.getName());
    		Message message = isSender ? Message.GAMEMODE_ALREADY_SET : Message.GAMEMODE_ALREADY_SET_OTHER;
    		Object[] values = !isSender ? new Object[] {player.getName(), setGamemode.name()} : new Object[] {setGamemode.name()};
    		message.sendFormattedMessage(sender, true, values);    		
    		return false;
    	}
    	
    	
    	player.setGameMode(setGamemode.getAsBukkit());
		boolean isSender = player.getName().equalsIgnoreCase(sender.getName());
		if (isSender) {
			Message.GAMEMODE_CHANGED.sendFormattedMessage(sender, true, setGamemode.name());
			return true;
		}
		
		
		Message.GAMEMODE_CHANGED_OTHER.sendFormattedMessage(sender, true, player.getName(), setGamemode.name());
		Message.GAMEMODE_CHANGED_BY_OTHER.sendFormattedMessage(player, true, setGamemode.name(), sender.getName());
		return true;
	}

	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
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
