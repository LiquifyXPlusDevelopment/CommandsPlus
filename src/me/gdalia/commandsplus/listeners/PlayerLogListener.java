package me.gdalia.commandsplus.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.gdalia.commandsplus.Main;
import me.gdalia.commandsplus.Main.PlayerCollection;
import me.gdalia.commandsplus.models.Punishments;
import me.gdalia.commandsplus.structs.PunishmentType;
import me.gdalia.commandsplus.utils.Utils;

public class PlayerLogListener implements Listener {
	
	@EventHandler
	public void onPreJoin(AsyncPlayerPreLoginEvent event) {
		UUID uniqueId = event.getUniqueId();
		
		Punishments.getInstance().getActivePunishment(uniqueId, PunishmentType.TEMPBAN, PunishmentType.TEMPBAN).ifPresent(punishment -> {
			event.setLoginResult(Result.KICK_BANNED);
			
			String typeName = punishment.getType().name().toLowerCase();
			StringBuilder sb = new StringBuilder();
			Main.getInstance().getConfig().getStringList("ban-lang." + typeName + "-template").forEach(msg -> sb.append(msg).append("\n"));
			
			event.setKickMessage(Utils.color(sb.toString()));
		});
	}
	
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if(Main.getInstance().getConfig().getBoolean("welcome_message") == false) {
		event.setJoinMessage(null);
		return;
		}
		
		if (!player.hasPermission("commandsplus.vanish.see"))
			PlayerCollection.getVanishPlayers()
				.stream()
				.map(Bukkit::getPlayer)
				.forEach(x -> player.hidePlayer(Main.getInstance(), x));

		String msg = PlayerCollection.getVanishPlayers().contains(uuid) ? null : Utils.color("&2&l+ &6" + player.getName() + "&7 Connected");
		event.setJoinMessage(msg);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if(Main.getInstance().getConfig().getBoolean("welcome_message") == false) {
			event.setQuitMessage(null);
			return;
		}
		
		String msg = PlayerCollection.getVanishPlayers().contains(uuid) ? null : Utils.color("&4&l- &6" + player.getName() + "&7 Disconnected");
		event.setQuitMessage(msg);
	}
}
