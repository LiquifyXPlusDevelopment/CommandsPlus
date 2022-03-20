package dev.gdalia.commandsplus.commands;

import java.util.UUID;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Message;

@CommandAutoRegistration.Command(value = "buildmode")
public class BuildmodeCommand implements CommandExecutor{

	/**
	 * /buildmode
	 * LABEL
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if(!(sender instanceof Player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }
		
		Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        if(!player.hasPermission("commandsplus.buildmode")) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
        
        if (!PlayerCollection.getBuildmodePlayers().contains(uuid)) {
        	PlayerCollection.getBuildmodePlayers().add(uuid);
			Message.BUILDMODE_ENABLE.sendMessage(player, true);
			return true;
        }
        
        PlayerCollection.getBuildmodePlayers().remove(uuid);
		Message.BUILDMODE_DISABLE.sendMessage(player, true);
		return true;
	}
	
}
