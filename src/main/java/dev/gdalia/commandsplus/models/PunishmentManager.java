package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.punishmentdrivers.FlatFilePunishments;
import dev.gdalia.commandsplus.structs.Flag;
import dev.gdalia.commandsplus.structs.events.PunishmentInvokeEvent;
import dev.gdalia.commandsplus.structs.events.PunishmentOverrideEvent;
import dev.gdalia.commandsplus.structs.events.PunishmentRevokeEvent;
import dev.gdalia.commandsplus.structs.exceptions.PunishmentRevokeConvertException;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentRevoke;
import dev.gdalia.commandsplus.utils.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class PunishmentManager {

	@Getter
	private static final PunishmentManager instance = new PunishmentManager();
	public void invoke(Punishment punishment, Flag[] flags) {
		Config config = Main.getInstance().getPunishmentsConfig();

		FlatFilePunishments.getInstance().getActivePunishment(punishment.getPunished(), punishment.getType()).ifPresent(activePunish -> {
			FlatFilePunishments.getInstance().upsert(activePunish, ConfigFields.PunishFields.OVERRIDE, true, false);
			Bukkit.getPluginManager().callEvent(new PunishmentOverrideEvent(punishment, activePunish));
		});

		ConfigurationSection section = config.createSection(punishment.getPunishmentUniqueId().toString());

		section.set(ConfigFields.PunishFields.PUNISHED, punishment.getPunished().toString());

		Optional.ofNullable(punishment.getExecutor()).ifPresent(uniqueId ->
				section.set(ConfigFields.PunishFields.EXECUTOR, punishment.getExecutor().toString()));

		section.set(ConfigFields.PunishFields.TYPE, punishment.getType().name());

		Optional.ofNullable(punishment.getExpiry()).ifPresent(uniqueId ->
				section.set(ConfigFields.PunishFields.EXPIRY, punishment.getExpiry().toEpochMilli()));

		section.set(ConfigFields.PunishFields.REASON, punishment.getReason());
		config.saveConfig();

		Bukkit.getPluginManager().callEvent(new PunishmentInvokeEvent(punishment, flags));
	}

	public void revoke(Punishment punishment, @Nullable UUID whoRemoved) {
		String[] executor = new String[1];
		executor[0] = "CONSOLE";
		Optional.ofNullable(whoRemoved).ifPresent(uuid -> executor[0] = uuid.toString());

		FlatFilePunishments.getInstance().update(
				punishment.getPunishmentUniqueId(),
				ConfigFields.PunishFields.REMOVED_BY,
				executor[0],
				false);

		FlatFilePunishments.getInstance().update(
				punishment.getPunishmentUniqueId(),
				ConfigFields.PunishFields.EXPIRY,
				Instant.now().toEpochMilli(),
				true);

		if (!FlatFilePunishments.getInstance().convertToRevokedPunishment(punishment))
			throw new PunishmentRevokeConvertException("couldn't convert punishment into revoked punishment, check console details.");

		if (FlatFilePunishments.getInstance().getPunishment(punishment.getPunishmentUniqueId()).isEmpty() ||
			!(FlatFilePunishments.getInstance().getPunishment(punishment.getPunishmentUniqueId()).get() instanceof PunishmentRevoke punishmentRevoke)) {
			throw new PunishmentRevokeConvertException("couldn't convert punishment into revoked punishment, check console details.");
		}

		Bukkit.getPluginManager().callEvent(new PunishmentRevokeEvent(punishmentRevoke));
	}
}
