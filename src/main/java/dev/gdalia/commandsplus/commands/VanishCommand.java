package dev.gdalia.commandsplus.commands;

import java.util.UUID;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Message;

@CommandAutoRegistration.Command(value = "vanish")
public class VanishCommand implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if(!(sender instanceof Player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }

        Player p = (Player) sender;
        UUID uuid = p.getUniqueId();
        if(!p.hasPermission("commandsplus.vanish")) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
        
        if (!PlayerCollection.getVanishPlayers().contains(uuid)) {
        	PlayerCollection.getVanishPlayers().add(uuid);
        	PlayerCollection.getBuildmodePlayers().add(uuid);
    		p.setAllowFlight(true);
    		p.setFlying(true);
			Bukkit.getOnlinePlayers()
			.stream()
			.filter(player -> player.canSee(p) && !player.hasPermission("commandsplus.vanish.see"))
			.forEach(player -> player.hidePlayer(Main.getInstance(), p));
			Message.VANISH_ENABLE.sendMessage(p, true);
			return true;
        }
        
        PlayerCollection.getVanishPlayers().remove(uuid);
    	PlayerCollection.getBuildmodePlayers().remove(uuid);
		p.setAllowFlight(false);
		p.setFlying(false);
		Bukkit.getOnlinePlayers()
			.stream()
			.filter(player -> !player.canSee(p))
			.forEach(player -> player.showPlayer(Main.getInstance(), p));
		Message.VANISH_DISABLE.sendMessage(p, true);
		return true;
	}
}
