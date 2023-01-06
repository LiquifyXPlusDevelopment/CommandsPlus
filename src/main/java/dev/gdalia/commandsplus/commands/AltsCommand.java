package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.*;

@CommandAutoRegistration.Command(value = "alts")
public class AltsCommand extends BasePlusCommand {

	public AltsCommand() {
		super(false, "alts");
	}

	/**
	 * /alts {username} {check - banall - kickall}
	 * LABEL ARG0 ARG1
	 */
	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;

		if (args.length == 0) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.DESCRIBE_PLAYER.sendMessage(sender, true);
			return;
		}

		Player target = Bukkit.getPlayerExact(args[0]);

		if (target == null) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.PLAYER_NOT_ONLINE.sendMessage(sender, true);
			return;
		}

		InetAddress address = target.getAddress().getAddress();
		List<? extends Player> alts = Bukkit.getOnlinePlayers().stream()
				.filter(x -> x.getAddress().getAddress().equals(address))
				.filter(x -> !x.getName().equals(target.getName()))
				.toList();

		if (args.length == 1) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			player.sendMessage(ChatColor.GRAY + getSyntax());
			return;
		}

		if (alts.isEmpty() && List.of("check", "kickall", "banall").contains(args[1].toLowerCase())) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			Message.ALTS_CHECK_FAILED.sendFormattedMessage(player, true, target.getName());
			return;
		}

		switch (args[1].toLowerCase()) {
			case "check" -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Message.ALTS_ONLINE.sendFormattedMessage(sender, true, target.getName());
				StringBuilder sb = new StringBuilder();
				alts.forEach(x -> sb.append(Message.fixColor("&7- " + x.getName() + ".\n")));
				Arrays.asList(sb.toString().split("\n")).forEach(sender::sendMessage);
			}
			case "banall" -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Message.ALTS_BANNED.sendFormattedMessage(sender, true, target.getName());
				alts.forEach(x ->
						Optional.ofNullable(Main.getInstance().getConfig().getString("ban-command"))
						.stream()
						.filter(Objects::nonNull)
						.map(kick -> kick.replace("{player}", x.getName()))
						.forEach(kick -> Bukkit.dispatchCommand(sender, kick)));
			}
			case "kickall" -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
				Message.ALTS_KICKED.sendFormattedMessage(sender, true, target.getName());
				alts.forEach(x ->
						Optional.ofNullable(Main.getInstance().getConfig().getString("kick-command"))
						.stream()
						.filter(Objects::nonNull)
						.map(kick -> kick.replace("{player}", x.getName()))
						.forEach(kick -> Bukkit.dispatchCommand(sender, kick)));
			}
			default -> {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
				player.sendMessage(ChatColor.GRAY + getSyntax());
			}
		}
	}

	@Override
	public Map<Integer, List<TabCompletion>> getTabCompletions() {
		return Map.of(2, List.of(new TabCompletion(List.of("check", "banall", "kickall"), getRequiredPermission())));
	}
	@Override
	public String getDescription() {
		return "Check/Ban/Kick user related alts/bots.";
	}

	@Override
	public String getSyntax() {
		return "/alts [player] [kickall/banall/check]";
	}

	@Override
	public Permission getRequiredPermission() {
		return Permission.PERMISSION_ALTS;
	}

	@Override
	public boolean isPlayerCommand() {
		return true;
	}
}
