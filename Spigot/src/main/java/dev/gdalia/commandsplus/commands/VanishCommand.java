package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@dev.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "vanish")
public class VanishCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if(!(sender instanceof Player executer)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }

		UUID uuid = executer.getUniqueId();
        if(!executer.hasPermission("commandsplus.vanish")) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
        
        if (!PlayerCollection.getVanishPlayers().contains(uuid)) {
        	PlayerCollection.getVanishPlayers().add(uuid);
        	PlayerCollection.getBuildmodePlayers().add(uuid);
    		executer.setAllowFlight(true);
    		executer.setFlying(true);
			Bukkit.getOnlinePlayers()
			.stream()
			.filter(player -> player.canSee(executer) && !player.hasPermission("commandsplus.vanish.see"))
			.forEach(player -> player.hidePlayer(Main.getInstance(), executer));
			Message.VANISH_ENABLE.sendFormattedMessage(executer, true);
			return true;
        }
        
        PlayerCollection.getVanishPlayers().remove(uuid);
    	PlayerCollection.getBuildmodePlayers().remove(uuid);
		executer.setAllowFlight(false);
		executer.setFlying(false);
		Bukkit.getOnlinePlayers()
			.stream()
			.filter(player -> !player.canSee(executer))
			.forEach(player -> player.showPlayer(Main.getInstance(), executer));
		Message.VANISH_DISABLE.sendFormattedMessage(executer, true);
		return true;
	}
}
