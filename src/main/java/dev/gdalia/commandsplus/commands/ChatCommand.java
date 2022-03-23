package dev.gdalia.commandsplus.commands;

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

@CommandAutoRegistration.Command(value = "chat")
public class ChatCommand implements CommandExecutor, TabCompleter {

	/**
	 * /chat {clear - lock}
	 * LABEL ARG0 ARG1
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return false;
		}

		Player player = (Player) sender;

		if (!Permission.PERMISSION_CHAT.hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(sender, true);
			return false;
		}

		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.CHAT_ARGUMENTS.sendMessage(sender, true);
			return true;
		}

		switch (args[0].toLowerCase()) {
			case "clear": {
				Bukkit.getOnlinePlayers().forEach(cleared -> {
					Message.playSound(cleared, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					for (int i = 0; i <= 100; i++) cleared.sendMessage(" ");
					Main.getInstance().getConfig().getStringList("chat.clear-template")
					.stream()
					.map(Message::fixColor)
					.forEach(cleared::sendMessage);
					});
				return true;
			}
			
			case "lock": {
				boolean isLocked = Main.getInstance().getConfig().getBoolean("chat.locked");
				if (!isLocked) {
					Bukkit.getOnlinePlayers().forEach(locked -> {
					Message.playSound(locked, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					Main.getInstance().getConfig().set("chat.locked", true);
					Message.LOCK_MESSAGE.sendMessage(sender, true);
					});
				} else {
					Bukkit.getOnlinePlayers().forEach(locked -> {
					Message.playSound(locked, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					Main.getInstance().getConfig().set("chat.locked", false);
					Message.UNLOCK_MESSAGE.sendMessage(sender, true);
					});
				}
				return true;
			}

			default: {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
				player.sendMessage(Message.fixColor("&7/chat [&eclear&7/&elock&7]"));
				return true;
			}
		}
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (!Permission.PERMISSION_CHAT.hasPermission(sender)) return null;
		if (args.length == 0) return null;
		return List.of("clear", "lock");
	}
}
