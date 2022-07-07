package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "fly")
public class FlyCommand implements CommandExecutor {

	/**
	 * /fly {user}
	 * LABEL ARG0
	 */
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)){
        	Message.PLAYER_CMD.sendMessage(sender, true);
            return false;
        }

		if (!Permission.PERMISSION_FLY.hasPermission(player)) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.NO_PERMISSION.sendMessage(player, true);
			return false;
		}

		if (args.length >= 1) {
			if (Bukkit.getPlayerExact(args[0]) == null) {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				Message.INVALID_PLAYER.sendMessage(sender, true);
				return false;
			}
			player = Bukkit.getPlayer(args[0]);
		}
    	
		boolean negation = !player.getAllowFlight();
		player.setAllowFlight(negation);
		player.setFlying(negation);
		
		if (!player.equals(sender)) {
			Message.FLIGHT_MSG_BY_OTHER.sendFormattedMessage(player, true, StringUtils.getStatusString(player.getAllowFlight()), sender.getName());
			Message.FLIGHT_MSG_TO_OTHER.sendFormattedMessage(sender, true, StringUtils.getStatusString(player.getAllowFlight()), player.getName());
		} else Message.FLIGHT_MSG.sendFormattedMessage(sender, true, StringUtils.getStatusString(player.getAllowFlight()));
		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		return true;
	}
}
