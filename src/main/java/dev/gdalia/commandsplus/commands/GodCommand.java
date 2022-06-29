package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "god")
public class GodCommand implements CommandExecutor {

	/**
	 * /god {user}
	 * LABEL ARG0
	 */
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!Permission.PERMISSION_GOD.hasPermission(player)) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(player, true);
			return true;
		}

		if (args.length == 0) {
			Message.DESCRIBE_PLAYER.sendMessage(player, true);
			return false;
		}

		Player target = Bukkit.getPlayerExact(args[0]);

		if (target == null) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.INVALID_PLAYER.sendMessage(player, true);
			return false;
		}
		
		if (!target.hasMetadata("godmode")) {
			target.setMetadata("godmode", Main.MetadataValues.godModeData(true));
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Message.PLAYER_GOD_ENABLED.sendMessage(target, true);
			if (!target.equals(player)) Message.TARGET_GOD_ENABLED.sendFormattedMessage(player, true, target.getName());
			return true;
		}
		
		target.removeMetadata("godmode", Main.getInstance());
		Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.PLAYER_GOD_DISABLED.sendMessage(target, true);
		if (!target.equals(player)) Message.TARGET_GOD_DISABLED.sendFormattedMessage(player, true, target.getName());
		return true;
	}
}