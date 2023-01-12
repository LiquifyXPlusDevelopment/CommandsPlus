package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.Main.PlayerCollection;
import dev.gdalia.commandsplus.inventory.InventoryUtils;
import dev.gdalia.commandsplus.models.punishmentdrivers.FlatFilePunishments;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.utils.StringUtils;
import dev.gdalia.commandsplus.utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class PlayerLogListener implements Listener {
	
	@EventHandler
	public void onPreJoin(AsyncPlayerPreLoginEvent event) {
		UUID uniqueId = event.getUniqueId();
				
		FlatFilePunishments.getInstance().getActivePunishment(uniqueId, PunishmentType.TEMPBAN, PunishmentType.BAN).ifPresent(punishment -> {
			event.setLoginResult(Result.KICK_BANNED);
			
			String typeName = punishment.getType().name().toLowerCase();
			StringBuilder sb = new StringBuilder();
			
			List<String> message = Main.getInstance().getConfig().getStringList("punishments-lang." + typeName + "-template");
			
			if (punishment.getExpiry() == null) {
				message.forEach(msg -> sb.append(msg.replace("%reason%", punishment.getReason())).append("\n"));
				event.setKickMessage(Message.fixColor(sb.toString()));
				return;
			}
			
		    Instant one = Instant.now();
		    Instant two = punishment.getExpiry();
		    Duration res = Duration.between(one, two);
			
			message.forEach(msg -> sb.append(msg.replace("%reason%", punishment.getReason())
					.replace("%time%", StringUtils.formatTime(res))).append("\n"));
			
			event.setKickMessage(Message.fixColor(sb.toString()));
		});
	}
	
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		if (player.isOp() || Permission.PERMISSION_ADMIN_RELOAD.hasPermission(player)) {
			UpdateChecker.get()
				.getLastResult()
				.filter(UpdateChecker.UpdateResult::requiresUpdate)
				.ifPresent(updateResult -> player.spigot().sendMessage(Main.getInstance().updateAvailableClickable()));
		}

		if (Main.getInstance().getConfig().getBoolean("disable_welcome_message")) {
			event.setJoinMessage(null);
			return;
		}
		
		if (PlayerCollection.getVanishPlayers().contains(uuid)) {
			Bukkit.getOnlinePlayers()
			.stream()
			.filter(p -> p.canSee(player) && !Permission.PERMISSION_VANISH_SEE.hasPermission(p))
			.forEach(p -> p.hidePlayer(Main.getInstance(), player));
		}

		if (!Permission.PERMISSION_VANISH_SEE.hasPermission(player))
			PlayerCollection.getVanishPlayers()
				.stream()
				.map(Bukkit::getPlayer)
				.forEach(x -> player.hidePlayer(Main.getInstance(), x));

		String msg = PlayerCollection.getVanishPlayers().contains(uuid) ? null : Message.fixColor("&2&l+ &6" + player.getName() + "&7 Connected");
		Message.playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
		event.setJoinMessage(msg);
		player.resetTitle();
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();

		InventoryUtils.getInstance().commentText.remove(uuid);

		if (Main.getInstance().getConfig().getBoolean("disable_welcome_message")) {
			event.setQuitMessage(null);
			return;
		}
		
		String msg = PlayerCollection.getVanishPlayers().contains(uuid) ? null : Message.fixColor("&4&l- &6" + player.getName() + "&7 Disconnected");
		event.setQuitMessage(msg);
	}
}
