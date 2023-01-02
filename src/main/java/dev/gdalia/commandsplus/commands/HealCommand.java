package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@CommandAutoRegistration.Command(value = "heal")
public class HealCommand extends BasePlusCommand {

	public HealCommand() {
		super(false, "heal");
	}

	@Override
	public String getDescription() {
		return "Fills health bar back to maximum.";
	}

	@Override
	public String getSyntax() {
		return "/heal [player]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_HEAL;
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
		Player target = (Player) sender;

		if (args.length >= 1) {
			if (Bukkit.getPlayerExact(args[0]) != null) {
				target = Bukkit.getPlayerExact(args[0]);
			} else {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				Message.PLAYER_NOT_ONLINE.sendMessage(sender, true);
				return;
			}
		}

		double playerMaxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

		if (target == sender) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Message.HEAL_TARGET.sendFormattedMessage(sender, true, target.getName(), String.valueOf(playerMaxHealth));
			Message.HEAL_BY_PLAYER.sendFormattedMessage(target, true, String.valueOf(playerMaxHealth), sender.getName());
		} else Message.HEAL_PLAYER.sendFormattedMessage(sender, true, String.valueOf(playerMaxHealth));

		target.setHealth(playerMaxHealth);
		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
	}
}
