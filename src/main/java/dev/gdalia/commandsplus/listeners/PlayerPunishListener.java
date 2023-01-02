package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.events.PunishmentInvokeEvent;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class PlayerPunishListener implements Listener {

	@EventHandler
	public void onPunish(PunishmentInvokeEvent event) {
		PunishmentType type = event.getPunishment().getType();
		OfflinePlayer player = Bukkit.getOfflinePlayer(event.getPunishment().getPunished());

		if (!player.isOnline() || player.getPlayer() == null) return;
		if (type.isKickable()) {
			String typeName = type.name().toLowerCase();
			List<String> message = Main.getInstance().getConfig().getStringList("punishments-lang." + typeName + "-template");
			StringBuilder sb = new StringBuilder();

			if (type.isPermanent()) {
				message.forEach(msg -> sb.append(msg.replace("%reason%", event.getPunishment().getReason())).append("\n"));
				player.getPlayer().kickPlayer(Message.fixColor(sb.toString()));
				return;
			}
			
		    Instant one = Instant.now();
		    Instant two = event.getPunishment().getExpiry();
		    Duration res = Duration.between(one, two).plusSeconds(1);
			
			message.forEach(msg -> sb.append(msg
					.replace("%time%", StringUtils.formatTime(res))
					.replace("%reason%", event.getPunishment().getReason())).append("\n"));

			player.getPlayer().kickPlayer(Message.fixColor(sb.toString()));
		}
		
		if (type.isConstrictive() && !type.isKickable()) {
			if (!type.isPermanent()) {
			    Instant one = Instant.now();
			    Instant two = event.getPunishment().getExpiry();
			    Duration res = Duration.between(one, two);
				type.getPunishTargetMessage().sendFormattedMessage(player.getPlayer(), true, StringUtils.formatTime(res), event.getPunishment().getReason());
			} else type.getPunishTargetMessage().sendFormattedMessage(player.getPlayer(), true, event.getPunishment().getReason());
		}
	}
}
