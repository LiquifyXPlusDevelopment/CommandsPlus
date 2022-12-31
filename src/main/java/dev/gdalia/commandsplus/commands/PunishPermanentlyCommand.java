package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.BasePlusCommand;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
		return Permission.PERMISSION_PUNISH;
	}

	@Override
	public boolean isPlayerCommand() {
		return false;
	}

	@Override
	public @Nullable Map<Integer, List<TabCompletion>> tabCompletions() {
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

		String reason = StringUtils.join(args, " ", 1, args.length);
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

			punishAction(sender, punishment, target.getName(), false);
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

			punishAction(sender, punishment, profile.playerName(), true);
		});
	}

	public void punishAction(CommandSender requester, Punishment punishment, String targetName, boolean isAsync) {
		Punishments.getInstance().getActivePunishment(
				punishment.getPunished(),
				punishment.getType(), PunishmentType.valueOf("TEMP" + punishment.getType().name()))
				.ifPresentOrElse(unused -> {
					punishment.getType().getAlreadyPunishedMessage().sendMessage(requester, true);
					Message.playSound(requester, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
				}, () -> {
					if (isAsync) Bukkit.getScheduler().runTask(Main.getInstance(), () -> PunishmentManager.getInstance().invoke(punishment));
					else PunishmentManager.getInstance().invoke(punishment);
					Message.playSound(requester, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
					punishment.getType().getPunishSuccessfulMessage().sendFormattedMessage(requester, true, targetName, punishment.getReason());
				});
	}
}
