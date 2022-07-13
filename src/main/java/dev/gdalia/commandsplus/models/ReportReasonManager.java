package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.reports.ReportReason;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ReportReasonManager {

    public ReportReasonManager() {
        ConfigurationSection reasonsToLoad = Main.getInstance().getConfig().getConfigurationSection("reasons");

        for (String reasonName : reasonsToLoad.getKeys(false)) {
            ConfigurationSection reasonSection = reasonsToLoad.getConfigurationSection(reasonName);
            ReportReason reason = ReportReason.deserialize(reasonName ,reasonSection.getValues(false));
            reportReasons.put(reasonName, reason);
        }
        //this.reportReasons = Main.getInstance().getConfig();
    }

    @Getter
    @Setter
    private static ReportReasonManager instance;

    @Getter
    private final Map<String, ReportReason> reportReasons = new HashMap<>();
}
