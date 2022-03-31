package dev.gdalia.commandsplus.commands;

import java.util.UUID;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "staffchat")
public class StaffChatCommand implements CommandExecutor{

	/**
	 * /staffchat
	 * LABEL
	 */
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if(!(sender instanceof Player player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }

		UUID uuid = player.getUniqueId();

        if(!Permission.PERMISSION_STAFFCHAT.hasPermission(sender)) {
        	Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
			
        if (!PlayerCollection.getStaffchatPlayers().contains(uuid)) {
        	PlayerCollection.getStaffchatPlayers().add(uuid);
        	Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Message.STAFFCHAT_ENABLE.sendMessage(player, true);
			return true;
        }
        
        PlayerCollection.getStaffchatPlayers().remove(uuid);
        Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.STAFFCHAT_DISABLE.sendMessage(player, true);
		
		return true;
	}

}
