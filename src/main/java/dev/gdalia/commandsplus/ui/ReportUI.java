package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.models.ReportManager;
import dev.gdalia.commandsplus.models.ReportReasonManager;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportReason;
import dev.gdalia.commandsplus.structs.reports.ReportStatus;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public record ReportUI(@Getter Player checker) {

    private static final GuiItem GUI_BORDER = new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create());
    public void openReportGUI(Player target) {
        PaginatedGui gui = new BaseUI().basePaginatedGui(6, "&6Reporting &7> &e" + target.getName());
        //gui.setCloseGuiAction(event -> Message.REPORT_CANCELLED.sendMessage(checker, true));

        gui.setItem(49, new GuiItem(new ItemBuilder(Material.BARRIER, "&cCancel Report").create(), event -> {
            if (!(event.getWhoClicked() instanceof Player player) || !player.getUniqueId().equals(checker.getUniqueId())) {
                checker.kickPlayer("HEHEHE HA! *King Noises*");
                return;
            }

            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.closeInventory();
        }));

        ReportReasonManager.getInstance().getReportReasons().forEach((key, reasonObject) -> gui.addItem(new GuiItem(new ItemBuilder(reasonObject.getIcon(), "&7Report for: &6" + reasonObject.getDisplayName())
                .addLoreLines(" &r")
                .addLoreLines(reasonObject.getLore().stream().map(x -> x = "Reason: &e" + x).toArray(String[]::new))
                .addLoreLines("Click to choose this report reason.")
                .create(), event -> openInitializeReportGUI(target, reasonObject))));

        gui.open(checker);
    }

    public void openInitializeReportGUI(Player target, ReportReason reportReason) {
        Gui gui = new Gui(GuiType.HOPPER, Message.fixColor("&e" + target.getName() + " &7> &6" + reportReason.getDisplayName()), Set.of());
        gui.disableAllInteractions();

        gui.setItem(0, GUI_BORDER);
        gui.setItem(4, GUI_BORDER);

        gui.setItem(1, new GuiItem(new ItemBuilder(
                Material.RED_WOOL,
                "&4&lCANCEL REPORT")
                .addLoreLines(
                        "Click to return to report",
                        "Reason selection menu.")
                .create(), event -> openReportGUI(target)));

        gui.setItem(2, new GuiItem(new ItemBuilder(Material.PLAYER_HEAD, "&aConfirming Report Details")
                .setPlayerSkull(target)
                .addLoreLines(
                        "Report target: &6" + target.getName(),
                        "Report type: &8" + reportReason.getDisplayName())
                .addLoreLines(reportReason.getLore().stream().map(x -> x = "Report reason: &e" + x).toArray(String[]::new))
                .addLoreLines("Click to file the report on this player.")
                .create()));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.GREEN_WOOL,
                "&4&lSEND REPORT")
                .addLoreLines(
                        "&cClick to send report to staff.",
                        "&cPlease notice reports are being held for 7 days max for review.")
                .create(), event -> {
            Report report = new Report(UUID.randomUUID(), target.getUniqueId(), checker.getUniqueId(), Instant.now(), reportReason, ReportStatus.OPEN, new ArrayList<>());
            ReportManager.getInstance().invoke(report);
            checker.closeInventory();
            Message.REPORT_SUCCESSFULLY.sendFormattedMessage(checker, true, target.getName(), reportReason.getLore());
        }));

        gui.open(checker);
    }
}
