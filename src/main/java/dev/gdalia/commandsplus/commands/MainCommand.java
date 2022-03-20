package dev.gdalia.commandsplus.commands;

import java.util.List;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Message;

@CommandAutoRegistration.Command(value = "commandsplus")
public class MainCommand implements CommandExecutor, TabCompleter {

	/**
	 * /commandsplus {help - reload}
	 * LABEL ARG0
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String label, String[] args) {
		
		if(!(sender instanceof Player)) {
        	Message.PLAYER_CMD.sendMessage(sender, true);
        	return false;
        }
		
		Player player = (Player) sender;
        if(!player.hasPermission("commandsplus.admin")) {
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
        
		if (args.length == 0) {
			player.sendMessage(Message.fixColor("&7/commandsplus [&ehelp&7/&ereload&7]"));
			return true;
		}
        
		switch (args[0].toLowerCase()) {
		case "help": {
			player.sendMessage(Message.fixColor("&7-------- &eHelp&7 --------"));
			player.sendMessage(Message.fixColor("&e/alts [player] [kickall/banall/check] &7Check if the player has alts on the server."));
			player.sendMessage(Message.fixColor("&e/ban [user] [reason] &7Banned the player from the server."));
			player.sendMessage(Message.fixColor("&e/buildmode &7Enable & Disable your buildmode."));
			player.sendMessage(Message.fixColor("&e/chat [clear/lock] &7Clear & Lock the chat."));
			player.sendMessage(Message.fixColor("&e/check [player] &7Check if the player is muted/banned."));
			player.sendMessage(Message.fixColor("&e/feed [player] &7Change the player level food to 20."));
			player.sendMessage(Message.fixColor("&e/fly [player] &7Enable & Disable your flight mode."));
			player.sendMessage(Message.fixColor("&e/flyspeed [player] &7Change your fly speed."));
			player.sendMessage(Message.fixColor("&e/Freeze [player] &7Freezed the player."));
			player.sendMessage(Message.fixColor("&e/gamemode [gamemode] [player] &7Change your gamemode to the gamemode you selected."));
			player.sendMessage(Message.fixColor("&e/god [player] &7Enable & Disable god mode to player."));
			player.sendMessage(Message.fixColor("&e/heal [player] &7Change the player health to 20."));
			player.sendMessage(Message.fixColor("&e/history [player] &7Show you all the punishments."));
			player.sendMessage(Message.fixColor("&e/kick [player] [reason] &7Kick the player from the server."));
			player.sendMessage(Message.fixColor("&e/mute [player] [reason] &7Mute the player from the server."));
			player.sendMessage(Message.fixColor("&e/staffchat &7Enable & Disable your staffchat."));
			player.sendMessage(Message.fixColor("&e/tempban [player] [time] [reason] &7Banned the player from the server for spesific time."));
			player.sendMessage(Message.fixColor("&e/tempmute [player] [time] [reason] &7Muted the player from the server for spesific time."));
			player.sendMessage(Message.fixColor("&e/time [day/night] &7Change the time to day/night."));
			player.sendMessage(Message.fixColor("&e/tpall [player] &7Tp all the server to the player."));
			player.sendMessage(Message.fixColor("&e/tphere [player] &7Tp the player to you."));
			player.sendMessage(Message.fixColor("&e/unban [player] &7Unbanned the player from the server."));
			player.sendMessage(Message.fixColor("&e/unmute [player] &7Unmuted the player from the server."));
			player.sendMessage(Message.fixColor("&e/vanish [player] &7Vanish you from other players."));
			player.sendMessage(Message.fixColor("&e/warn [player] [reason] &7Warn the player."));
			player.sendMessage(Message.fixColor("&7-------- &b1&7/&b1&7 --------"));
			return true;
		}
		case "reload": {
			Main.getInstance().reloadConfig();
			Main.getInstance().saveConfig();
			Main.getLanguageConfig().saveConfig();
			Main.getPunishmentsConfig().saveConfig();
			player.sendMessage(Message.fixColor("&7CommandsPlus has been &eReloded&7."));
			return true;
		}
		default:
			player.sendMessage(Message.fixColor("&7/commandsplus [&ehelp&7/&ereload&7]"));
			return true;
		}
}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("commandsplus.admin")) return null;
		if (args.length == 0) return null;
		return List.of("help", "reload");
	}
}
