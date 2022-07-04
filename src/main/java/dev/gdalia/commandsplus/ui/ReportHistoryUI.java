package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.models.ReportManager;
import dev.gdalia.commandsplus.models.Reports;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportStatus;
import dev.gdalia.commandsplus.utils.ReportUtils;
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
                checker.kickPlayer("HEHEHE HA! *King Noises*");
                return;
            }

            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.closeInventory();
        }));

        Reports.getInstance().getReportHistory(targetUniqueId).forEach(entry -> {
            OfflinePlayer Reporter = Bukkit.getOfflinePlayer(entry.getReporter());
            OfflinePlayer Reported = Bukkit.getOfflinePlayer(entry.getConvicted());

            gui.addItem(new GuiItem(new ItemBuilder(Material.PAPER, "&cReport: &7" + entry.getReportUuid())
                    .addGlow()
                    .addLoreLines(" &r")
                    .addLoreLines("Status: &6" + entry.getStatus().name())
                    .addLoreLines("Date: &e" + entry.getSentAt())
                    .addLoreLines(" &r")
                    .addLoreLines("Reporter: &a" + Reporter.getName() + (Reporter.isOnline() ? " &7{&aonline&7}" : " &7{&coffline&7}"))
                    .addLoreLines("Reported: &c" + Reported.getName() + (Reported.isOnline() ? " &7{&aonline&7}" : " &7{&coffline&7}"))
                    .addLoreLines(entry.getReason().getLore().stream().map(x -> x = "Reason: &e" + x).toArray(String[]::new))
                    .addLoreLines(" &r")
                    .addLoreLines("&6Left Click&7 to show details.")
                    .addLoreLines("&6Drop key&7 to remove.")
                    .create(), event -> {
                Report report = new Report(entry.getReportUuid(), entry.getConvicted(), entry.getReporter(), entry.getSentAt(), entry.getReason(), entry.getStatus(), new ArrayList<>());
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
                .addLoreLines("Report target: &6" + target.getName(),
                        "Report type: &8" + report.getReason().getDisplayName())
                .addLoreLines(report.getReason().getLore().stream().map(x -> x = "Report reason: &e" + x).toArray(String[]::new))
                .addLoreLines("Click to&c DELETE&7 the report of this player.")
                .create()));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.GREEN_WOOL,
                "&4&lDELETE REPORT")
                .addLoreLines("&cClick to delete the report.",
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
        Gui gui = new Gui(6, Message.fixColor("&6Reports &7> &e" + target.getName()), Set.of());
        gui.disableAllInteractions();

        gui.getFiller().fillBetweenPoints(2, 1, 2, 9, GUI_BORDER);
        gui.getFiller().fillBetweenPoints(6, 1, 6, 9, GUI_BORDER);

        OfflinePlayer Reporter = Bukkit.getOfflinePlayer(report.getReporter());
        OfflinePlayer Reported = Bukkit.getOfflinePlayer(report.getConvicted());

        gui.setItem(18, new GuiItem(new ItemBuilder(
                Material.PAPER,
                "&cReport: &7" + report.getReportUuid())
                .addGlow()
                .addLoreLines(" &r")
                .addLoreLines("Status: &6" + report.getStatus().name())
                .addLoreLines("Date: &e" + report.getSentAt())
                .addLoreLines(" &r")
                .addLoreLines("Reporter: &a" + Reporter.getName() + (Reporter.isOnline() ? " &7{&aonline&7}" : " &7{&coffline&7}"))
                .addLoreLines("Reported: &c" + Reported.getName() + (Reported.isOnline() ? " &7{&aonline&7}" : " &7{&coffline&7}"))
                .addLoreLines(report.getReason().getLore().stream().map(x -> x = "Reason: &e" + x).toArray(String[]::new))
                .addLoreLines(" &r")
                .addLoreLines("&6Left Click&7 to print in chat.")
                .create()));

        gui.setItem(21, new GuiItem(new ItemBuilder(
                Material.PLAYER_HEAD,
                "&7Reporter: &a" + Reporter.getName())
                .setPlayerSkull(Reporter)
                .addLoreLines(" &r")
                .addLoreLines("Sent reports: &b" + ReportUtils.getReports(Reporter).size())
                .addLoreLines("Received reports: &b" + Reports.getInstance().getReportHistory(report.getReporter()).size())
                .addLoreLines(" &r")
                .addLoreLines("&6Left click&7 to teleport to the")
                .addLoreLines("location of player &e" + Reporter.getName())
                .create(), event -> ReportUtils.teleportTo(event, Reporter.getPlayer(), checker)));

        gui.setItem(22, new GuiItem(new ItemBuilder(
                Material.GOLDEN_AXE,
                "&eAbusive report")
                .addLoreLines(" &r")
                .addLoreLines("&6Click&7 to punish the reporter")
                .addLoreLines("&a" + Reporter.getName())
                .create()));

        gui.setItem(23, new GuiItem(new ItemBuilder(
                Material.PLAYER_HEAD,
                "&7Reported: &c" + Reported.getName())
                .setPlayerSkull(Reported)
                .addLoreLines(" &r")
                .addLoreLines("Sent reports: &b" + ReportUtils.getReports(Reported).size())
                .addLoreLines("Received reports: &b" + Reports.getInstance().getReportHistory(report.getConvicted()).size())
                .addLoreLines(" &r")
                .addLoreLines("&6Left click&7 to teleport to the")
                .addLoreLines("location of player &e" + Reported.getName())
                .create(), event -> ReportUtils.teleportTo(event, Reported.getPlayer(), checker)));

        gui.setItem(26, new GuiItem(new ItemBuilder(
                Material.ENCHANTED_BOOK,
                "&eData")
                .addLoreLines(" &r")
                .addLoreLines("&eReported: &c" + Reported.getName() + (Reported.isOnline() ? " &7{&aonline&7}" : " &7{&coffline&7}"))
                .addLoreLines(" Gamemode: &b" + (Reported.isOnline() ? Reported.getPlayer().getGameMode().name().toLowerCase() : ""))
                .addLoreLines(" Health: &c" + (Reported.isOnline() ? Reported.getPlayer().getHealth() + "/" + Reported.getPlayer().getMaxHealth() : ""))
                .addLoreLines(" Food: &6" + (Reported.isOnline() ? Reported.getPlayer().getFoodLevel() : ""))
                .addLoreLines(" UUID: &8" + Reported.getUniqueId())
                .addLoreLines(" IP: &e" + (Reported.isOnline() ? Reported.getPlayer().getAddress() : ""))
                .addLoreLines(" &r")
                .addLoreLines("&eReporter: &a" + Reporter.getName() + (Reporter.isOnline() ? " &7{&aonline&7}" : " &7{&coffline&7}"))
                .addLoreLines(" UUID: &8" + Reporter.getUniqueId())
                .addLoreLines(" IP: &e" + (Reporter.isOnline() ? Reporter.getPlayer().getAddress() : ""))
                .addLoreLines(" &r")
                .addLoreLines("&6Left click&7 to print in chat.")
                .create()));

        int[] slots = {21, 22, 23};
        for (int i = 0; i < 3; i++) {
            ReportStatus status = ReportStatus.values()[i];
            String displayName = status.getRepresentativeColor() + status.getDisplayName();
            gui.setItem(slots[i], new GuiItem(new ItemBuilder(
                    status.getIcon(),
                    "&eMark as: " + displayName)
                    .addLoreLines(" &r")
                    .addLoreLines("&6Click&7 to define the status of report")
                    .addLoreLines("&7as: " + displayName)
                    .create(), event -> ReportUtils.getInstance().changeStatus(event, status, report, checker)));
        }

        gui.setItem(36, new GuiItem(new ItemBuilder(
                Material.FLINT_AND_STEEL,
                "&cRemove report")
                .addLoreLines(" &r")
                .addLoreLines("&6Click&7 to remove permanently")
                .addLoreLines("the report.")
                .create(), event -> deleteInitializeReportsGUI(targetUniqueID, report)));

        gui.setItem(44, new GuiItem(new ItemBuilder(
                Material.BOOK,
                "&eComments of report")
                .addGlow()
                .addLoreLines(" &r")
                .addLoreLines("&6Click&7 to show comments")
                .addLoreLines("of report.")
                .create()));

        gui.setItem(49, new GuiItem(new ItemBuilder(
                Material.BARRIER,
                "&cClose")
                .create(), event -> {
            Optional.of(event.getWhoClicked())
                    .stream()
                    .filter(p -> !(p instanceof Player))
                    .filter(p -> !p.getUniqueId().equals(checker.getUniqueId()))
                    .forEach(p -> checker.kickPlayer("HEHEHE HA! *King Noises*"));

            Message.playSound(event.getWhoClicked(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            event.getWhoClicked().closeInventory();
        }));

        gui.open(checker);
    }
}
