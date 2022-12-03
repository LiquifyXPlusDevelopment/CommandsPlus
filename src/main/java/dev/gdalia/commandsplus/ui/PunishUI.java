package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.InventoryUtils;
import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.models.Punishments;
import dev.gdalia.commandsplus.models.ReportReasonManager;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.punishments.PunishmentGUI;
import dev.gdalia.commandsplus.structs.punishments.PunishmentType;
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

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
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
                .create(), event -> {
            PunishmentGUI punishmentGUI = new PunishmentGUI(
                    UUID.randomUUID(),
                    target.getUniqueId(),
                    Optional.of((requester).getUniqueId()).orElse(null),
                    PunishmentType.KICK);
            openReasonGUI(targetUniqueId, punishmentGUI);
        }));

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
                .create(), event -> {
            PunishmentGUI punishmentGUI = new PunishmentGUI(
                    UUID.randomUUID(),
                    target.getUniqueId(),
                    Optional.of((requester).getUniqueId()).orElse(null),
                    PunishmentType.BAN);
            openReasonGUI(targetUniqueId, punishmentGUI);
        }));

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
                .create(), event -> openTimeGUI(targetUniqueId, PunishmentType.TEMPBAN)));

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
                .create(), event -> {
            PunishmentGUI punishmentGUI = new PunishmentGUI(
                    UUID.randomUUID(),
                    target.getUniqueId(),
                    Optional.of((requester).getUniqueId()).orElse(null),
                    PunishmentType.MUTE);
            openReasonGUI(targetUniqueId, punishmentGUI);
        }));

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
                .create(), event -> openTimeGUI(targetUniqueId, PunishmentType.TEMPMUTE)));

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
                requester.kickPlayer("HE HE HE HA! *King Noises*");
                return;
            }

            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.closeInventory();
        }));

        PunishmentGUI punishmentGUI = new PunishmentGUI(
                UUID.randomUUID(),
                target.getUniqueId(),
                Optional.of((requester).getUniqueId()).orElse(null),
                type);

        gui.setItem(9, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 30 Minutes")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 30 Minutes."
                ).create(), event -> {
            Duration duration = StringUtils.phraseToDuration("30m",
                    ChronoUnit.SECONDS, ChronoUnit.MINUTES,
                    ChronoUnit.HOURS, ChronoUnit.DAYS,
                    ChronoUnit.WEEKS, ChronoUnit.MONTHS,
                    ChronoUnit.YEARS);

            Instant expiry = Instant.now().plus(duration);

            punishmentGUI.setExpiry(expiry);
            openReasonGUI(targetUniqueID, punishmentGUI);
        }));

        gui.setItem(10, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 12 Hours")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 12 Hours."
                ).create(), event -> {
            Duration duration = StringUtils.phraseToDuration("12h",
                    ChronoUnit.SECONDS, ChronoUnit.MINUTES,
                    ChronoUnit.HOURS, ChronoUnit.DAYS,
                    ChronoUnit.WEEKS, ChronoUnit.MONTHS,
                    ChronoUnit.YEARS);

            Instant expiry = Instant.now().plus(duration);

            punishmentGUI.setExpiry(expiry);
            openReasonGUI(targetUniqueID, punishmentGUI);
        }));

        gui.setItem(11, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 1 Day")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 1 Day."
                ).create(), event -> {
            Duration duration = StringUtils.phraseToDuration("1d",
                    ChronoUnit.SECONDS, ChronoUnit.MINUTES,
                    ChronoUnit.HOURS, ChronoUnit.DAYS,
                    ChronoUnit.WEEKS, ChronoUnit.MONTHS,
                    ChronoUnit.YEARS);

            Instant expiry = Instant.now().plus(duration);

            punishmentGUI.setExpiry(expiry);
            openReasonGUI(targetUniqueID, punishmentGUI);
        }));

        gui.setItem(12, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 2 Days")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 2 Days."
                ).create(), event -> {
            Duration duration = StringUtils.phraseToDuration("2d",
                    ChronoUnit.SECONDS, ChronoUnit.MINUTES,
                    ChronoUnit.HOURS, ChronoUnit.DAYS,
                    ChronoUnit.WEEKS, ChronoUnit.MONTHS,
                    ChronoUnit.YEARS);

            Instant expiry = Instant.now().plus(duration);

            punishmentGUI.setExpiry(expiry);
            openReasonGUI(targetUniqueID, punishmentGUI);
        }));

        gui.setItem(13, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 3 Days")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 3 Days."
                ).create(), event -> {
            Duration duration = StringUtils.phraseToDuration("3d",
                    ChronoUnit.SECONDS, ChronoUnit.MINUTES,
                    ChronoUnit.HOURS, ChronoUnit.DAYS,
                    ChronoUnit.WEEKS, ChronoUnit.MONTHS,
                    ChronoUnit.YEARS);

            Instant expiry = Instant.now().plus(duration);

            punishmentGUI.setExpiry(expiry);
            openReasonGUI(targetUniqueID, punishmentGUI);
        }));

        gui.setItem(14, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 1 Week")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 1 Week."
                ).create(), event -> {
            Duration duration = StringUtils.phraseToDuration("1w",
                    ChronoUnit.SECONDS, ChronoUnit.MINUTES,
                    ChronoUnit.HOURS, ChronoUnit.DAYS,
                    ChronoUnit.WEEKS, ChronoUnit.MONTHS,
                    ChronoUnit.YEARS);

            Instant expiry = Instant.now().plus(duration);

            punishmentGUI.setExpiry(expiry);
            openReasonGUI(targetUniqueID, punishmentGUI);
        }));

        gui.setItem(15, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 2 Weeks")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 2 Weeks."
                ).create(), event -> {
            Duration duration = StringUtils.phraseToDuration("2w",
                    ChronoUnit.SECONDS, ChronoUnit.MINUTES,
                    ChronoUnit.HOURS, ChronoUnit.DAYS,
                    ChronoUnit.WEEKS, ChronoUnit.MONTHS,
                    ChronoUnit.YEARS);

            Instant expiry = Instant.now().plus(duration);

            punishmentGUI.setExpiry(expiry);
            openReasonGUI(targetUniqueID, punishmentGUI);
        }));

        gui.setItem(16, new GuiItem(new ItemBuilder(Material.PAPER, "&6&lTIME &7>&e 3 Weeks")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 To &e&l" + type.getDisplayName().toUpperCase() + "&7 " + target.getName() + " for 3 Weeks."
                ).create(), event -> {
            Duration duration = StringUtils.phraseToDuration("3w",
                    ChronoUnit.SECONDS, ChronoUnit.MINUTES,
                    ChronoUnit.HOURS, ChronoUnit.DAYS,
                    ChronoUnit.WEEKS, ChronoUnit.MONTHS,
                    ChronoUnit.YEARS);

            Instant expiry = Instant.now().plus(duration);

            punishmentGUI.setExpiry(expiry);
            openReasonGUI(targetUniqueID, punishmentGUI);
        }));

        gui.setItem(17, new GuiItem(new ItemBuilder(
                Material.NAME_TAG,
                "&e&lCUSTOM")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to enter a &e&lCUSTOM&7 reason."
                ).create(), event -> {
            InventoryUtils.getInstance().timeText.put(requester.getUniqueId(), punishmentGUI);
            requester.closeInventory();
            requester.sendTitle(Message.fixColor("&e&lCUSTOM"), "&7Please enter in the chat a &e&lCUSTOM&7 time.", 5, 1000000000, 5);
            Message.playSound(requester, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
        }));

        gui.open(requester);
    }

    public void openReasonGUI(UUID targetUniqueId, PunishmentGUI punishmentGUI) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetUniqueId);
        PaginatedGui gui = new BaseUI().basePaginatedGui(6, "&6&l" + punishmentGUI.getType().getDisplayName().toUpperCase() + " &7> &e" + target.getName());

        gui.setItem(49, new GuiItem(new ItemBuilder(Material.BARRIER, "&cCancel Punishment").create(), event -> {
            if (!(event.getWhoClicked() instanceof Player player) || !player.getUniqueId().equals(requester.getUniqueId())) {
                requester.kickPlayer("HE HE HE HA! *King Noises*");
                return;
            }

            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.closeInventory();
        }));

        ReportReasonManager.getInstance().getReportReasons().forEach((key, reasonObject) -> gui.addItem(new GuiItem(new ItemBuilder(reasonObject.getIcon(), "&7" + punishmentGUI.getType().getDisplayName().toLowerCase() + " for: &6" + reasonObject.getDisplayName())
                .addLoreLines(" &r")
                .addLoreLines(reasonObject.getLore().stream().map(x -> x = "Reason: &e" + x).toArray(String[]::new))
                .addLoreLines("Click to choose this " + punishmentGUI.getType().getDisplayName().toLowerCase() + " reason.")
                .create(), event ->  {
            punishmentGUI.setReason(reasonObject.getDisplayName());
            invokeInitializePunishGUI(targetUniqueId, punishmentGUI);
        })));

        gui.setItem(4, new GuiItem(new ItemBuilder(
                Material.NAME_TAG,
                "&e&lCUSTOM")
                .addLoreLines(
                        "&r",
                        "&6Left-Click&7 to enter a &e&lCUSTOM&7 reason."
                ).create(), event -> {
            InventoryUtils.getInstance().reasonText.put(requester.getUniqueId(), punishmentGUI);
            requester.closeInventory();
            requester.sendTitle(Message.fixColor("&e&lCUSTOM"), "&7Please enter in the chat a &e&lCUSTOM&7 reason.", 5, 1000000000, 5);
            Message.playSound(requester, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
        }));

        gui.open(requester);
    }

    public void invokeInitializePunishGUI(UUID targetUniqueID, PunishmentGUI punishmentGUI) {
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
                .create(), event -> openReasonGUI(targetUniqueID, punishmentGUI)));

        gui.setItem(2, new GuiItem(new ItemBuilder(Material.PLAYER_HEAD, "&aConfirming Punishment Details")
                .setPlayerSkull(target)
                .addLoreLines(
                        "Punishment target: &6" + target.getName(),
                        "Punishment type: &8&l" + punishmentGUI.getType().getDisplayName().toUpperCase(),
                        "Punishment time: &e" + (punishmentGUI.getExpiry() == null ? "&c&lPERMANENTLY" : "&e&l" + punishmentGUI.getExpiry()),
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
            if (punishmentGUI.getType().equals(PunishmentType.MUTE) || punishmentGUI.getType().equals(PunishmentType.BAN)) {
                if (Punishments.getInstance().getActivePunishment(punishmentGUI.getPunished(), PunishmentType.valueOf(punishmentGUI.getType().name().toUpperCase()),
                        PunishmentType.valueOf("TEMP" + punishmentGUI.getType().name().toUpperCase())).orElse(null) != null) {
                    Message.valueOf("PLAYER_" + punishmentGUI.getType().getNameAsPunishMsg().toUpperCase()).sendMessage(Bukkit.getPlayer(punishmentGUI.getExecuter()), true);
                    Message.playSound(Bukkit.getPlayer(punishmentGUI.getExecuter()), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    Bukkit.getPlayer(punishmentGUI.getExecuter()).closeInventory();
                    return;
                }
            }

            if (punishmentGUI.getExpiry() == null) {
                InventoryUtils.getInstance().invokePunishment(event, type, reasonObject.getLore().toString().replace("[", "").replace("]", ""), target.getPlayer(), requester);
                return;
            }
            InventoryUtils.getInstance().invokeInstantPunishment(event, type, time, reasonObject.getLore().toString().replace("[", "").replace("]", ""), target.getPlayer(), requester);
        }));

        gui.open(requester);
    }
}