package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public record PunishUI(@Getter Player requester) {

    private static final GuiItem GUI_BORDER = new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create());

    public void openReportGUI(UUID targetUniqueId) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(targetUniqueId);
        Gui gui = new BaseUI().baseGui(6, "&cPunishing &7> &e" + requester.getName());


        gui.open(requester);
    }
}
