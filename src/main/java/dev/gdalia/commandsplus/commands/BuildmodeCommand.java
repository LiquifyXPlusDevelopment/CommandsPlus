package dev.gdalia.commandsplus.commands;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;

import dev.gdalia.commandsplus.utils.StringUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import org.jetbrains.annotations.NotNull;

@CommandAutoRegistration.Command(value = "buildmode")
public class BuildmodeCommand extends BasePlusCommand {

	public BuildmodeCommand() {
		super(false, "buildmode");
	}

	@Override
	public String getDescription() {
		return "allows disabling/enabling your building and entity interacting.";
	}

	@Override
	public String getSyntax() {
		return "/buildmode";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_BUILDMODE;
	}

	@Override
	public boolean isPlayerCommand() {
		return true;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();
        
        if (!PlayerCollection.getBuildmodePlayers().contains(uuid)) PlayerCollection.getBuildmodePlayers().add(uuid);
		else PlayerCollection.getBuildmodePlayers().remove(uuid);

		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.BUILDMODE_MSG.sendFormattedMessage(player, true, StringUtils.getStatusString(PlayerCollection.getBuildmodePlayers().contains(uuid)));
	}

	@Override
	public Map<Integer, List<String>> tabCompletions() {
		return null;
	}
}
