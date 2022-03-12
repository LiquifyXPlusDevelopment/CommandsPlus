package me.gdalia.commandsplus.models;

import java.time.Instant;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import lombok.Getter;
import me.gdalia.commandsplus.Main;
import me.gdalia.commandsplus.structs.Punishment;
import me.gdalia.commandsplus.structs.PunishmentRevoke;
import me.gdalia.commandsplus.structs.events.PlayerPunishEvent;
import me.gdalia.commandsplus.structs.events.PlayerPunishRevokeEvent;
import me.gdalia.commandsplus.utils.Config;

public class PunishmentManager {

	@Getter
	private static PunishmentManager instance = new PunishmentManager();
		
	public void invoke(Punishment punishment) {
		Config config = Main.getPunishmentsConfig();
		
		Punishments.getInstance().getActivePunishment(punishment.getPunished(), punishment.getType()).ifPresent(activePunish ->
			Punishments.getInstance().writeTo(activePunish, ConfigFields.PunishFields.OVERRIDE, true, false));
		
		ConfigurationSection section = config.createSection(punishment.getPunishmentUniqueId().toString());
		
		section.set(ConfigFields.PunishFields.PUNISHED, punishment.getPunished().toString());
		
		Optional.ofNullable(punishment.getExecuter()).ifPresent(uniqueId -> 
			section.set(ConfigFields.PunishFields.EXECUTER, punishment.getPunished().toString()));
		
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
						punishment.getRemovedBy(),
						false));
		
		Punishments.getInstance().writeTo(
				punishment.getPunishment(),
				ConfigFields.PunishFields.EXPIRY,
				Instant.now().toEpochMilli(),
				true);
		
		Bukkit.getPluginManager().callEvent(new PlayerPunishRevokeEvent(punishment));
		
	}
}
