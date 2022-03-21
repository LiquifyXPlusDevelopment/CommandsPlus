package dev.gdalia.commandsplus.commands;

import java.util.UUID;

import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main.PlayerCollection;

@CommandAutoRegistration.Command(value = "freeze")
public class FreezeCommand implements CommandExecutor{
	
	/**
	 * /freeze {user}
	 * LABEL ARG0
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {

		if (!(sender instanceof Player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}

		Player player = (Player) sender;
		
		if (!Permission.PERMISSION_FREEZE.hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.NO_PERMISSION.sendMessage(sender, true);
			return true;
		}

		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.DESCRIBE_PLAYER.sendMessage(sender, true);
			return true;
		}

		Player target = Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.INVALID_PLAYER.sendMessage(sender, true);
			return true;
		}
		
		UUID uuid = target.getUniqueId();
		
		if (!PlayerCollection.getFreezePlayers().contains(uuid)) {
			PlayerCollection.getFreezePlayers().add(uuid);
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Message.FREEZE_TARGET.sendFormattedMessage(target, true, player.getName());
			Message.FREEZE_PLAYER.sendFormattedMessage(player, true, target.getName());
			return true;
		}
		
		PlayerCollection.getFreezePlayers().remove(uuid);
		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.UNFREEZE_TARGET.sendFormattedMessage(target, true, player.getName());
		Message.UNFREEZE_PLAYER.sendFormattedMessage(player, true, target.getName());
		return true;
	}
}
