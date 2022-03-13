package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@dev.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "buildmode")
public class BuildmodeCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if(!(sender instanceof Player player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }

		UUID uuid = player.getUniqueId();

        if(!player.hasPermission("commandsplus.buildmode")) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
        
        if (!PlayerCollection.getBuildmodePlayers().contains(uuid)) {
        	PlayerCollection.getBuildmodePlayers().add(uuid);
			Message.BUILDMODE_ENABLE.sendFormattedMessage(player, true);
			return true;
        }
        
        PlayerCollection.getBuildmodePlayers().remove(uuid);
		Message.BUILDMODE_DISABLE.sendFormattedMessage(player, true);
		return true;
	}
	
}
