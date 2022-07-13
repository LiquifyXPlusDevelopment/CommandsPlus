package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.models.ReportManager;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.reports.Report;
import dev.gdalia.commandsplus.structs.reports.ReportComment;
import dev.gdalia.commandsplus.utils.ReportUtils;
import dev.gdalia.commandsplus.utils.StringUtils;
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

public record CommentsUI(@Getter Player checker) {

    private static final GuiItem GUI_BORDER = new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create());

    public void openCommentsGUI(Report report) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(report.getConvicted());
        PaginatedGui gui = new BaseUI().basePaginatedGui(5, "&6Comments &7> &e" + target.getName());
        gui.disableAllInteractions();

        gui.getFiller().fillBetweenPoints(1, 1, 1, 9, GUI_BORDER);
        gui.getFiller().fillBetweenPoints(5, 1, 5, 9, GUI_BORDER);


        gui.setItem(4, new GuiItem(new ItemBuilder(
                Material.WRITABLE_BOOK,
                "&eWrite a comment")
                .addLoreLines(
                        " &r",
                        "&6Click&7 to write a new comment",
                        "on report.")
                .create(), event -> Optional.of(event.getClick())
                .filter(clickType -> clickType.equals(ClickType.LEFT))
                .filter(x -> !ReportUtils.getInstance().commentText.containsKey(checker.getUniqueId()))
                .ifPresent(comment -> {
                    ReportUtils.getInstance().commentText.put(checker.getUniqueId(), report);
                    checker.closeInventory();
                    Message.TYPE_AN_COMMENT.sendMessage(checker, true);
                })));

        int size = report.getComments() != null ? report.getComments().size() : 0;
        for (int i = 0; i < size; i++) {
            int finalI = i;
            OfflinePlayer reporter = Bukkit.getOfflinePlayer(report.getReporter());

            gui.addItem(new GuiItem(new ItemBuilder(
                    Material.PAPER,
                    "&eComment&7 #" + i)
                    .addGlow()
                    .addLoreLines(
                            " &r",
                            "Commenter: " + report.getComments().get(i).getOfflinePlayer().getName(),
                            "Date: &e" + StringUtils.createTimeFormatter(report.getComments().get(i).getSentAt(), "dd/MM/uu, HH:mm:ss"),
                            "Message: &f" + report.getComments().get(i).getComment(),
                            " &r",
                            "&6Left click&7 to send to reporter.",
                            "&6Drop key&7 to remove.")
                    .create(), event -> {
                ReportUtils.getInstance().sendComment(event, report.getComments().get(finalI), reporter.getPlayer());
                ReportUtils.getInstance().revokeComment(event, report, report.getComments().get(finalI), checker);
            }));
        }

        gui.setItem(40, new GuiItem(new ItemBuilder(
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
    
    public void deleteInitializeCommentsGUI(UUID targetUniqueID, Report report, ReportComment comment) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueID);
        Gui gui = new Gui(GuiType.HOPPER, Message.fixColor("&6Comments &7> &e" + target.getName()), Set.of());
        gui.disableAllInteractions();

        gui.setItem(0, GUI_BORDER);
        gui.setItem(4, GUI_BORDER);

        gui.setItem(1, new GuiItem(new ItemBuilder(
                Material.RED_WOOL,
                "&4&lCANCEL ACTION")
                .addLoreLines(
                        "Click to return to comments",
                        "selection menu.")
                .create(), event -> openCommentsGUI(report)));

        gui.setItem(2, new GuiItem(new ItemBuilder(Material.PLAYER_HEAD, "&aConfirming Report Details")
                .setPlayerSkull(target)
                .addLoreLines(
                        "Comment sender: &6" + comment.getOfflinePlayer().getName(),
                        "Comment date: &8" + StringUtils.createTimeFormatter(comment.getSentAt(), "dd/MM/uu, HH:mm:ss"),
                        "Comment message: &e" + comment.getComment(),
                        "Click to&c DELETE&7 the comment of this player.")
                .create()));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.GREEN_WOOL,
                "&4&lDELETE COMMENT")
                .addLoreLines(
                        "&cClick to delete the comment.",
                        "&cPlease notice that this action is undoable.")
                .create(), event -> {
            ReportManager.getInstance().deleteComment(report, comment);
            openCommentsGUI(report);
            Message.COMMENT_DELETED_SUCCESSFULLY.sendFormattedMessage(checker, true, comment.getOfflinePlayer().getName());
        }));

        gui.open(checker);
    }
}