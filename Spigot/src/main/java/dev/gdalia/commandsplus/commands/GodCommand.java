package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@dev.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "god")
public class GodCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!sender.hasPermission("commandsplus.god")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}

		if (args.length >= 1 && Bukkit.getPlayerExact(args[1]) != null)
			player = Bukkit.getPlayerExact(args[1]);
		else {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}
		
		if (!player.hasMetadata("godmode")) {
			player.setMetadata("godmode", Main.MetadataValues.godModeData(true));
			Message.TARGET_GOD_ENABLED.sendFormattedMessage(player, true, sender.getName());
			if (!player.getName().equals(sender.getName())) Message.PLAYER_GOD_ENABLED.sendFormattedMessage(sender, true, player.getName());
			return true;
		}
		
		player.removeMetadata("godmode", Main.getInstance());
		Message.TARGET_GOD_DISABLED.sendFormattedMessage(player, true, sender.getName());
		if (!player.getName().equals(sender.getName())) Message.PLAYER_GOD_DISABLED.sendFormattedMessage(sender, true, player.getName());
		return true;
	}
}