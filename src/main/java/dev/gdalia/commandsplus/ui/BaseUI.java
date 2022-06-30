package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BaseUI {

    private static final GuiItem GUI_BORDER = new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create());
    private static final GuiItem GUI_BORDER_REMOVABLE = new GuiItem(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, " ").create());

    public Gui baseGui(final int rows, @NotNull String title) {
        Gui gui = new Gui(rows, title);
        gui.disableAllInteractions();

        gui.getFiller().fillBorder(GUI_BORDER);
        gui.getFiller().fillBetweenPoints(2, 2, 5, 2, GUI_BORDER_REMOVABLE);
        gui.getFiller().fillBetweenPoints(2, 8, 5, 8, GUI_BORDER_REMOVABLE);

        return gui;
    }

    public PaginatedGui basePaginatedGui(final int rows, @NotNull String title) {
        PaginatedGui gui = new PaginatedGui(rows, title);
        gui.disableAllInteractions();

        GuiItem previousPageItem = new GuiItem(new ItemBuilder(Material.SPECTRAL_ARROW, "&aPrevious Page").create(), event -> {

        });

        GuiItem nextPageItem = new GuiItem(new ItemBuilder(Material.ARROW, "&aNext Page").create(), event -> {
            gui.next();
            if (gui.getGuiItems().values().stream().filter(x -> x.equals(previousPageItem)).findAny().isEmpty()) {
                gui.setItem(45, previousPageItem);
                gui.update();
            }
            if (gui.getGuiItems()
        });

        gui.getFiller().fillBorder(GUI_BORDER);
        gui.getFiller().fillBetweenPoints(2, 2, 5, 2, GUI_BORDER_REMOVABLE);
        gui.getFiller().fillBetweenPoints(2, 8, 5, 8, GUI_BORDER_REMOVABLE);

        gui.setItem(53, nextPageItem);

        return gui;
    }
}
