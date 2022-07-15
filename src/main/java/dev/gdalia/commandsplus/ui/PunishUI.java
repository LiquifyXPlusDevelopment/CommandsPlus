package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.InventoryUtils;
import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.models.ReportReasonManager;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
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

import java.util.Set;
import java.util.UUID;

public record PunishUI(@Getter Player requester) {

    private static final GuiItem GUI_BORDER = new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create());
    public void openReportGUI(UUID targetUniqueId) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueId);
        Gui gui = new Gui(GuiType.HOPPER, Message.fixColor("&cPunishing &7> &e" + requester.getName()), Set.of());
        gui.disableAllInteractions();

        gui.setItem(0, GUI_BORDER);
        gui.setItem(4, GUI_BORDER);

        gui.setItem(1, new GuiItem(new ItemBuilder(
                Material.STICK,
                "&a&lKICK")
                .addGlow()
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to&a Kick&7 the player&e " + target.getName() + "&7.")
                .create(), event -> openReasonGUI(targetUniqueId, PunishmentType.KICK)));

        gui.setItem(2, new GuiItem(new ItemBuilder(
                Material.FEATHER,
                "&b&lMUTE")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to&b Mute&7 the player&e " + target.getName() + "&7.")
                .create(), event -> openMuteGUI(targetUniqueId)));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.BARRIER,
                "&c&lBAN")
                .addGlow()
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to&c Ban&7 the player&e " + target.getName() + "&7.")
                .create(), event -> openBanGUI(targetUniqueId)));

        gui.open(requester);
    }

    public void openBanGUI(UUID targetUniqueId) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueId);
        Gui gui = new Gui(GuiType.HOPPER, Message.fixColor("&cPunishing &7> &e" + requester.getName()), Set.of());
        gui.disableAllInteractions();

        gui.setItem(0, GUI_BORDER);
        gui.setItem(4, GUI_BORDER);

        gui.setItem(1, new GuiItem(new ItemBuilder(
                Material.BARRIER,
                "&c&lBAN")
                .addGlow()
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to choose punishment&c Ban&7.")
                .create(), event -> openReasonGUI(targetUniqueId, PunishmentType.BAN)));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.BARRIER,
                "&c&lTEMP-BAN")
                .addGlow()
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to choose punishment&c Temp-Ban&7.")
                .create(), event -> {
            //TODO JUST OPEN TIME GUI.
            }));

        gui.open(requester);
    }

    public void openMuteGUI(UUID targetUniqueId) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueId);
        Gui gui = new Gui(GuiType.HOPPER, Message.fixColor("&cPunishing &7> &e" + requester.getName()), Set.of());
        gui.disableAllInteractions();

        gui.setItem(0, GUI_BORDER);
        gui.setItem(4, GUI_BORDER);

        gui.setItem(1, new GuiItem(new ItemBuilder(
                Material.FEATHER,
                "&b&lMUTE")
                .addGlow()
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to choose punishment&b Mute&7.")
                .create(), event -> openReasonGUI(targetUniqueId, PunishmentType.MUTE)));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.FEATHER,
                "&b&lTEMP-MUTE")
                .addGlow()
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to choose punishment&b Temp-Mute&7.")
                .create(), event -> {
            //TODO JUST OPEN TIME GUI.
            }));

        gui.open(requester);
    }

    public void openReasonGUI(UUID targetUniqueId, PunishmentType type) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueId);
        PaginatedGui gui = new BaseUI().basePaginatedGui(6, "&6" + type.getDisplayName() + "ing &7> &e" + target.getName());
        //gui.setCloseGuiAction(event -> Message.REPORT_CANCELLED.sendMessage(checker, true));

        gui.setItem(49, new GuiItem(new ItemBuilder(Material.BARRIER, "&cCancel Report").create(), event -> {
            if (!(event.getWhoClicked() instanceof Player player) || !player.getUniqueId().equals(requester.getUniqueId())) {
                requester.kickPlayer("HEHEHE HA! *King Noises*");
                return;
            }

            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.closeInventory();
        }));

        ReportReasonManager.getInstance().getReportReasons().forEach((key, reasonObject) -> gui.addItem(new GuiItem(new ItemBuilder(reasonObject.getIcon(), "&7" + type.getDisplayName().toLowerCase() + "for: &6" + reasonObject.getDisplayName())
                .addLoreLines(" &r")
                .addLoreLines(reasonObject.getLore().stream().map(x -> x = "Reason: &e" + x).toArray(String[]::new))
                .addLoreLines("Click to choose this " + type.getDisplayName() + " reason.")
                .create(), event -> InventoryUtils.getInstance().invokePunishment(event, type, reasonObject.getLore().toString(), target.getPlayer(), requester))));

        gui.open(requester);
    }
}