package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.punishmentdrivers.FlatFilePunishments;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
import dev.gdalia.commandsplus.structs.Flag;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import dev.gdalia.commandsplus.utils.profile.ProfileManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@CommandAutoRegistration.Command(value = {"ban", "kick", "warn", "mute"})
public class PunishPermanentlyCommand extends BasePlusCommand {


	public PunishPermanentlyCommand() {
		super(false, "ban", "kick", "warn", "mute");
	}

	@Override
	public String getDescription() {
		return "Permanently punish players from/in the server.";
	}

	@Override
	public String getSyntax() {
		return "/(ban || kick || warn || mute) [player] [reason]";
	}

	@Override
	public Permission getRequiredPermission() {
		return null;
	}

	@Override
	public boolean isPlayerCommand() {
		return false;
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> getTabCompletions() {
		return null;
	}

	@Override
	public void runCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		PunishmentType type = PunishmentType.canBeType(cmd.getName().toUpperCase()) ? PunishmentType.valueOf(cmd.getName().toUpperCase()) : null;

		if (type == null) {
			if (sender instanceof Player player) player.kickPlayer(Message.fixColor("&6how?"));
			sender.sendMessage(Message.fixColor("&6how?"));
			return;
		}

		if (!type.getRequiredPermission().hasPermission(sender)) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.COMMAND_NO_PERMISSION.sendMessage(sender, true);
			return;
		}
		
		if (args.length <= 1) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
			sender.sendMessage(ChatColor.GRAY + getSyntax());
			return;
		}

		if (!type.isConstrictive() && Bukkit.getPlayerExact(args[0]) == null) {
			Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			Message.PLAYER_NOT_ONLINE.sendMessage(sender, true);
			return;
		}

		int startIndex = 1;
		List<Flag> flagList = new ArrayList<>();
		for (int i = 1; i < args.length; i++) {

			String flagStr = args[i];

			if (flagStr.length() == 2 && Flag.isAFlag(flagStr)) {
				char typeChar = flagStr.charAt(1);
				Optional<Flag> futureFlag = Flag.getByChar(typeChar);
				startIndex++;

				if (futureFlag.isEmpty()) {
					Message.FLAG_UNKNOWN.sendFormattedMessage(sender, true, flagStr);
					return;
				}

				if (!flagList.contains(futureFlag.get()))
					flagList.add(futureFlag.get());
			}
		}

		String reason = StringUtils.join(args, " ", startIndex, args.length);
		UUID executor = Optional.of(sender)
			.filter(Player.class::isInstance)
			.map(Player.class::cast)
			.map(Player::getUniqueId)
			.orElse(null);



		if (Bukkit.getPlayerExact(args[0]) != null) {
			Player target = Bukkit.getPlayerExact(args[0]);
			Punishment punishment = new Punishment(
				UUID.randomUUID(),
				target.getUniqueId(),
				executor,
				type,
				reason,
				false);

			punishAction(sender, punishment, target.getName(), flagList);
			return;
		}

		ProfileManager.getInstance().getProfileAsync(args[0]).whenComplete((profile, throwable) -> {
			if (throwable != null) {
				Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				Message.PLAYER_NOT_EXIST.sendMessage(sender, true);
				return;
			}

			Punishment punishment = new Punishment(
				UUID.randomUUID(),
				profile.playerUUID(),
				executor,
				type,
				reason,
				false);

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> punishAction(sender, punishment, profile.playerName(), flagList));
		});
	}

	public void punishAction(CommandSender requester, Punishment punishment, String targetName, List<Flag> flags) {
		Optional<Punishment> activePunishment =
		FlatFilePunishments.getInstance().getActivePunishment(
			punishment.getPunished(),
			punishment.getType(),
			PunishmentType.canBeType("TEMP" + punishment.getType().name()) ? PunishmentType.valueOf("TEMP" + punishment.getType().name()) : punishment.getType());

		if (activePunishment.isPresent() && !flags.contains(Flag.OVERRIDE)) {
			punishment.getType().getAlreadyPunishedMessage().sendMessage(requester, true);
			if (Flag.OVERRIDE.getRequiredPermission().hasPermission(requester)) {
				Message.FLAG_OVERRIDE_OPTIONAL.sendMessage(requester, false);
				Message.playSound(requester, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
			}

			return;
		}

		PunishmentManager.getInstance().invoke(punishment, flags.toArray(Flag[]::new));
		Message.playSound(requester, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		punishment.getType().getPunishSuccessfulMessage().sendFormattedMessage(requester, true, targetName, punishment.getReason());
	}
}
