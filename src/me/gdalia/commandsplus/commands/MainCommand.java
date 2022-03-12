package me.gdalia.commandsplus.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.gdalia.commandsplus.Main;
import me.gdalia.commandsplus.structs.Message;
import me.gdalia.commandsplus.utils.Utils;

@me.gdalia.commandsplus.utils.CommandAutoRegistration.Command(value = "commandsplus")
public class MainCommand implements CommandExecutor, TabCompleter {

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
        
		if (args.length <= 0) {
			player.sendMessage(Utils.color("&7/commandsplus [&ehelp&7/&ereload&7]"));
			return true;
		}
        
		switch (args[0].toLowerCase()) {
		case "help": {
			player.sendMessage(Utils.color("&7-------- &eHelp&7 --------"));
			player.sendMessage(Utils.color("&e/alts [player] [kickall/banall/check] &7Check if the player has alts on the server."));
			player.sendMessage(Utils.color("&e/ban [user] [reason] &7Banned the player from the server."));
			player.sendMessage(Utils.color("&e/buildmode &7Enable & Disable your buildmode."));
			player.sendMessage(Utils.color("&e/chat [clear/lock] &7Clear & Lock the chat."));
			player.sendMessage(Utils.color("&e/check [player] &7Check if the player is muted/banned."));
			player.sendMessage(Utils.color("&e/feed [player] &7Change the player level food to 20."));
			player.sendMessage(Utils.color("&e/fly [player] &7Enable & Disable your flight mode."));
			player.sendMessage(Utils.color("&e/Freeze [player] &7Freezed the player."));
			player.sendMessage(Utils.color("&e/gamemode [player] [GameMode] &7Change your gamemode to the gamemode you selected."));
			player.sendMessage(Utils.color("&e/god [player] &7Enable & Disable god mode to player."));
			player.sendMessage(Utils.color("&e/heal [player] &7Change the player health to 20."));
			player.sendMessage(Utils.color("&e/history [player] &7Show you all the punishments."));
			player.sendMessage(Utils.color("&e/kick [player] [reason] &7Kick the player from the server."));
			player.sendMessage(Utils.color("&e/mute [player] [reason] &7Mute the player from the server."));
			player.sendMessage(Utils.color("&e/staffchat &7Enable & Disable your staffchat."));
			player.sendMessage(Utils.color("&e/tempban [player] [time] [reason] &7Banned the player from the server for spesific time."));
			player.sendMessage(Utils.color("&e/tempmute [player] [time] [reason] &7Muted the player from the server for spesific time."));
			player.sendMessage(Utils.color("&e/time [day/night] &7Change the time to day/night."));
			player.sendMessage(Utils.color("&e/tpall [player] &7Tp all the server to the player."));
			player.sendMessage(Utils.color("&e/tphere [player] &7Tp the player to you."));
			player.sendMessage(Utils.color("&e/unban [player] &7Unbanned the player from the server."));
			player.sendMessage(Utils.color("&e/unmute [player] &7Unmuted the player from the server."));
			player.sendMessage(Utils.color("&e/vanish [player] &7Vanish you from other players."));
			player.sendMessage(Utils.color("&e/warn [player] [reason] &7Warn the player."));
			player.sendMessage(Utils.color("&7-------- &b1&7/&b1&7 --------"));
			return true;
		}
		case "reload": {
			Main.getInstance().reloadConfig();
			Main.getInstance().saveConfig();
			player.sendMessage(Utils.color("&7CommandsPlus has been &eReloded&7."));
			return true;
		}
		default:
			player.sendMessage(Utils.color("&7/commandsplus [&ehelp&7/&ereload&7]"));
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
