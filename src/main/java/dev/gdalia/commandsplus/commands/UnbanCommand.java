package dev.gdalia.commandsplus.commands;

import java.util.UUID;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.PunishmentRevoke;
import dev.gdalia.commandsplus.structs.PunishmentType;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "unban")
public class UnbanCommand implements CommandExecutor{

	/**
	 * /unban {user}
	 * LABEL ARG0
	 */
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(sender instanceof Player player)) {
			Message.PLAYER_CMD.sendMessage(sender, true);
			return true;
		}
		
		if (!Permission.PERMISSION_UNBAN.hasPermission(player)) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.NO_PERMISSION.sendMessage(player, true);
			return true;
		}
		
		if (args.length == 0) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			Message.UNBAN_ARGUMENTS.sendMessage(player, true);
			return true;
		}
		
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (!target.hasPlayedBefore()) {
        	Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.INVALID_PLAYER.sendMessage(player, true);
            return true;
        }
        
        Punishments.getInstance()
        	.getActivePunishment(target.getUniqueId(), PunishmentType.BAN, PunishmentType.TEMPBAN)
        	.ifPresentOrElse(punishment -> {
				UUID executer = player.getUniqueId();
        		PunishmentManager.getInstance().revoke(new PunishmentRevoke(punishment, executer));
        		Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        		Message.PLAYER_UNBANNED.sendFormattedMessage(player, true, target.getName());
        		}, () -> Message.PLAYER_NOT_BANNED.sendMessage(player, true));
        return true;
	}

}
