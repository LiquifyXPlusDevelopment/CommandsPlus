package dev.gdalia.commandsplus.structs.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import lombok.Getter;
import dev.gdalia.commandsplus.structs.Punishment;
import org.jetbrains.annotations.NotNull;

public class PlayerPunishEvent extends PlayerEvent {

	private static final HandlerList HANDLERS = new HandlerList();

	@Getter
	private final Punishment punishment;

	public PlayerPunishEvent(Player player, Punishment punishment) {
		super(player);
		this.punishment = punishment;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	@Override
	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}
}
