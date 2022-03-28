package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;

@CommandAutoRegistration.Command(value = "flyspeed")
public class FlySpeedCommand implements CommandExecutor{

	/**
	 * /flyspeed
	 * LABEL
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!Permission.PERMISSION_FLYSPEED.hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}

		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.FLY_SPEED_ARGUMENTS.sendMessage(sender, true);
			return true;
		}
		
		try {
			
		if (!StringUtils.isNumeric(args[0])) return false;
		
		float speed = Integer.parseInt(args[0]);
		if (speed > 10.0F || speed < 0.0F) return false;
		
		float fspeed = speed / 10F;
		player.setFlySpeed(fspeed);
		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.FLY_SPEED.sendFormattedMessage(player, true, fspeed);
		return true;
		} catch (NumberFormatException ex) {
			Message.FLY_SPEED_ARGUMENTS.sendMessage(sender, true);
			return false;
		}
	}

}
