package dev.gdalia.commandsplus.structs.events.body;

import dev.gdalia.commandsplus.structs.reports.Report;
import lombok.NonNull;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class ReportEvent extends Event {

    @NotNull
    public abstract Report getReport();

}
