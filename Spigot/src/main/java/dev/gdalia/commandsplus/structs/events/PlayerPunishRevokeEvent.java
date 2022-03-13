package dev.gdalia.commandsplus.structs.events;

import dev.gdalia.commandsplus.structs.PunishmentRevoke;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Unused, but will trigger when punishment is revoked, can be used in the future.
 *
 */
@AllArgsConstructor
public class PlayerPunishRevokeEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	
	@Getter
	private final PunishmentRevoke punishment;
	
	@Override
	@NonNull
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
