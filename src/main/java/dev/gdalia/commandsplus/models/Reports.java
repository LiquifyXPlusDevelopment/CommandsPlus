package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportComment;
import dev.gdalia.commandsplus.structs.reports.ReportReason;
import dev.gdalia.commandsplus.structs.reports.ReportStatus;
import dev.gdalia.commandsplus.utils.Config;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Instant;
import java.util.*;

public class Reports {

    @Getter
    @Setter
    private static Reports instance;

    private final HashMap<UUID, Report> reports = new HashMap<>();
    private final Config pConfig = Main.getInstance().getReportsConfig();

    /**
     * Finds a report inside the database by the following UUID.
     * This method is working by checking if the report is already been saved
     * to memory, and if it did it'll access the local map and pull them.
     * if it doesn't, the method will access the database to check if the report exists,
     * and will return the report if it exists inside and put it inside the local map too,
     * if not, it will return an empty optional container.
     * @param reportUniqueId the uuid of the report for instance.
     * @return An Optional container that is either empty or containing a report.
     */
    @SuppressWarnings("unchecked")
    public Optional<Report> getReport(UUID reportUniqueId) {
        if (reports.containsKey(reportUniqueId))
            return Optional.of(reports.get(reportUniqueId));

        ConfigurationSection cs = pConfig.getConfigurationSection(reportUniqueId.toString());
        if (cs == null) return Optional.empty();

        Optional.ofNullable(cs.getList(ConfigFields.ReportsFields.COMMENTS)).stream();

        Report report = new Report(
                reportUniqueId,
                UUID.fromString(cs.getString(ConfigFields.ReportsFields.REPORTED)),
                UUID.fromString(cs.getString(ConfigFields.ReportsFields.REPORTER)),
                Instant.ofEpochMilli(cs.getLong(ConfigFields.ReportsFields.DATE)),
                (ReportReason) cs.get(ConfigFields.ReportsFields.REASON),
                ReportStatus.valueOf(cs.getString(ConfigFields.ReportsFields.STATUS)),
                (List<ReportComment>) cs.getList(ConfigFields.ReportsFields.COMMENTS, new ArrayList<ReportComment>()));

        reports.put(reportUniqueId, report);
        return Optional.of(report);
    }

    /**
     * Gets and finds any report on this targeted player.
     *
     * @param playerUniqueId The uuid of the player to lookup for reports of them.
     * @return A list of reports that has been filed about this player, or empty list for instance.
     */
    public List<Report> getReportHistory(UUID playerUniqueId) {
        return pConfig
                .getKeys(false)
                .stream()
                .map(UUID::fromString)
                .map(this::getReport)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(report -> report.getConvicted().equals(playerUniqueId))
                .toList();
    }

    /**
     * Looks up if the player has any open reports or handled reports.
     * This method will ignore all reports that has been closed already.
     *
     * @param playerUniqueId The uuid of the player to lookup for reports of them.
     * @return A report that was filed on this player that is either open or being handled.
     */
    public Optional<Report> getOpenReport(UUID playerUniqueId) {
        return getReportHistory(playerUniqueId).stream()
                .filter(x -> !x.getStatus().equals(ReportStatus.CLOSED))
                .findFirst();
    }

    /**
     * This method looks up for all the reports that the player ever
     * submitted to the report system, it'll then collect all the reports
     * 1 by 1, finalizing it with a List of Reports.
     *
     * @author Gdalia
     * @since 1.0.4
     * @param playerUniqueId The player UniqueId to look up sent reports.
     * @return A list of reports that the player ever submitted
     */
    public List<Report> getSentReports(UUID playerUniqueId) {
        return pConfig
                .getKeys(false)
                .stream()
                .map(UUID::fromString)
                .map(this::getReport)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(report -> report.getReporter().equals(playerUniqueId))
                .toList();
    }

    /**
     * Looks up via {@link Reports#getOpenReport(UUID)} method to see
     * if this player has any open reports on them.
     *
     * @param playerUniqueId The uuid of the player to lookup for reports of them.
     * @return True if the player has an open report or that is either being handled, false if none we're found.
     */
    public boolean hasOpenReport(UUID playerUniqueId) {
        return getOpenReport(playerUniqueId).isPresent();
    }

    /**
     * Looks up via {@link Reports#getOpenReport(UUID)} method to see
     * if this player has ever been reported before.
     * Even if the user was reported but the report was closed,
     * it'll change the boolean statement of this method.
     *
     * @param playerUniqueId The uuid of the player to lookup for reports of them.
     * @return True if the player was ever been reported, false if he is clean of reports.
     */
    public boolean haveBeenReportedBefore(UUID playerUniqueId) {
        return !getReportHistory(playerUniqueId).isEmpty();
    }

    /**
     * 	 * Writes into the report new information, this is good for
     * 	 * the report status change when a staff member updates it.
     *
     * @param reportUniqueId The uuid of the player to lookup for reports of them.
     * @param key the key name to create and write into.
     * @param value the object to insert.
     * @param instSave If the method should save once the key and value being written.
     */
    public void writeTo(UUID reportUniqueId, String key, Object value, boolean instSave) {
        Optional<ConfigurationSection> cs = Optional.ofNullable(pConfig.getConfigurationSection(reportUniqueId.toString()));
        cs.ifPresentOrElse(configurationSection -> {
            configurationSection.set(key, value);
            if (instSave) pConfig.saveConfig();
        }, () -> {
            pConfig.createSection(reportUniqueId.toString());
            pConfig.saveConfig();
            writeTo(reportUniqueId, key, value, instSave);
        });
    }

    /**
     * same as {@link Reports#writeTo(UUID, String, Object, boolean)}, the usage here
     * is for shortening code calls whenever possible
     *
     * @param report The report to write into, if existing.
     * @param key the key name to create and write into.
     * @param value the object to insert.
     * @param instSave If the method should save once the key and value being written.
     */
    public void writeTo(Report report, String key, Object value, boolean instSave) {
        writeTo(report.getReportUuid(), key, value, instSave);
    }

    /**
     * This method entirely deletes the report from the database, if this used
     * you can potentially lose data you didn't want to delete, so use with caution.
     */
    public void erase(Report report) {
        pConfig.set(report.getReportUuid().toString(), null);
        pConfig.saveConfig();
        reports.remove(report.getReportUuid());
    }
}
