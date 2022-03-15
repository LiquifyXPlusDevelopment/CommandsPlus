package dev.gdalia.commandsplus.structs.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import dev.gdalia.commandsplus.structs.PunishmentRevoke;

/**
 * Unused, but will trigger when punishment is revoked, can be used in the
 * future.
 *
 */
@AllArgsConstructor
public class PlayerPunishRevokeEvent extends Event {

	private static HandlerList HANDLERS = new HandlerList();

	@Getter
	private final PunishmentRevoke punishment;

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
