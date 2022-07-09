package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@CommandAutoRegistration.Command(value = "chat")
public class ChatCommand extends BasePlusCommand {

	public ChatCommand() {
		super(false, "chat");
	}

	@Override
	public String getDescription() {
		return "&7Clears/Locks or Unlocks the chat.";
	}

	@Override
	public String getSyntax() {
		return "/chat [clear/lock]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_CHAT;
	}

	@Override
	public boolean isPlayerCommand() {
		return false;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.CHAT_ARGUMENTS.sendMessage(sender, true);
			return;
		}

		switch (args[0].toLowerCase()) {
			case "clear" -> {
				List<String> stream = Main.getInstance().getConfig().getStringList("chat.clear-template")
						.stream()
						.map(Message::fixColor)
						.toList();

				Bukkit.getOnlinePlayers().forEach(cleared -> {
					Message.playSound(cleared, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					IntStream.range(0, 100).forEach(i -> cleared.sendMessage(" "));
					stream.forEach(cleared::sendMessage);
				});
			}
			case "lock" -> {
				boolean isLocked = Main.getInstance().getConfig().getBoolean("chat.locked");
					Bukkit.getOnlinePlayers().forEach(locked -> {
						Message.playSound(locked, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
						Main.getInstance().getConfig().set("chat.locked", !isLocked);
						Main.getInstance().saveConfig();
						if (!isLocked) Message.LOCK_MESSAGE.sendMessage(locked, true);
						else Message.UNLOCK_MESSAGE.sendMessage(locked, true);
					});
			}
			default -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
				sender.sendMessage(Message.fixColor("&7/chat [&eClear&7/&eLock&7]"));
			}
		}
	}

	@Override
	public Map<Integer, List<String>> tabCompletions() {
		return Map.of(1, List.of("clear", "lock"));
	}
}
