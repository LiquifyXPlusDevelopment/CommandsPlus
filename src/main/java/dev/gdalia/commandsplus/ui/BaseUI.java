package dev.gdalia.commandsplus.ui;

import dev.gdalia.commandsplus.inventory.ItemBuilder;
import dev.gdalia.commandsplus.structs.Message;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BaseUI {

    /*
    static {
        try {
            Method method = PaginatedGui.class.getDeclaredMethod("getPageNum", int.class);
            method.setAccessible(true);
            getPageNum = MethodHandles.lookup().unreflect(method);
        } catch (NoSuchMethodException | IllegalAccessException e1) {
            throw new RuntimeException(e1);
        }
    }
     */

    private static final GuiItem GUI_BORDER = new GuiItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, " ").create());
    private static final GuiItem GUI_BORDER_REMOVABLE = new GuiItem(new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, " ").create());

    public Gui baseGui(final int rows, @NotNull String title) {
        Gui gui = new Gui(rows, Message.fixColor(title));
        gui.disableAllInteractions();

        gui.getFiller().fillBorder(GUI_BORDER);
        gui.getFiller().fillBetweenPoints(2, 2, 5, 2, GUI_BORDER_REMOVABLE);
        gui.getFiller().fillBetweenPoints(2, 8, 5, 8, GUI_BORDER_REMOVABLE);

        return gui;
    }

    public PaginatedGui basePaginatedGui(final int rows, @NotNull String title) {
        PaginatedGui gui = new PaginatedGui(rows, Message.fixColor(title));
        gui.disableAllInteractions();

        GuiItem previousPageItem = new GuiItem(new ItemBuilder(Material.SPECTRAL_ARROW, "&aPrevious Page").create());
        GuiItem nextPageItem = new GuiItem(new ItemBuilder(Material.ARROW, "&aNext Page").create());

        previousPageItem.setAction(event -> {
            gui.previous();
            if (gui.getGuiItems().values().stream().filter(x -> x.equals(nextPageItem)).findAny().isEmpty()) {
                gui.setItem(53, nextPageItem);
                gui.update();
            }

            if (gui.getCurrentPageNum() == 1) {
                gui.setItem(45, GUI_BORDER);
                gui.update();
            }
        });

        nextPageItem.setAction(event -> {
            gui.next();
            if (gui.getGuiItems().values().stream().filter(x -> x.equals(previousPageItem)).findAny().isEmpty()) {
                gui.setItem(45, previousPageItem);
                gui.update();
            }

            if (getSpecificPageItems(gui, gui.getNextPageNum()).isEmpty()) {
                gui.setItem(53, GUI_BORDER);
                gui.update();
            }
        });

        gui.getFiller().fillBorder(GUI_BORDER);
        gui.getFiller().fillBetweenPoints(2, 2, 5, 2, GUI_BORDER_REMOVABLE);
        gui.getFiller().fillBetweenPoints(2, 8, 5, 8, GUI_BORDER_REMOVABLE);

        if (gui.getCurrentPageNum() + 1 <= gui.getPagesNum()) gui.setItem(53, nextPageItem);

        return gui;
    }
    private List<GuiItem> getSpecificPageItems(PaginatedGui gui, final int givenPage) {
        final int page = givenPage - 1;
        final List<GuiItem> guiPage = new ArrayList<>();

        for (int i = 0; i < 53; i++)
            guiPage.add(gui.getPageItems().get(i));

        return guiPage;
    }
}
