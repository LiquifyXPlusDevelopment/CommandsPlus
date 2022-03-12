package me.gdalia.commandsplus.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.gdalia.commandsplus.structs.Message;
import me.gdalia.commandsplus.utils.Utils;

@me.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "fly")
public class FlyCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
        if(!(sender instanceof Player)){
        	Message.PLAYER_CMD.sendMessage(sender, true);
            return false;
        }
        
		if(!sender.hasPermission("commandsplus.fly")) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
			return false;
		}
		
    	Player player = (Player) sender;
    	
    	if (args.length >= 1) {
    		if (Bukkit.getPlayerExact(args[0]) == null) {
            	Message.INVALID_PLAYER.sendMessage(sender, true);
    			return false;
    		}
    		player = Bukkit.getPlayer(args[0]);
    	}
    	
		boolean negatation = !player.getAllowFlight();
		player.setAllowFlight(negatation);
		player.setFlying(negatation);
		
		if (player.getName().equalsIgnoreCase(sender.getName())) Message.FLIGHT_MSG.sendFormattedMessage(sender, true, getStatusString(player));
		else Message.FLIGHT_MSG_BY_OTHER.sendFormattedMessage(sender, true, getStatusString(player), sender.getName());
		return true;
	}
	
	private String getStatusString(Player player) {
		return player.getAllowFlight() ? Utils.color("&a&lEnabled&7") : Utils.color("&c&lDisabled&7");
	}
}
