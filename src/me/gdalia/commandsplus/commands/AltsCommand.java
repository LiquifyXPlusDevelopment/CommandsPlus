package me.gdalia.commandsplus.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.gdalia.commandsplus.Main;
import me.gdalia.commandsplus.structs.Message;

@me.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "alts")
public class AltsCommand implements CommandExecutor, TabCompleter {

	/**
	 *  /alt (name)
	 *  LABEL ARG0
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {

		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!sender.hasPermission("commandsplus.alts")) {
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			Message.DESCRIBE_PLAYER.sendMessage(sender, true);
			return true;
		}

		Player target = Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}

		String address = target.getAddress().toString().split(":")[0];
		List<? extends Player> alts = Bukkit.getOnlinePlayers().stream()
				.filter(x -> x.getAddress().toString().split(":")[0].equalsIgnoreCase(address))
				.toList();

		switch (args[1].toLowerCase()) {
		case "check": {

			if (alts.isEmpty()) {
				Message.ALTS_CHECK.sendFormattedMessage(player, true, target.getName());
				return true;
			}

			Message.ALTS_ONLINE.sendFormattedMessage(player, true, target.getName());
			StringBuilder sb = new StringBuilder();
			alts.stream()
			.filter(x -> !x.getName().equalsIgnoreCase(target.getName()))
			.forEach(x -> sb.append(Message.fixColor("&7- " + x.getName() + ".\n")));
			Arrays.asList(sb.toString().split("\n")).forEach(player::sendMessage);
			return true;
		}
		case "banall": {
			alts.forEach(x -> {
				String banCommand = Main.getInstance().getConfig().getString("ban-command");
				banCommand = banCommand.replace("{player}", x.getName());
				Bukkit.dispatchCommand(sender, banCommand);
			});
			return true;
		}
		
		case "kickall": {
			alts.forEach(x -> {
				String kickCommand = Main.getInstance().getConfig().getString("kick-command");
				kickCommand = kickCommand.replace("{player}", x.getName());
				Bukkit.dispatchCommand(sender, kickCommand);
			});
			return true;
		}
		default:
			Message.cmdUsage(cmd, sender);
			return true;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("commandsplus.alts")) return null;
		if (args.length == 1) return null;
		return List.of("check", "banall", "kickall");
	}
}
