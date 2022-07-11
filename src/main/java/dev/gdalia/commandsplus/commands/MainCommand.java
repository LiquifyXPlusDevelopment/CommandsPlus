package dev.gdalia.commandsplus.commands;

import java.util.List;
import java.util.Map;

import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;

import dev.gdalia.commandsplus.utils.HelpPageSystem;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandAutoRegistration.Command(value = "commandsplus")
public class MainCommand extends BasePlusCommand {

	private static final HelpPageSystem hps = new HelpPageSystem(6, BasePlusCommand.getCommandMap().values()
			.stream()
			.map(basePlusCommand -> Message.fixColor("&e" + basePlusCommand.getSyntax() + " &7- &b" + basePlusCommand.getDescription()))
			.toArray(String[]::new));


	public MainCommand() {
		super(false, "commandsplus");
	}

	@Override
	public String getDescription() {
		return "Main command for CommandsPlus plugin.";
	}

	@Override
	public String getSyntax() {
		return "/commandsplus [help/reload]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_MAIN;
	}

	@Override
	public boolean isPlayerCommand() {
		return false;
	}

	@Override
	public @Nullable Map<Integer, List<String>> tabCompletions() {
		return Map.of(1, List.of("help", "reload"));
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			player.sendMessage(Message.fixColor("&7/commandsplus [&eHelp&7/&eReload&7]"));
			return true;
		}

		switch (args[0].toLowerCase()) {
			case "help" -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				sender.sendMessage(Message.fixColor("&7-------- &eHelp&7 --------"));
				sender.sendMessage(Message.fixColor("&e/alts [player] [kickall/banall/check] &7Check if the player has alts on the server."));
				sender.sendMessage(Message.fixColor("&e/ban [user] [reason] &7Bans the target player from the server."));
				sender.sendMessage(Message.fixColor("&e/buildmode &7Enables/Disables your buildmode."));
				sender.sendMessage(Message.fixColor("&e/chat [clear/lock] &7Clears/Locks or Unlocks the chat."));
				sender.sendMessage(Message.fixColor("&e/check [player] &7Checks if the target player currently is muted/banned."));
				sender.sendMessage(Message.fixColor("&e/feed [player] &7Resets target player food level back to maximum."));
				sender.sendMessage(Message.fixColor("&e/fly [player] &7Enables/Disables your flight mode."));
				sender.sendMessage(Message.fixColor("&e/flyspeed [amount] &7Change your flying speed."));
				sender.sendMessage(Message.fixColor("&e/freeze [player] &7Freezes the target player."));
				sender.sendMessage(Message.fixColor("&e/gamemode [gamemode] [player] &7Change your gamemode to the gamemode you selected."));
				sender.sendMessage(Message.fixColor("&e/god [player] &7Enables/Disables god mode to target player."));
				sender.sendMessage(Message.fixColor("&e/heal [player] &7Resets players health level back to maximum"));
				sender.sendMessage(Message.fixColor("&e/history [player] &7Gives all punishments logs of target player."));
				sender.sendMessage(Message.fixColor("&e/kick [player] [reason] &7Kicks the target player from the server."));
				sender.sendMessage(Message.fixColor("&e/mute [player] [reason] &7Mutes the target player."));
				sender.sendMessage(Message.fixColor("&e/staffchat &7Enables/Disables your staffchat."));
				sender.sendMessage(Message.fixColor("&e/tempban [player] [time] [reason] &7Bans the target player for a specific time."));
				sender.sendMessage(Message.fixColor("&e/tempmute [player] [time] [reason] &7Mutes the target player for a specific time."));
				sender.sendMessage(Message.fixColor("&e/time [day/night] &7Change the time to day/night."));
				sender.sendMessage(Message.fixColor("&e/tpall [player] &7Tp all online players to the target player."));
				sender.sendMessage(Message.fixColor("&e/tphere [player] &7Tp the target player to you."));
				sender.sendMessage(Message.fixColor("&e/unban [player] &7Unbans the target player."));
				sender.sendMessage(Message.fixColor("&e/unmute [player] &7Unmutes the target player."));
				sender.sendMessage(Message.fixColor("&e/vanish &7Vanishes you from all members online."));
				sender.sendMessage(Message.fixColor("&e/warn [player] [reason] &7Warns the target player."));
				sender.sendMessage(Message.fixColor("&7-------- &b1&7/&b1&7 --------"));
				//TODO Finish with the HelpPageSystem.java.
			}
			case "reload" -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Main.getInstance().reloadConfig();
				Main.getLanguageConfig().reloadConfig();
				sender.sendMessage(Message.fixColor(Main.getInstance().getPluginPrefix() + "&6Language.yml&7 has been &aReloaded&7!"));
				sender.sendMessage(Message.fixColor(Main.getInstance().getPluginPrefix() + "&6Config.yml&7 has been &aReloaded&7!"));
			}
			default -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
				sender.sendMessage(Message.fixColor("&7/commandsplus [&eHelp&7/&eReload&7]"));
			}
		}
	}
}
