package dev.gdalia.commandsplus.structs.events.body;

import dev.gdalia.commandsplus.structs.reports.Report;
import lombok.NonNull;
import org.bukkit.event.Event;

public abstract class ReportEvent extends Event {

    @NonNull
    public abstract Report getReport();

}
