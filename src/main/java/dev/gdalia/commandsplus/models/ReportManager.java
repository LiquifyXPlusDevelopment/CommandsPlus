package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.utils.Config;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

public class ReportManager {

    @Getter
    private static ReportManager instance = new ReportManager();

    public void invoke(Report report) {
        Config config = Main.getReportsConfig();

        Reports.getInstance().getReport(report.getConvicted()).ifPresent(activeReport ->
                Reports.getInstance().writeTo(activeReport, ConfigFields.ReportsFields.REPORTED, true, false));

        ConfigurationSection section = config.createSection(report.getReportUuid().toString());

        section.set(ConfigFields.ReportsFields.REPORTED, report.getConvicted().toString());

        Optional.of(report.getReporter()).ifPresent(uniqueId ->
                section.set(ConfigFields.ReportsFields.REPORTER, report.getReporter().toString()));

        section.set(ConfigFields.ReportsFields.REASON, report.getReason().getDisplayName());

        Optional.of(report.getSentAt()).ifPresent(uniqueId ->
                section.set(ConfigFields.ReportsFields.DATE, report.getSentAt().toEpochMilli()));

        section.set(ConfigFields.ReportsFields.REASON, report.getReason());

        config.saveConfig();
    }

    public void revoke() {
    }
}
