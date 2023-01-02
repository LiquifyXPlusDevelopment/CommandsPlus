package dev.gdalia.commandsplus.commands;

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

@CommandAutoRegistration.Command(value = "fly")
public class FlyCommand extends BasePlusCommand {

	public FlyCommand() {
		super(false, "fly");
	}

	@Override
	public String getDescription() {
		return "Toggles flying.";
	}

	@Override
	public String getSyntax() {
		return "/fly [player]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_FLY;
	}

	@Override
	public boolean isPlayerCommand() {
		return true;
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
		return null;
	}

	/**
	 * /fly {user}
	 * LABEL ARG0
	 */
	
	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Player target = (Player) sender;

		if (args.length >= 1) {
			if (Bukkit.getPlayerExact(args[0]) != null) target = Bukkit.getPlayerExact(args[0]);
			else {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				Message.PLAYER_NOT_ONLINE.sendMessage(sender, true);
				return;
			}
		}
    	
		boolean negation = !target.getAllowFlight();
		target.setAllowFlight(negation);
		target.setFlying(negation);
		
		if (!target.equals(sender)) {
			Message.FLIGHT_TOGGLE_BY_OTHER.sendFormattedMessage(target, true, StringUtils.getStatusString(target.getAllowFlight()), sender.getName());
			Message.FLIGHT_MSG_TO_OTHER.sendFormattedMessage(sender, true, StringUtils.getStatusString(target.getAllowFlight()), target.getName());
		} else Message.FLIGHT_TOGGLE.sendFormattedMessage(sender, true, StringUtils.getStatusString(target.getAllowFlight()));
		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
	}
}
