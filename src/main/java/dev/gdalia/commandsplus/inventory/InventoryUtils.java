package dev.gdalia.commandsplus.inventory;

import dev.gdalia.commandsplus.models.PunishmentManager;
import dev.gdalia.commandsplus.models.punishmentdrivers.FlatFilePunishments;
import dev.gdalia.commandsplus.models.ReportManager;
import dev.gdalia.commandsplus.structs.Flag;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

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

    public void invokePermanentPunishment(InventoryClickEvent event, PunishmentType type, String message, Player target, Player checker) {
        Optional.of(event.getClick())
                .filter(clickType -> clickType.equals(ClickType.LEFT))
                .filter(player -> target.isOnline())
                .ifPresent(clickable -> {
                    if (type.equals(PunishmentType.MUTE) || type.equals(PunishmentType.BAN)) {
                        if (FlatFilePunishments.getInstance().getActivePunishment(target.getUniqueId(), type,
                                PunishmentType.valueOf("TEMP" + type.name().toUpperCase())).orElse(null) != null) {
                            type.getAlreadyPunishedMessage().sendMessage(checker, true);
                            Message.playSound(checker, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                            checker.closeInventory();
                            return;
                        }
                    }

                    Punishment punishment = new Punishment(
                        UUID.randomUUID(),
                        target.getUniqueId(),
                        Optional.of((checker).getUniqueId()).orElse(null),
                        type,
                        message,
                        false);

                    PunishmentManager.getInstance().invoke(punishment, new Flag[0]);
                    Message.playSound(checker, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

                    type.getPunishSuccessfulMessage().sendFormattedMessage(checker, true, target.getName(), punishment.getReason());
                    checker.closeInventory();
                });
    }

    public void invokeTemporaryPunishment(InventoryClickEvent event, PunishmentType type, String time, String message, Player target, Player checker) {
        Optional.of(event.getClick())
                .filter(clickType -> clickType.equals(ClickType.LEFT))
                .filter(player -> target.isOnline())
                .ifPresent(clickable -> FlatFilePunishments.getInstance().getActivePunishment(target.getUniqueId(), PunishmentType.valueOf(type.name().toUpperCase()), PunishmentType.valueOf(type.name().replace("TEMP", "").toUpperCase())).ifPresentOrElse(punishment -> {
                    type.getAlreadyPunishedMessage().sendMessage(checker, true);
                    checker.closeInventory();
                }, () -> {
                    Duration duration;

                    try {
                        duration = StringUtils.phraseToDuration(time,
                                ChronoUnit.SECONDS, ChronoUnit.MINUTES,
                                ChronoUnit.HOURS, ChronoUnit.DAYS,
                                ChronoUnit.WEEKS, ChronoUnit.MONTHS,
                                ChronoUnit.YEARS);
                    } catch (IllegalStateException ex1) {
                        Message.playSound(checker, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                        Message.valueOf(type.name() + "_ARGUMENTS").sendMessage(checker, true);
                        return;
                    }

                    Instant expiry = Instant.now().plus(duration);

                    Punishment punishment = new Punishment(
                            UUID.randomUUID(),
                            target.getUniqueId(),
                            Optional.of((checker).getUniqueId()).orElse(null),
                            type,
                            message,
                            false);

                    punishment.setExpiry(expiry);

                    PunishmentManager.getInstance().invoke(punishment, new Flag[0]);
                    Message.playSound(checker, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    type.getPunishSuccessfulMessage().sendFormattedMessage(checker, true, target.getName(), StringUtils.formatTime(duration));
                    checker.closeInventory();
                }));
    }

    public String activePunishmentStateString(PunishmentType firstType, PunishmentType secondType, UUID target) {
        Optional<Punishment> anyActivePunishment = FlatFilePunishments
                .getInstance()
                .getAnyActivePunishment(target)
                .filter(punishment -> punishment.getType().equals(firstType) || punishment.getType().equals(secondType));
        return anyActivePunishment.isEmpty() ? "&a&lYES" : "&c&lNO";
    }

    public String getStatus(OfflinePlayer player) {
        return player.isOnline() ? " &7{&aONLINE&7}" : " &7{&cOFFLINE&7}";
    }

    public Object getAddress(OfflinePlayer player) {
        Player onlinePlayer = player.getPlayer();
        if (onlinePlayer == null) return "";

        return player.isOnline() ? onlinePlayer.getAddress() : "";
    }
}
