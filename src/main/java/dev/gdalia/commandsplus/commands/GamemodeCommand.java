package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Gamemode;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@CommandAutoRegistration.Command(value = "gamemode")
public class GamemodeCommand extends BasePlusCommand {

	public GamemodeCommand() {
		super(false, "gamemode");
	}

	@Override
	public String getDescription() {
		return "Better performant Gamemode Command.";
	}

	@Override
	public String getSyntax() {
		return "/gamemode [mode] [player]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_GAMEMODE;
	}

	@Override
	public boolean isPlayerCommand() {
		return true;
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
		return Map.of(1, List.of(
				new TabCompletion(List.of("s", "0"), Permission.PERMISSION_GAMEMODE_SURVIVAL),
				new TabCompletion(List.of("a", "2"), Permission.PERMISSION_GAMEMODE_ADVENTURE),
				new TabCompletion(List.of("c", "1"), Permission.PERMISSION_GAMEMODE_CREATIVE),
				new TabCompletion(List.of("sp", "3"), Permission.PERMISSION_GAMEMODE_SPECTATOR)));
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;

		if (args.length == 0) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			player.sendMessage(ChatColor.GRAY + getSyntax());
			return;
		}

		final Gamemode[] setGamemode = new Gamemode[1];

		if (StringUtils.isNumeric(args[0])) {
			Player finalPlayer = player;
			Gamemode.getFromInt(Integer.parseInt(args[0]))
				.ifPresentOrElse(
					gamemode -> setGamemode[0] = gamemode,
					() -> finalPlayer.sendMessage(ChatColor.GRAY + getSyntax()));
		} else {
				Player finalPlayer1 = player;
				Gamemode.getFromSubCommand(args[0].toLowerCase())
					.ifPresentOrElse(
							gamemode -> setGamemode[0] = gamemode,
							() -> finalPlayer1.sendMessage(ChatColor.GRAY + getSyntax()));
		}

		if (args.length >= 2) {
			if (Bukkit.getPlayerExact(args[1]) == null) {
				Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				Message.PLAYER_NOT_ONLINE.sendMessage(player, true);
				return;
			} else player = Bukkit.getPlayer(args[1]);
		}
    	
    	if (player.getGameMode() == setGamemode[0].getAsBukkit()) {
    		boolean isSender = player.equals(sender);
    		Message message = isSender ? Message.GAMEMODE_ALREADY_SET : Message.GAMEMODE_ALREADY_SET_OTHER;
    		Object[] values = !isSender ? new Object[] {player.getName(), setGamemode[0].name()} : new Object[] {setGamemode[0].name()};
    		Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    		message.sendFormattedMessage(sender, true, values);
    		return;
    	}
    	
    	
    	player.setGameMode(setGamemode[0].getAsBukkit());
		if (player.equals(sender)) {
			Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Message.GAMEMODE_CHANGED.sendFormattedMessage(player, true, setGamemode[0].name());
			return;
		}
		
		Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		Message.GAMEMODE_CHANGED_OTHER.sendFormattedMessage(sender, true, player.getName(), setGamemode[0].name());
		Message.GAMEMODE_CHANGED_BY_OTHER.sendFormattedMessage(player, true, setGamemode[0].name(), sender.getName());
	}
}
