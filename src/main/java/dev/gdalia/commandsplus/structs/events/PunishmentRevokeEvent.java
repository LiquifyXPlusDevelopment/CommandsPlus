package dev.gdalia.commandsplus.structs.events;

import dev.gdalia.commandsplus.structs.punishments.PunishmentRevoke;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Unused, but will trigger when punishment is revoked, can be used in the
 * future.
 *
 */
@AllArgsConstructor
public class PunishmentRevokeEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final PunishmentRevoke punishmentRevoked;

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}
}
