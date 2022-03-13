package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;
import java.util.UUID;

public class PlayerLogListener implements Listener {
	
	@EventHandler
	public void onPreJoin(AsyncPlayerPreLoginEvent event) {
		UUID uniqueId = event.getUniqueId();
		
		Punishments.getInstance().getActivePunishment(uniqueId, PunishmentType.TEMPBAN, PunishmentType.TEMPBAN).ifPresent(punishment -> {
			event.setLoginResult(Result.KICK_BANNED);
			
			String typeName = punishment.getType().name().toLowerCase();
			StringBuilder sb = new StringBuilder();
			Main.getInstance().getConfig().getStringList("ban-lang." + typeName + "-template").forEach(msg -> sb.append(msg).append("\n"));
			
			event.setKickMessage(Message.fixColor(sb.toString()));
		});
	}
	
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if(!Main.getInstance().getConfig().getBoolean("welcome_message")) {
		event.setJoinMessage(null);
		return;
		}
		
		if (!player.hasPermission("commandsplus.vanish.see"))
			PlayerCollection.getVanishPlayers()
				.stream()
				.map(Bukkit::getPlayer)
				.filter(Objects::nonNull)
				.forEach(x -> player.hidePlayer(Main.getInstance(), x));

		String msg = PlayerCollection.getVanishPlayers().contains(uuid) ? null : Message.fixColor("&2&l+ &6" + player.getName() + "&7 Connected");
		event.setJoinMessage(msg);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if(!Main.getInstance().getConfig().getBoolean("welcome_message")) {
			event.setQuitMessage(null);
			return;
		}
		
		String msg = PlayerCollection.getVanishPlayers().contains(uuid) ? null : Message.fixColor("&4&l- &6" + player.getName() + "&7 Disconnected");
		event.setQuitMessage(msg);
	}
}
