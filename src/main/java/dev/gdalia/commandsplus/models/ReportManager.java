package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.events.ReportInvokeEvent;
import dev.gdalia.commandsplus.structs.events.ReportRevokeEvent;
import dev.gdalia.commandsplus.structs.events.ReportStatusChangeEvent;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportComment;
import dev.gdalia.commandsplus.structs.reports.ReportStatus;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Optional;


public class ReportManager {

    @Getter
    private static final ReportManager instance = new ReportManager();

    public void invoke(Report report) {
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.REPORTED, report.getConvicted().toString(), false);
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.REPORTER, report.getReporter().toString(), false);
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.REASON, report.getReason(), false);
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.DATE, report.getSentAt().toEpochMilli(), false);
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.STATUS, ReportStatus.OPEN.name(), true);
        Bukkit.getPluginManager().callEvent(new ReportInvokeEvent(Bukkit.getPlayer(report.getConvicted()), report));
    }

    public void revoke(Report report) {
        Main.getReportsConfig().set(report.getReportUuid().toString(), null);
        Reports.getInstance().getReports().remove(report.getReportUuid());
        Main.getReportsConfig().saveConfig();
        Bukkit.getPluginManager().callEvent(new ReportRevokeEvent(report));
    }


    @SuppressWarnings({"unchecked"})
    public void addComment(Report report, ReportComment comment) {
        ConfigurationSection section = Main.getReportsConfig().getConfigurationSection(report.getReportUuid().toString());
        List<ReportComment> comments = report.getComments();
        comments.add(comment);
        section.set(ConfigFields.ReportsFields.COMMENTS, comments);
        Main.getReportsConfig().saveConfig();
        //report.getComments().add(comment);
    }

    public void revokeComment(Report report, ReportComment comment) {
        ConfigurationSection section = Main.getReportsConfig().getConfigurationSection(report.getReportUuid().toString());
        Optional.of(report.getComments())
                .filter(x -> !x.isEmpty() && x.contains(comment))
                .ifPresent(list -> {
                    list.remove(comment);
                    section.set(ConfigFields.ReportsFields.COMMENTS, list);
                    Main.getReportsConfig().saveConfig();
                });
    }

    public void changeStatus(Report report, ReportStatus newStatus) {
        Reports.getInstance().writeTo(report, ConfigFields.ReportsFields.STATUS, newStatus.name(), true);
        Bukkit.getPluginManager().callEvent(new ReportStatusChangeEvent(report));
        Reports.getInstance().getReports().remove(report.getReportUuid());
    }
}
