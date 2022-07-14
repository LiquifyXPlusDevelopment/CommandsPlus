package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.HelpPageSystem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

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
		return Permission.PERMISSION_MAIN_GENERAL;
	}

	@Override
	public boolean isPlayerCommand() {
		return false;
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
		return Map.of(1, List.of(
				new TabCompletion(List.of("help"), Permission.PERMISSION_MAIN_HELP),
				new TabCompletion(List.of("reload"), Permission.PERMISSION_MAIN_RELOAD)
		));
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			sender.sendMessage(Message.fixColor("&7/commandsplus [&eHelp&7/&eReload&7]"));
			return;
		}

		switch (args[0].toLowerCase()) {
			case "help" -> {
				if (!Permission.PERMISSION_MAIN_HELP.hasPermission(sender)) {
					Message.NO_PERMISSION.sendMessage(sender, true);
					Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					return;
				}

				int page = (args.length == 1 || !StringUtils.isNumeric(args[1]) || Integer.parseInt(args[1]) == 0) ? 1 : Integer.parseInt(args[1]);

				hps.ShowPage(sender, page);
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			}
			case "reload" -> {
				if (!Permission.PERMISSION_MAIN_RELOAD.hasPermission(sender)) {
					Message.NO_PERMISSION.sendMessage(sender, true);
					Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					return;
				}

				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Main.getInstance().reloadConfig();
				Main.getInstance().getLanguageConfig().reloadConfig();
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
