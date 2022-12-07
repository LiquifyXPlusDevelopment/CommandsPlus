package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@CommandAutoRegistration.Command(value = "flyspeed")
public class FlySpeedCommand extends BasePlusCommand {

	public FlySpeedCommand() {
		super(false, "flyspeed");
	}

	@Override
	public String getDescription() {
		return "Sets the defined speed for flying.";
	}

	@Override
	public String getSyntax() {
		return "/flyspeed [1-10]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_FLYSPEED;
	}

	@Override
	public boolean isPlayerCommand() {
		return true;
	}
	@Override
	public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
		int[] listOfNumbers = new int[]{1,2,3,4,5,6,7,8,9,10};
		List<String> listOfNumbersString = Arrays.stream(listOfNumbers).mapToObj(String::valueOf).toList();
		return Map.of(1, List.of(new TabCompletion(listOfNumbersString, getRequiredPermission())));
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;

		if (args.length == 0) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.FLY_SPEED_ARGUMENTS.sendMessage(player, true);
			return;
		}
		
		try {
			if (!StringUtils.isNumeric(args[0])) return;
			float speed = Integer.parseInt(args[0]);
			if (speed > 10.0F || speed < 0.0F) return;

			float flightSpeed = speed / 10F;
			player.setFlySpeed(flightSpeed);
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Message.FLY_SPEED.sendFormattedMessage(player, true, (flightSpeed * 10));
		} catch (NumberFormatException ex) {
			Message.FLY_SPEED_ARGUMENTS.sendMessage(player, true);
		}
	}
}
