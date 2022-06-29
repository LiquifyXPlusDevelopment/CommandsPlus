package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.inventory.InventoryFields;
import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.listeners.InventoryListener;
import dev.gdalia.commandsplus.models.ConfigFields;
import dev.gdalia.commandsplus.structs.Message;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public record ReportUI(@Getter Player checker) {


    private final static Plugin plugin = Main.getInstance();
    private final static ConfigurationSection DEFAULT_REASONS = Main.getInstance().getConfig().getConfigurationSection("default-reasons");

    private final static String
            NEXT_PAGE = "&6Next Page >>",
            PREV_PAGE = "&6<< Previous Page",
            REPORT_INV = "&6Report &7> &e%TARGET%",
            REPORTS_INV = "&6Reports &7> &ePage %PAGE%";

    public void openReportGUIUpdated(Player target) {
        PaginatedGui gui = new PaginatedGui(6, REPORT_INV.replace("%TARGET%", target.getName()));
        gui.disableAllInteractions();
        gui.setCloseGuiAction(event -> {
            if (!event.getPlayer().getUniqueId().equals(checker.getUniqueId())) {
                checker.kickPlayer("HEHEHE HA! *King Noises*");
                return;
            }

            //TODO Gdalia add message that you want here because I don't know what to type here.
        });


        gui.getFiller().fillTop(new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create()));
        gui.getFiller().fillBottom(new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create()));

        gui.setItem(4, new GuiItem(new ItemBuilder(Material.BOOK, "&e" + target.getName()).create()));
        gui.setItem(49, new GuiItem(new ItemBuilder(Material.BARRIER, "&cCancel Report").create(), event -> {
            if (!(event.getWhoClicked() instanceof Player player) || !player.getUniqueId().equals(checker.getUniqueId())) {
                checker.kickPlayer("HEHEHE HA! *King Noises*");
                return;
            }

            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.closeInventory();
        }));



        gui.open(checker);
    }

    public void openReportGUI(Player target) {

        for (int i = 0; i < DEFAULT_REASONS.getKeys(false).size();) {
            i++;
            if (plugin.getConfig().getString("default-reasons.Reason" + i) == null || !Material.valueOf(plugin.getConfig().getString("default-reasons.Reason" + i + ".Item").toUpperCase()).isItem() || DEFAULT_REASONS.getKeys(false).size() > 27 || DEFAULT_REASONS.getKeys(false).size() == 0) {
                Message.CONTACT_AN_ADMIN.sendMessage(checker, true);
                Message.playSound(checker, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return;
            }

            String name = plugin.getConfig().getString("default-reasons.Reason" + i + ".Name");
            String lore = plugin.getConfig().getString("default-reasons.Reason" + i + ".Lore");
            String material = plugin.getConfig().getString("default-reasons.Reason" + i + ".Item");

            List<String> ItemLore = Arrays.asList(
                    " ",
                    "&6Click&7 to report player &c" + target.getName(),
                    InventoryFields.ReportFields.REASON + lore);

            gui.setItem(17 + i, new ItemBuilder(Material.valueOf(material.toUpperCase()), InventoryFields.ReportFields.NAME + name).setLore(ItemLore).create());
        }

        checker.openInventory(gui);
    }

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
    }
}
