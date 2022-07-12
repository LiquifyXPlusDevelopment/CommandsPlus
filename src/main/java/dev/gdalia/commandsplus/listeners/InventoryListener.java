package dev.gdalia.commandsplus.listeners;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.inventory.InventoryFields;
import dev.gdalia.commandsplus.models.ConfigFields;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.ui.ReportUI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InventoryListener implements Listener {

    Main plugin = Main.getInstance();

    /*
    @EventHandler
    public void onReportClick(InventoryClickEvent event) {
        ItemStack is = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();
        Player target = Bukkit.getPlayer(ChatColor.stripColor(event.getInventory().getItem(4).getItemMeta().getDisplayName()));

        if (target == null) return;
        if (!event.getView().getTitle().equalsIgnoreCase(.replace("%TARGET%", target.getName()))) return;
        event.setCancelled(true);

        if (is == null || !is.hasItemMeta() || !is.getItemMeta().hasDisplayName()) return;
        int number = Main.getReportsConfig().getKeys(false).isEmpty() ? 1 : Main.getReportsConfig().getKeys(false).size() + 1;

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("default-reasons");
        ConfigurationSection reports = Main.getReportsConfig().createSection("Report-" + number);

        if (!event.isLeftClick()) return;
        if (event.getSlot() == 49) {

        }

        for (int i = 0; i < section.getKeys(false).size();) {
            i++;
            String name = plugin.getConfig().getString("default-reasons.Reason" + i + ".Name");
            String lore = plugin.getConfig().getString("default-reasons.Reason" + i + ".Lore");

            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            if (is.getItemMeta().getDisplayName().equals(Message.fixColor(InventoryFields.ReportFields.NAME + name))) {
                reports.set(ConfigFields.ReportsFields.REPORTED, target.getUniqueId().toString());
                reports.set(ConfigFields.ReportsFields.EXECUTER, player.getUniqueId().toString());
                reports.set(ConfigFields.ReportsFields.TYPE, name);
                reports.set(ConfigFields.ReportsFields.REASON, lore);
                reports.set(ConfigFields.ReportsFields.STATUS, InventoryFields.ReportFields.WAITING);
                reports.set(ConfigFields.ReportsFields.DATE, format.format(now));

                Main.getReportsConfig().saveConfig();
                Message.REPORT_SUCCESSFULLY.sendFormattedMessage(player, true, target.getName(), name);
                Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                player.closeInventory();

                Bukkit.getOnlinePlayers().forEach(online -> {
                    if (!Permission.PERMISSION_REPORT_ALERT.hasPermission(online)) return;

                    TextComponent targetClick = new TextComponent(Message.fixColor("&7Reported: &c" + target.getName()));
                    TextComponent playerClick = new TextComponent(Message.fixColor("&7Executer: &a" + player.getName()));

                    targetClick.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + target.getName()));
                    playerClick.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp " + player.getName()));

                    targetClick.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Message.fixColor("&7Click to &6Teleport&7 to &c" + target.getName())).create()));
                    playerClick.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Message.fixColor("&7Click to &6Teleport&7 to &a" + player.getName())).create()));

                    online.sendMessage(Message.fixColor("&7&m-----&e&lNew Report&7&m-----"));
                    online.spigot().sendMessage(targetClick);
                    online.spigot().sendMessage(playerClick);
                    online.sendMessage(Message.fixColor("&7Type: &e" + name));
                    online.sendMessage(Message.fixColor("&7Reason: &e" + lore));
                    online.sendMessage(Message.fixColor("&7Status: &a" + InventoryFields.ReportFields.WAITING));
                    online.sendMessage(Message.fixColor("&7&m---------------------"));
                    Message.playSound(online, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
                });
            }
        }
    }

    @EventHandler
    public void onReportsClick(InventoryClickEvent event) {
        ItemStack is = event.getCurrentItem();
        Player player = (Player) event.getWhoClicked();

        if (!event.getView().getTitle().split(">")[0].equalsIgnoreCase(.split(">")[0])) return;
        event.setCancelled(true);

        int page = Integer.parseInt(ChatColor.stripColor(event.getView().getTitle().split(">")[1].replace("Page ", "").trim()));

        if (is == null || !is.hasItemMeta() || !is.getItemMeta().hasDisplayName()) return;
        if (!event.isLeftClick()) return;

        if (is.getItemMeta().getDisplayName().equalsIgnoreCase()) {
            if (page == 0) return;
            new ReportUI(player).openReportsGUI(page - 1);
        }
        else if (is.getItemMeta().getDisplayName().equalsIgnoreCase()) {
            new ReportUI(player).openReportsGUI(page + 1);
        }

        if (event.getSlot() == 49) {
            Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            player.closeInventory();
        }
    }

     */
}
