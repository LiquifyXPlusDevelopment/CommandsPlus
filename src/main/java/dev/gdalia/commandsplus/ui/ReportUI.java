package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.models.ReportManager;
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
import java.util.List;
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

        ReportReason.getReasons().entrySet().forEach(entry -> {
            ReportReason reasonObject = entry.getValue();

            gui.addItem(new GuiItem(new ItemBuilder(reasonObject.getIcon(), "&7Report for: &6" + reasonObject.getDisplayName())
                    .addLoreLines(" &r")
                    .addLoreLines(reasonObject.getLore().stream().map(x -> x = "Reason: &e" + x).toArray(String[]::new))
                    .addLoreLines("Click to choose this report reason.")
                    .create(), event -> openInitializeReportGUI(target, reasonObject)));
        });

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
                .addLoreLines("Report target: &6" + target.getName(),
                        "Report type: &8" + reportReason.getDisplayName())
                .addLoreLines(reportReason.getLore().stream().map(x -> x = "Report reason: &e" + x).toArray(String[]::new))
                .addLoreLines("Click to file the report on this player.")
                .create()));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.GREEN_WOOL,
                "&4&lSEND REPORT")
                .addLoreLines("&cClick to send report to staff.",
                        "&cPlease notice reports are being held for 7 days max for review.")
                .create(), event -> {
            Report report = new Report(UUID.randomUUID(), target.getUniqueId(), checker.getUniqueId(), Instant.now(), reportReason, ReportStatus.OPEN, new ArrayList<>());
            ReportManager.getInstance().invoke(report);
            checker.closeInventory();
            Message.REPORT_SUCCESSFULLY.sendFormattedMessage(checker, true, target.getName(), reportReason.getLore());
        }));

        gui.open(checker);
    }

    /*
      staff.sendMessage(Message.fixColor("&7&m-----&e&lNew Report&7&m-----"));
      staff.sendMessage(targetClick);
      staff.sendMessage(playerClick);
      staff.sendMessage(Message.fixColor("&7Type: &e" + name));
      staff.sendMessage(Message.fixColor("&7Reason: &e" + lore));
      staff.sendMessage(Message.fixColor("&7Status: &a" + status));
      staff.sendMessage(Message.fixColor("&7&m---------------------"));
     */

/*
    public void openReportsGUI(int page) {
        Inventory gui = Bukkit.createInventory(null, 54, .replace("%PAGE%", page + ""));

        for (int j = 0; j < 9; j++)
            gui.setItem(j, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "&r").create());

        for (int j = 45; j < 54; j++)
            gui.setItem(j, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "&r").create());

        gui.setItem(4, new ItemBuilder(Material.BOOKSHELF, "&eReports").create());
        gui.setItem(49, new ItemBuilder(Material.BARRIER, "&cClose").create());

        gui.setItem(45, new ItemBuilder(Material.ARROW, ).create());
        gui.setItem(53, new ItemBuilder(Material.ARROW, ).create());
        
        for (int i = 0; i < Main.getReportsConfig().getKeys(false).size();) {
            i++;
            ConfigurationSection section = Main.getReportsConfig().getConfigurationSection("Report-" + i);

            if (section == null) {
                Message.CONTACT_AN_ADMIN.sendMessage(checker, true);
                Message.playSound(checker, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return;
            }

            Player reported = Bukkit.getPlayer(UUID.fromString(section.getString(ConfigFields.ReportsFields.REPORTED)));
            Player executer = Bukkit.getPlayer(UUID.fromString(section.getString(ConfigFields.ReportsFields.EXECUTER)));
            String reason = section.getString(ConfigFields.ReportsFields.REASON);
            String status = section.getString(ConfigFields.ReportsFields.STATUS);
            String date = section.getString(ConfigFields.ReportsFields.DATE);
            if (reported == null || executer == null) {
                Message.CONTACT_AN_ADMIN.sendMessage(checker, true);
                Message.playSound(checker, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return;
            }

            String reportedStatus = reported.isOnline() ? " &7{&aonline&7}" : " &7{&coffline&7}";
            String executerStatus = executer.isOnline() ? " &7{&aonline&7}" : " &7{&coffline&7}";
             List<String> ItemLore = Arrays.asList(
                     " ",
                     "&7Status: " + status,
                     "&7Date: &e" + date,
                     " ",
                     "&7Executer: &a" + executer.getName() + executerStatus,
                     "&7Reported: &c" + reported.getName() + reportedStatus,
                     "&7Reason: &6" + reason,
                     " ",
                     "&6Click&7 to show details.",
                     "&6Drop key&7 to remove."
             );
            gui.addItem(new ItemBuilder(Material.PAPER, "&cReport #" + i).setLore(ItemLore).create());
        }
        checker.openInventory(gui);
    }*/
}
