package dev.gdalia.commandsplus.structs.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import lombok.Getter;
import dev.gdalia.commandsplus.structs.Punishment;

public class PlayerPunishEvent extends PlayerEvent {

	private static HandlerList HANDLERS = new HandlerList();

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
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
