package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.Main;
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
import net.liquifymc.integrations.anvil.inventory.AnvilInventory;
import net.liquifymc.integrations.anvil.slots.AnvilSlot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

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
            openTimeGUI(targetUniqueId, PunishmentType.TEMPBAN);
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
            openTimeGUI(targetUniqueId, PunishmentType.TEMPMUTE);
            }));

        gui.open(requester);
    }

    public void openTimeGUI(UUID targetUniqueID, PunishmentType type) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueID);
        Gui gui = new Gui(3, Message.fixColor("&6&lTIME &7> &e" + target.getName()), Set.of());

        gui.disableAllInteractions();
        gui.getFiller().fillBetweenPoints(1, 1, 1, 9, GUI_BORDER);
        gui.getFiller().fillBetweenPoints(3, 1, 3, 9, GUI_BORDER);

        gui.setItem(22, new GuiItem(new ItemBuilder(Material.BARRIER, "&cCancel Punishment").create(), event -> {
            if (!(event.getWhoClicked() instanceof Player player) || !player.getUniqueId().equals(requester.getUniqueId())) {
                requester.kickPlayer("HEHEHE HA! *King Noises*");
                return;
            }

            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.closeInventory();
        }));

        gui.setItem(9, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 30 Minutes")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 30 Minutes."
                ).create(), event -> openReasonGUI(targetUniqueID, type, "30m")));

        gui.setItem(10, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 12 Hours")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 12 Hours."
                ).create(), event -> openReasonGUI(targetUniqueID, type, "12h")));

        gui.setItem(11, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 1 Day")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 1 Day."
                ).create(), event -> openReasonGUI(targetUniqueID, type, "1d")));

        gui.setItem(12, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 2 Days")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 2 Days."
                ).create(), event -> openReasonGUI(targetUniqueID, type, "2d")));

        gui.setItem(13, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 3 Days")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 3 Days."
                ).create(), event -> openReasonGUI(targetUniqueID, type, "3d")));

        gui.setItem(14, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 1 Week")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 1 Week."
                ).create(), event -> openReasonGUI(targetUniqueID, type, "1w")));

        gui.setItem(15, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 2 Weeks")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 2 Weeks."
                ).create(), event -> openReasonGUI(targetUniqueID, type, "2w")));

        gui.setItem(16, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 3 Weeks")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 3 Weeks."
                ).create(), event -> openReasonGUI(targetUniqueID, type, "3w")));

        gui.setItem(17, new GuiItem(new ItemBuilder(
                Material.NAME_TAG,
                "&e&lCUSTOM")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to enter a &e&lCUSTOM&7 time."
                ).create(), event -> {
            //TODO ADD CUSTOM TYPING SYSTEM.
        }));

        gui.open(requester);
    }

    public void openReasonGUI(UUID targetUniqueId, PunishmentType type, String time) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueId);
        PaginatedGui gui = new BaseUI().basePaginatedGui(6, "&6&l" + type.getDisplayName().toUpperCase() + " &7> &e" + target.getName());
        //gui.setCloseGuiAction(event -> Message.REPORT_CANCELLED.sendMessage(checker, true));

        gui.setItem(49, new GuiItem(new ItemBuilder(Material.BARRIER, "&cCancel Punishment").create(), event -> {
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
            invokeInitializePunishGUI(targetUniqueId, type, reasonObject.getLore().toString().replace("[", "").replace("]", ""), time);
        })));

        gui.setItem(4, new GuiItem(new ItemBuilder(
                Material.NAME_TAG,
                "&e&lCUSTOM")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to enter a &e&lCUSTOM&7 reason."
                ).create(), event -> {
            AnvilInventory inventory = new AnvilInventory(requester, inv -> {
                String reason = inv.getName();

                if (inv.getSlot().equals(AnvilSlot.OUTPUT) && reason.length() < 25) {
                    invokeInitializePunishGUI(targetUniqueId, type, reason, time);

                    inv.setWillClose(true);
                    inv.setWillDestroy(true);
                } else {
                    inv.setWillClose(false);
                    inv.setWillDestroy(false);
                }
            }, Main.getInstance());

            inventory.setSlot(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.NAME_TAG, "Enter a valid reason").addLoreLines(
                    "&7",
                    "&cIF YOU HAVE CLICKED ON THE OUTPUT AND NOTHING HAS CHANGED SO YOU NEED TO CHECK THE RULES.",
                    "&7",
                    "&7Rules:",
                    "&6 - Don't make the name length higher than 25.",
                    "&7").create());
            inventory.open(Message.fixColor("Custom Reason"));
        }));

        gui.open(requester);
    }

    public void invokeInitializePunishGUI(UUID targetUniqueID, PunishmentType type, String reasonObject, String time) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueID);
        Gui gui = new Gui(GuiType.HOPPER, Message.fixColor("&6Punishment &7> &e" + target.getName()), Set.of());
        gui.disableAllInteractions();

        gui.setItem(0, GUI_BORDER);
        gui.setItem(4, GUI_BORDER);

        gui.setItem(1, new GuiItem(new ItemBuilder(
                Material.RED_WOOL,
                "&4&lCANCEL ACTION")
                .addLoreLines(
                        "Click to return to reasons",
                        "selection menu.")
                .create(), event -> openReasonGUI(targetUniqueID, type, time)));

        gui.setItem(2, new GuiItem(new ItemBuilder(Material.PLAYER_HEAD, "&aConfirming Punishment Details")
                .setPlayerSkull(target)
                .addLoreLines(
                        "Punishment target: &6" + target.getName(),
                        "Punishment type: &8&l" + type.getDisplayName().toUpperCase(),
                        "Punishment time: &e" + (time == null ? "&c&lPERMANENTLY" : "&e&l" + time),
                        "&r",
                        "&6Left-Click&7 to&c&l INVOKE&7 the punishment of this player.")
                .create()));

        gui.setItem(3, new GuiItem(new ItemBuilder(
                Material.GREEN_WOOL,
                "&4&lINVOKE PUNISHMENT")
                .addLoreLines(
                        "&cClick to invoke the punishment.",
                        "&cPlease notice that this action is undoable.")
                .create(), event -> {
            if (time == null) {
                InventoryUtils.getInstance().invokePunishment(event, type, reasonObject, target.getPlayer(), requester);
                return;
            }

            InventoryUtils.getInstance().invokeInstantPunishment(event, type, time, reasonObject, target.getPlayer(), requester);
        }));

        gui.open(requester);
    }
}