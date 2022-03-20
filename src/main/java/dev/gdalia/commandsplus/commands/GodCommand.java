package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;

@CommandAutoRegistration.Command(value = "god")
public class GodCommand implements CommandExecutor {

	/**
	 * /god {user}
	 * LABEL ARG0
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!Permission.PERMISSION_GOD.hasPermission(sender)) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}

		Player player = (Player) sender;

		if (args.length >= 1 && Bukkit.getPlayerExact(args[0]) != null) {
			player = Bukkit.getPlayer(args[0]);
		} else if (args.length >= 1 && Bukkit.getPlayerExact(args[0]) == null) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return false;
		}
		
		if (!player.hasMetadata("godmode")) {
			player.setMetadata("godmode", Main.MetadataValues.godModeData(true));
			Message.PLAYER_GOD_ENABLED.sendMessage(player, true);
			if (!player.getName().equals(sender.getName())) Message.TARGET_GOD_ENABLED.sendFormattedMessage(sender, true, player.getName());
			return true;
		}
		
		player.removeMetadata("godmode", Main.getInstance());
		Message.PLAYER_GOD_DISABLED.sendMessage(player, true);
		if (!player.getName().equals(sender.getName())) Message.TARGET_GOD_DISABLED.sendFormattedMessage(sender, true, player.getName());
		return true;
	}
}