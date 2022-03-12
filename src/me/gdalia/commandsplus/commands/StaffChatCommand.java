package me.gdalia.commandsplus.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.gdalia.commandsplus.Main.PlayerCollection;
import me.gdalia.commandsplus.structs.Message;

@me.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "staffchat")
public class StaffChatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if(!(sender instanceof Player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }
		
		Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if(!player.hasPermission("commandsplus.staffchat")) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
			
        if (!PlayerCollection.getStaffchatPlayers().contains(uuid)) {
        	PlayerCollection.getStaffchatPlayers().add(uuid);
			Message.STAFFCHAT_ENABLE.sendMessage(player, true);
			return true;
        }
        
        PlayerCollection.getStaffchatPlayers().remove(uuid);
		Message.STAFFCHAT_DISABLE.sendMessage(player, true);
		
		return true;
	}

}
