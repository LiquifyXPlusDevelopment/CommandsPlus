package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.models.ReportManager;
import dev.gdalia.commandsplus.models.Reports;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportStatus;
import dev.gdalia.commandsplus.inventory.InventoryUtils;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

public record ReportHistoryUI(@Getter Player checker) {

    private static final GuiItem GUI_BORDER = new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create());
    public void openReportHistoryGUI(UUID targetUniqueId) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueId);
        PaginatedGui gui = new BaseUI().basePaginatedGui(6, "&6Reports &7> &e" + target.getName());
        gui.disableAllInteractions();


        gui.setItem(49, new GuiItem(new ItemBuilder(Material.BARRIER, "&cClose").create(), event -> {
            if (!(event.getWhoClicked() instanceof Player player) || !player.getUniqueId().equals(checker.getUniqueId())) {
                checker.kickPlayer("HE HE HE HA! *King Noises*");
                return;
            }

            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.closeInventory();
        }));

        Reports.getInstance().getReportHistory(targetUniqueId).forEach(report -> {
            OfflinePlayer Reporter = Bukkit.getOfflinePlayer(report.getReporter());
            OfflinePlayer Reported = Bukkit.getOfflinePlayer(report.getConvicted());

            gui.addItem(new GuiItem(new ItemBuilder(Material.PAPER, "&cReport: &7" + report.getReportUuid())
                    .addGlow()
                    .addLoreLines(
                            " &r",
                            "Status: &6" + report.getStatus().name(),
                            "Date: &e" + report.getSentAt(),
                            " &r",
                            "Reporter: &a" + Reporter.getName() + (Reporter.isOnline() ? " &7{&aOnline&7}" : " &7{&cOffline&7}"),
                            "Reported: &c" + Reported.getName() + (Reported.isOnline() ? " &7{&aOnline&7}" : " &7{&cOffline&7}"))
                    .addLoreLines(report.getReason().getLore().stream().map(x -> x = "Reason: &e" + x).toArray(String[]::new))
                    .addLoreLines(
                            " &r",
                            "&6Left Click&7 to show details.",
                            "&6Drop key&7 to remove.")
                    .create(), event -> {

                Optional.of(event.getClick())
                        .filter(click -> click.equals(ClickType.DROP))
                        .ifPresent(clickable -> deleteInitializeReportsGUI(target.getUniqueId(), report));

                Optional.of(event.getClick())
                        .filter(click -> click.equals(ClickType.LEFT))
                        .ifPresent(clickable -> ReportGUI(targetUniqueId, report));
            }));
        });

        gui.open(checker);
    }

    public void deleteInitializeReportsGUI(UUID targetUniqueID, Report report) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueID);
        Gui gui = new Gui(GuiType.HOPPER, Message.fixColor("&6Reports &7> &e" + target.getName()), Set.of());
        gui.disableAllInteractions();

        gui.setItem(0, GUI_BORDER);
        gui.setItem(4, GUI_BORDER);

        gui.setItem(1, new GuiItem(new ItemBuilder(
                Material.RED_WOOL,
                "&4&lCANCEL ACTION")
                .addLoreLines(
                        "Click to return to reports",
                        "selection menu.")
                .create(), event -> openReportHistoryGUI(target.getUniqueId())));

        gui.setItem(2, new GuiItem(new ItemBuilder(Material.PLAYER_HEAD, "&aConfirming Report Details")
                .setPlayerSkull(target)
                .addLoreLines(
                        "Report target: &6" + target.getName(),
                        "Report type: &8" + report.getReason().getDisplayName())
                .addLoreLines(report.getReason().getLore().stream().map(x -> x = "Report reason: &e" + x).toArray(String[]::new))
                .addLoreLines("Click to&c DELETE&7 the report of this player.")
                .create()));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.GREEN_WOOL,
                "&4&lDELETE REPORT")
                .addLoreLines(
                        "&cClick to delete the report.",
                        "&cPlease notice that this action is undoable.")
                .create(), event -> {
            checker.closeInventory();
            Reports.getInstance().getReport(report.getReportUuid())
                    .ifPresent(deleted -> ReportManager.getInstance().revoke(report));
            Message.REPORT_DELETED_SUCCESSFULLY.sendFormattedMessage(checker, true, target.getName());
        }));

        gui.open(checker);
    }

    public void ReportGUI(UUID targetUniqueID, Report report) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueID);
        Gui gui = new Gui(5, Message.fixColor("&6Reports &7> &e" + target.getName()), Set.of());
        gui.disableAllInteractions();

        gui.getFiller().fillBetweenPoints(1, 1, 1, 9, GUI_BORDER);
        gui.getFiller().fillBetweenPoints(5, 1, 5, 9, GUI_BORDER);

        OfflinePlayer Reporter = Bukkit.getOfflinePlayer(report.getReporter());
        OfflinePlayer Reported = Bukkit.getOfflinePlayer(report.getConvicted());

        gui.setItem(9, new GuiItem(new ItemBuilder(
                Material.PAPER,
                "&cReport: &7" + report.getReportUuid())
                .addGlow()
                .addLoreLines(
                        " &r",
                        "Status: &6" + report.getStatus().name(),
                        "Date: &e" + report.getSentAt(),
                        " &r",
                        "Reporter: &a" + Reporter.getName() + (Reporter.isOnline() ? " &7{&aOnline&7}" : " &7{&cOffline&7}"),
                        "Reported: &c" + Reported.getName() + (Reported.isOnline() ? " &7{&aOnline&7}" : " &7{&cOffline&7}"))
                .addLoreLines(report.getReason().getLore().stream().map(x -> x = "Reason: &e" + x).toArray(String[]::new))
                .addLoreLines(
                        " &r",
                        "&6Left Click&7 to print in chat.")
                .create()));

        gui.setItem(12, new GuiItem(new ItemBuilder(
                Material.PLAYER_HEAD,
                "&7Reporter: &a" + Reporter.getName())
                .setPlayerSkull(Reporter)
                .addLoreLines(
                        " &r",
                        "Sent reports: &b" + Reports.getInstance().getSentReports(Reporter.getUniqueId()).size(),
                        "Received reports: &b" + Reports.getInstance().getReportHistory(report.getReporter()).size(),
                        " &r",
                        "&6Left click&7 to teleport to the",
                        "location of player &e" + Reporter.getName())
                .create(), event -> InventoryUtils.getInstance().teleportTo(event, Reporter.getPlayer(), checker)));

        gui.setItem(13, new GuiItem(new ItemBuilder(
                Material.GOLDEN_AXE,
                "&eAbusive report")
                .addLoreLines(
                        " &r",
                        "&6Click&7 to punish the reporter",
                        "&a" + Reporter.getName())
                .create(), event -> new PunishUI(checker).openPunishGUI(targetUniqueID)));

        gui.setItem(14, new GuiItem(new ItemBuilder(
                Material.PLAYER_HEAD,
                "&7Reported: &c" + Reported.getName())
                .setPlayerSkull(Reported)
                .addLoreLines(
                        " &r",
                        "Sent reports: &b" + Reports.getInstance().getSentReports(Reported.getUniqueId()).size(),
                        "Received reports: &b" + Reports.getInstance().getReportHistory(report.getConvicted()).size(),
                        " &r",
                        "&6Left click&7 to teleport to the",
                        "location of player &e" + Reported.getName())
                .create(), event -> InventoryUtils.getInstance().teleportTo(event, Reported.getPlayer(), checker)));

        gui.setItem(17, new GuiItem(new ItemBuilder(
                Material.ENCHANTED_BOOK,
                "&eData")
                .addLoreLines(
                        " &r",
                        "&eReported: &c" + Reported.getName() + (Reported.isOnline() ? " &7{&aOnline&7}" : " &7{&cOffline&7}"),
                        " GameMode: &b" + (Reported.isOnline() ? Reported.getPlayer().getGameMode().name().toLowerCase() : ""),
                        " Health: &c" + (Reported.isOnline() ? Reported.getPlayer().getHealth() + "/" + Reported.getPlayer().getMaxHealth() : ""),
                        " Food: &6" + (Reported.isOnline() ? Reported.getPlayer().getFoodLevel() : ""),
                        " UUID: &8" + Reported.getUniqueId(),
                        " IP: &e" + (Reported.isOnline() ? Reported.getPlayer().getAddress() : ""),
                        " &r",
                        "&eReporter: &a" + Reporter.getName() + (Reporter.isOnline() ? " &7{&aOnline&7}" : " &7{&cOffline&7}"),
                        " UUID: &8" + Reporter.getUniqueId(),
                        " IP: &e" + (Reporter.isOnline() ? Reporter.getPlayer().getAddress() : ""),
                        " &r",
                        "&6Left click&7 to print in chat.")
                .create()));

        int[] slots = {21, 22, 23};
        for (int i = 0; i < 3; i++) {
            ReportStatus status = ReportStatus.values()[i];
            String displayName = status.getRepresentativeColor() + status.getDisplayName();
            gui.setItem(slots[i], new GuiItem(new ItemBuilder(
                    status.getIcon(),
                    "&eMark as: " + displayName)
                    .addLoreLines(
                            " &r",
                            "&6Click&7 to define the status of report",
                            "&7as: " + displayName)
                    .create(), event -> InventoryUtils.getInstance().changeStatus(event, status, report, checker)));
        }

        gui.setItem(27, new GuiItem(new ItemBuilder(
                Material.FLINT_AND_STEEL,
                "&cRemove report")
                .addLoreLines(
                        " &r",
                        "&6Click&7 to remove permanently",
                        "the report.")
                .create(), event -> deleteInitializeReportsGUI(targetUniqueID, report)));

        gui.setItem(35, new GuiItem(new ItemBuilder(
                Material.BOOK,
                "&eComments of report")
                .addGlow()
                .addLoreLines(
                        " &r",
                        "&6Click&7 to show comments",
                        "of report.")
                .create(), event -> new CommentsUI(checker).openCommentsGUI(report)));

        gui.setItem(40, new GuiItem(new ItemBuilder(
                Material.BARRIER,
                "&cClose")
                .create(), event -> {
            Optional.of(event.getWhoClicked())
                    .stream()
                    .filter(p -> !(p instanceof Player))
                    .filter(p -> !p.getUniqueId().equals(checker.getUniqueId()))
                    .forEach(p -> checker.kickPlayer("HE HE HE HA! *King Noises*"));

            Message.playSound(event.getWhoClicked(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            event.getWhoClicked().closeInventory();
        }));

        gui.open(checker);
    }
}