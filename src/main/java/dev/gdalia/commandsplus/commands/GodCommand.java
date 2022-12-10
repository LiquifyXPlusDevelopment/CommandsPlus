package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@CommandAutoRegistration.Command(value = "god")
public class GodCommand extends BasePlusCommand {

	public GodCommand() {
		super(false, "god");
	}

	@Override
	public String getDescription() {
		return "Allows entering god mode, no damage taken.";
	}

	@Override
	public String getSyntax() {
		return "/godmode [player]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_GOD;
	}

	@Override
	public boolean isPlayerCommand() {
		return true;
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
		return null;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;
		if (args.length >= 1 && Bukkit.getPlayerExact(args[0]) != null) {
			player = Bukkit.getPlayer(args[0]);
		} else if (args.length >= 1 && Bukkit.getPlayerExact(args[0]) == null) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.PLAYER_NOT_ONLINE.sendMessage(sender, true);
			return;
		}

		boolean negation = !player.hasMetadata("god-mode");

		if (!player.equals(sender)) {
			Message.GODMODE_TOGGLE_TO_PLAYER.sendFormattedMessage(sender, true, StringUtils.getStatusString(negation), player.getName());
			Message.GODMODE_TOGGLE_BY_OTHER.sendFormattedMessage(player, true, StringUtils.getStatusString(negation), sender.getName());
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		} else Message.GODMODE_TOGGLE.sendFormattedMessage(sender, true, StringUtils.getStatusString(negation));

		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

		if (!player.hasMetadata("god-mode")) {
			player.setMetadata("god-mode", Main.MetadataValues.godModeData(true));
			return;
		}
		
		player.removeMetadata("god-mode", Main.getInstance());
	}
}