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
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "flyspeed")
public class FlySpeedCommand implements CommandExecutor{

	/**
	 * /flyspeed
	 * LABEL
	 */
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!Permission.PERMISSION_FLYSPEED.hasPermission(player)) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(player, true);
			return true;
		}

		if (args.length == 0) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.FLY_SPEED_ARGUMENTS.sendMessage(player, true);
			return true;
		}
		
		try {
		if (!StringUtils.isNumeric(args[0])) return false;
		float speed = Integer.parseInt(args[0]);
		if (speed > 10.0F || speed < 0.0F) return false;
		
		float flightSpeed = speed / 10F;
		player.setFlySpeed(flightSpeed);
		Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.FLY_SPEED.sendFormattedMessage(player, true, (flightSpeed*10));
		return true;
		} catch (NumberFormatException ex) {
			Message.FLY_SPEED_ARGUMENTS.sendMessage(player, true);
			return false;
		}
	}

}
