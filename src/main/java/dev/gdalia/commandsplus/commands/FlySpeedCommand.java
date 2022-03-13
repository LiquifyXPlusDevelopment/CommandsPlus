package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.Message;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@dev.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "flyspeed")
public class FlySpeedCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!sender.hasPermission("commandsplus.flyspeed")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}

		if (args.length == 0) {
			Message.FLY_SPEED.sendMessage(sender, true);
			return true;
		}
		
		if (!StringUtils.isNumeric(args[0])) return false;

		float speed = Integer.parseInt(args[0]) / 10F;
		player.setFlySpeed(speed);
		Message.SPEED.sendFormattedMessage(player, true, speed);
		return false;
	}

}
