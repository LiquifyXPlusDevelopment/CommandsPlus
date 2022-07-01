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

		if (args.length >= 1 && Bukkit.getPlayerExact(args[0]) != null) {
			player = Bukkit.getPlayer(args[0]);
		} else if (args.length >= 1 && Bukkit.getPlayerExact(args[0]) == null) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.INVALID_PLAYER.sendMessage(sender, true);
			return false;
		}
		
		if (!player.hasMetadata("godmode")) {
			player.setMetadata("godmode", Main.MetadataValues.godModeData(true));
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Message.PLAYER_GOD_ENABLED.sendMessage(player, true);
			if (!player.equals(sender)) Message.TARGET_GOD_ENABLED.sendFormattedMessage(sender, true, player.getName());
			return true;
		}
		
		player.removeMetadata("godmode", Main.getInstance());
		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.PLAYER_GOD_DISABLED.sendMessage(player, true);
		if (!player.equals(sender)) Message.TARGET_GOD_DISABLED.sendFormattedMessage(sender, true, player.getName());
		return true;
	}
}