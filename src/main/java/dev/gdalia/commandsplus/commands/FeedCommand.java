package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@CommandAutoRegistration.Command(value = "feed")
public class FeedCommand extends BasePlusCommand {

	public FeedCommand() {
		super(false, "feed");
	}

	@Override
	public String getDescription() {
		return "Feed yourself/others.";
	}

	@Override
	public String getSyntax() {
		return "/feed [player]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_FEED;
	}

	@Override
	public boolean isPlayerCommand() {
		return true;
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> getTabCompletions() {
		return null;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Player target = (Player) sender;

		if (args.length >= 1 && Bukkit.getPlayerExact(args[0]) != null) target = Bukkit.getPlayer(args[0]);
		else if (args.length >= 1 && Bukkit.getPlayerExact(args[0]) == null) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.PLAYER_NOT_ONLINE.sendMessage(sender, true);
			return;
		}
		
		if (target != sender) {
			Message.FEED_TARGET.sendFormattedMessage(sender, true, target.getName());
			Message.FEED_BY_TARGET.sendFormattedMessage(target, true, sender.getName());
		} else Message.FEED_PLAYER.sendMessage(sender, true);

		target.setFoodLevel(20);
		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
	}
}
