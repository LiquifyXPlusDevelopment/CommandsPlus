package dev.gdalia.commandsplus.commands;

import java.util.List;

import dev.gdalia.commandsplus.utils.CommandAutoRegistration;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;

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
		
        if(!Permission.PERMISSION_MAIN.hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.NO_PERMISSION.sendMessage(sender, true);
        	return false;
        }
        
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			player.sendMessage(Message.fixColor("&7/commandsplus [&ehelp&7/&ereload&7]"));
			return true;
		}
        
		switch (args[0].toLowerCase()) {
		case "help": {
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			player.sendMessage(Message.fixColor("&7-------- &eHelp&7 --------"));
			player.sendMessage(Message.fixColor("&e/alts [player] [kickall/banall/check] &7Check if the player has alts on the server."));
			player.sendMessage(Message.fixColor("&e/ban [user] [reason] &7Bans the target player from the server."));
			player.sendMessage(Message.fixColor("&e/buildmode &7Enables/Disables your buildmode."));
			player.sendMessage(Message.fixColor("&e/chat [clear/lock] &7Clears/Locks or Unlocks the chat."));
			player.sendMessage(Message.fixColor("&e/check [player] &7Checks if the target player currently is muted/banned."));
			player.sendMessage(Message.fixColor("&e/feed [player] &7Resets target player food level back to maximum."));
			player.sendMessage(Message.fixColor("&e/fly [player] &7Enables/Disables your flight mode."));
			player.sendMessage(Message.fixColor("&e/flyspeed [player] &7Change your flying speed."));
			player.sendMessage(Message.fixColor("&e/Freeze [player] &7Freezes the target player."));
			player.sendMessage(Message.fixColor("&e/gamemode [gamemode] [player] &7Change your gamemode to the gamemode you selected."));
			player.sendMessage(Message.fixColor("&e/god [player] &7Enables/Disables god mode to target player."));
			player.sendMessage(Message.fixColor("&e/heal [player] &7Resets players health back to maximum"));
			player.sendMessage(Message.fixColor("&e/history [player] &7Gives all punishments logs of target player."));
			player.sendMessage(Message.fixColor("&e/kick [player] [reason] &7Kicks the target player from the server."));
			player.sendMessage(Message.fixColor("&e/mute [player] [reason] &7Mutes the target player."));
			player.sendMessage(Message.fixColor("&e/staffchat &7Enables/Disables your staffchat."));
			player.sendMessage(Message.fixColor("&e/tempban [player] [time] [reason] &7Bans the target player for a specific time."));
			player.sendMessage(Message.fixColor("&e/tempmute [player] [time] [reason] &7Mutes the target player for a specific time."));
			player.sendMessage(Message.fixColor("&e/time [day/night] &7Change the time to day/night."));
			player.sendMessage(Message.fixColor("&e/tpall [player] &7Tp all players online to the target player."));
			player.sendMessage(Message.fixColor("&e/tphere [player] &7Tp the player to you."));
			player.sendMessage(Message.fixColor("&e/unban [player] &7Unbans the target player."));
			player.sendMessage(Message.fixColor("&e/unmute [player] &7Unmutes the target player."));
			player.sendMessage(Message.fixColor("&e/vanish [player] &7Vanishes you from all members online."));
			player.sendMessage(Message.fixColor("&e/warn [player] [reason] &7Warns the target player."));
			player.sendMessage(Message.fixColor("&7-------- &b1&7/&b1&7 --------"));
			return true;
		}
		case "reload": {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Main.getInstance().reloadConfig();
			Main.getLanguageConfig().reloadConfig();
			player.sendMessage(Message.fixColor(Main.getInstance().getPluginPrefix() + "Language.yml has been reloaded!"));
			player.sendMessage(Message.fixColor(Main.getInstance().getPluginPrefix() + "Config.yml has been reloaded!"));
			return true;
		}
		default:
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			player.sendMessage(Message.fixColor("&7/commandsplus [&ehelp&7/&ereload&7]"));
			return true;
		}
}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if(!Permission.PERMISSION_MAIN.hasPermission(sender)) return null;
		if (args.length == 0) return null;
		return List.of("help", "reload");
	}
}
