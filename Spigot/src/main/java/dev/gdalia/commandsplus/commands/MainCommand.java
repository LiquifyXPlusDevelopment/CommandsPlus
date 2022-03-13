package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

@dev.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "commandsplus")
public class MainCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if(!(sender instanceof Player player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }

		if(!player.hasPermission("commandsplus.admin")) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
        
		if (args.length <= 0) {
			player.sendMessage(Message.fixColor("&7/commandsplus [&eHelp&7/&eReload&7]"));
			return true;
		}

		switch (args[0].toLowerCase()) {
			case "help" -> {
				player.sendMessage(Message.fixColor("&7-------- &eHelp&7 --------"));
				player.sendMessage(Message.fixColor("&e/alts [player] [kickall/banall/check] &7Checks if the player has online alts."));
				player.sendMessage(Message.fixColor("&e/ban [user] [reason] &7Bans the player from the server."));
				player.sendMessage(Message.fixColor("&e/buildmode &7Enable/Disable building abilities."));
				player.sendMessage(Message.fixColor("&e/chat [clear/lock] &7Clear/Lock the chat."));
				player.sendMessage(Message.fixColor("&e/check [player] &7Check if the player is currently punished."));
				player.sendMessage(Message.fixColor("&e/feed [player] &7Fills your/player's appetite."));
				player.sendMessage(Message.fixColor("&e/fly [player] &7Enables/Disables your flying ability."));
				player.sendMessage(Message.fixColor("&e/Freeze [player] &7Freezing the player."));
				player.sendMessage(Message.fixColor("&e/gamemode [GameMode] [player] &7Changes your/target's gamemode.."));
				player.sendMessage(Message.fixColor("&e/god [player] &7Enables/Disables god mode for you/target."));
				player.sendMessage(Message.fixColor("&e/heal [player] &7Fills your health back to maximum."));
				player.sendMessage(Message.fixColor("&e/history [player] &7Shows all the punishment records of a certain player."));
				player.sendMessage(Message.fixColor("&e/kick [player] [reason] &7Kicks the player from the server."));
				player.sendMessage(Message.fixColor("&e/mute [player] [reason] &7Mutes the player from the server."));
				player.sendMessage(Message.fixColor("&e/staffchat &7Enables/Disables your staffchat."));
				player.sendMessage(Message.fixColor("&e/tempban [player] [time] [reason] &7Bans the player from the server for specific time."));
				player.sendMessage(Message.fixColor("&e/tempmute [player] [time] [reason] &7Mutes the player from the server for specific time."));
				player.sendMessage(Message.fixColor("&e/time [day/night] &7Change the time to day/night."));
				player.sendMessage(Message.fixColor("&e/tpall [player] &7Tp all online players to the command sender."));
				player.sendMessage(Message.fixColor("&e/tphere [player] &7Tp the player to you."));
				player.sendMessage(Message.fixColor("&e/unban [player] &7Unbans target from the server."));
				player.sendMessage(Message.fixColor("&e/unmute [player] &7Unmutes target from the server."));
				player.sendMessage(Message.fixColor("&e/vanish [player] &7Disables/Enables your/target's vanish mode."));
				player.sendMessage(Message.fixColor("&7-------- &b1&7/&b1&7 --------"));
				return true;
			}
			case "reload" -> {
				Main.getInstance().reloadConfig();
				Main.getInstance().saveConfig();
				player.sendMessage(Message.fixColor("&7CommandsPlus has been &eReloaded&7."));
				return true;
			}
			default -> {
				player.sendMessage(Message.fixColor("&7/commandsplus [&eHelp&7/&eReload&7]"));
				return true;
			}
		}
}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("commandsplus.admin")) return null;
		if (args.length == 0) return null;
		return List.of("help", "reload");
	}
}
