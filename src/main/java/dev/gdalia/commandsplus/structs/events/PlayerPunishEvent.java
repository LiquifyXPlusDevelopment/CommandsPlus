package dev.gdalia.commandsplus.structs.events;

import dev.gdalia.commandsplus.structs.Punishment;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;


public class PlayerPunishEvent extends PlayerEvent {

	private static final HandlerList HANDLERS = new HandlerList();
	
	@Getter
	private final Punishment punishment;

	public PlayerPunishEvent(Player player, Punishment punishment) {
		super(player);
		this.punishment = punishment;
	}
	
	@Override
	@NonNull
	public HandlerList getHandlers() {
		return HANDLERS;
	}
}
