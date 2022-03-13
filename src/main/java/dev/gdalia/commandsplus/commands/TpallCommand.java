package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@dev.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "tpall")
public class TpallCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!sender.hasPermission("commandsplus.tpall")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}

		if (args.length == 0) {
			Message.DESCRIBE_PLAYER.sendMessage(sender, true);
			return true;
		}

		Player target = Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}
		
		Bukkit.getOnlinePlayers()
				.stream()
				.filter(p -> p.getName().equals(target.getName()))
				.forEach(p -> p.teleport(target));
		Message.TPALL.sendFormattedMessage(player, true, target.getName());
		return false;
	}

}
