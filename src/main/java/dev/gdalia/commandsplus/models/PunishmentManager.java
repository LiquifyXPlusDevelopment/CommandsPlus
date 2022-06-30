package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.events.PlayerPunishEvent;
import dev.gdalia.commandsplus.structs.events.PlayerPunishRevokeEvent;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentRevoke;
import dev.gdalia.commandsplus.utils.Config;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.time.Instant;
import java.util.Optional;

public class PunishmentManager {

	@Getter
	private static final PunishmentManager instance = new PunishmentManager();
		
	public void invoke(Punishment punishment) {
		Config config = Main.getPunishmentsConfig();

		if (config.getConfigurationSection(punishment.getPunishmentUniqueId().toString()) == null)
			config.createSection(punishment.getPunishmentUniqueId().toString());

		Punishments.getInstance().getActivePunishment(punishment.getPunished(), punishment.getType()).ifPresent(activePunish ->
			Punishments.getInstance().writeTo(activePunish, ConfigFields.PunishFields.OVERRIDE, true, false));
		

		Punishments.getInstance().writeTo(punishment, ConfigFields.PunishFields.PUNISHED, punishment.getPunished().toString(), false);

		Optional.ofNullable(punishment.getExecuter()).ifPresent(uniqueId ->
				Punishments.getInstance().writeTo(punishment, ConfigFields.PunishFields.EXECUTER, punishment.getExecuter().toString(), false));

		Punishments.getInstance().writeTo(punishment, ConfigFields.PunishFields.TYPE, punishment.getType().name(), false);
		
		Optional.ofNullable(punishment.getExpiry()).ifPresent(uniqueId ->
				Punishments.getInstance().writeTo(punishment, ConfigFields.PunishFields.EXPIRY, punishment.getExpiry().toEpochMilli(), false));

		Punishments.getInstance().writeTo(punishment, ConfigFields.PunishFields.REASON, punishment.getReason(), true);

		Bukkit.getPluginManager().callEvent(new PlayerPunishEvent(Bukkit.getPlayer(punishment.getPunished()), punishment));
	}
	
	public void revoke(PunishmentRevoke punishment) {
		Optional.ofNullable(punishment.getRemovedBy()).ifPresent(executer ->
			Punishments.getInstance().writeTo(
						punishment.getPunishment(),
						ConfigFields.PunishFields.REMOVED_BY,
						punishment.getRemovedBy().toString(),
						false));
		
		Punishments.getInstance().writeTo(
				punishment.getPunishment(),
				ConfigFields.PunishFields.EXPIRY,
				Instant.now().toEpochMilli(),
				true);
		
		Bukkit.getPluginManager().callEvent(new PlayerPunishRevokeEvent(punishment));
		
	}
}
