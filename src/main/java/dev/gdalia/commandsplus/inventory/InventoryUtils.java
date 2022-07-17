package dev.gdalia.commandsplus.inventory;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.ReportManager;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.punishments.Punishment;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportComment;
import dev.gdalia.commandsplus.structs.reports.ReportStatus;
import dev.gdalia.commandsplus.ui.CommentsUI;
import dev.gdalia.commandsplus.ui.ReportHistoryUI;
import dev.gdalia.commandsplus.utils.StringUtils;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.*;

public class InventoryUtils {

    @Getter
    private static final InventoryUtils instance = new InventoryUtils();
    public HashMap<UUID, Report> commentText = new HashMap<>();

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
                .ifPresent(clickable -> teleported.teleport(target.getPlayer()));
    }

    public void sendComment(InventoryClickEvent event, ReportComment comment, Player reporter) {
        Optional.of(event.getClick())
                .filter(clickType -> clickType.equals(ClickType.LEFT))
                .filter(player -> reporter.isOnline())
                .ifPresent(clickable ->
                        reporter.sendMessage(
                        Message.fixColor(
                        "&7&m----------&eRespond&7&m----------\n" +
                        "&7 Commenter: &6" +
                        comment.getOfflinePlayer().getName() +
                        "\n&7 Date: &e" + StringUtils.createTimeFormatter(comment.getSentAt(), "dd/MM/uu, HH:mm:ss") +
                        "\n&7 Message: &f" + comment.getComment() +
                        "\n&7&m---------------------------")));
        Message.playSound(reporter, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
    }


    public void revokeComment(InventoryClickEvent event, Report report, ReportComment comment, Player checker) {
        Optional.of(event.getClick())
                .filter(clickType -> clickType.equals(ClickType.DROP))
                .filter(reportComment -> report.getComments().contains(comment))
                .ifPresent(reportComment -> new CommentsUI(checker).deleteInitializeCommentsGUI(report.getConvicted(), report, comment));
    }

    public void invokePunishment(InventoryClickEvent event, PunishmentType type, String message, Player target, Player checker) {
        Optional.of(event.getClick())
                .filter(clickType -> clickType.equals(ClickType.LEFT))
                .filter(player -> target.isOnline())
                .ifPresent(clickable -> {
                    Punishment punishment = new Punishment(
                            UUID.randomUUID(),
                            target.getUniqueId(),
                            Optional.of(checker.getUniqueId()).orElse(null),
                            type,
                            message);

                    PunishmentManager.getInstance().invoke(punishment);
                    Message.playSound(checker, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    Message.valueOf("PLAYER_" + type.getNameAsPunishMsg().toUpperCase() + "_MESSAGE").sendFormattedMessage(checker, true, target.getName());
                });
    }
}
