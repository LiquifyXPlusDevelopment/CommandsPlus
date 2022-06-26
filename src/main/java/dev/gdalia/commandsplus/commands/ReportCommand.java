package dev.gdalia.commandsplus.commands;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.inventory.InventoryFields;
import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.listeners.InventoryListener;
import dev.gdalia.commandsplus.structs.Message;
import dev.gdalia.commandsplus.structs.Permission;
import dev.gdalia.commandsplus.utils.CommandAutoRegistration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@CommandAutoRegistration.Command(value = {"report"})
public class ReportCommand implements CommandExecutor {

    /**
     * /report {username}
     * LABEL ARG0
     */

    Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            Message.PLAYER_CMD.sendMessage(sender, true);
            return false;
        }

        if(!Permission.PERMISSION_REPORT.hasPermission(sender)) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.NO_PERMISSION.sendMessage(sender, true);
            return false;
        }

        if (args.length == 0) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.DESCRIBE_PLAYER.sendMessage(sender, true);
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            Message.playSound(sender, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
            Message.INVALID_PLAYER.sendMessage(sender, true);
            return true;
        }

        Inventory inv = Bukkit.createInventory(null, 54, InventoryListener.REPORT_INV.replace("%TARGET%", target.getName()));

        ConfigurationSection section = Main.getInstance().getConfig().getConfigurationSection("default-reasons");

        for (int j = 9; j < 18; j++)
            inv.setItem(j, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&r").build());

        for (int j = 45; j < 54; j++)
            inv.setItem(j, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("&r").build());

        inv.setItem(4, new ItemBuilder(Material.BOOK).setName("&e" + target.getName()).build());
        inv.setItem(49, new ItemBuilder(Material.BARRIER).setName("&cCancel Report").build());
        
        for (int i = 0; i < section.getKeys(false).size();) {
            i++;
            if (plugin.getConfig().getString("default-reasons.Reason" + i) == null || section.getKeys(false).size() > 27) {
                Message.CONTACT_AN_ADMIN.sendMessage(player, true);
                Message.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                return false;
            }

            String name = plugin.getConfig().getString("default-reasons.Reason" + i + ".Name");
            String lore = plugin.getConfig().getString("default-reasons.Reason" + i + ".Lore");
            String material = plugin.getConfig().getString("default-reasons.Reason" + i + ".Item");

            List<String> list = Arrays.asList(
                    " ",
                    "&6Click&7 to report player &c" + target.getName(),
                    InventoryFields.ReportFields.REASON + lore);

            inv.setItem(17 + i, new ItemBuilder(Material.valueOf(material.toUpperCase())).setName(InventoryFields.ReportFields.NAME + name).addLore(list).build());
        }

        player.openInventory(inv);
        return true;
    }
}
