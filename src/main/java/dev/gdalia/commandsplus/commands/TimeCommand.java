package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@CommandAutoRegistration.Command(value = "time")
public class TimeCommand extends BasePlusCommand {

	public TimeCommand() {
		super(false, "time");
	}

	@Override
	public String getDescription() {
		return "Changes through day/night.";
	}

	@Override
	public String getSyntax() {
		return "/time [day/night]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_TIME;
	}

	@Override
	public boolean isPlayerCommand() {
		return true;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;

		if (args.length == 0) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			player.sendMessage(ChatColor.GRAY + getSyntax());
			return;
		}

		switch (args[0].toLowerCase()) {
			case "day" -> {
				int day = 1000;
				player.getWorld().setTime(1000);
				Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Message.CHANGE_THE_TIME.sendFormattedMessage(player, true, day);
			}
			case "night" -> {
				int night = 14000;
				player.getWorld().setTime(night);
				Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Message.CHANGE_THE_TIME.sendFormattedMessage(player, true, night);
			}
			default -> {
				player.sendMessage(Message.fixColor("&7/time [&eDay&7/&eNight&7]"));
			}
		}
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> getTabCompletions() {
		return Map.of(1, List.of(new TabCompletion(List.of("day", "night"), Permission.PERMISSION_TIME)));
	}
}

