package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.inventory.InventoryFields;
import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.listeners.InventoryListener;
import dev.gdalia.commandsplus.structs.Message;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public record ReportUI(@Getter UUID t, @Getter UUID c) {

    /*
    public void openReportGUI() {
        Main plugin = Main.getInstance();
        Player target = Bukkit.getPlayer(t);
        Player player = Bukkit.getPlayer(c);

        Inventory inv = Bukkit.createInventory(null, 54, InventoryListener.REPORT_INV.replace("%TARGET%", target.getName()));
        ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection("default-reasons");

        for (int j = 9; j < 18; j++)
            inv.setItem(j, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "&r").create());

        for (int j = 45; j < 54; j++)
            inv.setItem(j, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, "&r").create());

        inv.setItem(4, new ItemBuilder(Material.BOOK, "&e" + target.getName()).create());
        inv.setItem(49, new ItemBuilder(Material.BARRIER, "&cCancel Report").create());

        for (int i = 0; i < section.getKeys(false).size();) {
            i++;
            if (plugin.getConfig().getString("default-reasons.Reason" + i) == null || section.getKeys(false).size() > 27) {
                Message.CONTACT_AN_ADMIN.sendMessage(player, true);
                Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return;
            }

            String name = plugin.getConfig().getString("default-reasons.Reason" + i + ".Name");
            String lore = plugin.getConfig().getString("default-reasons.Reason" + i + ".Lore");
            String material = plugin.getConfig().getString("default-reasons.Reason" + i + ".Item");

            List<String> list = Arrays.asList(
                    " ",
                    "&6Click&7 to report player &c" + target.getName(),
                    InventoryFields.ReportFields.REASON + lore);

            inv.setItem(17 + i, new ItemBuilder(Material.valueOf(material.toUpperCase()), InventoryFields.ReportFields.NAME + name).setLore(list).create());
        }

        player.openInventory(inv);
    }
     */
}
