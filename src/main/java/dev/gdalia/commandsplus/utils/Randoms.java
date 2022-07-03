package dev.gdalia.commandsplus.utils;

import dev.gdalia.commandsplus.models.Reports;
import dev.gdalia.commandsplus.structs.reports.Report;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Randoms {

    public static List<Map.Entry<UUID, Report>> status(OfflinePlayer report) {
        return Reports.getInstance().getReports()
                .entrySet()
                .stream()
                .filter(value -> value.getValue().getReporter().equals(report.getUniqueId()))
                .toList();
    }

}
