package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Punishments {

    /**
     * Finds a punishment inside the database by the following UUID.
     * This method is working by checking if the punishment is already been saved
     * to memory, and if it did it'll access the local map and pull them.
     * if it doesn't, the method will access the database to check if the punishment exists,
     * and will return the punishment if it exists inside and put it inside the local map too,
     * if not it will return an empty optional container.
     *
     * @param punishmentUuid The punishmentUuid of the punishment for instance.
     * @return An Optional container that is either empty or containing a punishment.
     */
    Optional<Punishment> getPunishment(UUID punishmentUuid);

    /**
     * Gets and finds any punishment involved with this player (only as a punished user).
     *
     * @param playerUniqueId The unique ID of player/user to find their punishments.
     * @return a list of punishments that this player has/had, or empty list for instance.
     */
    List<Punishment> getHistory(UUID playerUniqueId);

    /**
     * Checks and gets an active punishment that a user/player currently has.
     *
     * @param playerUniqueId The user/player unique ID.
     * @param type           The optional types to add into the filtering.
     * @return An optional container which could be empty or contain a punishment.
     */
    Optional<Punishment> getActivePunishment(UUID playerUniqueId, PunishmentType... type);

    /**
     * Checks and gets any active punishment that a user/player currently has, similar to {@link Punishments#getActivePunishment(UUID, PunishmentType...)}
     * but allows to look for any active one without selected few.
     *
     * @param playerUniqueId The user/player unique ID.
     * @return An optional container which could be empty or contain a punishment.
     */
    default Optional<Punishment> getAnyActivePunishment(UUID playerUniqueId) {
        return getActivePunishment(
                playerUniqueId,
                PunishmentType.values());
    }

    /**
     * makes a full deep check if the following user has a punishment of this type.
     *
     * @param playerUniqueId The user/player unique ID.
     * @param type           The type of punishment to be checked.
     * @return true if the player has/had a punishment of this type.
     */
    boolean hasPunishment(UUID playerUniqueId, PunishmentType type);

    /**
     * Writes into the punishment new information, this is good for
     * the punishment revoke system when applying new info,
     * or when overriding an existing active punishment.
     *
     * @param punishmentUuid The uniqueId of the punishment (NOT THE USER).
     * @param key            the key name to create and write into.
     * @param value          the object to insert.
     * @param instSave       If the method should save once the key and value being written.
     */
    void update(UUID punishmentUuid, String key, Object value, boolean instSave);

    void insertOrReplace(Punishment punishment);

    void init();

}