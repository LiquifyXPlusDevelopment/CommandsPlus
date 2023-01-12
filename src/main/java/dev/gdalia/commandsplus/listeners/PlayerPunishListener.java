package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Flag;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.events.PunishmentInvokeEvent;
import dev.gdalia.commandsplus.structs.events.PunishmentOverrideEvent;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class PlayerPunishListener implements Listener {

	@EventHandler
	public void onPunish(PunishmentInvokeEvent event) {
		PunishmentType type = event.getPunishment().getType();
		OfflinePlayer punished = Bukkit.getOfflinePlayer(event.getPunishment().getPunished());
		String executorName = event.getPunishment().getExecutor() != null
				? Bukkit.getPlayer(event.getPunishment().getExecutor()).getName()
				: "CONSOLE";

		List<Flag> flags = Arrays.asList(event.getFlags());
		Instant now = Instant.now();
		Instant future = event.getPunishment().getExpiry();
		Duration res = type.isPermanent() || !type.isConstrictive() ? Duration.ZERO : Duration.between(now, future).plusSeconds(1);


		Bukkit.getOnlinePlayers()
				.stream()
				.filter(player -> !flags.contains(Flag.SILENT) || Permission.PERMISSION_PUNISH_GENERAL.hasPermission(player))
				.forEach(player -> {
					if (flags.contains(Flag.NO_NAME))
						type.getBroadcastMessageNoName().sendFormattedMessage(
								player,
								false,
								punished.getName(),
								type.isPermanent() ? event.getPunishment().getReason() : StringUtils.formatTime(res), event.getPunishment().getReason());
					else type.getBroadcastMessage().sendFormattedMessage(
							player,
							false,
							punished.getName(), executorName,
							type.isPermanent() ? event.getPunishment().getReason() : StringUtils.formatTime(res), event.getPunishment().getReason());
				});


		if (!punished.isOnline() || punished.getPlayer() == null) return;
		if (type.isKickable()) {
			String typeName = type.name().toLowerCase();
			List<String> message = Main.getInstance().getConfig().getStringList("punishments-lang." + typeName + "-template");
			StringBuilder sb = new StringBuilder();

			if (type.isPermanent()) {
				message.forEach(msg -> sb.append(msg.replace("%reason%", event.getPunishment().getReason())).append("\n"));
				punished.getPlayer().kickPlayer(Message.fixColor(sb.toString()));
				return;
			}

			message.forEach(msg -> sb.append(msg
					.replace("%time%", StringUtils.formatTime(res))
					.replace("%reason%", event.getPunishment().getReason())).append("\n"));

			punished.getPlayer().kickPlayer(Message.fixColor(sb.toString()));
		}

		if (type.isConstrictive() && !type.isKickable()) {
			if (!type.isPermanent()) {
				type.getPunishTargetMessage().sendFormattedMessage(punished.getPlayer(), false, StringUtils.formatTime(res), event.getPunishment().getReason());
			} else type.getPunishTargetMessage().sendFormattedMessage(punished.getPlayer(), false, event.getPunishment().getReason());
		}
	}

	@EventHandler
	public void onPunishmentOverride(PunishmentOverrideEvent event) {

	}
}
