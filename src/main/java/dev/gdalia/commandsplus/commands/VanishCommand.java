package dev.gdalia.commandsplus.commands;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "vanish")
public class VanishCommand implements CommandExecutor{
	
	/**
	 * /vanish
	 * LABEL
	 */
	
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if(!(sender instanceof Player player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }

		UUID uuid = player.getUniqueId();
        if(!Permission.PERMISSION_VANISH.hasPermission(sender)) {
        	Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }

		Stream<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers().stream()
					.filter(Permission.PERMISSION_VANISH_SEE::hasPermission);

        if (!PlayerCollection.getVanishPlayers().contains(uuid)) {
        	PlayerCollection.getVanishPlayers().add(uuid);
        	PlayerCollection.getBuildmodePlayers().add(uuid);
    		player.setAllowFlight(true);
    		player.setFlying(true);
			onlinePlayers.forEach(p -> p.hidePlayer(Main.getInstance(), player));
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        } else {
			PlayerCollection.getVanishPlayers().remove(uuid);
			PlayerCollection.getBuildmodePlayers().remove(uuid);
			onlinePlayers.forEach(p -> p.showPlayer(Main.getInstance(), player));
		}

		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.VANISH_TOGGLE.sendMessage(player, true);
		return true;
	}
}
