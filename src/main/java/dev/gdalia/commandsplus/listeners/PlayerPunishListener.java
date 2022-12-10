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

		if (!player.isOnline()) return;
		if (List.of(PunishmentType.BAN, PunishmentType.TEMPBAN, PunishmentType.KICK).contains(type)) {

			String typeName = type.name().toLowerCase();
			List<String> message = Main.getInstance().getConfig().getStringList("punishments-lang." + typeName + "-template");
			StringBuilder sb = new StringBuilder();
						
			if (event.getPunishment().getExpiry() == null) {
				message.forEach(msg -> sb.append(msg.replace("%reason%", event.getPunishment().getReason())).append("\n"));
				player.getPlayer().kickPlayer(Message.fixColor(sb.toString()));
				return;
			}
			
		    Instant one = Instant.now();
		    Instant two = event.getPunishment().getExpiry();
		    Duration res = Duration.between(one, two);
			
			message.forEach(msg -> sb.append(msg.replace("%time%", StringUtils.formatTime(res)).replace("%reason%", event.getPunishment().getReason())).append("\n"));
			player.getPlayer().kickPlayer(Message.fixColor(sb.toString()));
		}
		
		if (List.of(PunishmentType.MUTE, PunishmentType.TEMPMUTE).contains(type)) {
			if (!type.isPermanent()) {
			    Instant one = Instant.now();
			    Instant two = event.getPunishment().getExpiry();
			    Duration res = Duration.between(one, two);
				Message.valueOf("TARGET_" + type.getNameAsPunishMsg().toUpperCase() + "_MESSAGE").sendFormattedMessage(player.getPlayer(), true, StringUtils.formatTime(res));
			} else Message.PUNISH_MUTE_MESSAGE.sendMessage(player.getPlayer(), true);
		}
	}
}
