package dev.gdalia.commandsplus.commands;

import java.util.Arrays;
import java.util.List;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "alts")
public class AltsCommand implements CommandExecutor, TabCompleter {

	/**
	 *  /alts {username} {check - banall - kickall}
	 *  LABEL ARG0 ARG1
	 */
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!Permission.PERMISSION_ALTS.hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}


		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.DESCRIBE_PLAYER.sendMessage(sender, true);
			return true;
		}

		Player target = Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}
		
		String address = target.getAddress().toString().split(":")[0];
		List<? extends Player> alts = Bukkit.getOnlinePlayers().stream()
				.filter(x -> x.getAddress().toString().split(":")[0].equalsIgnoreCase(address))
				.toList();
		
		if (args.length <= 1) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			player.sendMessage(Message.fixColor("&7/alts [&ePlayer&7] [&eCheck&7/&eBanall&7/&eKickall&7]"));
			return false;
		}

		switch (args[1].toLowerCase()) {
			case "check" -> {

				if (alts.isEmpty()) {
					Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					Message.ALTS_CHECK.sendFormattedMessage(player, true, target.getName());
					return true;
				}

				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Message.ALTS_ONLINE.sendFormattedMessage(player, true, target.getName());
				StringBuilder sb = new StringBuilder();
				alts.stream()
						.filter(x -> !x.getName().equalsIgnoreCase(target.getName()))
						.forEach(x -> sb.append(Message.fixColor("&7- " + x.getName() + ".\n")));
				Arrays.asList(sb.toString().split("\n")).forEach(player::sendMessage);
				return true;
			}
			case "banall" -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Message.ALTS_BANNED.sendFormattedMessage(player, true, target.getName());
				alts.forEach(x -> {
					String banCommand = Main.getInstance().getConfig().getString("ban-command");
					if (banCommand == null) return;
					banCommand = banCommand.replace("{player}", x.getName());
					Bukkit.dispatchCommand(sender, banCommand);
				});
				return true;
			}
			case "kickall" -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Message.ALTS_KICKED.sendFormattedMessage(player, true, target.getName());
				alts.forEach(x -> {
					String kickCommand = Main.getInstance().getConfig().getString("kick-command");
					if (kickCommand == null) return;
					kickCommand = kickCommand.replace("{player}", x.getName());
					Bukkit.dispatchCommand(sender, kickCommand);
				});
				return true;
			}
			default -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
				player.sendMessage(Message.fixColor("&7/alts [&ePlayer&7] [&eCheck&7/&eBanall&7/&eKickall&7]"));
				return true;
			}
		}
	}
	
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if(!Permission.PERMISSION_ALTS.hasPermission(sender)) return null;
		if (args.length == 1) return null;
		return List.of("check", "banall", "kickall");
	}
}
