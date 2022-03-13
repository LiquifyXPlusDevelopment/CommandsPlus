package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@dev.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "staffchat")
public class StaffChatCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if(!(sender instanceof Player player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }

		UUID uuid = player.getUniqueId();

        if(!player.hasPermission("commandsplus.staffchat")) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
			
        if (!PlayerCollection.getStaffchatPlayers().contains(uuid)) {
        	PlayerCollection.getStaffchatPlayers().add(uuid);
			Message.STAFFCHAT_ENABLE.sendFormattedMessage(player, true);
			return true;
        }
        
        PlayerCollection.getStaffchatPlayers().remove(uuid);
		Message.STAFFCHAT_DISABLE.sendFormattedMessage(player, true);
		
		return true;
	}

}
