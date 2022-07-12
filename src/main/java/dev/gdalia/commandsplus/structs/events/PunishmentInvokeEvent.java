package dev.gdalia.commandsplus.structs.events;

import dev.gdalia.commandsplus.structs.events.body.PunishmentEvent;
import lombok.AllArgsConstructor;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class PunishmentInvokeEvent extends PunishmentEvent {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final Punishment punishment;

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}
}
