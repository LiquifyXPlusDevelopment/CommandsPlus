package dev.gdalia.commandsplus.models;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.Punishment;
import dev.gdalia.commandsplus.structs.PunishmentType;
import dev.gdalia.commandsplus.utils.Config;
import lombok.Getter;

/**
 * TODO think about better ideas to make this even more better and cooler to use.
 */
public class Punishments {

	@Getter
	private static final Punishments instance = new Punishments();
	
	@Getter
	private final HashMap<UUID, Punishment> punishments = new HashMap<>();
	
	private final Config pConfig = Main.getPunishmentsConfig();
	
	/**
	 * Finds a punishment inside the database by the following UUID.
	 * This method is working by checking if the punishment is already been saved
	 * to memory, and if it did it'll access the local map and pull them.
	 * if it doesn't, the method will access the database to check if the punishment exists,
	 * and will return the punishment if it exists inside and put it inside the local map too,
	 * if not it will return an empty optional container. 
	 * 
	 * @param uuid The uuid of the punishment for instance.
	 * @return An Optional container that is either empty or containing a punishment.
	 */
	public Optional<Punishment> getPunishment(UUID uuid) {
		Optional<Punishment> opt;
		if (punishments.containsKey(uuid))
			opt = Optional.of(punishments.get(uuid));

		ConfigurationSection cs = pConfig.getConfigurationSection(uuid.toString());
		if (cs == null) return Optional.empty();

		Punishment punishment = new Punishment(
				uuid,
				UUID.fromString(cs.getString(ConfigFields.PunishFields.PUNISHED)),
				UUID.fromString(cs.getString(ConfigFields.PunishFields.EXECUTER)),
				PunishmentType.valueOf(cs.getString(ConfigFields.PunishFields.TYPE)),
				cs.getString(ConfigFields.PunishFields.REASON));
		
		Optional.ofNullable(cs.get(ConfigFields.PunishFields.EXPIRY))
				.filter(expiryAsObject -> expiryAsObject instanceof Long)
				.map(String::valueOf)
				.map(Long::parseLong)
				.ifPresent(expiry -> punishment.setExpiry(Instant.ofEpochMilli(expiry)));

		punishments.put(uuid, punishment);
		
		opt = Optional.of(punishment);
		
		return opt;
		//Gdalia was here
	    //OfirTIM was here
	}
	
	/**
	 * Gets and finds any punishment involved with this player (only as a punished user).
	 * 
	 * @param uuid The unique ID of player/user to find their punishments.
	 * @return a list of punishments that this player has/had, or empty list for instance.
	 */
	public List<Punishment> getHistory(UUID uuid) {
		return pConfig
				.getKeys(false)
				.stream()
				.map(UUID::fromString)
				.map(this::getPunishment)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.filter(punishment -> punishment.getPunished().equals(uuid))
				.toList();
	}
	
	/**
	 * Checks and gets an active punishment that a user/player currently has.
	 * 
	 * @param uuid The user/player unique ID.
	 * @param type The optional types to add into the filtering.
	 * @return An optional container which could be empty or contain a punishment.
	 */	
	public Optional<Punishment> getActivePunishment(UUID uuid, PunishmentType... type) {
        return getHistory(uuid).stream()
                .filter(punishment -> {
                    if (type.length == 0) return true;
                    return Arrays.asList(type).contains(punishment.getType());
                }).filter(punishment -> punishment.getExpiry() == null || punishment.getExpiry().isAfter(Instant.now()))
                .filter(punishment -> {
                    ConfigurationSection cs = pConfig.getConfigurationSection(punishment.getPunishmentUniqueId().toString());
                    if (cs == null) return false;
                    return !cs.contains(ConfigFields.PunishFields.OVERRIDE) && !cs.contains(ConfigFields.PunishFields.REMOVED_BY);
                }).findFirst();
    }
	
	/**
	 * makes a full deep check if the following user has a punishment of this type.
	 * @param uuid The user/player unique ID.
	 * @param type The type of punishment to be checked.
	 * @return true if the player has/had a punishment of this type.
	 */
	public boolean hasPunishment(UUID uuid, PunishmentType type) {
		return getHistory(uuid).stream()
				.anyMatch(punishment -> punishment.getType() == type);
	}
	
	/**
	 * Writes into the punishment new information, this is good for
	 * the punishment revoke system when adding written stuff,
	 * or when overriding an existing active punishment.
	 * 
	 * @param uuid The uniqueId of the punishment (NOT THE USER).
	 * @param key the key name to create and write into.
	 * @param value the object to insert.
	 * @param instSave If the method should save once the key and value being written.
	 */
	public void writeTo(UUID uuid, String key, Object value, boolean instSave) {
		Optional<ConfigurationSection> cs = Optional.ofNullable(pConfig.getConfigurationSection(uuid.toString()));
		cs.ifPresent(configurationSection -> {
			configurationSection.set(key, value);
			if (instSave) pConfig.saveConfig();
		});
	}
	
	/**
	 * same as {@link Punishments#writeTo(UUID, String, Object, boolean)}, the usage here
	 * is for shortening code calls whenever possible
	 * 
	 * @param punishment The punishment to write into, if existing.
	 * @param key the key name to create and write into.
	 * @param value the object to insert.
	 * @param instSave If the method should save once the key and value being written.
	 */
	public void writeTo(Punishment punishment, String key, Object value, boolean instSave) {
		writeTo(punishment.getPunishmentUniqueId(), key, value, instSave);
	}
}