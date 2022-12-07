package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.events.PunishmentInvokeEvent;
import dev.gdalia.commandsplus.structs.events.PunishmentRevokeEvent;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentRevoke;
import dev.gdalia.commandsplus.utils.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class PunishmentManager {

	@Getter
	private static final PunishmentManager instance = new PunishmentManager();
	public void invoke(Punishment punishment) {
		Config config = Main.getInstance().getPunishmentsConfig();

		Punishments.getInstance().getActivePunishment(punishment.getPunished(), punishment.getType()).ifPresent(activePunish ->
				Punishments.getInstance().writeTo(activePunish, ConfigFields.PunishFields.OVERRIDE, true, false));

		ConfigurationSection section = config.createSection(punishment.getPunishmentUniqueId().toString());

		section.set(ConfigFields.PunishFields.PUNISHED, punishment.getPunished().toString());

		Optional.ofNullable(punishment.getExecutor()).ifPresent(uniqueId ->
				section.set(ConfigFields.PunishFields.EXECUTOR, punishment.getExecutor().toString()));

		section.set(ConfigFields.PunishFields.TYPE, punishment.getType().name());

		Optional.ofNullable(punishment.getExpiry()).ifPresent(uniqueId ->
				section.set(ConfigFields.PunishFields.EXPIRY, punishment.getExpiry().toEpochMilli()));

		section.set(ConfigFields.PunishFields.REASON, punishment.getReason());
		config.saveConfig();

		Bukkit.getPluginManager().callEvent(new PunishmentInvokeEvent(punishment));
	}

	public void revoke(PunishmentRevoke punishment) {
		AtomicReference<String> executor = new AtomicReference<>("CONSOLE");
		Optional.ofNullable(punishment.getRemovedBy()).ifPresent(uuid -> executor.set(uuid.toString()));

		Punishments.getInstance().writeTo(
				punishment.getPunishment(),
				ConfigFields.PunishFields.REMOVED_BY,
				executor.get(),
				false);

		Punishments.getInstance().writeTo(
				punishment.getPunishment(),
				ConfigFields.PunishFields.EXPIRY,
				Instant.now().toEpochMilli(),
				true);

		Bukkit.getPluginManager().callEvent(new PunishmentRevokeEvent(punishment));

	}
}
