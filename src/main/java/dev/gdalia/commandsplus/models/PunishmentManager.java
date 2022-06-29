package dev.gdalia.commandsplus.models;

import java.time.Instant;
import java.util.Optional;

import dev.gdalia.commandsplus.structs.punishments.PunishmentRevoke;
import dev.gdalia.commandsplus.structs.events.PlayerPunishEvent;
import dev.gdalia.commandsplus.structs.events.PlayerPunishRevokeEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.utils.Config;

public class PunishmentManager {

	@Getter
	private static PunishmentManager instance = new PunishmentManager();
		
	public void invoke(Punishment punishment) {
		Config config = Main.getPunishmentsConfig();

		if (config.getConfigurationSection(punishment.getPunishmentUniqueId().toString()) == null)
			config.createSection(punishment.getPunishmentUniqueId().toString());

		Punishments.getInstance().getActivePunishment(punishment.getPunished(), punishment.getType()).ifPresent(activePunish ->
			Punishments.getInstance().writeTo(activePunish, ConfigFields.PunishFields.OVERRIDE, true, false));
		

		Punishments.getInstance().writeTo(punishment, ConfigFields.PunishFields.PUNISHED, punishment.getPunished().toString(), false);
		section.set(ConfigFields.PunishFields.PUNISHED, punishment.getPunished().toString());
		
		Optional.ofNullable(punishment.getExecuter()).ifPresent(uniqueId -> 
			section.set(ConfigFields.PunishFields.EXECUTER, punishment.getExecuter().toString()));
		
		section.set(ConfigFields.PunishFields.TYPE, punishment.getType().name());
		
		Optional.ofNullable(punishment.getExpiry()).ifPresent(uniqueId -> 
			section.set(ConfigFields.PunishFields.EXPIRY, punishment.getExpiry().toEpochMilli()));
		
		section.set(ConfigFields.PunishFields.REASON, punishment.getReason());
		
		config.saveConfig();
		
		Bukkit.getPluginManager().callEvent(new PlayerPunishEvent(Bukkit.getPlayer(punishment.getPunished()), punishment));
	}
	
	public void revoke(PunishmentRevoke punishment) {
		Optional.ofNullable(punishment.getRemovedBy()).ifPresent(punisher -> 
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
