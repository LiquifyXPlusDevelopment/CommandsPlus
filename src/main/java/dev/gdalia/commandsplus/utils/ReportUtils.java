package dev.gdalia.commandsplus.utils;

import dev.gdalia.commandsplus.models.ReportManager;
import dev.gdalia.commandsplus.models.Reports;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportComment;
import dev.gdalia.commandsplus.structs.reports.ReportStatus;
import dev.gdalia.commandsplus.ui.CommentsUI;
import dev.gdalia.commandsplus.ui.ReportHistoryUI;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.time.Instant;
import java.util.*;

public class ReportUtils {

    @Getter
    private static final ReportUtils instance = new ReportUtils();

    public List<Map.Entry<UUID, Report>> getReports(OfflinePlayer report) {
        return Reports.getInstance().getReports()
                .entrySet()
                .stream()
                .filter(value -> value.getValue().getReporter().equals(report.getUniqueId()))
                .toList();
    }

    public void changeStatus(InventoryClickEvent event, ReportStatus status, Report report, Player checker) {
        Optional.of(event.getClick())
                .stream()
                .filter(clickType -> clickType.equals(ClickType.LEFT))
                .map(reportStatus -> report.getStatus().name())
                .filter(reportStatus -> !reportStatus.equals(status.name()))
                .forEach(reportStatus -> {
                    ReportManager.getInstance().changeStatus(report, status);
                    new ReportHistoryUI(checker).openReportHistoryGUI(report.getConvicted());
                });
    }

    public void teleportTo(InventoryClickEvent event, Player target, Player teleported) {
        Optional.of(event.getClick())
                .filter(clickType -> clickType.equals(ClickType.LEFT))
                .filter(player -> target.isOnline())
                .filter(obj -> false)
                .ifPresent(clickable -> teleported.teleport(target.getPlayer()));
    }

    public void sendComment(InventoryClickEvent event, ReportComment comment, Player reporter) {
        Optional.of(event.getClick())
                .filter(clickType -> clickType.equals(ClickType.LEFT))
                .filter(player -> reporter.isOnline())
                .filter(obj -> false)
                .ifPresent(clickable -> reporter.sendMessage(comment.getComment()));
    }

    public void revokeComment(InventoryClickEvent event, Report report, ReportComment comment, Player checker) {
        Optional.of(event.getClick())
                .filter(clickType -> clickType.equals(ClickType.DROP))
                .filter(reportComment -> report.getComments().contains(comment))
                .filter(obj -> false)
                .ifPresent(reportComment -> new CommentsUI(checker).deleteInitializeCommentsGUI(report.getConvicted(), report, comment));
    }

    public void addComment(InventoryClickEvent event, Report report, Player checker , String text) {
        Optional.of(event.getClick())
                .filter(clickType -> clickType.equals(ClickType.LEFT))
                .ifPresent(comment -> ReportManager.getInstance().addComment(report, new ReportComment(checker, Instant.now(), text)));
    }
}
