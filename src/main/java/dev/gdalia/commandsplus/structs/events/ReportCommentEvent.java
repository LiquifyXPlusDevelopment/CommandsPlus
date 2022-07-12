package dev.gdalia.commandsplus.structs.events;

import dev.gdalia.commandsplus.structs.events.body.ReportEvent;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class ReportCommentEvent extends ReportEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final Report report;

    @Getter
    private final ReportComment comment;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
