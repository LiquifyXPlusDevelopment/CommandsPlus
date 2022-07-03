package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.models.ReportManager;
import dev.gdalia.commandsplus.models.Reports;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportStatus;
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

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
                    .addLoreLines(" &r")
                    .addLoreLines("Status: &6" + entry.getStatus().name())
                    .addLoreLines("Date: &e" + entry.getSentAt())
                    .addLoreLines(" &r")
                    .addLoreLines("Reporter: &a" + Reporter.getName())
                    .addLoreLines("Reported: &c" + Reported.getName())
                    .addLoreLines(entry.getReason().getLore().stream().map(x -> x = "Reason: &e" + x).toArray(String[]::new))
                    .addLoreLines(" &r")
                    .addLoreLines("&6Click&7 to show details.")
                    .addLoreLines("&6Drop key&7 to remove.")
                    .create(), event -> {
                Report report = new Report(entry.getReportUuid(), entry.getConvicted(), entry.getReporter(), entry.getSentAt(), entry.getReason(), entry.getStatus());
                Optional.of(event.getClick())
                        .stream()
                        .filter(click -> click.equals(ClickType.DROP))
                        .map(status -> report.getStatus().name())
                        .filter(status -> !status.equals(ReportStatus.CLOSED.name()))
                        .forEach(clickable -> closeInitializeReportsGUI(target.getUniqueId(), report));
            }));
        });

        gui.open(checker);
    }

    public void closeInitializeReportsGUI(UUID targetUniqueID, Report report) {
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
                .addLoreLines("Click to&c close&7 the report of this player.")
                .create()));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.GREEN_WOOL,
                "&4&lCLOSE REPORT")
                .addLoreLines("&cClick to close the report.",
                        "&cPlease notice that this action is undoable.")
                .create(), event -> {

            ReportManager.getInstance().changeStatus(report, ReportStatus.CLOSED);
            checker.closeInventory();
            Message.REPORT_CLOSED_SUCCESSFULLY.sendFormattedMessage(checker, true, target.getName());
        }));

        gui.open(checker);
    }
}
