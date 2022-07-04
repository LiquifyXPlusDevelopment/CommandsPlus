package dev.gdalia.commandsplus.structs.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

@AllArgsConstructor
public enum ReportStatus {

    OPEN(Material.GREEN_WOOL, "Open", ChatColor.GREEN),

    IN_INSPECTION(Material.ORANGE_WOOL, "In Inspection", ChatColor.GOLD),
    CLOSED(Material.RED_WOOL, "Close", ChatColor.RED);

    @Getter
    private final Material icon;

    @Getter
    private final String displayName;

    @Getter
    private final ChatColor representativeColor;

    @Override
    public String toString() {
        return "status={" + this.name().toUpperCase() + "}";
    }
}
