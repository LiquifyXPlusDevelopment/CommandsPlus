package dev.gdalia.commandsplus.models;

import dev.gdalia.commandsplus.Main;
import dev.gdalia.commandsplus.structs.reports.Reason;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class ReasonManager {

    public ReasonManager() {
        ConfigurationSection reasonsToLoad = Main.getInstance().getConfig().getConfigurationSection("reasons");

        for (String reasonName : reasonsToLoad.getKeys(false)) {
            ConfigurationSection reasonSection = reasonsToLoad.getConfigurationSection(reasonName);
            Reason reason = Reason.deserialize(reasonName,reasonSection.getValues(false));
            reportReasons.put(reasonName, reason);
        }
        //this.reportReasons = Main.getInstance().getConfig();
    }

    @Getter
    @Setter
    private static ReasonManager instance;

    private final Map<String, Reason> reportReasons = new HashMap<>();

    public Map<String, Reason> getReasons() {
        return Map.copyOf(reportReasons);
    }
}
