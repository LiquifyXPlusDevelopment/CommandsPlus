package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.structs.events.PlayerReportPlayerEvent;
import dev.gdalia.commandsplus.structs.events.ReportStatusChangeEvent;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportStatus;
import lombok.Getter;
import org.bukkit.Bukkit;


public class ReportManager {

    @Getter
    private static final ReportManager instance = new ReportManager();

    public void invoke(Report report) {
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.REPORTED, report.getConvicted().toString(), false);
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.REPORTER, report.getReporter().toString(), false);
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.REASON, report.getReason(), false);
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.DATE, report.getSentAt().toEpochMilli(), false);
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.STATUS, ReportStatus.OPEN.name(), true);

        Bukkit.getPluginManager().callEvent(new PlayerReportPlayerEvent(Bukkit.getPlayer(report.getConvicted()), report));
    }

    public void changeStatus(Report report, ReportStatus newStatus) {
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.STATUS, newStatus.name(), true);
        Bukkit.getPluginManager().callEvent(new ReportStatusChangeEvent(report));
    }
}
