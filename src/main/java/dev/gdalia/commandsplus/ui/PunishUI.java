package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.InventoryUtils;
import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.models.Punishments;
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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.function.IntFunction;

public record PunishUI(@Getter Player requester) {

    private static final GuiItem GUI_BORDER = new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create());
    public void openPunishGUI(UUID targetUniqueId) {
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
                        "&6Left-Click&7 to&a&l KICK&7 the player&e " + target.getName() + "&7.")
                .create(), event -> openReasonGUI(targetUniqueId, PunishmentType.KICK, null)));

        gui.setItem(2, new GuiItem(new ItemBuilder(
                Material.FEATHER,
                "&b&lMUTE")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to&b&l MUTE&7 the player&e " + target.getName() + "&7.")
                .create(), event -> openMuteGUI(targetUniqueId)));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.BARRIER,
                "&c&lBAN")
                .addGlow()
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to&c&l BAN&7 the player&e " + target.getName() + "&7.")
                .create(), event -> openBanGUI(targetUniqueId)));

        gui.open(requester);
    }

    public void openBanGUI(UUID targetUniqueId) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueId);
        Gui gui = new Gui(GuiType.HOPPER, Message.fixColor("&cPunishing &7> &e" + requester.getName()), Set.of());
        gui.disableAllInteractions();

        gui.setItem(0, GUI_BORDER);
        gui.setItem(4, GUI_BORDER);

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.RED_WOOL,
                "&c&lBAN")
                .addGlow()
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to choose punishment&c&l BAN&7.")
                .create(), event -> openReasonGUI(targetUniqueId, PunishmentType.BAN, null)));

        gui.setItem(2, new GuiItem(new ItemBuilder(
                Material.PLAYER_HEAD,
                "&eTarget")
                .setPlayerSkull(target)
                .addLoreLines(
                        "&r",
                        "&7Name: &e" + target.getName(),
                        "&7Punishable: " + InventoryUtils.getInstance().activePunishment(PunishmentType.BAN, PunishmentType.TEMPBAN, targetUniqueId)
                ).create()));

        gui.setItem(1, new GuiItem(new ItemBuilder(
                Material.ORANGE_WOOL,
                "&6&lTEMP-BAN")
                .addGlow()
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to choose punishment&6&l TEMP-BAN&7.")
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

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.YELLOW_WOOL,
                "&e&lMUTE")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to choose punishment&e&l MUTE&7.")
                .create(), event -> openReasonGUI(targetUniqueId, PunishmentType.MUTE, null)));

        gui.setItem(2, new GuiItem(new ItemBuilder(
                Material.PLAYER_HEAD,
                "&eTarget")
                .setPlayerSkull(target)
                .addLoreLines(
                        "&r",
                        "&7Name: &e" + target.getName(),
                        "&7Punishable: " + InventoryUtils.getInstance().activePunishment(PunishmentType.MUTE, PunishmentType.TEMPMUTE, targetUniqueId)
                ).create()));

        gui.setItem(1, new GuiItem(new ItemBuilder(
                Material.GREEN_WOOL,
                "&a&lTEMP-MUTE")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to choose punishment&a&l TEMP-MUTE&7.")
                .create(), event -> {
            //TODO JUST OPEN TIME GUI.
            }));

        gui.open(requester);
    }

    public void openTimeGUI(UUID targetUniqueID, PunishmentType type) {

    }

    public void openReasonGUI(UUID targetUniqueId, PunishmentType type, String time) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueId);
        PaginatedGui gui = new BaseUI().basePaginatedGui(6, "&6&l" + type.getDisplayName().toUpperCase() + " &7> &e" + target.getName());
        //gui.setCloseGuiAction(event -> Message.REPORT_CANCELLED.sendMessage(checker, true));

        gui.setItem(49, new GuiItem(new ItemBuilder(Material.BARRIER, "&cCancel Report").create(), event -> {
            if (!(event.getWhoClicked() instanceof Player player) || !player.getUniqueId().equals(requester.getUniqueId())) {
                requester.kickPlayer("HEHEHE HA! *King Noises*");
                return;
            }

            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.closeInventory();
        }));

        ReportReasonManager.getInstance().getReportReasons().forEach((key, reasonObject) -> gui.addItem(new GuiItem(new ItemBuilder(reasonObject.getIcon(), "&7" + type.getDisplayName().toLowerCase() + " for: &6" + reasonObject.getDisplayName())
                .addLoreLines(" &r")
                .addLoreLines(reasonObject.getLore().stream().map(x -> x = "Reason: &e" + x).toArray(String[]::new))
                .addLoreLines("Click to choose this " + type.getDisplayName().toLowerCase() + " reason.")
                .create(), event -> {
            if (time == null) {
                InventoryUtils.getInstance().invokePunishment(event, type, reasonObject.getLore().toString().replace("[", "").replace("]", ""), target.getPlayer(), requester);
                return;
            }

            InventoryUtils.getInstance().invokeInstantPunishment(event, type, time, reasonObject.getLore().toString().replace("[", "").replace("]", ""), target.getPlayer(), requester);
        })));

        gui.setItem(4, new GuiItem(new ItemBuilder(
                Material.NAME_TAG,
                "&e&lCUSTOM")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to enter a &e&lCUSTOM&7 reason."
                ).create(), event -> {
            //TODO ADD CUSTOM TYPING SYSTEM.
        }));

        gui.open(requester);
    }
}