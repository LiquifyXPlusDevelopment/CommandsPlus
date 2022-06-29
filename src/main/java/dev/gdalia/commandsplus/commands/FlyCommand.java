package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
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

		if (args.length == 0) {
			Message.DESCRIBE_PLAYER.sendMessage(player, true);
			return false;
		}

		Player target = Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.INVALID_PLAYER.sendMessage(player, true);
			return false;
		}
    	
		boolean negation = !target.getAllowFlight();
		target.setAllowFlight(negation);
		target.setFlying(negation);
		
		if (target.equals(player)) Message.FLIGHT_MSG.sendFormattedMessage(player, true, getStatusString(target));
		else Message.FLIGHT_MSG_BY_OTHER.sendFormattedMessage(target, true, getStatusString(target), player.getName());
		Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		return true;
	}
	
	private String getStatusString(Player player) {
		return player.getAllowFlight() ? Message.fixColor("&a&lEnabled&7") : Message.fixColor("&c&lDisabled&7");
	}
}
