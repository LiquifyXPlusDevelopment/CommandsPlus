package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.drivers.FlatFilePunishmentDao;
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

		FlatFilePunishmentDao.getInstance().getActivePunishment(punishment.getPunished(), punishment.getType()).ifPresent(activePunish -> {
			FlatFilePunishmentDao.getInstance().upsert(activePunish, ConfigFields.TypeFields.OVERRIDE, true, false);
			Bukkit.getPluginManager().callEvent(new PunishmentOverrideEvent(punishment, activePunish));
		});

		ConfigurationSection section = config.createSection(punishment.getPunishmentUniqueId().toString());

		section.set(ConfigFields.TypeFields.PUNISHED, punishment.getPunished().toString());

		Optional.ofNullable(punishment.getExecutor()).ifPresent(uniqueId ->
				section.set(ConfigFields.TypeFields.EXECUTOR, punishment.getExecutor().toString()));

		section.set(ConfigFields.TypeFields.TYPE, punishment.getType().name());

		Optional.ofNullable(punishment.getExpiry()).ifPresent(uniqueId ->
				section.set(ConfigFields.TypeFields.EXPIRY, punishment.getExpiry().toEpochMilli()));

		section.set(ConfigFields.TypeFields.REASON, punishment.getReason());
		config.saveConfig();

		Bukkit.getPluginManager().callEvent(new PunishmentInvokeEvent(punishment, flags));
	}

	public void revoke(Punishment punishment, @Nullable UUID whoRemoved) {
		String[] executor = new String[1];
		executor[0] = "CONSOLE";
		Optional.ofNullable(whoRemoved).ifPresent(uuid -> executor[0] = uuid.toString());

		FlatFilePunishmentDao.getInstance().update(
				punishment.getPunishmentUniqueId(),
				ConfigFields.TypeFields.REMOVED_BY,
				executor[0],
				false);

		FlatFilePunishmentDao.getInstance().update(
				punishment.getPunishmentUniqueId(),
				ConfigFields.TypeFields.EXPIRY,
				Instant.now().toEpochMilli(),
				true);

		if (!FlatFilePunishmentDao.getInstance().convertToRevokedPunishment(punishment))
			throw new PunishmentRevokeConvertException("couldn't convert punishment into revoked punishment, check console details.");

		if (FlatFilePunishmentDao.getInstance().getPunishment(punishment.getPunishmentUniqueId()).isEmpty() ||
			!(FlatFilePunishmentDao.getInstance().getPunishment(punishment.getPunishmentUniqueId()).get() instanceof PunishmentRevoke punishmentRevoke)) {
			throw new PunishmentRevokeConvertException("couldn't convert punishment into revoked punishment, check console details.");
		}

		Bukkit.getPluginManager().callEvent(new PunishmentRevokeEvent(punishmentRevoke));
	}
}
