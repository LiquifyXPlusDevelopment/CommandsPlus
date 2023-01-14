package dev.gdalia.commandsplus.models.punishmentdrivers;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.models.ConfigFields;
import dev.gdalia.commandsplus.models.PunishmentDao;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentRevoke;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.utils.Config;
import dev.gdalia.commandsplus.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Instant;
import java.util.*;
import java.util.logging.Level;

/**
 * TODO think about better ideas to make this even more better and cooler to use.
 */
public class FlatFilePunishments implements PunishmentDao {

	@Getter
	@Setter
	private static FlatFilePunishments instance;

	private final HashMap<UUID, Punishment> punishments = new HashMap<>();
	
	private final Config pConfig = Main.getInstance().getPunishmentsConfig();

	public Optional<Punishment> getPunishment(UUID punishmentUuid) {
		// I am too shamen
		if (punishments.containsKey(punishmentUuid))
			return Optional.of(punishments.get(punishmentUuid));

		ConfigurationSection cs = pConfig.getConfigurationSection(punishmentUuid.toString());
		if (cs == null) return Optional.empty();

		UUID executor =
				cs.getString(ConfigFields.PunishFields.EXECUTOR) != null && StringUtils.isUniqueId(cs.getString(ConfigFields.PunishFields.EXECUTOR)) ?
				UUID.fromString(Objects.requireNonNull(cs.getString(ConfigFields.PunishFields.EXECUTOR))) :
				null;

		final Punishment[] punishment = new Punishment[1];
		try {
			punishment[0] = new Punishment(
					punishmentUuid,
					UUID.fromString(Objects.requireNonNull(cs.getString(ConfigFields.PunishFields.PUNISHED))),
					executor,
					PunishmentType.valueOf(cs.getString(ConfigFields.PunishFields.TYPE)),
					Objects.requireNonNull(cs.getString(ConfigFields.PunishFields.REASON)),
					cs.getBoolean(ConfigFields.PunishFields.OVERRIDE));
		} catch (NullPointerException e1) {
			Main.getInstance().getLogger().log(Level.SEVERE, "Couldn't load punishment " + punishmentUuid.toString());
			return Optional.empty();
		}

		Optional.ofNullable(cs.get(ConfigFields.PunishFields.EXPIRY))
		        .filter(Long.class::isInstance)
		        .map(String::valueOf)
		        .map(Long::parseLong)
				.ifPresent(expiry -> punishment[0].setExpiry(Instant.ofEpochMilli(expiry)));

		Optional.ofNullable(cs.get(ConfigFields.PunishFields.REMOVED_BY))
				.filter(String.class::isInstance)
				.map(String::valueOf)
				.ifPresent(s -> {
					UUID uuid = null;
					if (!StringUtils.isUniqueId(s)) uuid = UUID.fromString(s);
					punishment[0] = new PunishmentRevoke(punishment[0], uuid);
				});

		punishments.put(punishmentUuid, punishment[0]);

		return Optional.of(punishment[0]);
	}

	public List<Punishment> getHistory(UUID playerUniqueId) {
		return pConfig
				.getKeys(false)
				.stream()
				.map(UUID::fromString)
				.map(this::getPunishment)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(punishment -> punishment.getPunished().equals(playerUniqueId))
				.toList();
	}


	public Optional<Punishment> getActivePunishment(UUID playerUniqueId, PunishmentType... type) {
        return getHistory(playerUniqueId).stream()
				.filter(punishment -> !List.of(PunishmentType.KICK, PunishmentType.WARN).contains(punishment.getType()))
				.filter(punishment -> (type.length == 0) || Arrays.asList(type).contains(punishment.getType()))
				.filter(punishment -> punishment.getExpiry() == null || punishment.getExpiry().isAfter(Instant.now()))
				.filter(punishment -> !PunishmentRevoke.class.isInstance(punishment))
				.filter(punishment -> punishment.getType().isConstrictive())
                .filter(punishment -> {
                    ConfigurationSection cs = pConfig.getConfigurationSection(punishment.getPunishmentUniqueId().toString());
                    if (cs == null) return false;
                    return !cs.contains(ConfigFields.PunishFields.OVERRIDE) && !cs.contains(ConfigFields.PunishFields.REMOVED_BY);
                }).findAny();
    }

	public Optional<Punishment> getAnyActivePunishment(UUID playerUniqueId) {
		return getActivePunishment(
				playerUniqueId,
				PunishmentType.values());
	}

	/**
	 * makes a full deep check if the following user has a punishment of this type.
	 * @param playerUniqueId The user/player unique ID.
	 * @param type The type of punishment to be checked.
	 * @return true if the player has/had a punishment of this type.
	 */
	public boolean hasPunishment(UUID playerUniqueId, PunishmentType type) {
		return getHistory(playerUniqueId).stream()
				.anyMatch(punishment -> punishment.getType() == type);
	}
	
	/**
	 * Writes into the punishment new information, this is good for
	 * the punishment revoke system when adding written stuff,
	 * or when overriding an existing active punishment.
	 * 
	 * @param punishmentUuid The uniqueId of the punishment (NOT THE USER).
	 * @param key the key name to create and write into.
	 * @param value the object to insert.
	 * @param instSave If the method should save once the key and value being written.
	 */
	public void update(UUID punishmentUuid, String key, Object value, boolean instSave) {
		Optional<ConfigurationSection> cs = Optional.ofNullable(pConfig.getConfigurationSection(punishmentUuid.toString()));
		cs.ifPresentOrElse(configurationSection -> {
			configurationSection.set(key, value);
			if (instSave) pConfig.saveConfig();
		}, () -> {
			pConfig.createSection(punishmentUuid.toString());
			pConfig.saveConfig();
			update(punishmentUuid, key, value, instSave);
		});
	}

	/**
	 * This generally returns all Server Punishments, since this is the punishment "manager",
	 * this will give a fed Immutable List of server punishments.
	 * @return an immutable {@link List<Punishment>} of all server punishments.
	 */
	public List<Punishment> getServerPunishments() {
		return List.copyOf(punishments.values());
	}
	
	/**
	 * same as {@link FlatFilePunishments#update(UUID, String, Object, boolean)}, the usage here
	 * is for shortening code calls whenever possible
	 * 
	 * @param punishment The punishment to write into, if existing.
	 * @param key the key name to create and write into.
	 * @param value the object to insert.
	 * @param instSave If the method should save once the key and value being written.
	 */
	public void upsert(Punishment punishment, String key, Object value, boolean instSave) {
		update(punishment.getPunishmentUniqueId(), key, value, instSave);
	}

	public boolean convertToRevokedPunishment(Punishment punishment) {
		if (!getServerPunishments().contains(punishment)) return false;

		ConfigurationSection cs = pConfig.getConfigurationSection(punishment.getPunishmentUniqueId().toString());
		if (cs.get(ConfigFields.PunishFields.REMOVED_BY) == null) return false;

		Optional<UUID> uuid = Optional.ofNullable(cs.getString(ConfigFields.PunishFields.REMOVED_BY))
				.filter(StringUtils::isUniqueId)
				.map(UUID::fromString);

		PunishmentRevoke punishmentRevoke = new PunishmentRevoke(punishment, uuid.orElse(null));
		punishments.put(punishment.getPunishmentUniqueId(), punishmentRevoke);

		return true;
	}
}