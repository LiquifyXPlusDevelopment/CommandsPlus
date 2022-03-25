package dev.gdalia.commandsplus.listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.PunishmentType;
import dev.gdalia.commandsplus.structs.events.PlayerPunishEvent;
import dev.gdalia.commandsplus.utils.StringUtils;

public class PlayerPunishListener implements Listener {

	@EventHandler
	public void onPunish(PlayerPunishEvent event) {
		PunishmentType type = event.getPunishment().getType();
		
		if (!event.getPlayer().isOnline()) return;
		
		if (List.of(PunishmentType.BAN, PunishmentType.TEMPBAN, PunishmentType.KICK).contains(type)) {
			String typeName = type.name().toLowerCase();
						
			List<String> message = Main.getInstance().getConfig().getStringList("punishments-lang." + typeName + "-template");

			StringBuilder sb = new StringBuilder();
						
			if (event.getPunishment().getExpiry() == null) {
				message.forEach(msg -> sb.append(msg.replace("%reason%", event.getPunishment().getReason())).append("\n"));
				event.getPlayer().kickPlayer(Message.fixColor(sb.toString()));
				return;
			}
			
			String expiryAsString = StringUtils.createTimeFormatter(event.getPunishment().getExpiry(), "HH:mm, dd/MM/uu");
			
			message.forEach(msg -> sb.append(msg.replace("%time%", expiryAsString).replace("%reason%", event.getPunishment().getReason())).append("\n"));
			event.getPlayer().kickPlayer(Message.fixColor(sb.toString()));
		}
		
		if (List.of(PunishmentType.MUTE, PunishmentType.TEMPMUTE).contains(type)) {
			if (!type.isPermanent()) {
				String expiryAsString = StringUtils.createTimeFormatter(event.getPunishment().getExpiry(), "HH:mm, dd/MM/uu");
				Message.valueOf("TARGET_" + type.getNameAsPunishMsg().toUpperCase() + "_MESSAGE").sendFormattedMessage(event.getPlayer(), true, expiryAsString);
			} else Message.TARGET_MUTED_MESSAGE.sendMessage(event.getPlayer(), true);
		}
	}
}
