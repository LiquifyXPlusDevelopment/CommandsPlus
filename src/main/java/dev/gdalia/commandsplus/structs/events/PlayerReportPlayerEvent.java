package dev.gdalia.commandsplus.structs.events;

import dev.gdalia.commandsplus.structs.reports.Report;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerReportPlayerEvent extends PlayerEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final Report report;

    public PlayerReportPlayerEvent(Player player, Report report) {
        super(player);
        this.report = report;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

}
