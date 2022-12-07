package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.StringUtils;
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
import java.util.stream.Stream;

@CommandAutoRegistration.Command(value = "vanish")
public class VanishCommand extends BasePlusCommand {

	public VanishCommand() {
		super(false, "vanish");
	}

	@Override
	public String getDescription() {
		return "Toggles vanish mode (hiding from online players).";
	}

	@Override
	public String getSyntax() {
		return "/vanish";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_VANISH;
	}

	@Override
	public boolean isPlayerCommand() {
		return true;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;
		UUID uuid = player.getUniqueId();
		Stream<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers().stream()
					.filter(x -> !Permission.PERMISSION_VANISH_SEE.hasPermission(x));

        if (!PlayerCollection.getVanishPlayers().contains(uuid)) {
        	PlayerCollection.getVanishPlayers().add(uuid);
        	PlayerCollection.getBuildmodePlayers().add(uuid);
    		player.setAllowFlight(true);
    		player.setFlying(true);
			onlinePlayers.forEach(p -> p.hidePlayer(Main.getInstance(), player));
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        } else {
			PlayerCollection.getVanishPlayers().remove(uuid);
			PlayerCollection.getBuildmodePlayers().remove(uuid);
			onlinePlayers.forEach(p -> p.showPlayer(Main.getInstance(), player));
		}

		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.VANISH_TOGGLE.sendFormattedMessage(player, true, StringUtils.getStatusString(PlayerCollection.getVanishPlayers().contains(uuid)));
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
		return null;
	}
}
