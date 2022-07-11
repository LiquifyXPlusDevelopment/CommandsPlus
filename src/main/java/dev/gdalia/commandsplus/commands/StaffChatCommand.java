package dev.gdalia.commandsplus.commands;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@CommandAutoRegistration.Command(value = "staffchat")
public class StaffChatCommand extends BasePlusCommand {

	public StaffChatCommand() {
		super(false, "staffchat");
	}

	@Override
	public String getDescription() {
		return "Staff chat command to toggle your chat channel to staff chat.";
	}

	@Override
	public String getSyntax() {
		return "/staffchat";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_STAFFCHAT;
	}

	@Override
	public boolean isPlayerCommand() {
		return true;
	}

	@Override
	public @Nullable Map<Integer, List<String>> tabCompletions() {
		return null;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();

        if (!Permission.PERMISSION_STAFFCHAT.hasPermission(player)) {
        	Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.NO_PERMISSION.sendMessage(player, true);
        	return;
        }
			
        if (!PlayerCollection.getStaffchatPlayers().contains(uuid)) PlayerCollection.getStaffchatPlayers().add(uuid);
		else PlayerCollection.getStaffchatPlayers().remove(uuid);

        PlayerCollection.getStaffchatPlayers().remove(uuid);
        Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.STAFFCHAT_TOGGLE.sendMessage(player, true);
	}
}
