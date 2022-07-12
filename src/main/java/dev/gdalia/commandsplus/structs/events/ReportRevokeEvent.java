package dev.gdalia.commandsplus.structs.events;

import dev.gdalia.commandsplus.structs.events.body.ReportEvent;
import dev.gdalia.commandsplus.structs.reports.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Unused, but will trigger when report is revoked, can be used in the
 * future.
 *
 */
@AllArgsConstructor
public class ReportRevokeEvent extends ReportEvent {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final Report report;

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}
}
