package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CommandAutoRegistration.Command(value = "freeze")
public class FreezeCommand extends BasePlusCommand {

	public FreezeCommand() {
		super(false, "freeze");
	}

	@Override
	public String getDescription() {
		return "Freezing targeted player for all purposes.";
	}

	@Override
	public String getSyntax() {
		return "/freeze [player]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_FREEZE;
	}

	@Override
	public boolean isPlayerCommand() {
		return false;
	}
	@Override
	public @Nullable Map<Integer, List<String>> tabCompletions() {
		return null;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.DESCRIBE_PLAYER.sendMessage(sender, true);
			return;
		}

		Player target = Bukkit.getPlayerExact(args[0]);
		if (target == null) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        	Message.INVALID_PLAYER.sendMessage(sender, true);
			return;
		}
		
		UUID uuid = target.getUniqueId();
		
		if (!PlayerCollection.getFreezePlayers().contains(uuid)) {
			PlayerCollection.getFreezePlayers().add(uuid);
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Message.FREEZE_TARGET.sendFormattedMessage(target, true, sender.getName());
			Message.FREEZE_PLAYER.sendFormattedMessage(sender, true, target.getName());
			return;
		}
		
		PlayerCollection.getFreezePlayers().remove(uuid);
		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.UNFREEZE_TARGET.sendFormattedMessage(target, true, sender.getName());
		Message.UNFREEZE_PLAYER.sendFormattedMessage(sender, true, target.getName());
	}
}
