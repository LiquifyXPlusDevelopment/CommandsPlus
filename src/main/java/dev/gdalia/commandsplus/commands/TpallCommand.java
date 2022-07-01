package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "tpall")
public class TpallCommand implements CommandExecutor{

	/**
	 * /tpall {user}
	 * LABEL ARG0
	 */
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		if (!Permission.PERMISSION_TPALL.hasPermission(player)) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(player, true);
			return true;
		}

		if (args.length == 0) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.DESCRIBE_PLAYER.sendMessage(player, true);
			return true;
		}

		Player target = Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.INVALID_PLAYER.sendMessage(player, true);
			return true;
		}
		
		Bukkit.getOnlinePlayers()
				.stream()
				.filter(all -> !all.equals(target))
				.forEach(all -> all.teleport(target));
		Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.TPALL.sendFormattedMessage(player, true, target.getName());
		return false;
	}

}
